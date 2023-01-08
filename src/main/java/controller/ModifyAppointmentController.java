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
 * The Modify Appointment controller pulls up the selected appointment information to modify.
 */
public class ModifyAppointmentController implements Initializable {
    @FXML
    public TextField TitleTxt;
    @FXML
    public TextField DescriptionTxt;
    @FXML
    public TextField LocationTxt;
    @FXML
    public TextField IdTxt;
    @FXML
    public ComboBox<String> typeDD;
    @FXML
    public ComboBox<LocalTime> STimeDD;
    @FXML
    public ComboBox<String> ContactDD;
    @FXML
    public ComboBox<String> CustomerDD;
    @FXML
    public ComboBox ETimeDD;
    @FXML
    public DatePicker SDatePicker;
    @FXML
    public DatePicker EDatePicker;
    public ComboBox<String> UserDD;
    Stage stage;
    Parent scene;

    /**
     * This code returns an ObservableList of appointment times between 8AM and 10PM EST converted to local time spread out in 15 minute increments.
     */
    private ObservableList<LocalTime> getAppointmentTimes() {
        ObservableList<LocalTime> appointmentTimes = FXCollections.observableArrayList();

        LocalTime firstAppointment = LocalTime.MIN.plusHours(8);
        LocalTime lastAppointment = LocalTime.MAX.minusHours(4);

        if (!firstAppointment.equals(0) || !lastAppointment.equals(0)) {
            while (firstAppointment.isBefore(lastAppointment)) {
                appointmentTimes.add(firstAppointment);
                firstAppointment = firstAppointment.plusMinutes(15);
            }
        }
        return appointmentTimes;
    }

    /**
     * This method retrieves the selected appointments data and populates it in the appropriate fields.
     */
    public void sendAppointment(Appointments appointment) throws SQLException {

        DateTimeFormatter formatter = Appointments.getLocalDateTimeFormatter();

        // Convert the start and end strings to ZonedDateTime objects
        ZonedDateTime startZDT = ZonedDateTime.parse(appointment.getStart(), formatter);
        ZonedDateTime endZDT = ZonedDateTime.parse(appointment.getEnd(), formatter);

        // Convert the ZonedDateTime objects to local time
        ZoneId localZoneId = ZoneId.systemDefault();
        ZonedDateTime localStartZDT = startZDT.withZoneSameInstant(localZoneId);
        ZonedDateTime localEndZDT = endZDT.withZoneSameInstant(localZoneId);

        // Extract the local time and date from the ZonedDateTime objects
        LocalTime sTime = localStartZDT.toLocalTime();
        LocalDate sDate = localStartZDT.toLocalDate();
        LocalTime eTime = localEndZDT.toLocalTime();
        LocalDate eDate = localEndZDT.toLocalDate();

        IdTxt.setText(String.valueOf(appointment.getApptId()));
        TitleTxt.setText(appointment.getTitle());
        DescriptionTxt.setText(appointment.getDescription());
        LocationTxt.setText(appointment.getLocation());
        typeDD.setValue(appointment.getType());
        STimeDD.setValue(sTime);
        ETimeDD.setValue(eTime);
        SDatePicker.setValue(sDate);
        EDatePicker.setValue(eDate);
        ContactDD.setValue(appointment.getContactName());
        //Double check this line ???
        UserDD.setValue(String.valueOf(appointment.getUserId()));
        try {
            // Retrieve the customer name from the database
            String customerName = CustomerDAO.getCustomerName(appointment.getCustId());
            // Set the customer name in the combo box
            CustomerDD.setValue(customerName);
        } catch (SQLException e) {
            // Handle the exception
        }
    }


