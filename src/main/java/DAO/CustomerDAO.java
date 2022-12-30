package DAO;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class CustomerDAO {

    public static boolean addCustomer(String custName, String address, String postalCode, String Phone, int divisionId) throws SQLException {
        JDBC.openConnection();
        String sql = "INSERT INTO CUSTOMERS (Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, custName);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, Phone);
        ps.setInt(5, divisionId);

        try {
            // Use executeUpdate to execute the INSERT statement
            int rowsAffected = ps.executeUpdate();
            // Return true if the INSERT statement was successful (1 or more rows were affected)
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // Return false if the INSERT statement failed
            return false;
        } finally {
            JDBC.closeConnection();
        }
    }


    public static Boolean deleteCustomer(int custId) throws SQLException {
        JDBC.openConnection();
        String sql = "DELETE FROM CUSTOMERS WHERE CUSTOMER_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, custId);

        try {
            ps.executeUpdate();
            JDBC.closeConnection();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JDBC.closeConnection();
            return false;
        }
    }

//    public static Boolean updateCustomer(int custId, String custName, String address, String postalCode, String Phone, int divisionId) throws SQLException {
//
//        JDBC.openConnection();
//
//        String sql = "UPDATE CUSTOMERS (Customer_ID, Customer_Name, Address, Postal_Code, Phone, divisionId) VALUES(?, ?, ?, ?, ?, ?)";
//        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
//        ps.setInt(1, custId);
//        ps.setString(2, custName);
//        ps.setString(3, address);
//        ps.setString(4, postalCode);
//        ps.setString(5, Phone);
//        ps.setInt(5, divisionId);
//
//        try {
//            ps.executeUpdate();
//            JDBC.closeConnection();
//            return true;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            JDBC.closeConnection();
//            return false;
//        }
//    }

    public static boolean updateCustomer(int custId, String custName, String address, String postalCode, String phone, int divisionId) throws SQLException {
        String sql = "UPDATE CUSTOMERS SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?";

        JDBC.openConnection();
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, custName);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phone);
        ps.setInt(5, divisionId);
        ps.setInt(6, custId);

        try {
            // Use executeUpdate to execute the UPDATE statement
            int rowsAffected = ps.executeUpdate();
            JDBC.closeConnection();
            // Return true if the UPDATE statement was successful (1 or more rows were affected)
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JDBC.closeConnection();
            // Return false if the UPDATE statement failed
            return false;
        }
    }


    public static ObservableList<Customers> getAllCustomers() throws SQLException, Exception {
        ObservableList<Customers> allCustomers = FXCollections.observableArrayList();
        JDBC.openConnection();

        String sql = "SELECT c.Customer_ID, c.Customer_Name, c.Address, c.Postal_Code, c.Phone, c.Division_ID, first_level_divisions.Division, countries.Country FROM CUSTOMERS AS c LEFT JOIN first_level_divisions ON first_level_Divisions.Division_ID = c.Division_ID LEFT JOIN countries ON countries.Country_ID = first_level_divisions.Country_ID";

        Query.makeQuery(sql);
        ResultSet rs = Query.getResult();
        while (rs.next()) {
            int customerId = rs.getInt("Customer_ID");
            String customerName = rs.getString("Customer_Name");
            String customerAddress = rs.getString("Address");
            String postalCode = rs.getString("Postal_Code");
            String phone = rs.getString("Phone");
            String division = rs.getString("Division");
            int divisionId = rs.getInt("Division_ID");
            String country = rs.getString("Country");
            Customers customerResult = new Customers(customerId, customerName, customerAddress, postalCode, phone, divisionId, division, country);
            allCustomers.add(customerResult);
        }
        JDBC.closeConnection();
        return allCustomers;
    }
}
