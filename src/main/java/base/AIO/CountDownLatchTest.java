package base.AIO;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ClassName CountDownLatchTest
 * @Description   CountDownLatch     同步辅助类     同步计数器
 * @Date 2019/11/13
 * @Created by lizhanxu
 */
public class CountDownLatchTest {
    private static final CountDownLatch latch = new CountDownLatch(2);//count = 2
    public static void main(String[] args) {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        executor.execute(new MyRun());
        executor.execute(new MyRun());
        try {
            System.out.println("等待两个子线程执行完毕");
            latch.await();//阻塞到count=0
            System.out.println("两个子线程执行完毕");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    private static class MyRun implements Runnable {
        public void run() {
            System.out.println("子线程  " + Thread.currentThread().getName() + "运行中");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch.countDown();//count-1
        }
    }

}
