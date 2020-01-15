package base.NIO.NetIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @ClassName Server
 * @Description
 * @Date 2019/11/11
 * @Created by lizhanxu
 */
public class Server {
    //监听的端口号
    private static final int port = 8080;

    public static void main(String[] args) {
        MultiplexerServer server = new MultiplexerServer(port);
        new Thread(server,"NIO-Server-001").start();
    }

    /**
     * 多路复用类，它是一个独立的线程,负责轮询
     */
    public static class MultiplexerServer implements Runnable {

        private Selector selector;

        private ServerSocketChannel serverSocketChannel;

        private volatile boolean stop;

        /**
         * 初始化多路复用器、绑定监听端口
         * @param port
         */
        public MultiplexerServer(int port) {
            try {
                serverSocketChannel = ServerSocketChannel.open();//打开ServerSocketChannel，用于监听客户端的连接，它是所有客户端连接的父管道
                serverSocketChannel.configureBlocking(false);//设置为非阻塞模式
                serverSocketChannel.socket().bind(new InetSocketAddress(port),1024);//绑定端口，设置请求最大数
                selector = Selector.open();//创建多路复用器

                //将serverSocketChannel注册到多路复用器selector上，监听ACCEPT事件。
                // register的第二个参数是指Selector监听Channel时对什么事件感兴趣
                // register()方法返回一个SelectionKey对象，该对象是用于跟踪这些被注册事件的句柄。
                // SelectionKey——A token representing the registration of a SelectableChannel with a Selector.
                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
                System.out.println("The server is start in port : "+port);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);//初始化失败则退出程序
            }
        }

        public void stop() {
            this.stop = true;
        }

        @Override
        public void run() {
            while (!stop) {//轮询
                try {
                    selector.select(1000);//阻塞等待一秒，去等待channel就绪
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();//selectedKeys()方法返回所有channel就绪(监听到事件发生)的key。keys()方法返回所有注册的key
                    Iterator<SelectionKey> it = selectionKeys.iterator();//iterator迭代的是副本，而非本身
                    SelectionKey key = null;
                    while (it.hasNext()) {
                        key = it.next();
                        it.remove();//擦除处理过的事件
                        try {
                            handleInput(key);//处理
                        } catch (Exception e) {//处理异常时直接释放资源
                            if (key != null) {
                                key.cancel();
                                if (key.channel() != null) {
                                    key.channel().close();
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //多路复用器Selector关闭后，所有注册在上面的Channel和Pipe等资源都会被自动关闭
            if (selector != null) {
                try {
                    selector.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 多路复用器监听到请求
         * @param key
         * @throws IOException
         */
        private void handleInput(SelectionKey key) throws IOException {
            if (key.isValid()) {
                //处理新接入的请求消息
                if (key.isAcceptable()) {//对Accept事件是否就绪，是否监听到Accept事件      Accept 接收到来自客户端的Socket请求
                    ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                    SocketChannel sc = ssc.accept();//相当于三次握手，建立物理连接，得到这个连接的SocketChannel      非阻塞模式下，失败会直接返回null不会阻塞等待
                    sc.configureBlocking(false);
                    sc.register(selector, SelectionKey.OP_READ);//将与该客户端的SocketChannel注册到多路复用器上，监听该channel上的读操作
                }
                if (key.isReadable()) {//对Read事件是否就绪，是否监听到Read事件     Server的Read事件通过Client的Write事件触发
                    SocketChannel sc = (SocketChannel) key.channel();
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    int readBytes = sc.read(readBuffer);//异步读取客户端请求消息到缓冲区
                    if (readBytes > 0) {
                        readBuffer.flip();
                        byte[] bytes = new byte[readBuffer.remaining()];
                        readBuffer.get(bytes);
                        String body = new String(bytes, "UTF-8");
                        System.out.println("The Server receive :" + body);

                        doWrite(sc);//异步向客户端发送数据
                    } else if (readBytes < 0) {//-1说明客户端的数据发送完毕，并且   主动的close socket
                        //对端链路关闭
                        key.cancel();
                        sc.close();
                    }
                }
            }
        }

        private void doWrite(SocketChannel channel) throws IOException {
            System.out.print("请输入: \t");
            String str = new BufferedReader(new InputStreamReader(System.in)).readLine();//键盘输入
            byte[] resp = str.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(resp.length);
            writeBuffer.put(resp);
            writeBuffer.flip();
            channel.write(writeBuffer);
            if (!writeBuffer.hasRemaining()) {//发送完毕
                System.out.println("Send Response Success");
            }
        }
    }
}
