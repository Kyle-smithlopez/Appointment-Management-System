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
import java.util.ResourceBundle;

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

//    private ObservableList<String> getAppointmentTimes() {
//        ObservableList<String> appointmentTimes = FXCollections.observableArrayList();
//
//        LocalTime firstAppointment = LocalTime.MIN.plusHours(8);
//        LocalTime lastAppointment = LocalTime.MAX.minusHours(4);
//
//        if (!firstAppointment.equals(0) || !lastAppointment.equals(0)) {
//            while (firstAppointment.isBefore(lastAppointment)) {
//                appointmentTimes.add(String.valueOf(firstAppointment));
//                firstAppointment = firstAppointment.plusMinutes(15);
//            }
//        }
//        return appointmentTimes;
//    }

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

        //        JDBC.openConnection();


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
//        JDBC.closeConnection();
    }


//    Commented out as I am converting the Timestamp to string 1/4/23
    @FXML
    public void OnActionSaveAppt(ActionEvent event) throws IOException, SQLException {

        // Retrieve the customerId from the text field
        int apptId = Integer.parseInt(IdTxt.getText());

        //Gets String from Customer ComboBox and pulls the INT ID associated with it.
        String customerName = CustomerDD.getValue();
        int custId = CustomerDAO.getCustomerId(customerName);

        // Retrieve the selected userId from the combo box and convert it to an integer
        String selectedUserId = UserDD.getSelectionModel().getSelectedItem();
        int userId = Integer.parseInt(selectedUserId);

        // Retrieve the selected contactId from the combo box and convert it to an integer
        String contactName = ContactDD.getValue();
        // take user selected Contact_Name and find the contact_ID FK so we can add to appointments table.
        int contactId = ContactDAO.getContactId(contactName);

        // Retrieve the selected date and time from the DatePicker and ComboBox
        LocalDate sdate = SDatePicker.getValue();
        String stime = String.valueOf(STimeDD.getValue());
        LocalDate edate = EDatePicker.getValue();
        String etime = String.valueOf(ETimeDD.getValue());

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
                String title = TitleTxt.getText();
                String description = DescriptionTxt.getText();
                String location = LocationTxt.getText();
                String type = typeDD.getValue();


                // Try to update the customer in the database
                boolean success = AppointmentsDAO.updateAppointment(apptId, title, description, location, type, start, end, custId, userId, contactId);
                if (success) {
                    // Display message to user indicating successful update of customer
                    System.out.println("Customer updated successfully");

                    // Go back to the customers view
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

    @FXML
    public void OnActionCancel(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/appointments-view.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

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


//Made changes to ComboBox from String to LocalTimes -
//  -Changed ComboBox, getAppointmentTimes and Initialize list.
