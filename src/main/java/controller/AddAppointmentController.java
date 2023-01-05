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
    @FXML
    Stage stage;
    Parent scene;

        // May want to set up a way to populate only appointments within 8AM - 10PM EST.
    private ObservableList<String> getAppointmentTimes() {
        ObservableList<String> appointmentTimes = FXCollections.observableArrayList();

        LocalTime firstAppointment = LocalTime.MIN.plusHours(6);
        LocalTime lastAppointment = LocalTime.MAX.minusHours(2);

        if (!firstAppointment.equals(0) || !lastAppointment.equals(0)) {
            while (firstAppointment.isBefore(lastAppointment)) {
                appointmentTimes.add(String.valueOf(firstAppointment));
                firstAppointment = firstAppointment.plusMinutes(15);
            }
        }
        return appointmentTimes;
    }


    @FXML
    public void OnActionSaveAppt(ActionEvent event) throws IOException, SQLException {


        // Retrieve the selected contactId from the combo box and convert it to an integer
        String customerName = customerDD.getValue();
        // take user selected Contact_Name and find the contact_ID FK so it can be add to appointments table.
        int custId = CustomerDAO.getCustomerId(customerName);

        // Retrieve the list of appointments for the selected customer
        List<Appointments> appointments = AppointmentsDAO.getAppointmentsForCustomer(custId);

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


        // Combine the date and time into a LocalDateTime object
        LocalDateTime ldt = LocalDateTime.of(sdate, lt);
        LocalDateTime eldt = LocalDateTime.of(edate, elt);

        ZoneId estZoneId = ZoneId.of("EST", ZoneId.SHORT_IDS);
        ZonedDateTime estStartTime = ldt.atZone(ZoneId.systemDefault()).withZoneSameInstant(estZoneId);
        ZonedDateTime estEndTime = eldt.atZone(ZoneId.systemDefault()).withZoneSameInstant(estZoneId);

        if (sdate.isAfter(edate)) {
            // end date is before start date, show an error message or do something else
            Alert invalidDate = new Alert(Alert.AlertType.ERROR);
            invalidDate.setTitle("Error");
            invalidDate.setHeaderText("Invalid Date Range");
            invalidDate.setContentText("End date must be on the same day or after the start date.");
            invalidDate.showAndWait();
        } else {

            if (estStartTime.toLocalTime().isBefore(LocalTime.of(8, 0)) || estStartTime.toLocalTime().isAfter(LocalTime.of(22, 0)) || estEndTime.toLocalTime().isBefore(LocalTime.of(8, 0)) || estEndTime.toLocalTime().isAfter(LocalTime.of(22, 0)) || estEndTime.toLocalTime().isBefore(estStartTime.toLocalTime())) {
                // start time is invalid, show an error message or do something else
                Alert businessHours = new Alert(Alert.AlertType.ERROR);
                businessHours.setTitle("Error");
                businessHours.setHeaderText("Invalid Time Range");
                businessHours.setContentText("Start and end times must be during business hours between 8:00 EST and 22:00 EST and the end time must be after the start time.");
                businessHours.showAndWait();
            } else {


                // Convert the LocalDateTime objects to ZonedDateTime objects in UTC
                ZonedDateTime startTimeUTC = ldt.atZone(ZoneId.of("UTC"));
                ZonedDateTime endTimeUTC = eldt.atZone(ZoneId.of("UTC"));

                //CURRENTLY WORKING ON THE CODE BELOW

                // Check if the start or end time of the new appointment overlaps with any of the existing appointments





                // Retrieve the form input values
                String title = titleTxt.getText();
                String description = descriptionTxt.getText();
                String location = locationTxt.getText();
                String type = typeDD.getValue();

                try {
                    // Try to add the appointment to the database
                    boolean success = AppointmentsDAO.addAppointment(title, description, location, type, startTimeUTC, endTimeUTC, custId, userId, contactId);
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
