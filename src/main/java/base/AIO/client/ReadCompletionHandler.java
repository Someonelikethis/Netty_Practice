package base.AIO.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * @ClassName ReadCompletionHandler
 * @Description
 * @Date 2019/11/14
 * @Created by lizhanxu
 */
public class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel client;
    private CountDownLatch latch;

    public ReadCompletionHandler(AsynchronousSocketChannel client, CountDownLatch latch) {
        this.client = client;
        this.latch = latch;
    }

    //读操作成功回调
    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        buffer.flip();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        String body;
        try {
            body = new String(bytes, "UTF-8");
            System.out.println("Client Receive :" + body);

            AsyncClientHandler.doWrite(client,latch);//写
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //读操作失败回调
    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            client.close();
            latch.countDown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
