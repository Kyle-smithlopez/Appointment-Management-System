package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Customers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModifyCustomerController implements Initializable {

    @FXML
    public TextField customerIdTxt;
    @FXML
    public TextField customerNameTxt;
    @FXML
    public TextField customerPhoneTxt;
    @FXML
    public TextField customerAddressTxt;
    @FXML
    public TextField customerPostalCodeTxt;
    @FXML
    public ComboBox countryDD;
    @FXML
    public ComboBox divisionDD;
    Stage stage;
    Parent scene;

    public void sendCustomer(Customers customer) {
        customerIdTxt.setText(String.valueOf(customer.getCustomerId()));
        customerNameTxt.setText(customer.getCustomerName());
        customerPhoneTxt.setText(customer.getPhone());
        customerAddressTxt.setText(customer.getCustomerAddress());
        customerPostalCodeTxt.setText(customer.getPostalCode());
//  NEED TO ADD COUNTRY AND DIVISION
//        modifyPartMaxTxt.setText(String.valueOf(part.getMax()));

//        if(part instanceof InHouse) {
//            inHouseRBtn.setSelected(true);
//            machineIdTxt.setText("Machine ID");
//            modifyMachineIdTxt.setText(String.valueOf(((InHouse) part).getMachineId()));
//        }
//        else {
//            outsourcedRBtn.setSelected(true);
//            machineIdTxt.setText("Company Name");
//            modifyMachineIdTxt.setText(String.valueOf(((Outsourced) part).getCompanyName()));
//        }
    }

    @FXML
    public void OnActionSaveBtn(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/Smith/scheduler/customers-view.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    @FXML
    public void OnActionCancel(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/Smith/scheduler/customers-view.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
