package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    Stage stage;
    Parent scene;
    @FXML
    public void OnActionAppointments(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/appointments-view.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    @FXML
    public void OnActionCustomers(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/customers-view.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    @FXML
    public void OnActionReports(ActionEvent event) {
    }
    @FXML
    public void OnActionLogout(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/login-view.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
}
