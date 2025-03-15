package Client.View;

import Client.http.ClientCommunication;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author xiezhr
 * @create 2024-06-25 15:25
 */
public class GuestView {

    Scanner scanner = new Scanner(System.in);
    Logger logger = Logger.getLogger(AdminView.class);
    UI UI = new UI();
    private ClientCommunication clientCommunication;
    private static final Gson gson = new Gson();
    private static GuestView instance;

    private GuestView() {
        try {
            clientCommunication = ClientCommunication.getInstance();
        } catch (IOException e) {
            // 处理连接异常
            logger.error(e);
        }
    }

    public static synchronized GuestView getInstance() {
        if (instance == null) {
            instance = new GuestView();
        }
        return instance;
    }

    public void closeConnection() {
        try {
            clientCommunication.close();
        } catch (IOException e) {
            // 处理关闭连接异常
            logger.error(e);
        }
    }


}