package echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @ClassName EchoClientHandler
 * @Description
 * @Date 2020/1/13
 * @Created by lizhanxu
 */
@ChannelHandler.Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    /**
     * 当从服务器接收到一条消息时被调用
     *
     * 由服务器发送的消息可能会被分块接收。比如服务器发送了5个字节，可能会调用两次channelRead0()方法，第一次3个字节，第二次2个字节
     * 作为面向流的协议，TCP保证了字节数组将会按照服务器发送它们的顺序被接受
     *
     * SimpleChannelInboundHandler和ChannelInboundHandlerAdapter的区别：
     * SimpleChannelInboundHandler的channelRead()调用channelRead0()，当channelRead0()完成时会释放消息(释放指向该消息的ByteBuf的内存引用)
     * ChannelInboundHandlerAdapter的channelRead()方法不会释放消息，会在channelReadComplete()中调用writeAndFlush()时被释放
     *
     * 注：在channelRead()中write()操作是异步的，channelRead()方法返回后可能仍然没有完成，固channelRead()不会释放消息。
     *
     * channelRead()/channelRead0()方法中不应该阻塞当前I/O线程
     */
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        //打印接收到的消息
        System.out.println("Client received: "+msg.toString(CharsetUtil.UTF_8));
    }

    @Override
    //在到服务器的连接建立之后将被调用
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //当被通知Channel是活跃的时候，发送一条消息
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        //关闭Channel
        ctx.close();
    }
}
