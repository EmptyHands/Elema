package Client.View;

import Client.http.ClientCommunication;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.Logger;
import Client.http.ClientCommunication;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class AdminView {

    Scanner scanner = new Scanner(System.in);
    Logger logger = Logger.getLogger(AdminView.class);
    UI UI = new UI();
    private ClientCommunication clientCommunication;
    private static final Gson gson = new Gson();
    private static AdminView instance;

    private AdminView() {
        try {
            clientCommunication = ClientCommunication.getInstance();
        } catch (IOException e) {
            // 处理连接异常
            logger.error(e);
        }
    }

    public static synchronized AdminView getInstance() {
        if (instance == null) {
            instance = new AdminView();
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



    public void AdminEntry() {
        String adminName;
        String password;
        String input;
        View view = View.getInstance();

        try {
            ClientCommunication clientCommunication = ClientCommunication.getInstance();
            String response;

            while (true) {
                logger.info("请输入管理员名称:");
                adminName = scanner.nextLine();
                response = clientCommunication.sendQuery("AdminName:" + adminName);

                if (response.startsWith("Password:")) {
                    password = response.split(":")[1];
                    logger.info("请输入密码:");
                    input = scanner.nextLine();

                    if (input.equals(password)) {
                        view.AdminView();
                        break; // 退出循环
                    } else {
                        logger.info("密码错误，请重新输入管理员名称。");
                    }
                } else {
                    logger.info("管理员不存在，请重新输入。");
                }
            }
        } catch (IOException e) {
            logger.error("连接服务器出错!", e);
        }
    }

    public void AdminList() {
        try {
            ClientCommunication clientCommunication = ClientCommunication.getInstance();
            String response = clientCommunication.sendQuery("AdminList");

            Type listType = new TypeToken<List<List<Object>>>() {}.getType();
            List<List<Object>> adminList = gson.fromJson(response, listType);

            for (List<Object> admin : adminList) {
                for (Object field : admin) {
                    System.out.print(field + "\t");
                }
                System.out.println();
            }
        } catch (IOException e) {
            logger.error("连接服务器出错!", e);
        }
    }

    public void AdminSearch(){
        String Name_search = null;
        String Address_search = null;
        char hasName = 0;
        char hasAddress = 0;
        while (hasName != 'y' && hasAddress != 'y') {
            hasName = 0;
            hasAddress = 0;
            while (hasName != 'y' && hasName != 'n') {
                logger.info("是否需要输入商家名称关键词:(y/n)");
                String NameInput_string = scanner.next();
                hasName = NameInput_string.toLowerCase().charAt(0);
            }
            if (hasName == 'y'){
                logger.info("请输入商家名称关键词:");
                Name_search = scanner.next();
            }
            while (hasAddress != 'y' && hasAddress != 'n') {
                logger.info("是否需要输入商家地址关键词:(y/n)");
                String AddressInput_string = scanner.next();
                hasAddress = AddressInput_string.toLowerCase().charAt(0);
            }
            if (hasAddress == 'y'){
                logger.info("请输入商家地址关键词:");
                Address_search = scanner.next();
            }
            if (hasAddress == 'n' && hasName == 'n'){
                logger.info(("请至少输入一类关键词"));
            }
        }
        try {
            ClientCommunication clientCommunication = ClientCommunication.getInstance();

            String response = clientCommunication.sendQuery("MerchantSearch:"+Name_search+":"+Address_search);
            Type listType = new TypeToken<List<List<Object>>>() {}.getType();
            List<List<Object>> MerchantSearch = gson.fromJson(response, listType);
            UI.MerchantHead();
            for (List<Object> admin : MerchantSearch) {
                for (Object field : admin) {
                    System.out.print(field + "\t");
                }
                System.out.println();
            }

            //merchantDao.MerchantSearch(Name_search, Address_search); // 查找商家
        } catch (IOException e){
            logger.error(e);
        }
    }

    public void AdminNew(){
        logger.info("请输入商家名称");
        String newName = scanner.next();
        try {
            ClientCommunication clientCommunication = ClientCommunication.getInstance();
            String response = clientCommunication.sendQuery("MerchantNew:"+newName);
            logger.info("新建商家成功!商家编号为:"+response);
        } catch (IOException e){
            logger.error("服务器连接失败!");
        }
    }

    public void AdminDel(){
        logger.info("请输入商家编号");
        int Id_del = scanner.nextInt();
        char verDel = 0;
        boolean hasDel = false;
        try {
            while (verDel != 'y' && verDel != 'n') {
                logger.info("确认要删除吗:(y/n)");
                String NameInput_string = scanner.next();
                verDel = NameInput_string.toLowerCase().charAt(0);
            }
            ClientCommunication clientCommunication = ClientCommunication.getInstance();
            String response = clientCommunication.sendQuery("DelMerchant:"+Id_del);
            if (response.equals("true")){
                logger.info("删除商家成功!");
            }
            else logger.info("没有找到该商家");

        } catch (IOException e) {
            logger.error(e);
        }
    }

}
