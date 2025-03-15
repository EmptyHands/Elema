import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Connected to server.");

            Scanner scanner = new Scanner(System.in);

            while (true) {
                // 接收用户输入
                System.out.print("Enter a query (or 'exit' to quit): ");
                String query = scanner.nextLine();

                if (query.equals("exit")) {
                    break;
                }

                // 发送查询请求到服务器
                out.write(query);
                out.newLine(); // 添加换行符
                out.flush(); // 刷新输出流

                // 读取服务器的响应
                String response = in.readLine();
                System.out.println("Server response: " + response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}