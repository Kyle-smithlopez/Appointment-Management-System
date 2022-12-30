package DAO;

import helper.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static helper.JDBC.connection;

public class Query {

    private static String query;
    private static Statement stmt;
    private static ResultSet result;

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
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            int userId = rs.getInt("User_ID");
            String userName = rs.getString("User_Name");
            System.out.println(userId + " " + userName);
        }
    }

    public static void makeQuery(String q){
        query = q;
        try{
            stmt=connection.createStatement();
            // determine query execution
            if(query.toLowerCase().startsWith("select"))
                result = stmt.executeQuery(q);
            if(query.toLowerCase().startsWith("delete") || query.toLowerCase().startsWith("insert") || query.toLowerCase().startsWith("update"))
                stmt.executeUpdate(q);
        }
        catch(Exception ex){
            System.out.println("Error: " + ex.getMessage());
        }
    }
    public static ResultSet getResult(){
        return result;
    }

}
