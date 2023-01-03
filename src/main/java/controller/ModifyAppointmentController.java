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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Appointments;
import model.Customers;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
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
    public ComboBox typeDD;
    @FXML
    public ComboBox STimeDD;
    @FXML
    public ComboBox ContactDD;
    @FXML
    public ComboBox CustomerDD;
    @FXML
    public ComboBox ETimeDD;
    @FXML
    public DatePicker SDatePicker;
    @FXML
    public DatePicker EDatePicker;
    public ComboBox UserDD;
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

    public void sendAppointment(Appointments appointment) {

        LocalDate sDate = appointment.getStart().toLocalDate();
        LocalTime sTime = appointment.getStart().toLocalTime();
        LocalDate eDate = appointment.getEnd().toLocalDate();
        LocalTime eTime = appointment.getEnd().toLocalTime();

        IdTxt.setText(String.valueOf(appointment.getApptId()));
        TitleTxt.setText(appointment.getTitle());
        DescriptionTxt.setText(appointment.getDescription());
        LocationTxt.setText(appointment.getLocation());
        typeDD.setValue(appointment.getType());
        STimeDD.setValue(sTime);
        ETimeDD.setValue(eTime);
        SDatePicker.setValue(sDate);
        EDatePicker.setValue(eDate);
        ContactDD.setValue(appointment.getContactId());
        CustomerDD.setValue(appointment.getCustId());
        UserDD.setValue(appointment.getUserId());
    }

    @FXML
    public void OnActionSaveAppt(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/appointments-view.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
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
