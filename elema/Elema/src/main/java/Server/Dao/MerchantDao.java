package Server.Dao;

import Server.Entitles.Admin;
import Server.Entitles.Merchant;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author xiezhr
 * @create 2024-04-27 14:11
 */
public class MerchantDao {

    String queryAll = "SELECT businessId, businessName, businessAddress, businessExplain, starPrice, deliveryPrice FROM Merchant";
    String queryId = "SELECT businessId, businessName, businessAddress, businessExplain, starPrice, deliveryPrice FROM Merchant WHERE businessId = ?";

    private static final Logger logger = Logger.getLogger(MerchantDao.class);

    public List<List<Object>> MerchantAll() throws SQLException {
        Connection conn = Server.Dao.Connect.getConnection();
        String sql = queryAll;

        List<List<Object>> result = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (rs.next()) {
                    List<Object> row = new ArrayList<>();
                    for (int i = 1; i <= columnCount; i++) {

                        row.add(String.valueOf(rs.getObject(i)));
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
    public List<List<Object>> MerchantSearch(String Name_search, String Address_search) throws SQLException {
        Connection conn = Server.Dao.Connect.getConnection();

        // 根据传入的参数构建SQL查询
        StringBuilder sqlBuilder = new StringBuilder("SELECT businessId, businessName, businessAddress, businessExplain, starPrice, deliveryPrice FROM Merchant");
        boolean hasWhere = false;



        List<List<Object>> result = new ArrayList<>();

        if (Name_search != null && !Name_search.isEmpty()) {
            if (hasWhere) {
                sqlBuilder.append(" AND ");
            } else {
                sqlBuilder.append(" WHERE ");
                hasWhere = true;
            }
            sqlBuilder.append("businessName LIKE ?");
        }

        if (Address_search != null && !Address_search.isEmpty()) {
            if (hasWhere) {
                sqlBuilder.append(" AND ");
            } else {
                sqlBuilder.append(" WHERE ");
                hasWhere = true;
            }
            sqlBuilder.append("businessAddress LIKE ?");
        }

        String sql = sqlBuilder.toString();


        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            int index = 1;

            if (Name_search != null && !Name_search.isEmpty()) {
                ps.setString(index++, "%" + Name_search + "%"); // 使用通配符进行模糊搜索
            }

            if (Address_search != null && !Address_search.isEmpty()) {
                ps.setString(index++, "%" + Address_search + "%");
            }

            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (rs.next()) {
                    List<Object> row = new ArrayList<>();
                    for (int i = 1; i <= columnCount; i++) {
                        Object columnValue = rs.getObject(i);
                        String valueStr = (columnValue != null) ? columnValue.toString() : " ";
                        row.add(valueStr); // 确保所有值为字符串
                    }
                    result.add(row);
                }
            } catch (SQLException e) {
                logger.error("查询数据库时出错: " + e.getMessage(), e);
                throw new SQLException("Error while executing query", e);
            }
        } catch (SQLException e) {
            logger.error("连接数据库时出错: " + e.getMessage(), e);
            throw new SQLException("Error while preparing statement", e);
        }

        return result;
    }





    public int MerchantNew(String newName) throws SQLException {
        Connection conn = Server.Dao.Connect.getConnection();
        int businessId = 0;

        // 插入新的businessName
        String insertSql = "INSERT INTO Merchant (businessName, password) VALUES (?, ?)";
        try (PreparedStatement psInsert = conn.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            psInsert.setString(1, newName);
            psInsert.setString(2, "000");
            psInsert.executeUpdate(); // 执行插入操作

            // 使用getGeneratedKeys获取生成的键
            try (ResultSet rsGeneratedKeys = psInsert.getGeneratedKeys()) {
                if (rsGeneratedKeys.next()) {
                    businessId = rsGeneratedKeys.getInt(1); // 获取businessId
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return businessId;
    }

    public boolean MerchantDel(int Id_del) throws SQLException {
        Connection conn = Server.Dao.Connect.getConnection();

        String deleteSql = "DELETE FROM Merchant WHERE businessId = ?";
        boolean hasDel = false;

        try (PreparedStatement ps = conn.prepareStatement(deleteSql)) {
            // 设置占位符的值
            ps.setInt(1, Id_del);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected != 0) {
                hasDel = true;
            }

        } catch (SQLException e) {
            logger.error(e);
        }
        return hasDel;

    }

    public Merchant getMerchantById(int businessId) throws SQLException{
        Connection conn = Server.Dao.Connect.getConnection();
        String sql = "SELECT * FROM Merchant WHERE businessId = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, businessId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Merchant(rs.getInt("businessId"), rs.getString("password"));
                } else {
                    return null;
                }
            }
        }
    }

    public List<List<Object>> MerchantQuery(int businessId) throws SQLException {
        Connection conn = Server.Dao.Connect.getConnection();

        List<List<Object>> result = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(queryId)) {
            ps.setInt(1, businessId); // 设置占位符的值

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

    public String MerchantUpdateSome(int businessId, String columnName, Object value) throws SQLException {
        Connection conn = Server.Dao.Connect.getConnection();
        String result = "false";

        List<String> allowedColumns = Arrays.asList("businessName", "businessAddress", "businessExplain", "starPrice", "deliveryPrice", "password");

        if (!allowedColumns.contains(columnName)) {
            throw new IllegalArgumentException("列名不合法" + columnName);

        }

        String sql = "UPDATE Merchant SET " + columnName + " = ? WHERE businessId = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            if (value instanceof String) {
                ps.setString(1, (String) value);
            } else if (value instanceof Integer) {
                ps.setInt(1, (Integer) value);
            } else if (value instanceof Float) {
                ps.setFloat(1, (float) value);
            } else {
                throw new IllegalArgumentException("数据类型不合法: " + columnName);
            }

            ps.setInt(2, businessId);

            int updatedRows = ps.executeUpdate();
            if (updatedRows != 0) {
                result = "true";
            }

        } catch (SQLException e) {
            logger.error(e);
        }

        return result;
    }

}