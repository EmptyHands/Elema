package Server.API;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final int PORT = 8083;
    private static String choice; //Choose the Server client wanted

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is starting on port " + PORT);

            while (true) { // 无限循环以接受多个连接
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start(); // 使用新线程处理客户端请求
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Server received request: " + inputLine);
                if (inputLine.equals("Admin")) {
                    choice = "Admin";
                } else if (inputLine.equals("Merchant")) {
                    choice = "Merchant";
                } else if (inputLine.equals("Food")) {
                    choice = "Food";
                }                // 模拟业务服务处理
                String response;
                if (choice.equals("Food")) {
                    response = FoodServer.handleRequest(inputLine);
                } else if (choice.equals("Merchant")) {
                    response = MerchantServer.handleRequest(inputLine);
                } else if (choice.equals("Admin")) {
                    response = AdminServer.handleRequest(inputLine);
                } else {
                    response = "Invalid query";
                }

                // 发送响应给客户端
                out.write(response);
                out.newLine(); // 添加换行符
                out.flush(); // 刷新输出流
            }

        } catch (IOException e) {
            System.out.println("Server出现连接问题!"+e);
        } finally {
            try {
                clientSocket.close();
                System.out.println("Client disconnected.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
