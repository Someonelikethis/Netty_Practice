package base.AIO.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @ClassName ReadCompletionHandler
 * @Description
 * @Date 2019/11/13
 * @Created by lizhanxu
 */
public class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {
    private AsynchronousSocketChannel channel;

    public ReadCompletionHandler(AsynchronousSocketChannel channel) {
        if (channel!=null) {
            this.channel = channel;
        }
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        attachment.flip();
        byte[] body = new byte[attachment.remaining()];
        attachment.get(body);
        try {
            String req = new String(body, "UTF-8");
            System.out.println("The Server Receive : " + req);
            doWrite();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doWrite() {
        System.out.print("请输入: \t");
        String str = null;//键盘输入
        try {
            str = new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] resp = str.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(resp.length);
        writeBuffer.put(resp);
        writeBuffer.flip();
        channel.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {//异步写
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                //如果没有发送完，继续发送
                if (attachment.hasRemaining()) {
                    channel.write(attachment, attachment, this);//异步写
                } else {//写完继续读
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    channel.read(buffer, buffer, new ReadCompletionHandler(channel));//异步读取
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        if (!writeBuffer.hasRemaining()) {//发送完毕
            System.out.println("Send Response Success");
        }
    }
}
