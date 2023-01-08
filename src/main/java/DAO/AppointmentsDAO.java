package DAO;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.AppointmentReports;
import model.Appointments;
import model.DivisionReports;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static model.Appointments.getLocalDateTimeFormatter;

/**
 * The AppointmentsDAO class creates the Appointments DAO. Allows SQL and CRUD statements.
 */
public abstract class AppointmentsDAO {

    /**
     * The addAppointment method adds the appointment to the database with the selected values in the controller.
     * RUNTIME ERRORS: Error with adding Start and End times to the list. Originally values were timestamp however had an issue with timezone conversion. Converted Timestamps to string for better flexibility.
     */
    //CREATE
    public static boolean addAppointment(String title, String description, String location, String type, String start, String end, int custId, int userId, int contactId) throws SQLException {
        JDBC.openConnection();

        String sql = "INSERT INTO APPOINTMENTS (Title, Type, Description, Location, Start, End, Contact_ID, Customer_ID, User_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, type);
        ps.setString(3, description);
        ps.setString(4, location);
        ps.setString(5, start);
        ps.setString(6, end);
        ps.setInt(7, contactId);
        ps.setInt(8, custId);
        ps.setInt(9, userId);

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

    //UPDATE

    /**
     * The updateAppointment method updates the database values of the Appointment selected.
     */
    public static boolean updateAppointment(int apptId, String title, String description, String location, String type, String start, String end, int custId, int userId, int contactId) throws SQLException {

        String sql = "UPDATE appointments SET title = ?, description = ?, location = ?, type = ?, start = ?, end = ?, customer_id = ?, user_id = ?, contact_id = ? WHERE APPOINTMENT_ID = ?";

        JDBC.openConnection();
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setString(5, start);
        ps.setString(6, end);
        ps.setInt(7, custId);
        ps.setInt(8, userId);
        ps.setInt(9, contactId);
        ps.setInt(10, apptId);

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

    //DELETION

    /**
     * The deleteAppointment method deletes the selected appointment from the database.
     */
    public static boolean deleteAppointment(int apptId) throws SQLException {
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

    /**
     * The getAppointmentTypes method gets the appointment types from the database in order to populate the Type Combobox.
     */
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

    /**
     * The getAppointmentsForCustomer retrieves all the appointments related to the selected customer ID.
     */
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
            String start = rs.getString("start");
            String end = rs.getString("end");
            int contactId = rs.getInt("contact_id");
            int userId = rs.getInt("user_id");
            String contactName = rs.getString("Contact_Name");

            Appointments appointment = new Appointments(appointmentId, title, description, location, type, start, end, contactId, customerId, userId, contactName);
            appointments.add(appointment);
        }
        JDBC.closeConnection();
        return appointments;
    }

    /**
     * The getAppointmentsWithinRange filters appointment by current week or current month.
     */
    public static ObservableList<Appointments> getAppointmentsWithinRange(ObservableList<Appointments> appointments, ZonedDateTime start, ZonedDateTime end) {
        ObservableList<Appointments> filteredAppointments = FXCollections.observableArrayList();
        DateTimeFormatter formatter = getLocalDateTimeFormatter();
        for (Appointments appointment : appointments) {
            ZonedDateTime startZDT = ZonedDateTime.parse(appointment.getStart(), formatter);
            ZonedDateTime endZDT = ZonedDateTime.parse(appointment.getEnd(), formatter);
            Instant startInstant = startZDT.toInstant();
            Instant endInstant = endZDT.toInstant();
            if (startInstant.isAfter(start.toInstant()) && endInstant.isBefore(end.toInstant())) {
                filteredAppointments.add(appointment);
            }
        }
        return filteredAppointments;
    }

    /**
     * The getAppointmentsForUser method retrieves all appointments associated with the logged-in user.
     */
    public static List<Appointments> getAppointmentsForUser(int userId) throws SQLException {

        JDBC.openConnection();
        String sql = "SELECT * FROM APPOINTMENTS AS a" + " LEFT JOIN CONTACTS AS C ON a.CONTACT_ID = C.CONTACT_ID" + " WHERE a.user_id = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, userId);

        ResultSet rs = ps.executeQuery();
        List<Appointments> appointments = new ArrayList<>();
        while (rs.next()) {
            int appointmentId = rs.getInt("appointment_id");
            String title = rs.getString("title");
            String description = rs.getString("description");
            String location = rs.getString("location");
            String type = rs.getString("type");
            String start = rs.getString("start");
            String end = rs.getString("end");
            int contactId = rs.getInt("contact_id");
            int customerId = rs.getInt("customer_id");
            String contactName = rs.getString("Contact_Name");

            Appointments appointment = new Appointments(appointmentId, title, description, location, type, start, end, contactId, customerId, userId, contactName);
            appointments.add(appointment);
        }
        JDBC.closeConnection();
        return appointments;
    }

    /**
     * The getAppointmentsForContact retrieves appointment for the selected Contact.
     */
    public static List<Appointments> getAppointmentsForContact(int contactId) throws SQLException {

        JDBC.openConnection();
        String sql = "SELECT * FROM APPOINTMENTS AS a" + " LEFT JOIN CONTACTS AS C ON a.CONTACT_ID = C.CONTACT_ID" + " WHERE a.contact_id = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, contactId);

        ResultSet rs = ps.executeQuery();
        List<Appointments> appointments = new ArrayList<>();
        while (rs.next()) {
            int appointmentId = rs.getInt("appointment_id");
            String title = rs.getString("title");
            String description = rs.getString("description");
            String location = rs.getString("location");
            String type = rs.getString("type");
            String start = rs.getString("start");
            String end = rs.getString("end");
            int userId = rs.getInt("user_id");
            int custId = rs.getInt("customer_id");
            String contactName = rs.getString("Contact_Name");

            DateTimeFormatter formatter = Appointments.getUTCDateTimeFormatter();
            // Convert the start and end strings to ZonedDateTime objects
            ZonedDateTime startZDT = ZonedDateTime.parse(start, formatter);
            ZonedDateTime endZDT = ZonedDateTime.parse(end, formatter);

            // Convert the ZonedDateTime objects to local time
            ZoneId localZoneId = ZoneId.systemDefault();
            ZonedDateTime localStartZDT = startZDT.withZoneSameInstant(localZoneId);
            ZonedDateTime localEndZDT = endZDT.withZoneSameInstant(localZoneId);

            // Set the formatter to use the local time zone
            formatter = formatter.withZone(localZoneId);

            // Format the ZonedDateTime objects as strings using the DateTimeFormatter
            String localStart = localStartZDT.format(formatter);
            String localEnd = localEndZDT.format(formatter);

            Appointments appointment = new Appointments(appointmentId, title, description, location, type, localStart, localEnd, custId, userId, contactId, contactName);
            appointments.add(appointment);
        }
        JDBC.closeConnection();
        return appointments;
    }

    /**
     * The getReportTotalByTypeAndMonth retrieves and sorts the total appointments by Type and by Month.
     * RUNTIME ERROR: Issue adding data to Appointment model, Resolved by creating a specific model for the report.
     */
    public static List<AppointmentReports> getReportTotalByTypeAndMonth() throws SQLException {
        JDBC.openConnection();
        List<AppointmentReports> appointmentReports = new ArrayList<>();
        String sql = "SELECT MONTHNAME(Start) as Month, Type, COUNT(*) as Total " + "FROM appointments " + "GROUP BY Month, Type " + "ORDER BY Month, Type";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        // Retrieve data from the database and store it in the list
        while (rs.next()) {
            String month = rs.getString("month");
            String type = rs.getString("type");
            int total = rs.getInt("total");
            appointmentReports.add(new AppointmentReports(month, type, total));
        }
        JDBC.closeConnection();
        return appointmentReports;
    }

    /**
     * The getDivisionCustomerCountReport retrieves the total customer by each division.
     * RUNTIME ERROR: Issue adding data to Appointment model, Resolved by creating a specific model for the report.
     */
    public static List<DivisionReports> getDivisionCustomerCountReport(String country) throws SQLException {
        JDBC.openConnection();
        // Create an empty list of AppointmentReport objects
        List<DivisionReports> divisionReports = new ArrayList<>();

        String sql = "SELECT d.division, COUNT(*) as total " + "FROM customers c " + "JOIN first_level_divisions d ON c.Division_ID = d.Division_ID " + "JOIN countries co ON d.country_id = co.country_id " + "WHERE co.country = ? " + "GROUP BY d.division " + "ORDER BY d.division";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, country);  // Set the value of the country parameter
        ResultSet rs = ps.executeQuery();
        // Retrieve data from the database and store it in the list
        while (rs.next()) {
            String division = rs.getString("division");
//            int divisionId = rs.getInt("Division_ID");
            int total = rs.getInt("total");
            divisionReports.add(new DivisionReports(country, division, total));
        }
        JDBC.closeConnection();
        return divisionReports;
    }

