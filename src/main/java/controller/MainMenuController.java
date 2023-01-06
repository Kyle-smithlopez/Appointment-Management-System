package controller;

import DAO.AppointmentsDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Appointments;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML
    public Label appointmentsLabel;
    Stage stage;
    Parent scene;

    @FXML
    public void OnActionAppointments(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/appointments-view.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    public void OnActionCustomers(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/customers-view.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    public void OnActionReports(ActionEvent event) {
    }

    @FXML
    public void OnActionLogout(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/login-view.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public static void checkUpcomingAppointments(int userId, Label appointmentsLabel) throws SQLException {

        // Format the dates in the appointments list as LocalDateTime objects
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("UTC"));

        // Create a ZoneId for the user's local time zone
        ZoneId localZoneId = ZoneId.systemDefault();

        // Get the user's login time as a LocalDateTime object
        LocalDateTime loginTime = LocalDateTime.now();
        // LocalDateTime specificTime = LocalDateTime.of(2023, Month.JANUARY, 6, 15, 46, 0);


        // Create a ZonedDateTime object using the LocalDateTime and local ZoneId
        ZonedDateTime zonedDateTime = ZonedDateTime.of(loginTime, localZoneId);

        // Create a ZoneId for UTC
        ZoneId utcZoneId = ZoneId.of("UTC");

        // Convert the ZonedDateTime object to a LocalDateTime object in UTC
        LocalDateTime loginTimeUtc = zonedDateTime.withZoneSameInstant(utcZoneId).toLocalDateTime();

        // Retrieve the user's appointments
        List<Appointments> appointments = AppointmentsDAO.getAppointmentsForUser(userId);

        for (Appointments appointment : appointments) {
            // Convert the start and end times of the appointment to LocalDateTime objects
            LocalDateTime startTime = LocalDateTime.parse(appointment.getStart(), formatter);
            LocalDateTime endTime = LocalDateTime.parse(appointment.getEnd(), formatter);
            System.out.println(loginTime);
            System.out.println(loginTimeUtc);
            System.out.println(startTime);
            System.out.println(endTime);

            if (startTime.isAfter(loginTimeUtc) && startTime.isBefore(loginTimeUtc.plusMinutes(15))) {
                // Appointment starts within 15 minutes of login or starts before login and ends within 15 minutes of login
                appointmentsLabel.setText("You have an upcoming appointment at " + startTime.format(DateTimeFormatter.ofPattern("HH:mm")) + " on " + startTime.format(DateTimeFormatter.ofPattern("MM-dd-yyyy")) + " (ID: " + appointment.getApptId() + ")");
            }
        }

        if (appointmentsLabel.getText().equals("")) {
            // No upcoming appointments within 15 minutes of login
            appointmentsLabel.setText("You have no upcoming appointments within the next 15 minutes.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Get the userId of the currently logged-in user
        int userId = LoginController.Global.currentUserId;
        try {
            checkUpcomingAppointments(userId, appointmentsLabel);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println(userId);
    }
}

// Added 15 minute notification for appointments