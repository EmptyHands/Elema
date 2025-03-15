package Server.Dao;

import Client.View.AdminView;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
    private static final String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String dbURL = "jdbc:sqlserver://localhost:1433;DatabaseName=Elema";
    private static final String userName = "Ernie";
    private static final String userPwd = "123";
    private static Connection connection;

    private static final Logger logger = Logger.getLogger(Connect.class);

    // 加载数据库驱动，并在静态初始化块中执行，以确保在首次访问时驱动已经加载
    static {
        try {
            Class.forName(driverName);
            logger.info("加载驱动成功！");
        } catch (ClassNotFoundException e) {
            logger.error("加载驱动失败！", e);
            throw new RuntimeException("数据库驱动加载失败", e);
        }
    }

    // 获取数据库连接的方法，使用双重检查锁定实现线程安全
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            synchronized (Connect.class) {
                if (connection == null || connection.isClosed()) {
                    connection = DriverManager.getConnection(dbURL, userName, userPwd);
                }
            }
        }
        return connection;
    }

    // 关闭数据库连接的方法
    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            connection = null; // 将connection置为null，以便下次调用getConnection时重新创建
        }
    }

    public void connect(){
        try {
            // 获取数据库连接
            connection = Connect.getConnection();

            if (connection != null && !connection.isClosed()) {
                logger.info("数据库连接成功！");
            } else {
                logger.error("无法获取数据库连接！");
            }
        } catch (SQLException e) {
            logger.error("数据库连接发生错误！", e);
        }
    }

}