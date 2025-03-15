package Server.API;

import Client.View.AdminView;
import Server.Dao.AdminDao;
import Server.Dao.FoodDao;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * @author xiezhr
 * @create 2024-06-25 11:14
 */
public class FoodServer {


    private static FoodDao foodDao = new FoodDao();
    private static final Gson gson = new Gson();
    Logger logger = Logger.getLogger(AdminView.class);

    public static String handleRequest(String request) {

        String response = null;

        if (request.startsWith("FoodInfo:")) {
            String businessId_s = request.split(":")[1].trim();
            int businessId = Integer.valueOf(businessId_s);
            try {
                List<List<Object>> FoodInfo = foodDao.FoodListById(businessId);
                response = gson.toJson(FoodInfo); // 将结果转换为 JSON 字符串
            } catch (SQLException e) {
                response = "Database error: " + e.getMessage();
            }
        } else if (request.startsWith("FoodNew:")) {
            String businessId_s = request.split(":")[1].trim();
            int businessId = Integer.valueOf(businessId_s);
            String foodName = request.split(":")[2].trim();
            String foodExplain = request.split(":")[3].trim();
            String price_s = request.split(":")[4].trim();
            float price = Float.valueOf(price_s);
            try {
                response = foodDao.FoodNew(businessId, foodName, foodExplain, price);
            } catch (SQLException e) {

            }
        } else if (request.startsWith("FoodQuery:")) {
            String businessId_s = request.split(":")[1].trim();
            int businessId = Integer.valueOf(businessId_s);
            String foodUpdateId_s = request.split(":")[2].trim();
            int foodUpdateId = Integer.valueOf(foodUpdateId_s);
            try {
                response = foodDao.FoodQuery(businessId, foodUpdateId);
            } catch (SQLException e) {
                response = "Database error: " + e.getMessage();
            }
        } else if (request.startsWith("FoodUpdate:")) {
            String businessId_s = request.split(":")[1].trim();
            int businessId = Integer.valueOf(businessId_s);
            String foodUpdateId_s = request.split(":")[2].trim();
            int foodUpdateId = Integer.valueOf(foodUpdateId_s);
            String columnName = request.split(":")[3].trim();
            String value = request.split(":")[4].trim();
            try {
                response = foodDao.foodUpdateById(businessId, foodUpdateId, columnName, value);
            } catch (SQLException e) {
                response = "Database error: " + e.getMessage();
            }
        } else if (request.startsWith("FoodUpdatePrice:")) {
            String businessId_s = request.split(":")[1].trim();
            int businessId = Integer.valueOf(businessId_s);
            String foodUpdateId_s = request.split(":")[2].trim();
            int foodUpdateId = Integer.valueOf(foodUpdateId_s);
            String columnName = request.split(":")[3].trim();
            String value_s = request.split(":")[4].trim();
            float value = Float.valueOf(value_s);
            try {
                response = foodDao.foodUpdateById(businessId, foodUpdateId, columnName, value);
            } catch (SQLException e) {
                response = "Database error: " + e.getMessage();
            }
        } else if (request.startsWith("FoodDel:")) {
            String businessId_s = request.split(":")[1].trim();
            int businessId = Integer.valueOf(businessId_s);
            String foodDelId_s = request.split(":")[2].trim();
            int foodDelId = Integer.valueOf(foodDelId_s);
            try {
                response = foodDao.FoodDel(businessId, foodDelId);
            } catch (SQLException e) {
                response = "Database error: " + e.getMessage();
            }
        } else {
            response = "Invalid Food query";
        }
        return response;
    }
}