    /**
     * The save button saves the modified appointment to the database and returns to the main screen.
     * RUNTIME ERROR: The save button was not saving the modified appointment to the database. I had to change the setApptId to setApptId(Integer.parseInt(IdTxt.getText())) to get the appointment id to save.
     */
    @FXML
    public void OnActionSaveAppt(ActionEvent event) throws IOException, SQLException {

        // Retrieve the appointment Id from the text field
        int apptId = Integer.parseInt(IdTxt.getText());

        // Retrieve the selected Customer name.
        String customerName = CustomerDD.getValue();

        // Retrieve the selected userId from the combo box.
        String selectedUserId = UserDD.getSelectionModel().getSelectedItem();

        // Retrieve the selected contact name from the combo box.
        String contactName = ContactDD.getValue();

        // Retrieve the selected date and time from the DatePicker and ComboBox
        LocalDate sdate = SDatePicker.getValue();
        String stime = String.valueOf(STimeDD.getValue());
        LocalDate edate = EDatePicker.getValue();
        String etime = String.valueOf(ETimeDD.getValue());

        // Retrieve the form input values
        String title = TitleTxt.getText();
        String description = DescriptionTxt.getText();
        String location = LocationTxt.getText();
        String type = typeDD.getValue();

        // Validates that fields are not empty.
        if (customerName == null || selectedUserId == null || contactName == null || sdate == null || stime == null || edate == null || etime == null || title.isEmpty() || description.isEmpty() || location.isEmpty() || type == null) {
            // show an error message or do something else
            Alert emptyFields = new Alert(Alert.AlertType.ERROR);
            emptyFields.setTitle("Error");
            emptyFields.setHeaderText("Empty Fields");
            emptyFields.setContentText("All fields are required. Please make sure all fields are filled out.");
            emptyFields.showAndWait();
            return;
        }

        // Convert the selected userId to an integer
        int userId = Integer.parseInt(selectedUserId);

        // Retrieves the customer Id from the database associated with the customer name.
        int custId = CustomerDAO.getCustomerId(customerName);

        // Retrieves the contact ID from the database associated with the contacts name.
        int contactId = ContactDAO.getContactId(contactName);

        // Convert the selected time to a LocalTime object.
        LocalTime lt = LocalTime.parse(stime);
        LocalTime elt = LocalTime.parse(etime);

        // Create a ZoneId for UTC.
        ZoneId utcZoneId = ZoneId.of("UTC", ZoneId.SHORT_IDS);
        // Create a ZoneId for local.
        ZoneId myZoneId = ZoneId.systemDefault();

        // Combine the date and time into a LocalDateTime object.
        LocalDateTime ldt = LocalDateTime.of(sdate, lt);
        LocalDateTime eldt = LocalDateTime.of(edate, elt);

        ZonedDateTime myZDT = ZonedDateTime.of(ldt, myZoneId);
        ZonedDateTime myEZDT = ZonedDateTime.of(eldt, myZoneId);

        ZonedDateTime utcZDT = ZonedDateTime.ofInstant(myZDT.toInstant(), utcZoneId);
        ZonedDateTime utcEZDT = ZonedDateTime.ofInstant(myEZDT.toInstant(), utcZoneId);

        String start = utcZDT.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String end = utcEZDT.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Retrieve the list of appointments for the selected customer
        List<Appointments> appointments = AppointmentsDAO.getAppointmentsForCustomer(custId);

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

            if (estStartTime.toLocalTime().isBefore(LocalTime.of(8, 0)) || estStartTime.toLocalTime().isAfter(LocalTime.of(22, 0)) || estEndTime.toLocalTime().isBefore(LocalTime.of(8, 0)) || estEndTime.toLocalTime().isAfter(LocalTime.of(22, 0)) || estEndTime.toLocalTime().isBefore(estStartTime.toLocalTime())) {
                Alert businessHours = new Alert(Alert.AlertType.ERROR);
                businessHours.setTitle("Error");
                businessHours.setHeaderText("Invalid Time Range");
                businessHours.setContentText("Start and end times must be during business hours between 8:00 EST and 22:00 EST and the end time must be after the start time.");
                businessHours.showAndWait();
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

                // Replace this with the ID of the appointment being modified
                int appointmentId = apptId;

                // Loop through the appointments for the selected customer
                for (Appointments appointment : appointments) {

                    // Skip the overlap check for the appointment being modified
                    if (appointment.getApptId() == appointmentId) {
                        continue;
                    }
                    // Gets string Start and End from appointment list
                    String sStart = appointment.getStart();

                    String sEnd = appointment.getEnd();

                    // Converts String to LocalDateTime type and formats
                    LocalDateTime checkStart = LocalDateTime.parse(sStart, formatter);
                    LocalDateTime checkEnd = LocalDateTime.parse(sEnd, formatter);

                    // Create a ZonedDateTime object for the start and end time of the existing appointment using the UTC ZoneId
                    ZonedDateTime checkAppointmentStart = ZonedDateTime.of(checkStart, utcZoneId);
                    ZonedDateTime checkAppointmentEnd = ZonedDateTime.of(checkEnd, utcZoneId);

                    if ((utcEZDT.isBefore(checkAppointmentEnd) && utcZDT.isAfter(checkAppointmentStart)) || utcZDT.isEqual(checkAppointmentStart) || utcEZDT.isEqual(checkAppointmentEnd)) {
                        // There is an overlap, show an alert
                        Alert overlap = new Alert(Alert.AlertType.ERROR);
                        overlap.setTitle("Error");
                        overlap.setHeaderText("Overlapping Appointments");
                        overlap.setContentText("The customer already has an appointment scheduled during this time. Please reschedule for another time.");
                        overlap.showAndWait();
                        return;
                    }
                }
                // Try to update the customer in the database.
                boolean success = AppointmentsDAO.updateAppointment(apptId, title, description, location, type, start, end, custId, userId, contactId);
                if (success) {
                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/view/appointments-view.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.show();
                } else {
                    // Display error message to user
                    System.out.println("Error adding customer");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setContentText("Please enter a valid value for each Text Field.");
                    alert.showAndWait();
                }
            }
        }
    }

    /**
     * This method is called when the user clicks the cancel button. It returns the user to the
     * appointments view.
     *
     * @param event The event that triggered the method.
     * @throws IOException
     */
    @FXML
    public void OnActionCancel(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/appointments-view.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** The initialize method populates the ComboBoxes. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<LocalTime> appointmentTimes = getAppointmentTimes();
        try {
            ContactDD.setItems(ContactDAO.getContacts());
            CustomerDD.setItems((CustomerDAO.getCustomers()));
            UserDD.setItems((UserDAO.getUsers()));
            typeDD.setItems((AppointmentsDAO.getType()));
            STimeDD.setItems(appointmentTimes);
            ETimeDD.setItems(appointmentTimes);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}