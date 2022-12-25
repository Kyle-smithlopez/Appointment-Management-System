package Smith.scheduler;

import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 310);
        stage.setTitle("Appointment Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        JDBC.openConnection();
        Locale.setDefault(new Locale("fr"));
//        try {
//            ResourceBundle rb = ResourceBundle.getBundle("Language", Locale.getDefault());
//            if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en"))
//                System.out.println(rb.getString("hello") + " " + rb.getString("world"));
//        }
//
//        catch(MissingResourceException e) {
//                System.out.println("Resource file missing: " + e);
//
//        }
        launch();
        JDBC.closeConnection();

    }
}