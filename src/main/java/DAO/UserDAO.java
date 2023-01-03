package DAO;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

    public static ObservableList<String> getUsers() throws SQLException {

        ObservableList<String> allUsers = FXCollections.observableArrayList();
        String sql = "SELECT * FROM USERS";
        JDBC.openConnection();
        Query.makeQuery(sql);
        ResultSet rs = Query.getResult();

        while (rs.next()) {
            int id = rs.getInt("User_ID");
//            String id = rs.getString("User_ID");
            String name = rs.getString("User_Name");
//            allUsers.add(name + " [" + id + "]");
            allUsers.add(String.valueOf(id));
        }
        JDBC.closeConnection();
        return allUsers;
    }

    public static int getUserId(String userName) throws SQLException {
        int userId = -1;
        JDBC.openConnection();
        String sql = "SELECT USER_ID FROM USERS WHERE USER_NAME = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, userName);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            userId = rs.getInt("USER_ID");
        }
        JDBC.closeConnection();
        return userId;
    }

}
