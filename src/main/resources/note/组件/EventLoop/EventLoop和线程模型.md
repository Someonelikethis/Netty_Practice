###线程模型

线程模型指定了操作系统、编程语言、框架或者应用程序的上下文中的线程管理的关键方面。

jdk5引入Executor API，其线程池通过缓存和重用Thread极大地提高了性能。

池化和重用线程并不能消除上下文切换所带来的的开销。

###EventLoop接口——事件循环

一个EventLoop将由一个永远不会改变的Thread驱动

EventLoop和Channel是一对多的关系

EventLoop将负责处理一个Channel的整个生命周期内的所有事件。

每个EventLoop都有它自己的任务队列

事件/任务的执行顺序：FIFO

在Netty4中，所有I/O操作和事件都由已经被分配给了EventLoop的那个Thread来处理。

####EventLoopGroup
EventLoopGroup负责为每个新创建的Channel分配一个EventLoop。
一旦一个Channel被分配一个EventLoop，它将在它的整个生命周期中都使用这个EventLoop。因为这一点，使得无需在ChannelHandler实现中担忧线程安全和同步问题。

###任务调度

####JDK的任务调度API
Java5之前，任务调度室建立在java.util.Timer类之上的。随后JDK提供了java.util.concurrent.ScheduledExecutorService，它支持任务调度(定时任务，周期任务...)
ScheduledExecutorService的实现由局限性，大量任务被紧凑调度会有瓶颈。

####使用EventLoop调度任务

EventLoop扩展了ScheduledExecutorService