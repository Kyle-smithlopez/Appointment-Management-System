package Smith.scheduler;

import DAO.CustomerDAO;
import DAO.Query;
import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/appointments-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 930, 520);
        stage.setTitle("Appointment Management System");
        stage.setScene(scene);
        stage.show();
//
//        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 500, 310);
//        stage.setTitle("Appointment Management System");
//        stage.setScene(scene);
//        stage.show();
    }

    public static void main(String[] args) throws Exception{
        JDBC.openConnection();
//        Locale.setDefault(new Locale("fr"));
        launch();
        JDBC.closeConnection();

    }
}