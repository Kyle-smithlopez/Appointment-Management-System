package DAO;

import helper.JDBC;
import javafx.beans.property.adapter.JavaBeanDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;

import javax.xml.transform.Result;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    static boolean act;

//    public static User getUser(String userName) throws SQLException, Exception {
//        JDBC.openConnection();
//        String sqlStatement = "SELECT * FROM USERS WHERE USER_NAME = '" + userName + "'";
//        Query.makeQuery(sqlStatement);
//        User userResult;
//        ResultSet result = Query.getResult();
//        while(result.next()) {
//            int userId = result.getInt("User_ID");
//            String userName = result.getString("User_Name");
//            String password = result.getString("Password");
//            userResult = new User(userId,userName, password);
//            return userResult;
//        }
//        JDBC.closeConnection();
//        return null;
//    }

    public static int validateUser(String userName, String password) throws SQLException
    {
        try
        {
            String sql = "SELECT * FROM users WHERE user_name = '" + userName + "' AND password = '" + password +"'";

            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            rs.next();
            if (rs.getString("User_Name").equals(userName))
            {
                if (rs.getString("Password").equals(password))
                {
                    return rs.getInt("User_ID");

                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return -1;
    }
//    public static ObservableList<User> getAllUsers() throws SQLException, Exception{
//        ObservableList<User> allUsers = FXCollections.observableArrayList();
//        JDBC.openConnection();
//        String sqlStatement = "SELECT * FROM USERS";
//        Query.makeQuery(sqlStatement);
//        ResultSet result = Query.getResult();
//        while (result.next()) {
//            int userId = result.getInt("User_ID");
//            String userName = result.getString("User_Name");
//            String password = result.getString("Password");
//            User userResult = new User(userId, userName, password);
//            allUsers.add(userResult);
//        }
//        JDBC.closeConnection();
//        return allUsers;
//    }
}
