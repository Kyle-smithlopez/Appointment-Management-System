package DAO;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointments;
import model.Customers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public abstract class AppointmentsDAO {

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
//                Timestamp startTimestamp = rs.getTimestamp("Start");
//                LocalDateTime start = startTimestamp.toLocalDateTime();
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
//                Timestamp endTimestamp = rs.getTimestamp("End");
//                LocalDateTime end = endTimestamp.toLocalDateTime();
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

