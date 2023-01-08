package controller;

import DAO.AppointmentsDAO;
import DAO.ContactDAO;
import DAO.CountryDAO;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.AppointmentReports;
import model.Appointments;
import model.DivisionReports;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The Report Controller creates the Report Menu.
 */
public class ReportController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    public TableView<Appointments> contactApptTableView;
    @FXML
    public TableColumn<Appointments, Integer> apptIdCol;
    @FXML
    public TableColumn<Appointments, String> titleCol;
    @FXML
    public TableColumn<Appointments, String> typeCol;
    @FXML
    public TableColumn<Appointments, String> descriptionCol;
    @FXML
    public TableColumn<Appointments, String> locationCol;
    @FXML
    public TableColumn<Appointments, String> sTimeCol;
    @FXML
    public TableColumn<Appointments, String> eTimeCol;
    @FXML
    public TableColumn<Appointments, Integer> custIdCol;
    @FXML
    public TableView<AppointmentReports> sortedApptTableView;
    @FXML
    public TableColumn<Appointments, String> apptMonthCol;
    @FXML
    public TableColumn<Appointments, String> sortedTypeCol;
    @FXML
    public TableColumn<Appointments, Integer> sortedTotalCol;
    @FXML
    public TableView<DivisionReports> divisionReportTableView;
    @FXML
    public TableColumn<Appointments, String> divisionCol;
    @FXML
    public TableColumn<Appointments, Integer> divisionTotalCol;
    @FXML
    public ComboBox<String> contactsDD;
    @FXML
    public ComboBox<String> countryDD;

    /**
     * This method filters appointment by Selected Contact.
     */
    @FXML
    public void OnActionFilterContact(ActionEvent event) throws SQLException {

        // Get the selected contact's name from the combo box
        String contactName = contactsDD.getSelectionModel().getSelectedItem();

        // take user selected Contact_Name and find the contact_ID FK so we can add to appointments table.
        int contactId = ContactDAO.getContactId(contactName);

        // Retrieve the list of appointments for the selected contact from the database
        List<Appointments> appointments = AppointmentsDAO.getAppointmentsForContact(contactId);

        // Set the items in the table view to the list of appointments
        contactApptTableView.setItems(FXCollections.observableArrayList(appointments));
    }

    /**
     * This method filters division by Selected Country.
     */
    @FXML
    public void OnActionFilterCountry(ActionEvent event) throws SQLException {
        // Get the selected country from the combo box
        String country = countryDD.getSelectionModel().getSelectedItem();

        // Retrieve the list of division reports for the selected country from the database
        List<DivisionReports> divReports = AppointmentsDAO.getDivisionCustomerCountReport(country);

        // Set the items in the table view to the list of division reports
        divisionReportTableView.setItems(FXCollections.observableArrayList(divReports));
    }

    /**
     * This method returns to the Main Menu.
     */
    @FXML
    public void OnActionBackBtn(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/main-menu-view.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** The initialize method populates the table column data as well as the ComboBoxes. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Lambda Expression - Populates the Contact Filtered Table
        /**
         * Lambda Expression: Sets the cell value factory for various table columns in the program, allowing the program to populate the table with data from a specified field in the objects being added to the table.
         */

        apptIdCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getApptId()));
        titleCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        typeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        descriptionCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        locationCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocation()));
        sTimeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStart()));
        eTimeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEnd()));
        custIdCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCustId()));

        // Populates to the Sorted Table by Type and Month
        apptMonthCol.setCellValueFactory(new PropertyValueFactory<>("month"));
        sortedTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        sortedTotalCol.setCellValueFactory(new PropertyValueFactory<>("total"));

        //Populates DivisionCustomer table
        divisionCol.setCellValueFactory(new PropertyValueFactory<>("division"));
        divisionTotalCol.setCellValueFactory(new PropertyValueFactory<>("total"));

        try {
            contactsDD.setItems(ContactDAO.getContacts());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            countryDD.setItems(CountryDAO.getCountries());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<AppointmentReports> reports = null;
        try {
            reports = FXCollections.observableArrayList(AppointmentsDAO.getReportTotalByTypeAndMonth());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        sortedApptTableView.setItems(reports);
    }
}
