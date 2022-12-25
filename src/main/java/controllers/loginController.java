package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class loginController implements Initializable {

    Stage stage;
    Parent scene;
    @FXML
    public Label UsernameLbl;
    @FXML
    public Label PasswordLbl;
    @FXML
    public Button LoginBtn;
    @FXML
    public Button ExitBtn;
    @FXML
    public Label timeZoneLbl;
    @FXML
    public Label LoginLbl;



    @FXML
    public void OnActionLogin(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/smith/scheduler/main-view.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();


    }

    @FXML
    public void OnActionExit(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
