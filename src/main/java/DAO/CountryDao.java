package DAO;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Countries;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class CountryDao {

    public static ObservableList<Countries> getAllCountries() throws SQLException, Exception{
        ObservableList<Countries> allCountries = FXCollections.observableArrayList();
        JDBC.openConnection();
        String sql = "SELECT * FROM COUNTRIES";
        Query.makeQuery(sql);
        ResultSet rs = Query.getResult();
        while(rs.next()) {
            int countryId = rs.getInt("Country_ID");
            String country = rs.getString("Country");
            Countries countriesResult = new Countries(countryId, country);
            allCountries.add(countriesResult);
        }
        JDBC.closeConnection();
        return allCountries;
    }
}
