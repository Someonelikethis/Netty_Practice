package base.BIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName Server
 * @Description 基于BIO的Server
 * 该例子中采用Socket短连接
 * @Date 2019/11/1
 * @Created by lizhanxu
 */
public class Server {
    //监听的端口号
    private static final int port = 8080;
    private static List<Socket> socketList = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);//负责监听来自客户端的Socket请求
            System.out.println("Server Start ...");
            while (true) {//采用循环不断地接收来自客户端的请求
                Socket client = serverSocket.accept();//如果接收到一个客户端的Socket连接请求，返回一个与该客户端对应的Socket，否则该方法将一直处于等待状态，线程阻塞
                socketList.add(client);
                new Thread(new ServerHandler(client)).start();//每一个Socket请求创建一个线程去处理
            }
        } catch (IOException e) {
            System.err.println("服务器运行异常：" + e.getMessage());
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    System.err.println("服务器关闭异常：" + e.getMessage());
                }
                serverSocket = null;
            }
        }
    }

    private static class ServerHandler implements Runnable {
        //当前线程所处理的Socket
        Socket socket;
        BufferedReader in = null;
        PrintWriter out = null;

        public ServerHandler(Socket client) throws IOException {
            this.socket = client;
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));//Socket的输入
        }

        public void run() {
            try {
                System.out.println(in.readLine());
                System.out.print("请输入： ");
                String s = new BufferedReader(new InputStreamReader(System.in)).readLine();//键盘输入
                for (Socket client : socketList) {
                    out = new PrintWriter(client.getOutputStream(), true);//Socket的输出
                    out.println(s);//发送数据,close方法在close之前会flush一次
                }
            } catch (IOException e) {
                System.err.println("服务器异常： " + e.getMessage());
            } finally {
                socketList.remove(socket);
                closeAll();
            }
        }

        //关闭资源
        private void closeAll() {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                socket = null;
            }
        }
    }
}
