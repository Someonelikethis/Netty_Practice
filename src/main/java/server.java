import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @ClassName server
 * @Description
 * @Date 2019/11/26
 * @Created by lizhanxu
 */
public class server {
    public static void main(String[] args) {
        //NioEventLoopGroup是个线程组，它包含一组NIO线程，专门用于网络事件的处理，实际上他们就是Reactor线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();//用于服务端接收客户端连接
        EventLoopGroup workGroup = new NioEventLoopGroup();//用于进行SocketChannel的网络读写
        ServerBootstrap b = new ServerBootstrap();//用于启动NIO服务端的辅助启动类，目的是降低服务端的开发复杂度
        b.group(bossGroup, workGroup)//设置group
                .channel(NioServerSocketChannel.class)//设置channel，NioServerSocketChannel作用对应于NIO库中的ServerSocketChannel
                //使用option()方法设置channel的参数
                .option(ChannelOption.SO_BACKLOG,1024);//设置accept队列长度，最大全连接数为1024（如果未设置或所设置的值小于1，默认值50），该队列满后会拒绝请求
    }
}
