package Client.http;

import java.io.*;
import java.net.Socket;

public class ClientCommunication {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8083;
    private static ClientCommunication instance;
    private Socket socket;
    private BufferedWriter out;
    private BufferedReader in;

    // 私有构造函数，防止直接实例化
    private ClientCommunication() throws IOException {
        connect();
    }

    // 获取单例实例的方法
    public static synchronized ClientCommunication getInstance() throws IOException {
        if (instance == null) {
            instance = new ClientCommunication();
        }
        return instance;
    }

    // 连接服务器的方法
    private void connect() throws IOException {
        if (socket == null || socket.isClosed()) {
            this.socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
    }

    // 发送查询请求的方法
    public String sendQuery(String query) throws IOException {
        out.write(query);
        out.newLine();
        out.flush();
        return in.readLine();
    }

    // 关闭连接的方法
    public void close() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        if (out != null) {
            out.close();
        }
        if (in != null) {
            in.close();
        }
        instance = null; // 确保关闭后可以重新连接
    }
}
