package DAO;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointments;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class AppointmentsDAO {

    //CREATE
    public static boolean addAppointment(String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int custId, int userId, int contactId) throws SQLException {
        JDBC.openConnection();
        // Format inputStart and inputEnd
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String inputStart = start.format(formatter).toString();
        String inputEnd = end.format(formatter).toString();

        String sql = "INSERT INTO APPOINTMENTS (Title, Type, Description, Location, Start, End, Contact_ID, Customer_ID, User_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, type);
        ps.setString(3, description);
        ps.setString(4, location);

        ps.setString(5, inputStart);
        ps.setString(6, inputEnd);

        ps.setInt(7, contactId);
        ps.setInt(8, custId);
        ps.setInt(9, userId);

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

    //UPDATE - CURRENTLY WORKING ON IT 1/3/2023
//    public static boolean updateAppointment(int custId, String custName, String address, String postalCode, String phone, int divisionId) throws SQLException {
//        String sql = "UPDATE CUSTOMERS SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?";
//
//        JDBC.openConnection();
//        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
//        ps.setString(1, custName);
//        ps.setString(2, address);
//        ps.setString(3, postalCode);
//        ps.setString(4, phone);
//        ps.setInt(5, divisionId);
//        ps.setInt(6, custId);
//
//        try {
//            // Use executeUpdate to execute the UPDATE statement
//            int rowsAffected = ps.executeUpdate();
//            JDBC.closeConnection();
//            // Return true if the UPDATE statement was successful (1 or more rows were affected)
//            return rowsAffected > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            JDBC.closeConnection();
//            // Return false if the UPDATE statement failed
//            return false;
//        }
//    }

    //DELETION
    public static Boolean deleteAppointment(int apptId) throws SQLException {
        JDBC.openConnection();
        String sql = "DELETE FROM APPOINTMENTS WHERE APPOINTMENT_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, apptId);

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

    public static ObservableList<String> getType() throws SQLException {

        ObservableList<String> allTypes = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT * FROM APPOINTMENTS";
        JDBC.openConnection();
        Query.makeQuery(sql);
        ResultSet rs = Query.getResult();

        while (rs.next()) {

            String type = rs.getString("Type");
            if (!allTypes.contains(type)) {
                // Add the type to the list if it's not already present
                allTypes.add(type);
            }
        }
        JDBC.closeConnection();
        return allTypes;
    }

    public static ObservableList<Appointments> getAllAppointments() throws SQLException, Exception {
        ObservableList<Appointments> allAppointments = FXCollections.observableArrayList();
        JDBC.openConnection();

        String sql = "SELECT * FROM APPOINTMENTS";

        Query.makeQuery(sql);
        ResultSet rs = Query.getResult();
        while (rs.next()) {
            int apptId = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String type = rs.getString("Type");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
            int contactId = rs.getInt("Contact_ID");
            int custId = rs.getInt("Customer_ID");
            int userId = rs.getInt("User_ID");
            Appointments appointmentResult = new Appointments(apptId, title, description, location, type, start, end, custId, userId, contactId);
            allAppointments.add(appointmentResult);
        }
        JDBC.closeConnection();
        return allAppointments;
    }
}

