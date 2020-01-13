### Netty的核心组件(构建块)

这些构建块代表了不同类型的构造：资源、逻辑以及通知。应用程序将使用它们来访问网络以及流经网络的数据。

#### Channel

它代表一个到实体(一个文件、一个网络套接字等)的开放连接

每个Channel都有一个与之相关的ChannelPipeline，其持有一个ChannelHandler的实例链。ChannelPipeline是线程安全的。

#### 回调

一个回调其实就是一个方法。当一个回调被触发时，相关的事件可以被一个ChannelHandler接口的实现来处理。

#### Future

Future提供了另一种在操作完成时通知应用程序的方式。这个对象可以看做是一个异步操作的结果的占位符，它将在未来的某个时刻完成，并提供对其结果的访问。

netty提供了自己的实现——ChannelFuture。

每个netty的出站I/O操作都将返回一个ChannelFuture。

ChannelFuture提供了几种额外的方法，这些方法使得我们能过注册一个 或者多个ChannelFutureListener实例。如果ChannelFutureListerner添加到ChannelFuture的时候，ChannelFuture已经完成，那么ChannelFutureListerner将会被直接地通知。

ChannelFutureListener看作是回调的一个更加精细的版本。回调和Future是相互补充的机制。

#### 事件和ChannelHandler

netty使用不同的事件来通知我们状态的改变或者操作的状态。使我们能过基于已经发生的事件来触发适当的动作。

事件按照它们与出站或者入站数据流的相关性进行分类。

ChannelHandler为处理器提供了基本的抽象。每一个ChannelHandler实例都类似于一种为了响应特定事件而被执行的回调。

ChannelHandler的实现负责接收并响应事件通知。

ChannelInboundHandler用来定义响应入站事件的方法。

ChannelInboundHandlerAdapter类提供了ChannelInboundHandler的默认实现。



在内部，会为每一个Channel分配一个EventLoop，用以处理所有事件。EventLoop本身只由一个线程驱动，其处理了一个Channel的所有I/O事件，并且在EventLoop的整个生命周期内都不会改变。

