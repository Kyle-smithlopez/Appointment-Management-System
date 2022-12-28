package DAO;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class CustomerDAO {

//    public static int addCustomer(String custName, int custId) {
//
//    }
    public static void select() throws SQLException {
        String sql = "SELECT * FROM CUSTOMERS";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            int customerID = rs.getInt("Customer_ID");
            String customerName = rs.getString("Customer_Name");
            String customerAddress = rs.getString("Address");
            String postalCode = rs.getString("Postal_Code");
            String phone = rs.getString("Phone");
        }
    }

    public static ObservableList<Customers> getAllCustomers() throws SQLException, Exception{
        ObservableList<Customers> allCustomers = FXCollections.observableArrayList();
//        JDBC.openConnection();
        String sql = "SELECT c.Customer_ID, c.Customer_Name, c.Address, c.Postal_Code, c.Phone, c.Division_ID, first_level_divisions.Division FROM CUSTOMERS AS c LEFT JOIN  first_level_divisions ON first_level_Divisions.Division_ID = c.Division_ID";

        Query.makeQuery(sql);
        ResultSet rs = Query.getResult();
        while(rs.next()) {
            int customerId = rs.getInt("Customer_ID");
            String customerName = rs.getString("Customer_Name");
            String customerAddress = rs.getString("Address");
            String postalCode = rs.getString("Postal_Code");
            String phone = rs.getString("Phone");
            String division = rs.getString("Division");
            int divisionId = rs.getInt("Division_ID");
//            String country = rs.getString("country");
            Customers customerResult = new Customers(customerId, customerName, customerAddress, postalCode, phone, divisionId, division);
            allCustomers.add(customerResult);
        }
//        JDBC.closeConnection();
        return allCustomers;
    }
}
