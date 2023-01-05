package controller;

import DAO.AppointmentsDAO;
import DAO.ContactDAO;
import DAO.CustomerDAO;
import DAO.UserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointments;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.time.ZonedDateTime;

public class AddAppointmentController implements Initializable {

    @FXML
    public TextField titleTxt;
    @FXML
    public TextField descriptionTxt;
    @FXML
    public TextField locationTxt;
    @FXML
    public TextField apptIdTxt;
    @FXML
    public DatePicker sDatePicker;
    @FXML
    public DatePicker eDatePicker;
    @FXML
    public ComboBox startDD;
    @FXML
    public ComboBox endDD;
    @FXML
    public ComboBox<String> customerDD;
    @FXML
    public ComboBox<String> contactsDD;
    @FXML
    public ComboBox<String> typeDD;
    @FXML
    public ComboBox<String> userDD;
    Stage stage;
    Parent scene;

        // May want to set up a way to populate only appointments within 8AM - 10PM EST.
//    private ObservableList<String> getAppointmentTimes() {
//        ObservableList<String> appointmentTimes = FXCollections.observableArrayList();
//
//        LocalTime firstAppointment = LocalTime.MIN.plusHours(6);
//        LocalTime lastAppointment = LocalTime.MAX.minusHours(2);
//
//        if (!firstAppointment.equals(0) || !lastAppointment.equals(0)) {
//            while (firstAppointment.isBefore(lastAppointment)) {
//                appointmentTimes.add(String.valueOf(firstAppointment));
//                firstAppointment = firstAppointment.plusMinutes(15);
//            }
//        }
//        return appointmentTimes;
//    }

