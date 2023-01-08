package DAO;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contacts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The ContactDAO class is used to access the Contacts table in the database.
 */
public abstract class ContactDAO {
    /**
     * This method is used to get all contacts in the table.
     */
    public static ObservableList<Contacts> getAllContacts() {
        ObservableList<Contacts> allContacts = FXCollections.observableArrayList();
        JDBC.openConnection();
        try {
            String sql = "SELECT * FROM CONTACTS";
            Query.makeQuery(sql);
            ResultSet rs = Query.getResult();
            while (rs.next()) {
                int contactId = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                Contacts contactsResult = new Contacts(contactId, contactName);
                allContacts.add(contactsResult);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBC.closeConnection();
        }
        return allContacts;
    }

    /**
     * This method is used to get contacts from the table to populate the Contacts ComboBox.
     */
    public static ObservableList<String> getContacts() throws SQLException {

        ObservableList<String> allContacts = FXCollections.observableArrayList();
        String sql = "SELECT * FROM CONTACTS";
        JDBC.openConnection();
        Query.makeQuery(sql);
        ResultSet rs = Query.getResult();
        while (rs.next()) {
            int id = rs.getInt("Contact_ID");
//            String id = rs.getString("Contact_ID");
            String name = rs.getString("Contact_Name");
//            allContacts.add(name + " [" + id + "]");
            allContacts.add(name);
        }
        JDBC.closeConnection();
        return allContacts;
    }

    /**
     * This method is used to get the contact ID associated with the contact name from the table.
     */
    public static int getContactId(String contact) throws SQLException {
        int contactId = -1;
        JDBC.openConnection();
        String sql = "SELECT CONTACT_ID FROM CONTACTS WHERE CONTACT_NAME = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, contact);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            contactId = rs.getInt("CONTACT_ID");
        }
        JDBC.closeConnection();
        return contactId;
    }
}
