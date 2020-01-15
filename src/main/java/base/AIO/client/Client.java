package base.AIO.client;

/**
 * @ClassName Client
 * @Description
 * @Date 2019/11/13
 * @Created by lizhanxu
 */
public class Client {
    private static final int port = 8080;

    public static void main(String[] args) {
        //启动客户端处理线程
        new Thread(new AsyncClientHandler("127.0.0.1", port),"AIO-AsyncClientHandler-001").start();
    }
}