    private ObservableList<String> getAppointmentTimes() {
        ObservableList<String> appointmentTimes = FXCollections.observableArrayList();

        // Set the start and end times to 8:00 AM and 10:00 PM EST, respectively
        LocalTime startTime = LocalTime.of(8, 0, 0);
        LocalTime endTime = LocalTime.of(22, 0, 0);

        // Convert the start and end times to the local time zone
        ZonedDateTime startTimeEST = ZonedDateTime.of(LocalDate.now(), startTime, ZoneId.of("America/New_York"));
        ZonedDateTime endTimeEST = ZonedDateTime.of(LocalDate.now(), endTime, ZoneId.of("America/New_York"));
        LocalTime startTimeLocal = startTimeEST.withZoneSameInstant(ZoneId.systemDefault()).toLocalTime();
        LocalTime endTimeLocal = endTimeEST.withZoneSameInstant(ZoneId.systemDefault()).toLocalTime();

        // Add 15-minute increments to the appointment times list between the start and end times
        if (!startTimeLocal.equals(0) || !endTimeLocal.equals(0)) {
            LocalTime currentTime = startTimeLocal;
            while (currentTime.isBefore(endTimeLocal)) {
                appointmentTimes.add(String.valueOf(currentTime));
                currentTime = currentTime.plusMinutes(15);
            }
        }
        return appointmentTimes;
    }




//    @FXML
//    public void OnActionSaveAppt(ActionEvent event) throws IOException, SQLException {
//
//
//        // Retrieve the selected contactId from the combo box and convert it to an integer
//        String customerName = customerDD.getValue();
//        // take user selected Contact_Name and find the contact_ID FK so it can be add to appointments table.
//        int custId = CustomerDAO.getCustomerId(customerName);
//
//        // Retrieve the list of appointments for the selected customer
//        List<Appointments> appointments = AppointmentsDAO.getAppointmentsForCustomer(custId);
//
//        // Retrieve the selected userId from the combo box and convert it to an integer
//        String selectedUserId = userDD.getSelectionModel().getSelectedItem();
//        int userId = Integer.parseInt(selectedUserId);
//
//        // Retrieve the selected contactId from the combo box and convert it to an integer
//        String contactName = contactsDD.getValue();
//
//        // take user selected Contact_Name and find the contact_ID FK so we can add to appointments table.
//        int contactId = ContactDAO.getContactId(contactName);
//
//
//        // Retrieve the selected date and time from the DatePicker and ComboBox
//        LocalDate sdate = sDatePicker.getValue();
//        String stime = String.valueOf(startDD.getValue());
//        LocalDate edate = eDatePicker.getValue();
//        String etime = String.valueOf(endDD.getValue());
//
//        // Convert the selected time to a LocalTime object
//        LocalTime lt = LocalTime.parse(stime);
//        LocalTime elt = LocalTime.parse(etime);
//
//
//        // Combine the date and time into a LocalDateTime object
//        LocalDateTime ldt = LocalDateTime.of(sdate, lt);
//        LocalDateTime eldt = LocalDateTime.of(edate, elt);
//
//
//        if (sdate.isAfter(edate)) {
//            // end date is before start date, show an error message or do something else
//            Alert invalidDate = new Alert(Alert.AlertType.ERROR);
//            invalidDate.setTitle("Error");
//            invalidDate.setHeaderText("Invalid Date Range");
//            invalidDate.setContentText("End date must be on the same day or after the start date.");
//            invalidDate.showAndWait();
//        } else {
//            ZoneId estZoneId = ZoneId.of("EST", ZoneId.SHORT_IDS);
//            ZonedDateTime estStartTime = ldt.atZone(ZoneId.systemDefault()).withZoneSameInstant(estZoneId);
//            ZonedDateTime estEndTime = eldt.atZone(ZoneId.systemDefault()).withZoneSameInstant(estZoneId);
//
//            if (estStartTime.toLocalTime().isBefore(LocalTime.of(8, 0)) || estStartTime.toLocalTime().isAfter(LocalTime.of(22, 0)) || estEndTime.toLocalTime().isBefore(LocalTime.of(8, 0)) || estEndTime.toLocalTime().isAfter(LocalTime.of(22, 0)) || estEndTime.toLocalTime().isBefore(estStartTime.toLocalTime())) {
//                // start time is invalid, show an error message or do something else
//                Alert businessHours = new Alert(Alert.AlertType.ERROR);
//                businessHours.setTitle("Error");
//                businessHours.setHeaderText("Invalid Time Range");
//                businessHours.setContentText("Start and end times must be during business hours between 8:00 EST and 22:00 EST and the end time must be after the start time.");
//                businessHours.showAndWait();
//            } else {
//
//
//                // Convert the LocalDateTime objects to ZonedDateTime objects in UTC
//                ZonedDateTime startTimeUTC = ldt.atZone(ZoneId.of("UTC"));
//                ZonedDateTime endTimeUTC = eldt.atZone(ZoneId.of("UTC"));
//
//
//                // Check if the start or end time of the new appointment overlaps with any of the existing appointments
//                // Check if the start or end time of the new appointment overlaps with any of the existing appointments
//                ZonedDateTime startZDT = null;
//                ZonedDateTime endZDT = null;
//
//
//                for (Appointments appointment : appointments) {
//                    //CURRENTLY WORKING ON THE CODE BELOW
//
//                    Timestamp startTimestamp = appointment.getStart();
//                    startZDT = startTimestamp.toInstant().atZone(ZoneId.of("UTC"));
//                    Timestamp endTimestamp = appointment.getEnd();
//                    endZDT = endTimestamp.toInstant().atZone(ZoneId.of("UTC"));
//
//
//                }
//                if ((startTimeUTC.isAfter(startZDT) && ldt.isBefore(ChronoLocalDateTime.from(endZDT))) || (eldt.isAfter(ChronoLocalDateTime.from(startZDT)) && eldt.isBefore(ChronoLocalDateTime.from(endZDT)))) {
//                    {
//                        // overlap detected, show an error message or do something else
//                        Alert overlap = new Alert(Alert.AlertType.ERROR);
//                        overlap.setTitle("Error");
//                        overlap.setHeaderText("Overlapping Appointment");
//                        overlap.setContentText("The selected time range overlaps with an existing appointment for this customer. Please select a different time range.");
//                        overlap.showAndWait();
//                        return;
//                    }
//                }
//
//
//                // Retrieve the form input values
//                String title = titleTxt.getText();
//                String description = descriptionTxt.getText();
//                String location = locationTxt.getText();
//                String type = typeDD.getValue();
//
//                try {
//                    // Try to add the appointment to the database
//                    boolean success = AppointmentsDAO.addAppointment(title, description, location, type, startTimeUTC, endTimeUTC, custId, userId, contactId);
//                    if (success) {
//                        // Display message to user indicating successful addition of appointment
//                        System.out.println("Appointment added successfully");
//
//                        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
//                        scene = FXMLLoader.load(getClass().getResource("/view/appointments-view.fxml"));
//                        stage.setScene(new Scene(scene));
//                        stage.show();
//                    } else {
//                        // Display error message to user
//                        System.out.println("Error adding appointment");
//                        Alert alert = new Alert(Alert.AlertType.ERROR);
//                        alert.setTitle("Error Dialog");
//                        alert.setContentText("Please enter a valid value for each Text Field.");
//                        alert.showAndWait();
//                    }
//                } catch (Exception e) {
//                    // Display error message to user
//                    System.out.println("Error adding appointment: " + e.getMessage());
//                    Alert alert = new Alert(Alert.AlertType.ERROR);
//                    alert.setTitle("Error Dialog");
//                    alert.setContentText("An error occurred while adding the appointment: " + e.getMessage());
//                    alert.showAndWait();
//                }
//            }
//        }
//    }

//    @FXML
//    public void OnActionSaveAppt(ActionEvent event) throws IOException, SQLException {
//
//
//        // Retrieve the selected contactId from the combo box and convert it to an integer
//        String customerName = customerDD.getValue();
//        // take user selected Contact_Name and find the contact_ID FK so it can be add to appointments table.
//        int custId = CustomerDAO.getCustomerId(customerName);
//
////        // Retrieve the list of appointments for the selected customer
////        List<Appointments> appointments = AppointmentsDAO.getAppointmentsForCustomer(custId);
//
//        // Retrieve the selected userId from the combo box and convert it to an integer
//        String selectedUserId = userDD.getSelectionModel().getSelectedItem();
//        int userId = Integer.parseInt(selectedUserId);
//
//        // Retrieve the selected contactId from the combo box and convert it to an integer
//        String contactName = contactsDD.getValue();
//
//        // take user selected Contact_Name and find the contact_ID FK so we can add to appointments table.
//        int contactId = ContactDAO.getContactId(contactName);
//
//
//        // Retrieve the selected date and time from the DatePicker and ComboBox
//        LocalDate sdate = sDatePicker.getValue();
//        String stime = String.valueOf(startDD.getValue());
//        LocalDate edate = eDatePicker.getValue();
//        String etime = String.valueOf(endDD.getValue());
//
//        // Convert the selected time to a LocalTime object
//        LocalTime lt = LocalTime.parse(stime);
//        LocalTime elt = LocalTime.parse(etime);
//
//        // Create a ZoneId for UTC
//        ZoneId utcZoneId = ZoneId.of("UTC", ZoneId.SHORT_IDS);
//        // Create a ZoneId for UTC
//        ZoneId myZoneId = ZoneId.systemDefault();
//
//
//        // Combine the date and time into a LocalDateTime object
//        LocalDateTime ldt = LocalDateTime.of(sdate, lt);
//        LocalDateTime eldt = LocalDateTime.of(edate, elt);
//
//        ZonedDateTime myZDT = ZonedDateTime.of(ldt, myZoneId);
//        ZonedDateTime myEZDT = ZonedDateTime.of(eldt, myZoneId);
//
//        ZonedDateTime utcZDT = ZonedDateTime.ofInstant(myZDT.toInstant(), utcZoneId);
//        ZonedDateTime utcEZDT = ZonedDateTime.ofInstant(myEZDT.toInstant(), utcZoneId);
//
//        Timestamp utcStart = Timestamp.valueOf(utcZDT.format(DateTimeFormatter.ofPattern("YYYY-MM-DD HH:mm:ss")));
//        Timestamp utcEnd = Timestamp.valueOf(utcEZDT.format(DateTimeFormatter.ofPattern("YYYY-MM-DD HH:mm:ss")));
//
//
//        if (sdate.isAfter(edate)) {
//            // end date is before start date, show an error message or do something else
//            Alert invalidDate = new Alert(Alert.AlertType.ERROR);
//            invalidDate.setTitle("Error");
//            invalidDate.setHeaderText("Invalid Date Range");
//            invalidDate.setContentText("End date must be on the same day or after the start date.");
//            invalidDate.showAndWait();
//        } else {
//            ZoneId estZoneId = ZoneId.of("EST", ZoneId.SHORT_IDS);
//            ZonedDateTime estStartTime = ldt.atZone(ZoneId.systemDefault()).withZoneSameInstant(estZoneId);
//            ZonedDateTime estEndTime = eldt.atZone(ZoneId.systemDefault()).withZoneSameInstant(estZoneId);
//
//            if (estStartTime.toLocalTime().isBefore(LocalTime.of(8, 0)) || estStartTime.toLocalTime().isAfter(LocalTime.of(22, 0)) || estEndTime.toLocalTime().isBefore(LocalTime.of(8, 0)) || estEndTime.toLocalTime().isAfter(LocalTime.of(22, 0)) || estEndTime.toLocalTime().isBefore(estStartTime.toLocalTime())) {
//                // start time is invalid, show an error message or do something else
//                Alert businessHours = new Alert(Alert.AlertType.ERROR);
//                businessHours.setTitle("Error");
//                businessHours.setHeaderText("Invalid Time Range");
//                businessHours.setContentText("Start and end times must be during business hours between 8:00 EST and 22:00 EST and the end time must be after the start time.");
//                businessHours.showAndWait();
//            } else {
//
//
//                // Retrieve the form input values
//                String title = titleTxt.getText();
//                String description = descriptionTxt.getText();
//                String location = locationTxt.getText();
//                String type = typeDD.getValue();
//
//                try {
//                    // Try to add the appointment to the database
//                    boolean success = AppointmentsDAO.addAppointment(title, description, location, type, utcStart, utcEnd, custId, userId, contactId);
//                    if (success) {
//                        // Display message to user indicating successful addition of appointment
//                        System.out.println("Appointment added successfully");
//
//                        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
//                        scene = FXMLLoader.load(getClass().getResource("/view/appointments-view.fxml"));
//                        stage.setScene(new Scene(scene));
//                        stage.show();
//                    } else {
//                        // Display error message to user
//                        System.out.println("Error adding appointment");
//                        Alert alert = new Alert(Alert.AlertType.ERROR);
//                        alert.setTitle("Error Dialog");
//                        alert.setContentText("Please enter a valid value for each Text Field.");
//                        alert.showAndWait();
//                    }
//                } catch (Exception e) {
//                    // Display error message to user
//                    System.out.println("Error adding appointment: " + e.getMessage());
//                    Alert alert = new Alert(Alert.AlertType.ERROR);
//                    alert.setTitle("Error Dialog");
//                    alert.setContentText("An error occurred while adding the appointment: " + e.getMessage());
//                    alert.showAndWait();
//                }
//            }
//        }
//    }

