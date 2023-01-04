package controller;

import DAO.AppointmentsDAO;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointments;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

import static DAO.AppointmentsDAO.getAppointmentsWithinRange;

public class AppointmentsController implements Initializable {

    @FXML
    public TableView<Appointments> apptTableView;
    @FXML
    public TableColumn<Appointments, Integer> apptIdCol;
    @FXML
    public TableColumn<Appointments, String> titleCol;
    @FXML
    public TableColumn<Appointments, String> typeCol;
    @FXML
    public TableColumn<Appointments, String> descriptCol;
    @FXML
    public TableColumn<Appointments, String> locationCol;
    @FXML
    public TableColumn<Appointments, String> contactCol;
    @FXML
    public TableColumn<Appointments, ZonedDateTime> sTimeCol;

    @FXML
    public TableColumn<Appointments, ZonedDateTime> eTimeCol;

    @FXML
    public TableColumn<Appointments, Integer> custIdCol;
    @FXML
    public TableColumn<Appointments, Integer> userIdCol;
    @FXML
    public ToggleGroup apptTG;
    @FXML
    public RadioButton allViewRB;
    @FXML
    public RadioButton weekRB;
    @FXML
    public RadioButton monthRB;

    Stage stage;
    Parent scene;

    public void refreshTableView() throws Exception {
        // Clear the current list of customers
        Appointments.clear();

        // Open a connection to the database
        JDBC.openConnection();

        // Retrieve the list of customers from the database
        Appointments = AppointmentsDAO.getAllAppointments();

        // Close the connection to the database
        JDBC.closeConnection();

        // Set the items in the table view to the list of customers
        apptTableView.setItems(Appointments);
    }


    @FXML
    public void OnActionAddApptBtn(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/add-appointment-view.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    public void OnActionModifyApptBtn(ActionEvent event) throws IOException, SQLException {
        if (apptTableView.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Error: No Appointment Selected");
            alert.showAndWait();
        } else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/modify-appointment-view.fxml"));
            loader.load();

            ModifyAppointmentController MCController = loader.getController();
            MCController.sendAppointment(apptTableView.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    @FXML
    public void OnActionDeleteApptBtn(ActionEvent actionEvent) throws Exception {

        // Check if an appointment is selected in the table view
        if (apptTableView.getSelectionModel().getSelectedItem() == null) {
            // Display warning message if no appointment is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Error: No Appointment Selected");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this appointment?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Retrieve the selected customer's ID
                int appointmentId = apptTableView.getSelectionModel().getSelectedItem().getApptId();
                String Type = apptTableView.getSelectionModel().getSelectedItem().getType();

                // Try to delete the appointment from the database
                boolean success = AppointmentsDAO.deleteAppointment(appointmentId);
                if (success) {
                    // Display message to user indicating successful deletion of appointment
                    System.out.println("Appointment deleted successfully");
                    Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
                    confirmation.setTitle("Appointment Cancelled");
                    confirmation.setHeaderText("Appointment ID: " + appointmentId);
                    confirmation.setContentText("Type of Appointment: " + Type);
                    confirmation.showAndWait();

                    // Refresh the table view to reflect the changes
                    refreshTableView();
                } else {
                    // Display error message to user
                    System.out.println("Error deleting appointment");
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error Dialog");
                    error.setContentText("An error occurred while deleting the appointment. Please try again.");
                    error.showAndWait();
                }
            }
        }
    }

    @FXML
    public void OnActionBackBtn(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/main-menu-view.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void OnActionAllRB(ActionEvent event) throws Exception {
        refreshTableView();
//        apptTableView.setItems(Appointments);
    }

    public void OnActionWeekRB(ActionEvent event) throws Exception {
        refreshTableView();
        ObservableList<Appointments> filteredAppointments = getAppointmentsWithinRange(Appointments, ZonedDateTime.now().withZoneSameInstant(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()), ZonedDateTime.now().withZoneSameInstant(ZoneId.systemDefault()).plusDays(7).toLocalDate().atStartOfDay(ZoneId.systemDefault()));
        apptTableView.setItems(filteredAppointments);
    }

    public void OnActionMonthRB(ActionEvent event) throws Exception {
        refreshTableView();
        ObservableList<Appointments> filteredAppointments = getAppointmentsWithinRange(Appointments, ZonedDateTime.now().withZoneSameInstant(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()), ZonedDateTime.now().withZoneSameInstant(ZoneId.systemDefault()).plusMonths(1).toLocalDate().atStartOfDay(ZoneId.systemDefault()));
        apptTableView.setItems(filteredAppointments);
    }


    ObservableList<Appointments> Appointments = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        apptIdCol.setCellValueFactory(new PropertyValueFactory<>("apptId"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        sTimeCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        eTimeCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        custIdCol.setCellValueFactory(new PropertyValueFactory<>("custId"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));

        try {
            Appointments.addAll(AppointmentsDAO.getAllAppointments());

        } catch (Exception e) {
            System.out.println(e);
        }
        apptTableView.setItems(Appointments);
    }
}