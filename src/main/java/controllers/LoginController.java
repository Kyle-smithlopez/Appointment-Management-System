package controllers;

import DAO.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoginController implements Initializable {



    Stage stage;
    Parent scene;
    @FXML
    public TextField username;
    @FXML
    public PasswordField password;
    @FXML
    public Label UsernameLbl;
    @FXML
    public Label PasswordLbl;
    @FXML
    public Button LoginBtn;
    @FXML
    public Button ExitBtn;
    @FXML
    public Label TimeZoneLbl;
    @FXML
    public Label timeZoneResultLbl;
    @FXML
    public Label LoginLbl;



    @FXML
    public void OnActionLogin(ActionEvent event) throws SQLException {

        try {
            String usernameInput = username.getText();
            String passwordInput = password.getText();
            int userId = UserDAO.validateUser(usernameInput, passwordInput);
            ResourceBundle rb = ResourceBundle.getBundle("language", Locale.getDefault());

            if (userId > 0) {

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/Smith/scheduler/main-menu-view.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }

            //log the successful login
//                outputFile.print("user: " + usernameInput + " successfully logged in at: " + "\n");
            else if (userId < 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(rb.getString("Error"));
                alert.setContentText(rb.getString("Incorrect") + " " + rb.getString("Username") + " " + rb.getString("or") + " " + rb.getString("Password"));
                alert.showAndWait();
            }
        }

         catch(IOException e){
             e.printStackTrace();
            }
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
    public void initialize(URL url, ResourceBundle rb) {
        try
        {

            Locale locale = Locale.getDefault();
            Locale.setDefault(locale);

//            ZoneId z = ZoneId.systemDefault();
//
//
//            timeZoneResultLbl.setText(String.valueOf(z));
            timeZoneResultLbl.setText(ZoneId.systemDefault().toString());

            rb = ResourceBundle.getBundle("language", Locale.getDefault());
            LoginLbl.setText(rb.getString("Login"));
            UsernameLbl.setText(rb.getString("Username") + ":");
            PasswordLbl.setText(rb.getString("Password") + ":" +
                    "" +
                    "");
            LoginBtn.setText(rb.getString("Login"));
            ExitBtn.setText(rb.getString("Exit"));
            TimeZoneLbl.setText(rb.getString("TimeZone"));

        } catch(MissingResourceException e) {
            System.out.println("Resource file missing: " + e);
        } catch (Exception e)
        {
            System.out.println(e);
        }
    }
}
