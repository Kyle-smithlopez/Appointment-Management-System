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
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("customers-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
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

    public static void main(String[] args) throws SQLException {
        JDBC.openConnection();
//        Locale.setDefault(new Locale("fr"));
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

//        int rowsAffected = Query.insert("Cherries", 1);
//        int rowsAffected = Query.update("Cherries", 1);
//        if(rowsAffected > 0) {
//            System.out.println("Insert Successful");
//        } else {
//            System.out.println("Insert Failed");
//        }
//        Query.select();

//        System.out.println(ZoneId.systemDefault());
//        ZoneId.getAvailableZoneIds().stream().filter(z->z.contains("America")).sorted().forEach(System.out::println);
        launch();
        JDBC.closeConnection();

    }
}