package base.AIO.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * @ClassName WriteCompletionHandler
 * @Description
 * @Date 2019/11/14
 * @Created by lizhanxu
 */
public class WriteCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel client;
    private CountDownLatch latch;

    public WriteCompletionHandler(AsynchronousSocketChannel client, CountDownLatch latch) {
        this.client = client;
        this.latch = latch;
    }

    //写操作成功回调
    @Override
    public void completed(Integer result, ByteBuffer writeBuffer) {
        if (writeBuffer.hasRemaining()) {//没写完继续写
            client.write(writeBuffer, writeBuffer, this);//异步写处理
        } else {//写完之后去读数据
            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
            client.read(readBuffer, readBuffer, new ReadCompletionHandler(client,latch));//异步读处理
        }
    }

    //写操作失败回调
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
