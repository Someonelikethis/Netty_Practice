package base.BIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @ClassName Client
 * @Description   基于BIO的Client
 *                注意：
 *                将Socket的输出流包装成PrintWriter需要设置autoFlush为true(默认为false)，或者手动flush
 *                close方法在close之前会flush一次
 *
 *                flush方法，强制输出并清空缓冲区
 * @Date 2019/11/1
 * @Created by lizhanxu
 */
public class Client {
    public static void main(String[] args) {
        System.out.println("Client Start ...");
        while (true) {
            Socket socket = null;
            BufferedReader in = null;
            PrintWriter out = null;
            try {
                socket = new Socket("127.0.0.1", 8080);

                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));//Socket的输入流，用以获取客户端传过来的数据
                out = new PrintWriter(socket.getOutputStream(),true);//Socket的输出流，用以向客户端传输数据

                System.out.print("请输入: \t");
                String str = new BufferedReader(new InputStreamReader(System.in)).readLine();//键盘输入
                if (str.equals("exit")) {
                    out.println(str);//发送数据
                    closeAll(out, in, socket);
                    break;
                }
                out.println(str);//发送数据
//                out.flush();//手动flush

                System.out.println("服务器端响应: " + in.readLine());//接收数据
            } catch (IOException e) {
                System.err.println("客户端异常：" + e.getMessage());
                closeAll(out,in,socket);
            }
        }
    }

    //关闭资源
    private static void closeAll(PrintWriter out,BufferedReader in,Socket socket) {
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
