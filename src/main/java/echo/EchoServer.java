package echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * @ClassName EchoServer
 * @Description
 * @Date 2020/1/13
 * @Created by lizhanxu
 */
public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        new EchoServer(8000).start();
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        final EchoServerHandler serverHandler = new EchoServerHandler();//EchoServerHandler有@Sharable注解
        try {
            ServerBootstrap b = new ServerBootstrap();
            //指定线程池来进行事件处理,如接受和处理新的连接等
            b.group(group)
                    //指定Channel类型，* 注：在服务端，Channel绑定本地监听端口只用于接受连接，子Channel才真正处理连接
                    .channel(NioServerSocketChannel.class)
                    //使用指定的端口设置套接字地址
                    .localAddress(new InetSocketAddress(port))
                    /**
                     * 添加一个EchoServerHandler到子Channel的ChannelPipeline
                     *
                     * 当一个新的连接被接受时，一个新的子Channel将会被创建，
                     * ChannelInitializer把一个EchoServerHandler的实例添加到该Channel的ChannelPipeline中
                     *
                     * 重点：每一个Channel创建时都会调用该初始化方法进行初始化
                     */
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(serverHandler);
                        }
                    });
            //对sync()方法的调用将导致当前线程阻塞，一直到操作完成为止
            ChannelFuture f = b.bind().sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //这两种的区别，sync()可以中断，并抛出异常，syncUninterruptibly()不可中断。
//            group.shutdownGracefully().sync();
            group.shutdownGracefully().syncUninterruptibly();
        }
    }
}
