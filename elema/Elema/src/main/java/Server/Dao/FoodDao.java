package Server.Dao;

import org.apache.log4j.Logger;
import java.util.List;
import java.util.ArrayList;
import javax.management.Query;
import java.sql.*;

/**
 * @author xiezhr
 * @create 2024-04-28 11:29
 */
public class FoodDao {
    private static final Logger logger = Logger.getLogger(FoodDao.class);
    public List<List<Object>> FoodListById(int businessId) throws SQLException {
        Connection conn = Server.Dao.Connect.getConnection();
        String sql = "SELECT foodId, foodName, foodExplain, foodPrice FROM Food WHERE businessId = ?";

        List<List<Object>> result = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, businessId);

            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (rs.next()) {
                    List<Object> row = new ArrayList<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.add(rs.getObject(i));
                    }
                    result.add(row);
                }

            } catch (SQLException e) {
                throw new SQLException("Error while executing query", e);
            }
        } catch (SQLException e) {
            throw new SQLException("Error while preparing statement", e);
        }

        return result;
    }

    public String FoodNew(int businessId, String foodName, String foodExplain, float foodPrice) throws SQLException {
        Connection conn = Server.Dao.Connect.getConnection();
        String result = "false";

        String sql = "INSERT INTO Food (businessId, foodName, foodExplain, foodPrice) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, businessId);
            ps.setString(2, foodName);
            ps.setString(3, foodExplain);
            ps.setFloat(4, foodPrice);

            // 执行插入操作
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                result = "true";
            }

        } catch (SQLException e) {
            // 处理SQLException
            logger.error(e);
        }
        return result;
    }

    public String foodUpdateById(int businessId, int foodIdUpdate, String columnName, Object value) throws SQLException {
        Connection conn = Server.Dao.Connect.getConnection();
        String result = "false";

        String sql = "UPDATE Food SET " + columnName + " = ? WHERE businessId = ? AND foodId = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            // 设置更新值
            ps.setObject(1, value);
            // 设置查询条件
            ps.setInt(2, businessId);
            ps.setInt(3, foodIdUpdate);

            // 执行更新操作
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                result = "true";
            }
        } catch (SQLException e) {
            // 处理SQLException
            logger.error(e);
            throw e; // 可以选择重新抛出异常，让调用者处理
        }
        return result;
    }

    public String FoodQuery(int businessId, int foodUpdateId) throws SQLException {
        Connection conn = Server.Dao.Connect.getConnection();
        String result = "false";

        String queryId = "SELECT foodId, foodName, foodExplain, foodPrice " +
                "FROM Food WHERE businessId = ? AND foodId = ?";

        try (PreparedStatement ps = conn.prepareStatement(queryId)) {
            // 设置占位符的值
            ps.setInt(1, businessId);
            ps.setInt(2, foodUpdateId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = String.valueOf(rs.getInt("foodId"));
                    result = result+"\t"+rs.getString("foodName");
                    result = result+"\t"+rs.getString("foodExplain");
                    result = result+"\t"+rs.getDouble("foodPrice");
                    result = result+"\t"+businessId;

                }
            } catch (SQLException e) {
                logger.error(e);
                throw e;
            }
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
        return result;
    }

    public String FoodDel(int businessId, int foodDelId) throws SQLException {
        Connection conn = Server.Dao.Connect.getConnection();
        String result = "true";

        String deleteId = "DELETE FROM Food WHERE businessId = ? AND foodId = ?";

        try (PreparedStatement ps = conn.prepareStatement(deleteId)) {
            // 设置占位符的值
            ps.setInt(1, businessId);
            ps.setInt(2, foodDelId);

            // 执行删除操作
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                result = "false";
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        return result;
    }

}
