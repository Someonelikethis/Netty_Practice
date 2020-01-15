### AIO(异步非阻塞   NIO2.0)
    CompletionHandler接口的实现类作为操作完成的回调
    
    注意：
    使用异步Channel时，accept()、connect()、read()、write()等方法都不会阻塞，
    也就是说如果使用返回Future的这些方法，程序并不能知道什么时候成功IO，必须要使用get方法，
    等get方法的阻塞结束后才能确保IO完成，继续执行下面的操作。
    
    
    示例代码中没有很好的解决多个client同时和服务器通信的问题