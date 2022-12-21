module com.example.scheduler {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens Smith.scheduler to javafx.fxml;
    exports Smith.scheduler;
}