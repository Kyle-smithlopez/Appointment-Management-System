package controller;

import DAO.CountryDAO;
import DAO.CustomerDAO;
import DAO.FirstLevelDivisionDAO;
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
import java.sql.SQLException;
import java.util.Optional;
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
        countryDD.setValue(customer.getCountry());
        divisionDD.setValue(customer.getDivision());
    }


    @FXML
    public void OnActionSaveBtn(ActionEvent event) throws IOException, SQLException {

        // Retrieve the division value from the combo box
        String division = String.valueOf(divisionDD.getValue());

        // Retrieve the divisionId using the division value
        int divisionId = FirstLevelDivisionDAO.getDivisionId(division);

        // Retrieve the customerId from the text field
        int custId = Integer.parseInt(customerIdTxt.getText());

        // Retrieve the other customer details from the text fields
        String custName = customerNameTxt.getText();
        String address = customerAddressTxt.getText();
        String postalCode = customerPostalCodeTxt.getText();
        String phone = customerPhoneTxt.getText();

        // Try to update the customer in the database
        boolean success = CustomerDAO.updateCustomer(custId, custName, address, postalCode, phone, divisionId);
        if (success) {
            // Display message to user indicating successful update of customer
            System.out.println("Customer updated successfully");

            // Go back to the customers view
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/customers-view.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        } else {
            // Display error message to user
            System.out.println("Error adding customer");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please enter a valid value for each Text Field.");
            alert.showAndWait();
        }
    }


    @FXML
    public void OnActionCancel(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/customers-view.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Sets the Country Combo Box
        try {
            countryDD.setItems(CountryDAO.getCountries());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //Sets the Division Combo Box
        //Lambda Expression
        countryDD.valueProperty().addListener((obs, oldVal, newVal) -> {
            Optional.ofNullable(newVal).ifPresent(val -> {
                divisionDD.setDisable(false);
                try {
                    divisionDD.setItems(FirstLevelDivisionDAO.getFilteredDivisions(countryDD.getSelectionModel().getSelectedItem().toString()));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
        });
    }
}
