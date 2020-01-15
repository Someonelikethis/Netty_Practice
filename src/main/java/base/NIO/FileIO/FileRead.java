package base.NIO.FileIO;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * @ClassName FileRead
 * @Author lizhanxu
 * @Date 2019/9/25  17:20
 * @Version V1.0.0
 */
public class FileRead {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("src/main/java/base/NIO/FileIO/file.txt");
        FileChannel fileChannel = fileInputStream.getChannel();//从流中获得通道
        ByteBuffer byteBuffer = ByteBuffer.allocate(20);//创建缓冲区
        fileChannel.read(byteBuffer);//从通道中读取数据写入缓冲区
        byteBuffer.flip();//写模式转换为读模式

        //使用解码器解码
        Charset charset = Charset.forName("UTF-8");
        CharsetDecoder decoder = charset.newDecoder();
        CharBuffer charBuffer = CharBuffer.allocate(20);
        decoder.decode(byteBuffer, charBuffer, true);
        charBuffer.flip();//写模式转换为读模式

        while (charBuffer.hasRemaining()){//读取数据
            char b = charBuffer.get();//读取一个字节
            System.out.print(b);
        }
        fileInputStream.close();//关闭流
    }
}
