package controllers;

import DAO.CountryDao;
import DAO.CustomerDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Countries;
import model.Customers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    @FXML
    public void OnActionAddCustBtn(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/Smith/scheduler/add-customer-view.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    @FXML
    public void OnActionModifyCustBtn(ActionEvent event) throws IOException {

        if(custTableView.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Error: No Customer Selected");
            alert.showAndWait();
        } else {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Smith/scheduler/modify-customer-view.fxml"));
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
    public void OnActionDeleteCustBtn(ActionEvent event) {
    }
    @FXML
    public void OnActionBackBtn(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/Smith/scheduler/main-menu-view.fxml"));
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
