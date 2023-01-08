package controller;

import DAO.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;

import static model.Appointments.getLocalDateTimeFormatter;

/**
 * The Login Controller creates the Login Screen.
 */
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

    /**
     * The Login button validates the username and password and redirects the user to the main menu.
     */
    @FXML
    public void OnActionLogin(ActionEvent event) throws SQLException, IOException {

        DateTimeFormatter formatter = getLocalDateTimeFormatter();

        // Get username and password from text fields and validates user.
        try {
            String username = this.username.getText();
            String password = this.password.getText();
            int userId = UserDAO.validateUser(username, password);
            // Saves user ID to be used in other controllers.
            Global.currentUserId = userId;

            FileWriter fwriter = new FileWriter("login_activity.txt", true);
            PrintWriter outputFile = new PrintWriter(fwriter);

            ResourceBundle rb = ResourceBundle.getBundle("language", Locale.getDefault());

            if (userId > 0) {
                // Log the successful login
                outputFile.print("User: " + username + " successfully logged in at: " + LocalDateTime.now().format(formatter) + "\n");
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/main-menu-view.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            } else if (userId < 0) {
                outputFile.print("User: " + username + " failed logged in at: " + LocalDateTime.now().format(formatter) + "\n");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(rb.getString("Error"));
                alert.setContentText(rb.getString("Incorrect") + " " + rb.getString("Username") + " " + rb.getString("or") + " " + rb.getString("Password"));
                alert.showAndWait();
            }
            outputFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The Exit button closes the application.
     */
    @FXML
    public void OnActionExit(ActionEvent actionEvent) {
        ResourceBundle rb = ResourceBundle.getBundle("language", Locale.getDefault());
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(rb.getString("Are") + " " + rb.getString("you") + " " + rb.getString("sure") + " " + rb.getString("you") + " " + rb.getString("want") + " " + rb.getString("to") + " " + rb.getString("exit") + "?");

//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    public class Global {
        public static int currentUserId;
    }

    /**
     * The initialize method retrieves the local time data as well as the labels to change to FR or EN.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Retrieves local time zone and displays it on the login screen.
        try {
            Locale locale = Locale.getDefault();
            Locale.setDefault(locale);
            timeZoneResultLbl.setText(ZoneId.systemDefault().toString());
            rb = ResourceBundle.getBundle("language", Locale.getDefault());
            LoginLbl.setText(rb.getString("Login"));
            UsernameLbl.setText(rb.getString("Username") + ":");
            PasswordLbl.setText(rb.getString("Password") + ":" + "" + "");
            LoginBtn.setText(rb.getString("Login"));
            ExitBtn.setText(rb.getString("Exit"));
            TimeZoneLbl.setText(rb.getString("TimeZone"));
        } catch (MissingResourceException e) {
            System.out.println("Resource file missing: " + e);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
