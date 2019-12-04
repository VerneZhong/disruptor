package com.zxb.disruptor.common;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * UnsafeUtil class
 *
 * @author Mr.zxb
 * @date 2019-12-04 10:09
 */
public class UnsafeUtil {

    public static Unsafe reflectGetUnsafe() {
        try {
            // 获取Unsafe实例字段
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            // 设置允许访问
            field.setAccessible(true);
            // 获取实例
            return  (Unsafe) field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
