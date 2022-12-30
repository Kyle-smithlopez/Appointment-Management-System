package DAO;

import helper.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {


//    public static int validateUser(String userName, String password) throws SQLException
//    {
//        try
//        {
//            String sql = "SELECT * FROM users WHERE user_name = '" + userName + "' AND password = '" + password +"'";
//
//            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
//            ResultSet rs = ps.executeQuery();
//            rs.next();
//            if (rs.getString("User_Name").equals(userName))
//            {
//                if (rs.getString("Password").equals(password))
//                {
//                    return rs.getInt("User_ID");
//
//                }
//            }
//        }
//        catch (SQLException e)
//        {
//            e.printStackTrace();
//        }
//        return -1;
//    }

    public static int validateUser(String userName, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_name = ? AND password = ?";

        JDBC.openConnection();
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, userName);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int userId = rs.getInt("User_ID");
            JDBC.closeConnection();
            return userId;
        } else {
            JDBC.closeConnection();
            return -1;
        }
    }




}
