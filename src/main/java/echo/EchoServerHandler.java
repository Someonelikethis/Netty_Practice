package echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @ClassName EchoServerHandler
 * @Description
 * @Date 2020/1/13
 * @Created by lizhanxu
 */

/**
 * 对于添加同样的 ChannelHandler，默认情况下每一个 ChannelPipeline 都有自己的ChannelHandler实例
 * 如果 ChannelHandler 被注解为 @Sharable，全局只有一个handler实例，它会被多个 ChannelPipeline 共享，会被多线程并发调用
 * 所以应该在确定了 ChannelHandler 是线程安全的时才使用 @Sharable 注解
 * 使用这个注解是为了收集跨越多个 Channel 的信息
 *
 * 如果ChannelHandler是无状态的（即不需要保存任何状态参数），那么使用Sharable注解，并在bootstrap时只创建一个实例，减少GC。否则每次连接都会new出handler对象。
 */
@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    //对于每个传入的消息都要调用
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //获得入站消息
        ByteBuf in = (ByteBuf) msg;
        //打印入站消息
        System.out.println("Server received: "+in.toString(CharsetUtil.UTF_8));
        //回写入站消息到出站，此处没有冲刷缓冲区
        ctx.write(in);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //冲刷缓冲区，并关闭该Channel
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //打印异常信息
        cause.printStackTrace();
        //关闭该Channel
        ctx.close();
    }
}
