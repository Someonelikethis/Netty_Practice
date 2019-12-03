package handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @ClassName ClientHandler
 * @Description
 * @Date 2019/11/26
 * @Created by lizhanxu
 */
public class ClientHandler extends SimpleChannelInboundHandler<byte[]> {
    protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {

    }
}
