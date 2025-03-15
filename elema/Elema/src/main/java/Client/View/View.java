package Client.View;

import Client.View.MerchantView;
import Client.http.ClientCommunication;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.example.Main;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * @author xiezhr
 * @create 2024-04-27 11:12
 */
public class View {
    UI UI = new UI();
    AdminView adminView = AdminView.getInstance();
    MerchantView merchantView = MerchantView.getInstance();
    GuestView guestView = GuestView.getInstance();
    FoodView foodView = new FoodView();
    Scanner scanner = new Scanner(System.in);
    private ClientCommunication clientCommunication;
    private static View instance;
    Logger logger = Logger.getLogger(View.class);

    private View() {
        try {
            clientCommunication = ClientCommunication.getInstance();
        } catch (IOException e) {
            // 处理连接异常
            logger.error(e);
        }
    }

    public static synchronized View getInstance() {
        if (instance == null) {
            instance = new View();
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


    public void MainView(){
        UI.Head();
        UI.MainPrompt();
        String response;
        try {
            ClientCommunication clientCommunication = ClientCommunication.getInstance();
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    response = clientCommunication.sendQuery("Admin");
                    adminView.AdminEntry();
                    break;
                case 2:
                    response = clientCommunication.sendQuery("Merchant");
                    merchantView.MerchantEntry();
                    break;
                case 3:
                    response = clientCommunication.sendQuery("Guest");

                    break;

            }
        } catch (IOException e) {
            UI.error();
        }
    }

    public void AdminView(){
        UI.AdminPrompt();
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                adminView.AdminList(); //显示商家
                AdminView();
                break;
            case 2:
                adminView.AdminSearch(); //搜索商家
                AdminView();
                break;
            case 3:
                adminView.AdminNew(); //新建商家
                AdminView();
                break;
            case 4:
                adminView.AdminDel(); // 删除商家
                AdminView();
                break;
            case 5:
                break;
        }
    }

    public void MerchantView(int businessId){
        UI.MerchantPrompt();
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                merchantView.MerchantInfo(businessId); //显示商家详细信息
                MerchantView(businessId);
                break;
            case 2:
                merchantView.MerchantInfo(businessId);
                merchantView.MerchantUpdate(businessId); //更新商家
                MerchantView(businessId);
                break;
            case 3:
                merchantView.MerchantPassword(businessId); //修改密码
                MerchantView(businessId);
                break;
            case 4:
                try {
                    ClientCommunication clientCommunication = ClientCommunication.getInstance();
                    String response = clientCommunication.sendQuery("Food");
                } catch (IOException e) {
                    UI.error();
                }
                FoodView(businessId); // 进入二级菜单
                break;
            case 5:
                break;
        }
    }

    public void FoodView(int businessId) {
        UI.FoodPrompt();
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                foodView.FoodInfo(businessId);
                FoodView(businessId);
                break;
            case 2:
                foodView.FoodNew(businessId);
                FoodView(businessId);
                break;
            case 3:
                foodView.FoodUpdate(businessId);
                FoodView(businessId);
                break;
            case 4:
                foodView.FoodDel(businessId);
                FoodView(businessId);
                break;
            case 5:
                MerchantView(businessId);
                break;

        }
    }

}
