package DAO;

import helper.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Query {

    //Insert
//    public static int insert(String fruitName, int colorId) throws SQLException {
//        String sql = "INSERT INTO FRUITS (FruitName, Color_Id) VALUES(?, ?)";
//        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
//        ps.setString(1, fruitName);
//        ps.setInt(2, colorId);
//        int rowsAffected = ps.executeUpdate();
//        return rowsAffected;
//    }
    //Update
//    public static int update(int fruitId, String fruitName) throws SQLException {
//        String sql = "UPDATE FRUITS SET Fruit_Name = ? WHERE Fruit_ID = ?";
//        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
//        ps.setString(1, fruitName);
//        ps.setInt(2, fruitId);
//        int rowsAffected = ps.executeUpdate();
//        return rowsAffected;
//    }
    //Delete
//    public static int delete(int fruitId) throws SQLException {
//        String sql = "DELETE FROM FRUITS WHERE Fruit_ID = ?";
//        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
//        ps.setInt(1, fruitId);
//        int rowsAffected = ps.executeUpdate();
//        return rowsAffected;
//    }

    public static void select() throws SQLException {
        String sql = "SELECT * FROM USERS";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            int userId = rs.getInt("User_ID");
            String userName = rs.getString("User_Name");
            System.out.println(userId + " " + userName);
        }
    }

    public static void select(int UserId) throws SQLException {
        String sql = "SELECT * FROM USERS WHERE User_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1,UserId);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            int userId = rs.getInt("User_ID");
            String userName = rs.getString("User_Name");
            int userIdFK = rs.getInt("User_ID");
            System.out.println(userId + " " + userName);
        }
    }
}
