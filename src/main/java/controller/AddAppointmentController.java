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
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The class creates the add Appointment controller
 */
public class AddAppointmentController implements Initializable {

    @FXML
    public TextField titleText;
    @FXML
    public TextField descriptionText;
    @FXML
    public TextField locationText;
    @FXML
    public TextField apptIdText;
    @FXML
    public DatePicker startDatePicker;
    @FXML
    public DatePicker endDatePicker;
    @FXML
    public ComboBox startComboBox;
    @FXML
    public ComboBox endComboBox;
    @FXML
    public ComboBox<String> customerComboBox;
    @FXML
    public ComboBox<String> contactsCombBox;
    @FXML
    public ComboBox<String> typeComboBox;
    @FXML
    public ComboBox<String> userComboBox;
    Stage stage;
    Parent scene;


    /**
     * This code returns an ObservableList of appointment times between 8AM and 10PM EST converted to local time spread out in 15 minute increments.
     */
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

    /**
     * This method validates and saves the new appointment when the save button is clicked.
     * RUNTIME ERROR: The appointment time was not saving correctly. Issue was due to the time zone conversion.
     */
    @FXML
    public void OnActionSaveAppt(ActionEvent event) throws SQLException {

        // Retrieve the selected customer name from the combo box.
        String customerName = customerComboBox.getValue();

        // Retrieve the selected userId from the combo box.
        String selectedUserId = userComboBox.getSelectionModel().getSelectedItem();

        // Retrieve the selected contact name from the combo box and convert it to an integer
        String contactName = contactsCombBox.getValue();

        // Retrieve the selected date and time from the DatePicker and ComboBox
        LocalDate sdate = startDatePicker.getValue();
        String stime = String.valueOf(startComboBox.getValue());
        LocalDate edate = endDatePicker.getValue();
        String etime = String.valueOf(endComboBox.getValue());

        // Retrieve the form input values
        String title = titleText.getText();
        String description = descriptionText.getText();
        String location = locationText.getText();
        String type = typeComboBox.getValue();

        // Validates that the fields are not empty.
        if (customerName == null || selectedUserId == null || contactName == null || sdate == null || stime == null || edate == null || etime == null || title.isEmpty() || description.isEmpty() || location.isEmpty() || type == null) {
            // If fields are empty, displays an error message.
            Alert emptyFields = new Alert(Alert.AlertType.ERROR);
            emptyFields.setTitle("Error");
            emptyFields.setHeaderText("Empty Fields");
            emptyFields.setContentText("All fields are required. Please make sure all fields are filled out.");
            emptyFields.showAndWait();
            return;
        }

        // Convert the selected userId to an integer
        int userId = Integer.parseInt(selectedUserId);

        // Take the selected Customer Name and find the customers ID, so it can be added to appointments table.
        int custId = CustomerDAO.getCustomerId(customerName);

        // Retrieve the list of appointments for the selected customer
        List<Appointments> appointments = AppointmentsDAO.getAppointmentsForCustomer(custId);

        // Take user selected Contact_Name and find the contact_ID FK, so it can add to appointments table.
        int contactId = ContactDAO.getContactId(contactName);

        // Convert the selected time to a LocalTime object.
        LocalTime lt = LocalTime.parse(stime);
        LocalTime elt = LocalTime.parse(etime);

        // Create a ZoneId for UTC.
        ZoneId utcZoneId = ZoneId.of("UTC", ZoneId.SHORT_IDS);
        // Create a ZoneId for local time.
        ZoneId myZoneId = ZoneId.systemDefault();

        // Combine the date and time into a LocalDateTime object
        LocalDateTime ldt = LocalDateTime.of(sdate, lt);
        LocalDateTime eldt = LocalDateTime.of(edate, elt);

        // Convert the LocalDateTime to a ZonedDateTime.
        ZonedDateTime myZDT = ZonedDateTime.of(ldt, myZoneId);
        ZonedDateTime myEZDT = ZonedDateTime.of(eldt, myZoneId);

        // Convert the ZonedDateTime to UTC.
        ZonedDateTime utcZDT = ZonedDateTime.ofInstant(myZDT.toInstant(), utcZoneId);
        ZonedDateTime utcEZDT = ZonedDateTime.ofInstant(myEZDT.toInstant(), utcZoneId);

        // Convert the ZonedDateTime to a formatted string.
        String start = utcZDT.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String end = utcEZDT.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

        // Validates that the end date is not before the start date.
        if (sdate.isAfter(edate)) {
            Alert invalidDate = new Alert(Alert.AlertType.ERROR);
            invalidDate.setTitle("Error");
            invalidDate.setHeaderText("Invalid Date Range");
            invalidDate.setContentText("End date must be on the same day or after the start date.");
            invalidDate.showAndWait();
        } else {
            //Converts time to EST.
            ZoneId estZoneId = ZoneId.of("EST", ZoneId.SHORT_IDS);
            ZonedDateTime estStartTime = ldt.atZone(ZoneId.systemDefault()).withZoneSameInstant(estZoneId);
            ZonedDateTime estEndTime = eldt.atZone(ZoneId.systemDefault()).withZoneSameInstant(estZoneId);
            // Validates that the appointment is within business hours.
            if (estStartTime.toLocalTime().isBefore(LocalTime.of(8, 0)) || estStartTime.toLocalTime().isAfter(LocalTime.of(22, 0)) || estEndTime.toLocalTime().isBefore(LocalTime.of(8, 0)) || estEndTime.toLocalTime().isAfter(LocalTime.of(22, 0)) || estEndTime.toLocalTime().isBefore(estStartTime.toLocalTime())) {
                // Start time is invalid.
                Alert businessHours = new Alert(Alert.AlertType.ERROR);
                businessHours.setTitle("Error");
                businessHours.setHeaderText("Invalid Time Range");
                businessHours.setContentText("Start and end times must be during business hours between 8:00 EST and 22:00 EST and the end time must be after the start time.");
                businessHours.showAndWait();
            } else {
                // Loop through the appointments for the selected customer.
                for (Appointments appointment : appointments) {

                    //  Gets string Start and End from appointment list.
                    String sStart = appointment.getStart();
                    String sEnd = appointment.getEnd();

                    //Converts String to LocalDateTime type and formats.
                    LocalDateTime checkStart = LocalDateTime.parse(sStart, formatter);
                    LocalDateTime checkEnd = LocalDateTime.parse(sEnd, formatter);

                    // Create a ZonedDateTime object for the start and end time of the existing appointment using the UTC ZoneId.
                    ZonedDateTime checkAppointmentStart = ZonedDateTime.of(checkStart, utcZoneId);
                    ZonedDateTime checkAppointmentEnd = ZonedDateTime.of(checkEnd, utcZoneId);

                    //Validates that the appointment does not overlap with an existing appointment.
                    if ((utcEZDT.isBefore(checkAppointmentEnd) && utcZDT.isAfter(checkAppointmentStart)) || utcZDT.isEqual(checkAppointmentStart) || utcEZDT.isEqual(checkAppointmentEnd)) {
                        Alert overlap = new Alert(Alert.AlertType.ERROR);
                        overlap.setTitle("Error");
                        overlap.setHeaderText("Overlapping Appointments");
                        overlap.setContentText("The customer already has an appointment scheduled during this time. Please reschedule for another time.");
                        overlap.showAndWait();
                        return;
                    }
                }
                try {
                    // Try to add the appointment to the database
                    // If successful, display a confirmation message and return to the main screen.
                    boolean success = AppointmentsDAO.addAppointment(title, description, location, type, start, end, custId, userId, contactId);
                    if (success) {
                        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                        scene = FXMLLoader.load(getClass().getResource("/view/appointments-view.fxml"));
                        stage.setScene(new Scene(scene));
                        stage.show();
                    } else {
                        // Display error message to user.
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

    /**
     * This method is called when the user clicks the cancel button and returns the user to the appointments screen.
     */
    @FXML
    public void OnActionCancel(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/appointments-view.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** The initialize method populates the ComboBoxes, */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Populate the contacts, customer, user,type, start and end combo boxes.
        ObservableList<String> appointmentTimes = getAppointmentTimes();
        try {
            contactsCombBox.setItems(ContactDAO.getContacts());
            customerComboBox.setItems((CustomerDAO.getCustomers()));
            userComboBox.setItems((UserDAO.getUsers()));
            typeComboBox.setItems((AppointmentsDAO.getType()));
            startComboBox.setItems(appointmentTimes);
            endComboBox.setItems(appointmentTimes);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // May remove - Not necessary
//        startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
//            // Check if the day is greater than the number of days in the month
//            if (newValue.getDayOfMonth() > newValue.lengthOfMonth()) {
//                // Set the day to the last day of the month
//                startDatePicker.setValue(newValue.withDayOfMonth(newValue.lengthOfMonth()));
//            }
//        });
//
//        endDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
//            // Check if the day is greater than the number of days in the month
//            if (newValue.getDayOfMonth() > newValue.lengthOfMonth()) {
//                // Set the day to the last day of the month
//                endDatePicker.setValue(newValue.withDayOfMonth(newValue.lengthOfMonth()));
//            }
//        });
    }
}

