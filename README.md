# Disruptor
## 第一章 Disruptor并发编程框架核心讲解
  ### Disruptor介绍
   * Martin Fowler在自己网站上写了一篇LMAX架构的文章[官网](https://lmax-exchange.github.io/disruptor/)
   * 在文章中他结束了LMAX是一种新型零售金融交易平台
   * 它能够以很低的延迟产生大量交易
   * 这个系统是建立在JVM平台上，其核心是一个业务逻辑处理器
   * 它能够在一个线程里每秒处理6百万订单
   * 业务逻辑处理器完全是运行在内存中，使用事件源驱动方式
   * 业务逻辑处理器的核心是Disruptor
   
  ### 快速实践
  * 建立一个工厂Event类，用于创建Event类实例对象
  * 需要有一个监听事件类，用于处理数据（Event类）
  * 实例化Disruptor实例，配置一系列参数， 编写Disruptor核心组件
  * 编写生产者组件，向Disruptor容器中去投递数据
  
  ### Disruptor 核心原理
  * 初看Disruptor，给人的印象就是RingBuffer是其核心，生产者向RingBuffer中写入数据，消费者从RingBuffer中消费数据
  * RingBuffer：它是一个环（首尾相接的环），它用作在不同上下文（线程）间传递数据的Buffer
  * RingBuffer拥有一个序号，这个序号指向数组中下一个可用元素
  * 随着你不停地填充这个buffer（可能也会有相应地读取），这个序号会一直增长，直到绕过这个环
  * 要找到数组中当前序号指向的元素，可以通过mod操作：sequence mod array length = array index （取模操作）以上面的 RingBuffer为例（Java的mod语法）：12 % 10 = 2
  * 事实上，上图中的RingBuffer只有10个槽完全是个意外。如果槽的个数是2的N次方更有利于基于二进制的计算机进行计算

  #### Disruptor核心 - RingBuffer
   * RingBuffer：基于数组的缓存实现，也是创建sequence与定义WaitStrategy的入口  
   * Disruptor：持有RingBuffer、消费者线程池Executor、消费者集合ConsumerRepository等引用
   
  ### Disruptor核心 - Sequence
   * 通过顺序递增的序号来编号，管理进行交换的数据（事件）
   * 对数据（事件）的处理过程总是沿着序号逐个递增处理
   * 一个Sequence用于跟踪标识某个特定的事件处理者（RingBuffer/Producer/Consumer）的处理进度
   * Sequence可以看成是一个AtomicLong用于标识进度
   * 还有另外一个目的就是防止不同Sequence之间[CPU缓存伪共享（False Sharing）](https://www.cnblogs.com/cyfonly/p/5800758.html)的问题
   
  ### Disruptor核心 - Sequencer
   * Sequencer是Disruptor的真正核心
   * 此接口有两个实现类
     * SingleProducerSequencer
     * MultiProducerSequencer
   * 主要实现生产者与消费者之间快速、正确地传递数据的并发算法
   
   ### Disruptor核心 - SequenceBarrier
   * 用于保持对RingBuffer的 Main Published Sequence（Producer）和 Consumer 之间的平衡关系；SequenceBarrier还定义类决定Consumer是否还有可处理的事件的逻辑。
   
   ### Disruptor核心 - WaitStrategy
   * 决定一个消费者将如何等待生产者将Event置入Disruptor
   * 主要策略有：
     * BlockingWaitStrategy（阻塞式）：它是最低效的策略，但其对CPU的消耗最小并且在各种不同部署环境中能够提供更加一致的性能表现
     * SleepingWaitStrategy（休眠式）：它的性能表现跟BlockingWaitStrategy差不多，对CPU消耗也类似，但其对生产者线程对影响最小，适合用于异步日志类似的场景
     * YieldingWaitStrategy（线程礼让式）：它的性能是最好的，适合用于低延迟的系统，在要求极高性能且事件处理线程数小于CPU逻辑核心数的场景中，推荐使用此策略；例如，CPU开启超线程的特性
     * ...
   
   ### Disruptor核心 - Event
   * Event：从生产者到消费者过程中所处理到数据单元
   * Disruptor中没有代码表示Event，因为它完全是由用户定义的
   
   ### Disruptor核心 - EventProcessor
   * EventProcessor：主要事件循环，处理Disruptor中的Event，拥有消费者的Sequence
   * 它有一个实现类是BatchEventProcessor，包含了event loop有效的实现，并且将回调到一个EventHandler接口到实现对象
   
   ### Disruptor核心 - EventHandler
   * EventHandler：由用户实现并且代表了Disruptor中到一个消费者的接口，也就是我们的消费者逻辑都需要写在这里
   
   ### Disruptor核心 - WorkProcessor
   * WorkProcessor：确保每个Sequence只被一个processor消费，在同一个WorkPool中处理多个WorkProcessor不会消费同样的sequence
   
 
 ## 第二章 Disruptor高级特性讲解
   ### Disruptor串、并行操作
   * ```EventHandlerGroup<T> handlerEventsWith(final EventHandler<? super T>... handlers)``` 
   * 串行操作：使用链式调用的方式
   * 并行操作：使用单独调用的方式
   
   ### Disruptor-多生产者模型讲解
   * 依赖Disruptor配置实现多生产者：
    ```RingBuffer.create(ProducerType producerType, EventFactory<E> factory, int bufferSize, WaitStrategy waitStrategy)```
   
   ### Disruptor-多消费者模型讲解
   * 依赖WorkerPool实现多消费者：
    ```workerPool(final RingBuffer<T> ringBuffer, SequenceBarrie sequenceBarrier, ExceptionHandler<? super T> exceptionHandler, WorkHandler<? super T>... workHandlers)```
    
  ## 第三章 Disruptor底层源码分析
   ### Disruptor底层性能为何如此高
   * 数据结构层面：使用环形结构、数组、内存预加载
   * 使用单线程写方式、内存屏障
   * 消除伪共享（填充缓存行）
   * 序号栅栏和序号配合使用来消除锁和CAS
   
   ### 高性能之道-数据结构-内存预加载机制
   * RingBuffer使用数组Object[] entries作为存储元素