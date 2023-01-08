package Smith.scheduler;

import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;

/**
 * This application is for C195 - Software 2.
 * An application that schedules appointments.
 * FUTURE ENHANCEMENT: In the future, I would add the option to display a graph of scheduled appointments so that it is easier to read.
 * JavaDocs located in Javadoc folder under Scheduler.
 * @author Kyle Smith
 */

/** Launches the Main Application. */
public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 310);
        stage.setTitle("Appointment Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws Exception {
        JDBC.openConnection();
//        Locale.setDefault(new Locale("fr"));
        launch();
        JDBC.closeConnection();
    }
}