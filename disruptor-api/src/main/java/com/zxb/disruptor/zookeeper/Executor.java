package com.zxb.disruptor.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A simple example program to use DataMonitor to start and
 * stop executables based on a znode. The program watches the
 * specified znode and saves the data that corresponds to the
 * znode in the filesystem. It also starts the specified program
 * with the specified arguments when the znode exists and kills
 * the program if the znode goes away.
 *
 * 使用zookeeper实现配置动态管理：
 * zookeeper典型应用场景之一就是利用发布订阅模式实现配置动态管理。基本原理就是将配置信息存在zk的某个节点中，
 * 客户端启动时从这个节点读取配置信息，并Watcher，一旦配置发生变化，客户端会接收到变化通知，便可以再次读取节点内容。
 *
 * 作者：Woople
 * 链接：https://www.jianshu.com/p/14ab38601fe1
 * 来源：简书
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 *
 * @author Mr.zxb
 * @date 2019-12-02 11:10
 */
public class Executor implements Watcher, Runnable, DataMonitorListener {

    private DataMonitor dm;

    private ZooKeeper zk;

    private String filename;

    private String[] exec;

    private Process child;

    public Executor(String hostPort, String zNode, String filename,
                    String[] exec) throws IOException {
        this.filename = filename;
        this.exec = exec;
        zk = new ZooKeeper(hostPort, 3000, this);
        dm = new DataMonitor(zk, zNode, null, this);
    }

    public static void main(String[] args) {
        
        String hostPort = "localhost:2181";
        String zNode = "/zk_tutorials";
        String filename = "output.txt";
        String[] exec = {"/bin/bash", "-c", "./count.sh"};
        try {
            new Executor(hostPort, zNode, filename, exec).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exists(byte[] data) {
        if (data == null) {
            if (child != null) {
                System.out.println("Killing process");
                child.destroy();
                try {
                    child.waitFor();
                } catch (InterruptedException e) {
                }
            }
            child = null;
        } else {
            if (child != null) {
                System.out.println("Stopping child");
                child.destroy();
                try {
                    child.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                FileOutputStream fos = new FileOutputStream(filename);
                fos.write(data);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                System.out.println("Starting child");
                child = Runtime.getRuntime().exec(exec);
                new StreamWriter(child.getInputStream(), System.out);
                new StreamWriter(child.getErrorStream(), System.err);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void closing(int rc) {
        synchronized (this) {
            notifyAll();
        }
    }

    @Override
    public void run() {
        synchronized (this) {
            while (!dm.dead) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void process(WatchedEvent event) {
        dm.process(event);
    }

    static class StreamWriter extends Thread {

        OutputStream os;

        InputStream is;

        public StreamWriter(InputStream is, OutputStream os) {
            this.os = os;
            this.is = is;
        }

        @Override
        public void run() {
            byte[] bytes = new byte[80];
            int rc;
            try {
                while ((rc = is.read(bytes)) > 0) {
                    os.write(bytes, 0, rc);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
