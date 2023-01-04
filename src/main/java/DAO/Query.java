package DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static helper.JDBC.connection;

public class Query {

    private static String query;
    private static Statement stmt;
    private static ResultSet result;


    public static ResultSet getAppointmentById(int id) throws SQLException {
//        JDBC.openConnection();
        String sql = "SELECT start, end FROM appointments WHERE APPOINTMENT_Id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
//        JDBC.closeConnection();
        return ps.executeQuery();
    }

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
