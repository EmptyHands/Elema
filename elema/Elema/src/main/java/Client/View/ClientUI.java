package Client.View;

import java.io.IOException;
import java.util.Scanner;
import Client.http.ClientCommunication;
import Client.View.View;
import org.apache.log4j.Logger;

public class ClientUI {
    View view =View.getInstance();
    Logger logger = Logger.getLogger(AdminView.class);

    public void start() {
        try (Scanner scanner = new Scanner(System.in)) {
            ClientCommunication clientCommunication = ClientCommunication.getInstance();


            while (true) {
                view.MainView();
                logger.info("輸入exit以退出");
                // 接收用户输入
                String query = scanner.nextLine();

                if (query.equals("exit")) {
                    break;
                }

                // 发送查询请求并接收响应
                String response = clientCommunication.sendQuery(query);
            }

            clientCommunication.close();
        } catch (IOException e) {
            logger.error(e);
        }
    }
}
