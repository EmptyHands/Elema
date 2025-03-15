package Server.API;

import Client.View.AdminView;
import Server.Dao.AdminDao;
import Server.Dao.MerchantDao;
import Server.Entitles.Admin;
import com.google.gson.Gson;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminServer {

    private static AdminDao adminDao = new AdminDao(); // 假设 AdminDao 可以被静态访问
    private static MerchantDao merchantDao = new MerchantDao();
    private static final Gson gson = new Gson();
    Logger logger = Logger.getLogger(AdminView.class);

    public static String handleRequest(String request) {
        String response;

        if (request.startsWith("AdminName:")) {
            String adminName = request.split(":")[1].trim(); // 获取管理员名称
            try {
                Admin admin = adminDao.getAdminByName(adminName); // 查询数据库获取管理员信息
                if (admin != null) {
                    // 处理管理员请求的逻辑，例如验证密码等
                    response = "Password:" + admin.getPassword();
                } else {
                    response = "Admin not found";
                }
            } catch (SQLException e) {
                response = "Database error: " + e.getMessage();
            }
        } else if (request.equals("AdminList")) {
            try {
                List<List<Object>> adminListResult = merchantDao.MerchantAll();
                response = gson.toJson(adminListResult); // 将结果转换为 JSON 字符串
            } catch (SQLException e) {
                response = "Database error: " + e.getMessage();
            }
        } else if (request.startsWith("MerchantSearch:")) {
            String Name_search  = request.split(":")[1].trim();
            String Address_search = request.split(":")[2].trim();
            if (Address_search.equals("null")) {
                Address_search = null;
            }
            if (Name_search.equals("null")) {
                Name_search = null;
            }
            try {
                List<List<Object>> MerchantSearchResult = merchantDao.MerchantSearch(Name_search, Address_search);
                response = gson.toJson(MerchantSearchResult);
            } catch (SQLException e){
                response = "Database error: " + e.getMessage();
            }
        } else if (request.startsWith("MerchantNew:")) {
            String newName = request.split(":")[1].trim();
            try{
                int businessId = merchantDao.MerchantNew(newName);
                response = String.valueOf(businessId);
            } catch (SQLException e) {
                response = "Database error: " + e.getMessage();
            }
        } else if (request.startsWith("DelMerchant:")) {
            String Id_del_s = request.split(":")[1].trim();
            int Id_del = Integer.parseInt(Id_del_s);
            try{
                 boolean hasDel = merchantDao.MerchantDel(Id_del);
                 response = String.valueOf(hasDel);
            } catch (SQLException e) {
                response = "Database error: " + e.getMessage();
            }
        } else {
            response = "Invalid Admin query";
        }

        return response;
    }
}
