package Client.View;

import Client.http.ClientCommunication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.corba.se.spi.activation.Server;
import org.apache.log4j.Logger;
import Client.View.UI;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Scanner;

public class MerchantView {

    Logger logger = Logger.getLogger(MerchantView.class);
    Scanner scanner = new Scanner(System.in);
    private ClientCommunication clientCommunication;
    private static final Gson gson = new Gson();
    private static MerchantView instance;
    UI UI = new UI();

    private MerchantView(){
        try {
            clientCommunication = ClientCommunication.getInstance();
        } catch (IOException e) {
            // 处理连接异常
            logger.error(e);
        }
    }

    public static synchronized MerchantView getInstance() {
        if (instance == null) {
            instance = new MerchantView();
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

    public void MerchantEntry() {
        int businessId;
        String password;
        String input;
        View view = View.getInstance();

        try {
            ClientCommunication clientCommunication = ClientCommunication.getInstance();
            String response;

            while (true) {
                logger.info("请输入商家编号:");
                businessId = scanner.nextInt();

                response = clientCommunication.sendQuery("MerchantId:" + businessId);
                if (response.startsWith("Password:")) {
                    password = response.split(":")[1];
                    logger.info("请输入密码:");
                    input = scanner.next();

                    if (input.equals(password)) {
                        view.MerchantView(businessId);
                        break; // 退出循环
                    } else {
                        logger.error("密码错误, 请重新输入商家编号.");
                    }

                } else {
                    logger.error("商家不存在, 请重新输入");
                }
            }
        } catch (IOException e) {
            logger.error(e);
        }

    }

    public void MerchantInfo(int businessId) {
        try {
            ClientCommunication clientCommunication = ClientCommunication.getInstance();
            String response = clientCommunication.sendQuery("MerchantInfo:"+businessId);

            Type listType = new TypeToken<List<List<Object>>>() {}.getType();
            List<List<Object>> merchantQuery = gson.fromJson(response, listType);

            UI.MerchantHead();
            for (List<Object> merchant : merchantQuery) {
                for (Object field : merchant) {
                    System.out.print(field + "\t");
                }
                System.out.println();
            }

        } catch (IOException e) {
            logger.error(e);
        }

    }

    public void MerchantUpdate(int businessId) {
        String columName;
        Object value;
        char ifUpdate = 0;
        try {
            ClientCommunication clientCommunication = ClientCommunication.getInstance();
            while (ifUpdate != 'y' && ifUpdate != 'n') {
                logger.info("是否修改商家名称:(y/n)");
                String ifUpdate_string = scanner.next();
                ifUpdate = ifUpdate_string.toLowerCase().charAt(0);
            }
            if (ifUpdate == 'y') {
                columName = "businessName";
                logger.info("请输入新的商家名称:");
                value = scanner.next();
                String response = clientCommunication.sendQuery("MerchantUpdate:"+businessId+":"+columName+":"+value);
                if (response.equals("true")) {
                    logger.info("修改成功");
                }
            }

            ifUpdate = 0; //置0
            while (ifUpdate != 'y' && ifUpdate != 'n') {
                logger.info("是否修改商家地址:(y/n)");
                String ifUpdate_string = scanner.next();
                ifUpdate = ifUpdate_string.toLowerCase().charAt(0);
            }
            if (ifUpdate == 'y') {
                columName = "businessAddress";
                logger.info("请输入新的商家地址:");
                value = scanner.next();
                String response = clientCommunication.sendQuery("MerchantUpdate:"+businessId+":"+columName+":"+value);
                if (response.equals("true")) {
                    logger.info("修改成功");
                }
            }

            ifUpdate = 0; //置0
            while (ifUpdate != 'y' && ifUpdate != 'n') {
                logger.info("是否修改商家介绍:(y/n)");
                String ifUpdate_string = scanner.next();
                ifUpdate = ifUpdate_string.toLowerCase().charAt(0);
            }
            if (ifUpdate == 'y') {
                columName = "businessExplain";
                logger.info("请输入新的商家介绍:");
                value = scanner.next();
                String response = clientCommunication.sendQuery("MerchantUpdate:"+businessId+":"+columName+":"+value);
                if (response.equals("true")) {
                    logger.info("修改成功");
                }
            }

            ifUpdate = 0; //置0
            while (ifUpdate != 'y' && ifUpdate != 'n') {
                logger.info("是否修改起送费:(y/n)");
                String ifUpdate_string = scanner.next();
                ifUpdate = ifUpdate_string.toLowerCase().charAt(0);
            }
            if (ifUpdate == 'y') {
                columName = "starPrice";
                logger.info("请输入新的起送费:");
                value = scanner.nextFloat();
                String response = clientCommunication.sendQuery("MerchantUpdate:"+businessId+":"+columName+":"+value);
                if (response.equals("true")) {
                    logger.info("修改成功");
                }
            }

            ifUpdate = 0; //置0
            while (ifUpdate != 'y' && ifUpdate != 'n') {
                logger.info("是否修改配送费:(y/n)");
                String ifUpdate_string = scanner.next();
                ifUpdate = ifUpdate_string.toLowerCase().charAt(0);
            }
            if (ifUpdate == 'y') {
                columName = "deliveryPrice";
                logger.info("请输入新的配送费:");
                value = scanner.nextFloat();
                String response = clientCommunication.sendQuery("MerchantUpdate:"+businessId+":"+columName+":"+value);
                if (response.equals("true")) {
                    logger.info("修改成功");
                }
            }
            logger.info("修改商家信息成功!");

        } catch (IOException e) {
            logger.error(e);
        }
    }

    public void MerchantPassword(int businessId) {
        String password;
        String passwordNew1;
        String passwordNew2;
        String input;

        try {
            ClientCommunication clientCommunication = ClientCommunication.getInstance();
            String response = clientCommunication.sendQuery("MerchantId:" + businessId);
            if (response.startsWith("Password:")) {
                password = response.split(":")[1];
                while (true) {
                    logger.info("请输入旧密码:");
                    input = scanner.next();

                    if (input.equals(password)) {
                        break; // 退出循环
                    } else {
                        logger.info("密码错误!");
                    }
                }

                while(true) {
                    logger.info("请输入新密码:");
                    passwordNew1 = scanner.next();
                    logger.info("请再次输入新密码:");
                    passwordNew2 = scanner.next();
                    if (passwordNew1.equals(passwordNew2)){
                        String columnName = "password";
                        response = clientCommunication.sendQuery("MerchantUpdate:"+businessId+":"+columnName+":"+passwordNew1);
                        if (response.equals("true")) {
                            logger.info("修改密码成功");
                            break;
                        }

                    } else {
                        logger.info("两次输入密码必须相同");
                    }
                }

            } else {
                logger.error("原密码获取异常!");
            }

        } catch (IOException e) {
            logger.error(e);
        }

    }

}
