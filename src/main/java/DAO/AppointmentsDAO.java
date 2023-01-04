package DAO;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointments;

import java.sql.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public abstract class AppointmentsDAO {

    //CREATE
    public static boolean addAppointment(String title, String description, String location, String type, ZonedDateTime start, ZonedDateTime end, int custId, int userId, int contactId) throws SQLException {
        JDBC.openConnection();
        // Format inputStart and inputEnd
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String inputStart = start.format(formatter);
        String inputEnd = end.format(formatter);

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

    //UPDATE
    public static boolean updateAppointment(int apptId, String title, String description, String location, String type, ZonedDateTime start, ZonedDateTime end, int custId, int userId, int contactId) throws SQLException {

        // Format inputStart and inputEnd
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String inputStart = start.format(formatter);
        String inputEnd = end.format(formatter);

        String sql = "UPDATE appointments SET title = ?, description = ?, location = ?, type = ?, start = ?, end = ?, customer_id = ?, user_id = ?, contact_id = ? WHERE APPOINTMENT_ID = ?";

        JDBC.openConnection();
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);

        ps.setString(5, inputStart);
        ps.setString(6, inputEnd);

        ps.setInt(7, custId);
        ps.setInt(8, userId);
        ps.setInt(9, contactId);
        ps.setInt(10, apptId);

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

//     MAY NOT NEED - Commented out 1/3/2023
//    public static String getContactName(int contactId) throws SQLException {
//        JDBC.openConnection();
//        String sql = "SELECT contact FROM contacts WHERE contact_id = ?";
//        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
//        ps.setInt(1, contactId);
//        ResultSet rs = ps.executeQuery();
//        String contactName = null;
//        if (rs.next()) {
//            contactName = rs.getString("contact");
//        }
//        JDBC.closeConnection();
//        return contactName;
//    }


    public static ObservableList<Appointments> getAppointmentsWithinRange(ObservableList<Appointments> appointments, ZonedDateTime start, ZonedDateTime end) {
        ObservableList<Appointments> filteredAppointments = FXCollections.observableArrayList();
        for (Appointments appointment : appointments) {
            Instant startInstant = appointment.getStart().toInstant();
            Instant endInstant = appointment.getEnd().toInstant();
            if (startInstant.isAfter(start.toInstant()) && endInstant.isBefore(end.toInstant())) {
                filteredAppointments.add(appointment);
            }
        }
        return filteredAppointments;
    }

    public static ObservableList<Appointments> getAllAppointments() throws Exception {
        ObservableList<Appointments> allAppointments = FXCollections.observableArrayList();
        JDBC.openConnection();

        String sql = "SELECT * FROM APPOINTMENTS AS A" + " LEFT OUTER JOIN CONTACTS AS C ON a.CONTACT_ID = C.CONTACT_ID;";

        Query.makeQuery(sql);
        ResultSet rs = Query.getResult();
        while (rs.next()) {
            int apptId = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String type = rs.getString("Type");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            Timestamp start = rs.getTimestamp("Start");
            Timestamp end = rs.getTimestamp("End");
            int contactId = rs.getInt("Contact_ID");
            int custId = rs.getInt("Customer_ID");
            int userId = rs.getInt("User_ID");
            String contactName = rs.getString("Contact_Name");

            Appointments appointmentResult = new Appointments(apptId, title, description, location, type, start, end, custId, userId, contactId, contactName);
            allAppointments.add(appointmentResult);
        }
        JDBC.closeConnection();
        return allAppointments;
    }



//    public static List<Appointments> getAppointmentsForCustomer(int customerId) throws SQLException {
//        // Declare a list to hold the appointments
//        List<Appointments> appointments = new ArrayList<>();
//
////        PreparedStatement ps = null;
//        try {
//            // Establish a connection to the database
//            JDBC.openConnection();
//
//            // Define the SQL query to execute
//            String sql = "SELECT * FROM APPOINTMENTS AS A" + " LEFT OUTER JOIN CONTACTS AS C ON a.CONTACT_ID = C.CONTACT_ID;";
//
//            // Prepare the statement
//            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
//            ps.setInt(1, customerId);
//            ResultSet resultSet = ps.executeQuery();
//
//
//            // Iterate over the result set and create Appointment objects for each row
//            while (resultSet.next()) {
//                // Retrieve the values from the result set
//                int id = resultSet.getInt("appointment_Id");
//                String title = resultSet.getString("title");
//                String description = resultSet.getString("description");
//                String location = resultSet.getString("location");
//                String type = resultSet.getString("type");
//                Timestamp start = resultSet.getTimestamp("Start");
//                Timestamp end = resultSet.getTimestamp("End");
//
//                int contactId = resultSet.getInt("Contact_ID");
//                int custId = resultSet.getInt("Customer_ID");
//                int userId = resultSet.getInt("User_ID");
//                String contactName = resultSet.getString("Contact_Name");
//
//                // Create a new Appointment object
//                Appointments appointment = new Appointments(id, title, description, location, type, start, end, custId, userId, contactId, contactName);
//
//                // Add the appointment to the list
//                appointments.add(appointment);
//            }
//        } catch (SQLException e) {
//            // Print the stack trace and rethrow the exception
//            e.printStackTrace();
//            throw e;
//        } finally {
////            // Close the statement and connection
////            if (ps != null) {
////                ps.close();
////            }
//            JDBC.closeConnection();
//        }
//
//        // Return the list of appointments
//        return appointments;
//    }

    public static List<Appointments> getAppointmentsForCustomer(int customerId) throws SQLException {
        JDBC.openConnection();
        String sql = "SELECT * FROM APPOINTMENTS AS a" + " LEFT JOIN CONTACTS AS C ON a.CONTACT_ID = C.CONTACT_ID" + " WHERE a.CUSTOMER_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerId);

        ResultSet rs = ps.executeQuery();
        List<Appointments> appointments = new ArrayList<>();
        while (rs.next()) {
            int appointmentId = rs.getInt("appointment_id");
            String title = rs.getString("title");
            String description = rs.getString("description");
            String location = rs.getString("location");
            String type = rs.getString("type");
            Timestamp start = rs.getTimestamp("start");
            Timestamp end = rs.getTimestamp("end");
            int contactId = rs.getInt("contact_id");
            int userId = rs.getInt("user_id");
            String contactName = rs.getString("Contact_Name");

            Appointments appointment = new Appointments(appointmentId, title, description, location, type, start, end, contactId, customerId, userId, contactName);
            appointments.add(appointment);
        }
        JDBC.closeConnection();
        return appointments;
    }



}

