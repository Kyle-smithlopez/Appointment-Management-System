package controllers;

import DAO.CustomerDAO;
import helper.JDBC;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Customers;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddCustomerController implements Initializable {

    private static int custId = 4;
    @FXML
    public ComboBox countryDD;
    @FXML
    public TextField postalCodeTxt;
    @FXML
    public TextField addressTxt;
    @FXML
    public TextField phoneTxt;
    @FXML
    public TextField nameTxt;
    @FXML
    public TextField custIdTxt;
    @FXML
    public ComboBox divisionDD;

    Stage stage;
    Parent scene;

    @FXML
    public void OnActionSaveBtn(ActionEvent event) throws IOException, SQLException {
                JDBC.openConnection();
        ObservableList<Customers> Customers = FXCollections.observableArrayList();
        try {
        String sql = "INSERT INTO CUSTOMERS (Customer_ID, Customer_Name, Address, Postal_Code, Phone) VALUES (?,?,?,?,?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, Integer.parseInt(custIdTxt.getText()));
        ps.setString(2, nameTxt.getText());
        ps.setString(3, addressTxt.getText());
        ps.setString(4, postalCodeTxt.getText());
        ps.setString(5, phoneTxt.getText());
//        ps.setString(6, String.valueOf(divisionDD.getItems()));
        ps.executeUpdate();



        JDBC.closeConnection();
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/Smith/scheduler/customers-view.fxml"));
        stage.setScene(new Scene(scene));
        stage.show(); }
        catch(Exception e) {
            System.out.print(e);
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Error Dialog");
//            alert.setContentText("Please enter a valid value for each Text Field.");
//            alert.showAndWait();
        }
    }
    @FXML
    public void OnActionCancel(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/Smith/scheduler/customers-view.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    public static int getCustIdCount() {
        custId++;
        return custId;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        custIdTxt.setText(String.valueOf(custId));
    }
}