    @FXML
    public void OnActionSaveAppt(ActionEvent event) throws IOException, SQLException {


        // Retrieve the selected contactId from the combo box and convert it to an integer
        String customerName = customerDD.getValue();
        // take user selected Contact_Name and find the contact_ID FK so it can be add to appointments table.
        int custId = CustomerDAO.getCustomerId(customerName);

//        // Retrieve the list of appointments for the selected customer
//        List<Appointments> appointments = AppointmentsDAO.getAppointmentsForCustomer(custId);

        // Retrieve the selected userId from the combo box and convert it to an integer
        String selectedUserId = userDD.getSelectionModel().getSelectedItem();
        int userId = Integer.parseInt(selectedUserId);

        // Retrieve the selected contactId from the combo box and convert it to an integer
        String contactName = contactsDD.getValue();

        // take user selected Contact_Name and find the contact_ID FK so we can add to appointments table.
        int contactId = ContactDAO.getContactId(contactName);


        // Retrieve the selected date and time from the DatePicker and ComboBox
        LocalDate sdate = sDatePicker.getValue();
        String stime = String.valueOf(startDD.getValue());
        LocalDate edate = eDatePicker.getValue();
        String etime = String.valueOf(endDD.getValue());

        // Convert the selected time to a LocalTime object
        LocalTime lt = LocalTime.parse(stime);
        LocalTime elt = LocalTime.parse(etime);

        // Create a ZoneId for UTC
        ZoneId utcZoneId = ZoneId.of("UTC", ZoneId.SHORT_IDS);
        // Create a ZoneId for local
        ZoneId myZoneId = ZoneId.systemDefault();


        // Combine the date and time into a LocalDateTime object
        LocalDateTime ldt = LocalDateTime.of(sdate, lt);
        LocalDateTime eldt = LocalDateTime.of(edate, elt);

        ZonedDateTime myZDT = ZonedDateTime.of(ldt, myZoneId);
        ZonedDateTime myEZDT = ZonedDateTime.of(eldt, myZoneId);

        ZonedDateTime utcZDT = ZonedDateTime.ofInstant(myZDT.toInstant(), utcZoneId);
        ZonedDateTime utcEZDT = ZonedDateTime.ofInstant(myEZDT.toInstant(), utcZoneId);

        String start = utcZDT.format(DateTimeFormatter.ofPattern("YYYY-MM-DD HH:mm:ss"));
        String end = utcEZDT.format(DateTimeFormatter.ofPattern("YYYY-MM-DD HH:mm:ss"));

//        String start = myZDT.format(DateTimeFormatter.ofPattern("YYYY-MM-DD HH:mm:ss"));
//        String end = myEZDT.format(DateTimeFormatter.ofPattern("YYYY-MM-DD HH:mm:ss"));



        if (sdate.isAfter(edate)) {
            // end date is before start date, show an error message or do something else
            Alert invalidDate = new Alert(Alert.AlertType.ERROR);
            invalidDate.setTitle("Error");
            invalidDate.setHeaderText("Invalid Date Range");
            invalidDate.setContentText("End date must be on the same day or after the start date.");
            invalidDate.showAndWait();
        } else {
            ZoneId estZoneId = ZoneId.of("EST", ZoneId.SHORT_IDS);
            ZonedDateTime estStartTime = ldt.atZone(ZoneId.systemDefault()).withZoneSameInstant(estZoneId);
            ZonedDateTime estEndTime = eldt.atZone(ZoneId.systemDefault()).withZoneSameInstant(estZoneId);

            if (estStartTime.toLocalTime().isBefore(LocalTime.of(8, 0)) || estStartTime.toLocalTime().isAfter(LocalTime.of(22, 0)) || estEndTime.toLocalTime().isBefore(LocalTime.of(8, 0)) || estEndTime.toLocalTime().isAfter(LocalTime.of(22, 0)) || estEndTime.toLocalTime().isBefore(estStartTime.toLocalTime())) {
                // start time is invalid, show an error message or do something else
                Alert businessHours = new Alert(Alert.AlertType.ERROR);
                businessHours.setTitle("Error");
                businessHours.setHeaderText("Invalid Time Range");
                businessHours.setContentText("Start and end times must be during business hours between 8:00 EST and 22:00 EST and the end time must be after the start time.");
                businessHours.showAndWait();
            } else {


                // Retrieve the form input values
                String title = titleTxt.getText();
                String description = descriptionTxt.getText();
                String location = locationTxt.getText();
                String type = typeDD.getValue();

                try {
                    // Try to add the appointment to the database
                    boolean success = AppointmentsDAO.addAppointment(title, description, location, type, start, end, custId, userId, contactId);
                    if (success) {
                        // Display message to user indicating successful addition of appointment
                        System.out.println("Appointment added successfully");

                        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                        scene = FXMLLoader.load(getClass().getResource("/view/appointments-view.fxml"));
                        stage.setScene(new Scene(scene));
                        stage.show();
                    } else {
                        // Display error message to user
                        System.out.println("Error adding appointment");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Dialog");
                        alert.setContentText("Please enter a valid value for each Text Field.");
                        alert.showAndWait();
                    }
                } catch (Exception e) {
                    // Display error message to user
                    System.out.println("Error adding appointment: " + e.getMessage());
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setContentText("An error occurred while adding the appointment: " + e.getMessage());
                    alert.showAndWait();
                }
            }
        }
    }

    @FXML
    public void OnActionCancel(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/appointments-view.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> appointmentTimes = getAppointmentTimes();
// Retrieve the list of appointments for a given customer
//        int customerId = 1; // Replace with the customerId of the customer you want to retrieve appointments for
//        List<Appointments> appointments = null;
//        try {
//            appointments = AppointmentsDAO.getAppointmentsForCustomer(customerId);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }

//// Print the list of appointments
//        for (Appointments appointment : appointments) {
//            System.out.println(appointment);
//        }
        try {
            contactsDD.setItems(ContactDAO.getContacts());
            customerDD.setItems((CustomerDAO.getCustomers()));
            userDD.setItems((UserDAO.getUsers()));
            typeDD.setItems((AppointmentsDAO.getType()));
            startDD.setItems(appointmentTimes);
            endDD.setItems(appointmentTimes);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

//   1/5/2023   I will need to double Check for potential issues with this code due to the changes made from Timestamp to String
