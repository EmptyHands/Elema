package Server.Dao;

import Server.Entitles.Admin;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDao {

    public Admin getAdminByName(String adminName) throws SQLException {
        Connection conn = Server.Dao.Connect.getConnection();

        String sql = "SELECT * FROM Admin WHERE adminName = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, adminName);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Admin(rs.getString("adminName"), rs.getString("password"));
                } else {
                    return null;
                }
            }
        }
    }


}