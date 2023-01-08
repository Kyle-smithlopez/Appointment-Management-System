package DAO;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The CustomerDAO class is used to access the Customers table.
 */
public abstract class CustomerDAO {
    //Create

    /**
     * The addCustomer method creates a new customer in the database.
     */
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
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // Return false if the INSERT statement failed
            return false;
        } finally {
            JDBC.closeConnection();
        }
    }

    //Deletion

    /**
     * The deleteCustomer method deletes a customer from the database.
     */
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

    //Checks if Customer has an appointment.
    public static boolean hasAppointments(int customerId) throws SQLException {
        JDBC.openConnection();
        // Check if there are any appointments associated with the customer
        String sql = "SELECT COUNT(*) FROM appointments WHERE CUSTOMER_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerId);
        ResultSet rs = ps.executeQuery();
        int count = 0;
        if (rs.next()) {
            count = rs.getInt(1);
        }
        JDBC.closeConnection();
        // Return true if there are appointments associated with the customer.
        return count > 0;
    }


    //Create

    /**
     * The updateCustomer method updates the selected customer in the database.
     */
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
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JDBC.closeConnection();
            // Return false if the UPDATE statement failed
            return false;
        }
    }

    /**
     * The getCustomers method retrieves the customers name to populate the customers ComboBox.
     */
    public static ObservableList<String> getCustomers() throws SQLException {

        ObservableList<String> allCustomers = FXCollections.observableArrayList();
        String sql = "SELECT * FROM CUSTOMERS";
        JDBC.openConnection();
        Query.makeQuery(sql);
        ResultSet rs = Query.getResult();

        while (rs.next()) {
            String name = rs.getString("Customer_Name");
//            String custId = rs.getString("Customer_ID");
            int custId = rs.getInt("Customer_ID");
//            allCustomers.add(name + " " + custId);
            allCustomers.add(name);
        }
        JDBC.closeConnection();
        return allCustomers;
    }

    /**
     * The getCustomerID method retrieves the customers ID.
     */
    public static int getCustomerId(String custId) throws SQLException {
        int customerId = -1;
        JDBC.openConnection();
        String sql = "SELECT CUSTOMER_ID FROM CUSTOMERS WHERE CUSTOMER_NAME = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, custId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            customerId = rs.getInt("CUSTOMER_ID");
        }
        JDBC.closeConnection();
        return customerId;
    }

    /**
     * The getCustomerName method retrieves the customers name associated with the Customer ID.
     */
    public static String getCustomerName(int customerId) throws SQLException {
        String sql = "SELECT CUSTOMER_NAME FROM CUSTOMERS WHERE CUSTOMER_ID = ?";

        try {
            JDBC.openConnection();
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            // If the result set has a row, retrieve the customer name
            if (rs.next()) {
                return rs.getString("CUSTOMER_NAME");
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw e;
        } finally {
            JDBC.closeConnection();
        }
        // Return null if no customer was found with the given ID
        return null;
    }

    /**
     * The getAllCustomers method retrieves all customers in the database.
     */
    public static ObservableList<Customers> getAllCustomers() throws Exception {
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
