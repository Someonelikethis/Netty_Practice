package base.AIO.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * @ClassName AsyncServerHandler
 * @Description
 * @Date 2019/11/13
 * @Created by lizhanxu
 */
public class AsyncServerHandler implements Runnable {
    private int port;

    CountDownLatch latch; //同步计数器

    AsynchronousServerSocketChannel serverSocketChannel;

    public AsyncServerHandler(int port) {
        this.port = port;
        try {
            serverSocketChannel = AsynchronousServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));
            System.out.println("The Server is start in port : " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        latch = new CountDownLatch(1);//初始化同步计数器   计数初值count=1

        //第一个参数attachment(附加信息)会被传入第二个参数CompletionHandler的回调方法中使用，如果回调方法不需要这个附加信息，则attachment可以为null
        //真正的接收处理在AcceptCompletionHandler的回调方法中
        serverSocketChannel.accept(this,new AcceptCompletionHandler());//不会阻塞    异步接收处理
        try {
            latch.await();//阻塞当前线程直到count=0       阻塞在此处保证进程存活
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
