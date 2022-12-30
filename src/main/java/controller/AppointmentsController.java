package controller;

import DAO.AppointmentsDAO;
import DAO.CustomerDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointments;
import model.Customers;

import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

public class AppointmentsController implements Initializable {

    @FXML
    public TableView<Appointments> apptTableView;
    @FXML
    public TableColumn<Appointments, Integer> apptIdCol;
    @FXML
    public TableColumn<Appointments,String> titleCol;
    @FXML
    public TableColumn<Appointments,String> typeCol;
    @FXML
    public TableColumn<Appointments,String> descriptCol;
    @FXML
    public TableColumn<Appointments,String> locationCol;
    @FXML
    public TableColumn<Appointments,String> contactCol;
    @FXML
    public TableColumn<Appointments, ZonedDateTime> sTimeCol;
    @FXML
    public TableColumn<Appointments, ZonedDateTime> sDateCol;
    @FXML
    public TableColumn<Appointments, ZonedDateTime> eTimeCol;
    @FXML
    public TableColumn<Appointments, ZonedDateTime> eDateCol;
    @FXML
    public TableColumn<Appointments, Integer> custIdCol;
    @FXML
    public TableColumn<Appointments, Integer> userIdCol;
    @FXML
    public ToggleGroup apptTG;

    Stage stage;
    Parent scene;
    @FXML
    public void OnActionAddApptBtn(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/add-appointment-view.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    @FXML
    public void OnActionModifyApptBtn(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/modify-appointment-view.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    @FXML
    public void OnActionDeleteApptBtn(ActionEvent actionEvent) {
    }
    @FXML
    public void OnActionBackBtn(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/main-menu-view.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    ObservableList<Appointments> Appointments = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        apptIdCol.setCellValueFactory(new PropertyValueFactory<>("apptId"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        sTimeCol.setCellValueFactory(new PropertyValueFactory<>("start"));
//        sTimeCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        eTimeCol.setCellValueFactory(new PropertyValueFactory<>("end"));
//        eTimeCol.setCellValueFactory(new PropertyValueFactory<>("end"));
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