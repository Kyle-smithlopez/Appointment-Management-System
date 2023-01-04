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
    public ComboBox STimeDD;
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

    private ObservableList<String> getAppointmentTimes() {
        ObservableList<String> appointmentTimes = FXCollections.observableArrayList();

        LocalTime firstAppointment = LocalTime.MIN.plusHours(8);
        LocalTime lastAppointment = LocalTime.MAX.minusHours(4);

        if (!firstAppointment.equals(0) || !lastAppointment.equals(0)) {
            while (firstAppointment.isBefore(lastAppointment)) {
                appointmentTimes.add(String.valueOf(firstAppointment));
                firstAppointment = firstAppointment.plusMinutes(15);
            }
        }
        return appointmentTimes;
    }

    public void sendAppointment(Appointments appointment) throws SQLException {

        //        JDBC.openConnection();
        LocalDateTime localStart = appointment.getStart().toLocalDateTime();
        LocalDateTime localEnd = appointment.getEnd().toLocalDateTime();
        LocalTime sTime = localStart.toLocalTime();
        LocalDate sDate = localStart.toLocalDate();
        LocalTime eTime = localEnd.toLocalTime();
        LocalDate eDate = localEnd.toLocalDate();

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

        // Combine the date and time into a LocalDateTime object
        LocalDateTime ldt = LocalDateTime.of(sdate, lt);
        LocalDateTime eldt = LocalDateTime.of(edate, elt);

        // Convert the LocalDateTime objects to ZonedDateTime objects in UTC
        ZonedDateTime startTimeUTC = ldt.atZone(ZoneId.of("UTC"));
        ZonedDateTime endTimeUTC = eldt.atZone(ZoneId.of("UTC"));

        // Retrieve the form input values
        String title = TitleTxt.getText();
        String description = DescriptionTxt.getText();
        String location = LocationTxt.getText();
        String type = typeDD.getValue();


        // Try to update the customer in the database
        boolean success = AppointmentsDAO.updateAppointment(apptId, title, description, location, type, startTimeUTC, endTimeUTC, custId, userId, contactId);
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
