package com.company;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class Client {

    private static final int PORT = 5000;
    private String host = "localhost";
    private Socket socket;
    public Client() {
        try {
            this.socket = new Socket(host, PORT);
            System.out.println("客户端链接成功");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Client().talkToServer();
        System.out.println("client over");
    }

    // 测试写数据到服务器的连通性
    public void testServerWriter() {
        BufferedWriter bWriter = getWriter(socket);
        try {
            bWriter.write("vincent ok");
            bWriter.newLine();
            bWriter.flush();// 这个动作非常重要
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void talkToServer() {
        BufferedReader bReader = getReader(socket);
        BufferedWriter bWriter = getWriter(socket);
        BufferedReader localbr;
        String mString = null;
        // 构建本地客户端终端的输入流
        try {
            localbr = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
            System.out.println("server print  " + bReader.readLine());

            while ((mString = localbr.readLine()) != null) {
                // readLine方法必须读取到行分割符才返回读取。所以传递给输入流的字符串必须包含行分割符
                //System.out.println("client print  " + mString);
                bWriter.write(mString);
                bWriter.newLine();// 插入一个行分隔符,作为服务器端readline函数判断这个字符串结束的标识
                // 非常重要的是必须显式的将数据推送到服务器哪里去
                bWriter.flush();
                //System.out.println("输出到server的数据");
                // 读取到服务器的数据，readline方法是阻塞的
               // System.out.println("获得了服务器响应数据： " + "server print  " + bReader.readLine());
                System.out.println(bReader.readLine());
                if (mString.equals("bye")) {
                    socket.close();
                    break;
                }

            }
            System.out.println("退出while循环");
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    private BufferedWriter getWriter(Socket socket) {
        try {
            OutputStream outputStream = null;
            BufferedWriter bWriter = null;
            outputStream = socket.getOutputStream();
            bWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            return bWriter;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;

    }

    private BufferedReader getReader(Socket socket) {
        try {
            InputStream ins = null;
            BufferedReader brReader = null;
            ins = socket.getInputStream();
            brReader = new BufferedReader(new InputStreamReader(ins, "UTF-8"));
            return brReader;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

}
