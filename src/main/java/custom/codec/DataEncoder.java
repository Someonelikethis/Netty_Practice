package custom.codec;

import custom.entity.Data;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @ClassName DataEncoder
 * @Description
 * @Date 2019/11/26
 * @Created by lizhanxu
 */
public class DataEncoder extends MessageToByteEncoder<Data> {
    protected void encode(ChannelHandlerContext ctx, Data msg, ByteBuf out) throws Exception {
        out.writeBytes(msg.getBytes());
    }
}
