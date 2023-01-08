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

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class creates the add customer controller.
 */
public class AddCustomerController implements Initializable {

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
    public ComboBox<String> countryDD;
    @FXML
    public ComboBox<String> divisionDD;

    Stage stage;
    Parent scene;

    /**
     * This method saves a new customer when the Save button is clicked.
     */
    @FXML
    public void OnActionSaveBtn(ActionEvent event) throws IOException, SQLException {
        // Retrieve the division value from the combo box.
        String division = divisionDD.getValue();
        // Retrieve the divisionId using the division value.
        int divisionId = FirstLevelDivisionDAO.getDivisionId(division);

        // If success, adds the customer to the database.
        String custName = nameTxt.getText();
        String address = addressTxt.getText();
        String postalCode = postalCodeTxt.getText();
        String phone = phoneTxt.getText();
        boolean success = CustomerDAO.addCustomer(custName, address, postalCode, phone, divisionId);
        if (success) {
            // Display message to user indicating successful addition of customer. *May or may not want to add confirmation alert.

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/customers-view.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        } else {
            // Display error message to user indicating unsuccessful addition of customer.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please enter a valid value for each Text Field.");
            alert.showAndWait();
        }
    }

    /**
     * This method cancels the addition of a new customer when the Cancel button is clicked.
     */
    @FXML
    public void OnActionCancel(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/customers-view.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //  Sets the Country Combo Box.
        try {
            countryDD.setItems(CountryDAO.getCountries());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Lambda Expression
        /**
         * Lambda Expression: Sets the values of the division ComboBox based on the selected value of the country ComboBox.
         * When the value of the country ComboBox changes, the lambda expression retrieves a list of filtered
         * divisions from the database and sets the items of the division ComboBox to the list of filtered divisions.
         * RUNTIME ERROR: Lambda Expression originally didn't populate the division ComboBox with the correct values.
         */
        countryDD.valueProperty().addListener((obs, oldVal, newVal) -> {
            Optional.ofNullable(newVal).ifPresent(val -> {
                divisionDD.setDisable(false);
                try {
                    divisionDD.setItems(FirstLevelDivisionDAO.getFilteredDivisions(countryDD.getValue()));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
        });
    }
}


