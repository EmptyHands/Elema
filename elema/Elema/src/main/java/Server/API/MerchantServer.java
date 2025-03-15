package Server.API;

import Client.View.AdminView;
import Server.Dao.AdminDao;
import Server.Dao.MerchantDao;
import Server.Entitles.Admin;
import Server.Entitles.Merchant;
import com.google.gson.Gson;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiezhr
 * @create 2024-06-25 8:37
 */
public class MerchantServer {

    private static MerchantDao merchantDao = new MerchantDao();
    private static final Gson gson = new Gson();
    Logger logger = Logger.getLogger(AdminView.class);

    public static String handleRequest(String request) {
        String response;
        if (request.startsWith("MerchantId:")) {
            String businessId_s = request.split(":")[1].trim();
            int businessId = Integer.parseInt(businessId_s);
            try {
                Merchant merchant = merchantDao.getMerchantById(businessId);
                if (merchant != null) {
                    // 处理管理员请求的逻辑，例如验证密码等
                    response = "Password:" + merchant.getPassword();
                } else {
                    response = "Merchant not found";
                }
            } catch (SQLException e) {
                response = "Database error: " + e.getMessage();
            }
        } else if (request.startsWith("MerchantInfo:")) {
            String businessId_s = request.split(":")[1].trim();
            int businessId = Integer.parseInt(businessId_s);
            try {
                List<List<Object>> adminListResult = merchantDao.MerchantQuery(businessId);
                response = gson.toJson(adminListResult); // 将结果转换为 JSON 字符串
            } catch (SQLException e) {
                response = "Database error: " + e.getMessage();
            }
        } else if (request.startsWith("MerchantUpdate:")) {
            String businessId_s = request.split(":")[1].trim();
            int businessId = Integer.parseInt(businessId_s);
            String columName = request.split(":")[2].trim();
            String value = request.split(":")[3].trim();
            try {
                response = merchantDao.MerchantUpdateSome(businessId, columName, value);
            } catch (SQLException e) {
                response = "Database error: " + e.getMessage();
            }
        } else {
            response = "Invalid Admin query";
        }
        return response;
    }
}