    /**
     * The getAllAppointments method retrieves all the appointments within the database.
     * RUNTIME ERROR:  Issue with timezone conversion of timestamp. Converted Timestamp to string and added manual conversion.
     */
    public static ObservableList<Appointments> getAllAppointments() throws Exception {
        ObservableList<Appointments> allAppointments = FXCollections.observableArrayList();
        JDBC.openConnection();

        String sql = "SELECT * FROM APPOINTMENTS AS A" + " LEFT OUTER JOIN CONTACTS AS C ON A.CONTACT_ID = C.CONTACT_ID;";

        Query.makeQuery(sql);
        ResultSet rs = Query.getResult();
        while (rs.next()) {
            int apptId = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String type = rs.getString("Type");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String start = rs.getString("Start");
            String end = rs.getString("End");
            int contactId = rs.getInt("Contact_ID");
            int custId = rs.getInt("Customer_ID");
            int userId = rs.getInt("User_ID");
            String contactName = rs.getString("Contact_Name");

            DateTimeFormatter formatter = Appointments.getUTCDateTimeFormatter();
            // Convert the start and end strings to ZonedDateTime objects
            ZonedDateTime startZDT = ZonedDateTime.parse(start, formatter);
            ZonedDateTime endZDT = ZonedDateTime.parse(end, formatter);

            // Convert the ZonedDateTime objects to local time
            ZoneId localZoneId = ZoneId.systemDefault();
            ZonedDateTime localStartZDT = startZDT.withZoneSameInstant(localZoneId);
            ZonedDateTime localEndZDT = endZDT.withZoneSameInstant(localZoneId);

            // Set the formatter to use the local time zone
            formatter = formatter.withZone(localZoneId);

            // Format the ZonedDateTime objects as strings using the DateTimeFormatter
            String localStart = localStartZDT.format(formatter);
            String localEnd = localEndZDT.format(formatter);

            Appointments appointmentResult = new Appointments(apptId, title, description, location, type, localStart, localEnd, custId, userId, contactId, contactName);
            allAppointments.add(appointmentResult);
        }
        JDBC.closeConnection();
        return allAppointments;
    }
}