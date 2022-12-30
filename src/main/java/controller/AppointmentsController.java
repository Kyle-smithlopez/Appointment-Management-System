package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AppointmentsController implements Initializable {

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}