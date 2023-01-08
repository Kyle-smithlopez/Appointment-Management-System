package DAO;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*** The UserDAO class is used to access the User table in the database. */
public class UserDAO {

    /**
     * The validateUser method validates the user.
     */
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

    /**
     * The getUsers method retrieves the Users ID.
     */
    public static ObservableList<String> getUsers() throws SQLException {

        ObservableList<String> allUsers = FXCollections.observableArrayList();
        String sql = "SELECT * FROM USERS";
        JDBC.openConnection();
        Query.makeQuery(sql);
        ResultSet rs = Query.getResult();

        while (rs.next()) {
            int id = rs.getInt("User_ID");
            String name = rs.getString("User_Name");
            allUsers.add(String.valueOf(id));
        }
        JDBC.closeConnection();
        return allUsers;
    }

    // May not need.
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
