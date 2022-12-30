package DAO;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Countries;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class CountryDAO {

    public static ObservableList<Countries> getAllCountries() {
        ObservableList<Countries> allCountries = FXCollections.observableArrayList();
        JDBC.openConnection();
        try {
            String sql = "SELECT * FROM COUNTRIES";
            Query.makeQuery(sql);
            ResultSet rs = Query.getResult();
            while (rs.next()) {
                int countryId = rs.getInt("Country_ID");
                String country = rs.getString("Country");
                Countries countriesResult = new Countries(countryId, country);
                allCountries.add(countriesResult);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBC.closeConnection();
        }
        return allCountries;
    }

    public static ObservableList<String> getCountries() throws SQLException {

        ObservableList<String> allCountries = FXCollections.observableArrayList();
        String sql = "SELECT * FROM COUNTRIES";
        JDBC.openConnection();
        Query.makeQuery(sql);
        ResultSet rs = Query.getResult();

        while (rs.next()) {
            allCountries.add(rs.getString("Country"));
        }
        JDBC.closeConnection();
        return allCountries;
    }
}

