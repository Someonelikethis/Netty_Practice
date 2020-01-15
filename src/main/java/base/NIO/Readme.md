#### Java传统IO与NIO

| 传统的IO            | NIO             |
| ------------------- | --------------- |
| 阻塞(BIO)           | 支持非阻塞      |
| 面向流的I/O，速度慢 | 面向(块)缓冲区的I/O |
| Stream是单向的      | Channel是双向的 |

#### NIO三大核心部分——Channel(通道)、Buffer(缓冲区)、Selector(选择器)

    通道是数据的载体，是数据的传输通道，而缓冲区是存储数据的地方

##### Buffer
    缓冲区实际上是一个容器对象，即数组
    NIO中所有的数据都用缓冲区处理
    常用方法：
    flip(): 写模式和读模式转换
    rewind()：将 position 重置为 0 ，一般用于重复读。
    compact(): 将未读取的数据拷贝到 buffer 的头部位。

| 参数     | 说明                                            |
| -------- | ----------------------------------------------- |
| capacity | 缓冲数组的总长度                                |
| limit    | 第一个不应该被读写的元素的位置                  |
| position | 读写的下一个元素的位置                          |
| mark     | 标记位置，通过reset方法可以回到这个位置继续读写 |

```
mark <= position <= limit <= capacity
```

##### Channel

    就像自来水管一样，Channel用做从Buffer中读写数据，数据总是从通道读取到缓冲区中，或者从缓冲区写入到通道中。
    Channel是双向的,全双工，可以更好的映射底层操作系统的API，UNIX网络编程模型中，底层操作系统的通道就是全双工的。
    
    Channel可以分为两大类：用于网络读写的SelectableChannel和用于文件操作的FileChannel

##### Selector
    Selector是SelectableChannel对象的多路复用器，所有希望采用非阻塞方式进行通信的Channel都应该注册到Selector对象
    
    Selector用于监听多个通道的事件（比如：连接打开，数据到达）。因此，单个线程可以监听多个Channel，从而实现非阻塞
    
    工作原理：不断轮询注册在其上的Channel，当监听到Channel的事件时，该Channel处于就绪状态，会被Selector轮询出来，
             然后通过SelectionKey可以获取就绪Channel的集合，进行后续的I/O操作
             
    JDK使用了epoll()代替了传统的select实现，没有了最大句柄的限制，即一个Selector可以轮询任意多个Channel

#### 主要类

​    缓冲区 ByteBuffer
​    管道   Pipe
​    通道   Channel
​    文件通道  FileChannel
​    多路复用器 Selector

    所有的Channel默认都是阻塞模式





​	

