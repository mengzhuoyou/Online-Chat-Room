package com.company;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class Server {
    private static final int PORT = 5000;
    private ServerSocket serverSocket;

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
        System.out.println("服务器启动， 在 " + PORT + " 端口监听连接请求");
        InetAddress ip = InetAddress.getLocalHost();

        System.out.println(ip.getHostAddress());
    }

    public String echo(String msg) {
        return "echo: " + msg;
    }

    public void service() throws IOException {
        while (true) {
            Socket socket = null;

            try {
                System.out.println("开始监听");
                socket = serverSocket.accept();
                System.out.println("New connection" + "accepted" + socket.getInetAddress() + ": " + socket.getPort());

                BufferedReader br = getReader(socket);
                BufferedWriter bw = getWriter(socket);
                String msg = null;
                bw.write(socket.getInetAddress() + ": " + socket.getPort() + " 你好\n");
                // 强制flush，将数据推送到客户端哪里去。
                bw.flush();
               // System.out.println("获得了输入流和输出流了");

                while ((msg = br.readLine()) != null) {
                    // 服务器读取客户端数据时候readline函数阻塞了，
// 如果客户端断开了链接，说明到了流的末尾，将返回null，
//或者一串字符串跟着换行符也将不在阻塞，string返回。
                   // System.out.println("获得了" + socket.getInetAddress() + "数据：" + msg + "\t将回显回去");
                    System.out.println(msg);
                    Scanner scanner = new Scanner(System.in);
                    String word = scanner.nextLine();
                    bw.write(word);//回显
                    bw.newLine();// 这个操作也是必须的，相当于插入一个行分割符，readline方法用来判定一行字符串有没结束
                    bw.flush();// 刷新输出缓冲区
                    if (msg.equals("bye")) {
                        System.out.println("链接断开，监听下一次链接");
                        break;
                    }
                }
                // (!socket.isClosed()&& socket.isConnected())表示链接还在
                if (!(!socket.isClosed() && socket.isConnected())) {
                    System.out.println("链接断开，监听下一次链接");
                }


            } catch (IOException e) {
            } finally {
                try {// 一次链接后把这个socket销毁。等待下一次的
                    //socket链接
                    if (socket != null)
                        socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private BufferedReader getReader(Socket socket) throws IOException {

        InputStream ins = socket.getInputStream();
        return new BufferedReader(new InputStreamReader(ins, "UTF-8"));

    }

    private BufferedWriter getWriter(Socket socket) throws IOException {

        OutputStream ous = socket.getOutputStream();
        return new BufferedWriter(new OutputStreamWriter(ous, "UTF-8"));
    }

    public static void main(String arg[]) throws IOException {
        new Server().service();
    }

    // 通过这个函数测试后，客户端函数可以发送数据到服务器进程中。发现了客户端那边发送过来时候flush了一下。
    public void testClientReader() {
        try {
            Socket socket = serverSocket.accept();
            BufferedReader br = getReader(socket);
            System.out.println(br.readLine());
        } catch (IOException e) {
        }

    }
}
