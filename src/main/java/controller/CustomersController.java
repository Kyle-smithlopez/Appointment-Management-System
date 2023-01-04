package controller;

import DAO.CustomerDAO;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Countries;
import model.Customers;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomersController implements Initializable {

    @FXML
    public TableView<Customers> custTableView;
    @FXML
    public TableColumn<Customers, Integer> custIdCol;
    @FXML
    public TableColumn<Customers, String> nameCol;
    @FXML
    public TableColumn<Customers, String> phoneCol;
    @FXML
    public TableColumn<Customers, String> addressCol;
    @FXML
    public TableColumn<Customers, String> FLDCol;
    @FXML
    public TableColumn<Countries, String> countryCol;
    @FXML
    public TableColumn<Customers, String> postalCol;

    Stage stage;
    Parent scene;

    public void refreshTableView() throws Exception {
        // Clear the current list of customers
        Customers.clear();

        // Open a connection to the database
        JDBC.openConnection();

        // Retrieve the list of customers from the database
        Customers = CustomerDAO.getAllCustomers();

        // Close the connection to the database
        JDBC.closeConnection();

        // Set the items in the table view to the list of customers
        custTableView.setItems(Customers);
    }


    @FXML
    public void OnActionAddCustBtn(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/add-customer-view.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    public void OnActionModifyCustBtn(ActionEvent event) throws IOException {

        if (custTableView.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Error: No Customer Selected");
            alert.showAndWait();
        } else {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/modify-customer-view.fxml"));
            loader.load();

            ModifyCustomerController MCController = loader.getController();
            MCController.sendCustomer(custTableView.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    @FXML
    public void OnActionDeleteCustBtn(ActionEvent event) throws Exception {
        // Check if a customer is selected in the table view
        if (custTableView.getSelectionModel().getSelectedItem() == null) {
            // Display warning message if no customer is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Error: No Customer Selected");
            alert.showAndWait();
        } else {
            // Get the selected customer
            Customers selectedCustomer = custTableView.getSelectionModel().getSelectedItem();

            // Check if there are any appointments associated with the customer
            boolean hasAppointments = CustomerDAO.hasAppointments(selectedCustomer.getCustomerId());

            // If there are no appointments associated with the customer, proceed with deleting the customer
            if (!hasAppointments) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setContentText("Are you sure you want to delete this customer?");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK) {
                    // Open a connection to the database
                    JDBC.openConnection();

                    // Delete the customer from the database
                    CustomerDAO.deleteCustomer(selectedCustomer.getCustomerId());

                    // Close the connection to the database
                    JDBC.closeConnection();

                    // Refresh the table view
                    refreshTableView();
                }
            } else {
                // If there are appointments associated with the customer, display an error message
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Error: Cannot delete customer because there are appointments associated with them.");
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void OnActionBackBtn(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/main-menu-view.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    ObservableList<Customers> Customers = FXCollections.observableArrayList();

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        custIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        FLDCol.setCellValueFactory(new PropertyValueFactory<>("division"));
        countryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        postalCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));

        try {
            Customers.addAll(CustomerDAO.getAllCustomers());
        } catch (Exception e) {
            System.out.println(e);
        }
        custTableView.setItems(Customers);
    }
}
