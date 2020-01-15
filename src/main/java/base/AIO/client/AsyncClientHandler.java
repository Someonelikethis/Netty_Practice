package base.AIO.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * @ClassName AsyncClientHandler
 * @Description
 * @Date 2019/11/14
 * @Created by lizhanxu
 */
public class AsyncClientHandler implements CompletionHandler<Void, AsyncClientHandler>,Runnable {

    private AsynchronousSocketChannel client;
    private String host;
    private int port;
    private CountDownLatch latch;

    public AsyncClientHandler(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            client = AsynchronousSocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        latch = new CountDownLatch(1); //初始化同步计数器   计数初值count=1
        client.connect(new InetSocketAddress(host, port), this, this);//异步连接处理
        try {
            latch.await();//阻塞当前线程直到count=0       阻塞在此处保证进程存活
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //连接成功回调
    @Override
    public void completed(Void result, AsyncClientHandler attachment) {
       doWrite(client,latch);
    }

    //连接失败回调
    @Override
    public void failed(Throwable exc, AsyncClientHandler attachment) {
        try {
            System.out.println("连接失败");
            client.close();
            latch.countDown();
        } catch (IOException e) {
            e.printStackTrace();
            latch.countDown();
        }
    }

    public static void doWrite(AsynchronousSocketChannel client,CountDownLatch latch) {
        System.out.print("请输入: \t");
        String str = null;//键盘输入
        try {
            str = new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] req = str.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
        writeBuffer.put(req);
        writeBuffer.flip();
        client.write(writeBuffer, writeBuffer, new WriteCompletionHandler(client,latch));//异步写处理
    }
}
