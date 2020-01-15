package base.AIO.server;

/**
 * @ClassName Server
 * @Description
 * @Date 2019/11/13
 * @Created by lizhanxu
 */
public class Server {
    private static final int port = 8080;

    public static void main(String[] args) {
        AsyncServerHandler server = new AsyncServerHandler(port);
        //启动服务端处理线程
        new Thread(server,"AIO-AsyncServerHandler-001").start();
    }
}
