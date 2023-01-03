package controller;

import DAO.*;
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
import model.Contacts;
import model.Customers;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

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


//    @FXML
//    public void OnActionSaveAppt(ActionEvent event) throws IOException, SQLException {
//
//        // Retrieve the selected date and time from the DatePicker and ComboBox
//        LocalDate date = sDatePicker.getValue().toLocalDate();
//        String time = startDD.getValue();
//
//// Convert the selected time to a LocalTime object
//        LocalTime lt = LocalTime.parse(time);
//
//// Combine the date and time into a LocalDateTime object
//        LocalDateTime ldt = LocalDateTime.of(date, lt);
//
//        // Retrieve the division value from the combo box
////        String division = divisionDD.getValue();
//
//        // Retrieve the divisionId using the division value
////        int divisionId = FirstLevelDivisionDAO.getDivisionId(division);
//
//        // Open a connection to the database
//        // Try to add a customer to the database
//        String title = titleTxt.getText();
//        String description = descriptionTxt.getText();
//        String location = locationTxt.getText();
//
//        String type = typeDD.getValue();
//        String start = startDD.getValue();
//        String end = endDD.getValue();
//
//        int custId = customerDD.getValue();
//        int userId = userDD.getValue();
//        int contactId = contactsDD.getValue();
//
//
//        boolean success = AppointmentsDAO.addAppointment(title, description, location, type, start, end, custId, userId, contactId);
//        if (success) {
//            // Display message to user indicating successful addition of customer
//            System.out.println("Customer added successfully");
//
//            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
//            scene = FXMLLoader.load(getClass().getResource("/view/appointments-view.fxml"));
//            stage.setScene(new Scene(scene));
//            stage.show();
//        } else {
//            // Display error message to user
//            System.out.println("Error adding customer");
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Error Dialog");
//            alert.setContentText("Please enter a valid value for each Text Field.");
//            alert.showAndWait();
//        }
//    }



    @FXML
    public void OnActionSaveAppt(ActionEvent event) throws IOException, SQLException {



        // Retrieve the division value from the combo box
        String customerName = customerDD.getValue();
        String userName = userDD.getValue();
        String contactName = contactsDD.getValue();


        // Retrieve the divisionId using the division value
        int custId = CustomerDAO.getCustomerId(customerName);
        int userId = UserDAO.getUserId(userName);
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

        // Retrieve the form input values
        String title = titleTxt.getText();
        String description = descriptionTxt.getText();
        String location = locationTxt.getText();
        String type = typeDD.getValue();


        try {
            // Try to add the appointment to the database
            boolean success = AppointmentsDAO.addAppointment(title, description, location, type, ldt, eldt, custId, userId, contactId);
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
