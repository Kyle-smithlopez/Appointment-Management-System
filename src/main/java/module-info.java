module com.example.scheduler {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens Smith.scheduler to javafx.fxml;
    exports Smith.scheduler;
    exports controllers;
    opens controllers to javafx.fxml;
    opens model to javafx.fxml;
    exports model;
}