Channel、EventLoop、ChannelFuture可以被认为是Netty网络抽象的代表

Channel——Socket;
EventLoop——控制流、多线程处理、并发;
ChannelFuture——异步通知;

Channel接口大大降低了直接使用Socket类的复杂性

EventLoop定义了Netty的核心抽象，用于处理连接的生命周期中所发生的事件

关系：
一个EventLoopGroup包含一个或者多个EventLoop；
一个EventLoop在它的生命周期内只和一个Thread绑定；
所有由EventLoop处理的I/O事件都将在它专有的Thread上被处理；
一个Channel在它的生命周期内只注册于一个EventLoop；
一个EventLoop可能会被分配给一个或多个Channel。

一个给定的Channel的I/O操作都是由相同的Thread执行的，实际上消除了对于同步的需求。



