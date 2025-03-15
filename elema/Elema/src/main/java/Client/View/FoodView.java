package Client.View;

import Client.http.ClientCommunication;
import com.google.gson.reflect.TypeToken;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.apache.log4j.Logger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * @author xiezhr
 * @create 2024-04-28 11:29
 */
public class FoodView {
    Logger logger = Logger.getLogger(FoodView.class);
    UI UI = new UI();
    Scanner scanner = new Scanner(System.in);
    private static final Gson gson = new Gson();
    public void FoodInfo(int businessId) {
        UI.FoodHead();

        try {
            ClientCommunication clientCommunication = ClientCommunication.getInstance();
            String response = clientCommunication.sendQuery("FoodInfo:"+businessId);
            Type listType = new TypeToken<List<List<Object>>>() {}.getType();
            List<List<Object>> foodInfo = gson.fromJson(response, listType);
            for (List<Object> food : foodInfo) {
                for (Object field : food) {
                    System.out.print(field + "\t");
                }
                System.out.println();
            }
        } catch (IOException e) {
            logger.info(e);
        }
    }

    public void FoodNew(int businessId) {
        logger.info("请输入食品名称:");
        String foodName = scanner.next();
        logger.info("请输入食品介绍:");
        String foodExplain = scanner.next();
        logger.info("请输入食品价格:");
        float foodPrice = scanner.nextFloat();

        try {
            ClientCommunication clientCommunication = ClientCommunication.getInstance();
            String response = clientCommunication.sendQuery("FoodNew:"+businessId+":"+foodName+":"+foodExplain+":"+foodPrice);
            if (response.equals("true")) {
                logger.info("添加成功");
            } else {
                logger.info("添加失败");
            }
        } catch (IOException e) {
            logger.info(e);
        }
    }

    public void FoodUpdate(int businessId) {
        String columnName;
        Object value;
        char ifUpdate = 0;
        int foodIdUpdate = 0;
        String response = "false";
        FoodInfo(businessId);
        while (response.equals("false")) {
            logger.info("请选择要更新的食品编号:");
            foodIdUpdate = scanner.nextInt();
            try {
                ClientCommunication clientCommunication = ClientCommunication.getInstance();
                response = clientCommunication.sendQuery("FoodQuery:"+businessId+":"+foodIdUpdate);
                UI.FoodHead();
                logger.info(response);

            } catch (IOException e) {
                logger.error(e);
            }
            if (response.equals("false"))
                logger.info("不存在的编号!");
        }
        try {
            ClientCommunication clientCommunication = ClientCommunication.getInstance();
            while (ifUpdate != 'y' && ifUpdate != 'n') {
                logger.info("是否更新食品名称:(y/n)");
                String ifUpdate_string = scanner.next();
                ifUpdate = ifUpdate_string.toLowerCase().charAt(0);
            }
            if (ifUpdate == 'y') {
                columnName = "foodName";
                logger.info("请输入新的食品名称:");
                value = scanner.next();
                response = clientCommunication.sendQuery("FoodUpdate:"+businessId+":"+foodIdUpdate+":"+columnName+':'+value);
                if (response.equals("true")) {
                    logger.info("食品名称更新成功!");
                } else {
                    logger.error("更新失败!");
                }

            }

            ifUpdate = 0; //置0
            while (ifUpdate != 'y' && ifUpdate != 'n') {
                logger.info("是否更新食品介绍:(y/n)");
                String ifUpdate_string = scanner.next();
                ifUpdate = ifUpdate_string.toLowerCase().charAt(0);
            }
            if (ifUpdate == 'y') {
                columnName = "foodExplain";
                logger.info("请输入新的食品介绍:");
                value = scanner.next();
                response = clientCommunication.sendQuery("FoodUpdate:"+businessId+":"+foodIdUpdate+":"+columnName+':'+value);
                if (response.equals("true")) {
                    logger.info("食品介绍更新成功!");
                } else {
                    logger.error("更新失败!");
                }
            }

            ifUpdate = 0; //置0
            while (ifUpdate != 'y' && ifUpdate != 'n') {
                logger.info("是否更新食品价格:(y/n)");
                String ifUpdate_string = scanner.next();
                ifUpdate = ifUpdate_string.toLowerCase().charAt(0);
            }
            if (ifUpdate == 'y') {
                columnName = "foodPrice";
                logger.info("请输入食品价格:");
                value = scanner.nextFloat();
                response = clientCommunication.sendQuery("FoodUpdatePrice:"+businessId+":"+foodIdUpdate+":"+columnName+':'+value);
                if (response.equals("true")) {
                    logger.info("食品价格更新成功!");
                } else {
                    logger.error("更新失败!");
                }
            }

        } catch (IOException e) {
            logger.info(e);
        }

    }

    public void FoodDel(int businessId) {
        String hasFoodId = "true";
        char ifUpdate = 0;

        FoodInfo(businessId);
        while (hasFoodId.equals("true")) {
            logger.info("请选择要删除的食品编号");
            int foodDelId = scanner.nextInt();
            while (ifUpdate != 'y' && ifUpdate != 'n') {
                logger.info("确认要删除吗:(y/n)");
                String ifUpdate_string = scanner.next();
                ifUpdate = ifUpdate_string.toLowerCase().charAt(0);
            }
            if (ifUpdate == 'y') {
                try {
                    ClientCommunication clientCommunication = ClientCommunication.getInstance();
                    String response = clientCommunication.sendQuery("FoodDel:"+businessId+":"+foodDelId);
                    hasFoodId = response;

                } catch (IOException e) {
                    logger.info(e);
                }
            }
            if (hasFoodId.equals("true") && ifUpdate == 'y') {
                logger.info("不存在的编号!");
                ifUpdate = 0;
            }
        }

    }

}
