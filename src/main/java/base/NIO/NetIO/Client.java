package base.NIO.NetIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @ClassName Client
 * @Description
 * @Date 2019/11/11
 * @Created by lizhanxu
 */
public class Client {
    private static final int port = 8080;

    public static void main(String[] args) {
        new Thread(new ClientHandle(null,port),"Client-001").start();
    }

    private static class ClientHandle implements Runnable {

        private String host;
        private int port;
        private Selector selector;
        private SocketChannel socketChannel;
        private volatile boolean stop;

        public ClientHandle(String host, int port) {
            this.host = host == null ? "127.0.0.1" : host;
            this.port = port;
            try {
                selector = Selector.open();
                socketChannel = SocketChannel.open();
                socketChannel.configureBlocking(false);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        public void stop() {
            this.stop = true;
        }

        @Override
        public void run() {
            try {
                doConnect();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            while (!stop) {//轮询
                try {
                    selector.select(1000);//阻塞等待一秒，去等待channel就绪
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();//返回就绪key
                    Iterator<SelectionKey> it = selectionKeys.iterator();
                    SelectionKey key = null;
                    while (it.hasNext()) {
                        key = it.next();
                        it.remove();
                        try {
                            handleInput(key);//处理
                        } catch (IOException e) {//处理异常时直接释放资源
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
                    System.exit(1);
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

        private void handleInput(SelectionKey key) throws IOException {
            if (key.isValid()) {
                SocketChannel socketChannel = (SocketChannel) key.channel();
                if (key.isConnectable()) {//CONNECT事件触发
                    if (socketChannel.finishConnect()) {//完成建立连接
                        socketChannel.register(selector, SelectionKey.OP_READ);//连接完成，开启READ事件监听
                        doWrite(socketChannel);//向服务器发送数据
                    } else {
                        System.exit(1);//连接失败关闭程序
                    }
                }
                if (key.isReadable()) {//对Read事件是否就绪，是否监听到Read事件     Client的Read事件通过Server的Write事件触发
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    int readBytes = socketChannel.read(readBuffer);
                    if (readBytes > 0) {
                        readBuffer.flip();
                        byte[] bytes = new byte[readBuffer.remaining()];
                        readBuffer.get(bytes);
                        String body = new String(bytes, "UTF-8");
                        System.out.println("Now Body is :" + body);

                        doWrite(socketChannel);//向服务器发送数据
                    } else if (readBytes < 0) {//-1说明服务器的数据发送完毕，并且   主动的close socket
                        key.cancel();
                        socketChannel.close();
                    }
                }
            }
        }
        private void doConnect() throws IOException {
            //connect方法，连接建立则返回true
            //如果Channel是非阻塞模式，则返回false，真正的连接建立由finishConnect()完成      异步建立连接
            if (socketChannel.connect(new InetSocketAddress(host, port))) {
                socketChannel.register(selector, SelectionKey.OP_READ);//开启监听READ事件
                doWrite(socketChannel);//向服务器发送数据
            } else {//如果连接失败则监听连接
                socketChannel.register(selector, SelectionKey.OP_CONNECT);
            }
        }

        private void doWrite(SocketChannel socketChannel) throws IOException {
            System.out.print("请输入: \t");
            String str = new BufferedReader(new InputStreamReader(System.in)).readLine();//键盘输入
            byte[] req = str.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
            writeBuffer.put(req);
            writeBuffer.flip();
            socketChannel.write(writeBuffer);
            if (!writeBuffer.hasRemaining()) {//发送完毕
                System.out.println("Send Request Success");
            }
        }
    }
}
