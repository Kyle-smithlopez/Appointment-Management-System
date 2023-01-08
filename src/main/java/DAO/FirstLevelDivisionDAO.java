package DAO;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.FirstLevelDivisions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The FirstLevelDivisionDAO class is used to retrieve data from the first_level_divisions table.
 */
public class FirstLevelDivisionDAO {

    /**
     * The getFilteredDivisions method returns an ObservableList of first level divisions filtered by Country.
     */
    public static ObservableList<String> getFilteredDivisions(String country) throws SQLException {
        ObservableList<String> filteredDivision = FXCollections.observableArrayList();
        JDBC.openConnection();
        String sql = "SELECT c.Country, c.Country_ID,  d.Division_ID, d.Division FROM countries as c RIGHT OUTER JOIN " + "first_level_divisions AS d ON c.Country_ID = d.Country_ID WHERE c.Country = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, country);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            filteredDivision.add(rs.getString("Division"));
        }
        JDBC.closeConnection();
        return filteredDivision;
    }

    /**
     * The getDivisionId method retrieves a division ID by the division.
     */
    public static int getDivisionId(String division) throws SQLException {
        int divisionId = -1;
        JDBC.openConnection();
        String sql = "SELECT Division_ID FROM first_level_divisions WHERE Division = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, division);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            divisionId = rs.getInt("Division_ID");
        }
        JDBC.closeConnection();
        return divisionId;
    }

    /**
     * The getAll FirstLevelDivisions method retrieves all the divisions in the database.
     */
    // May not need ***
    public static ObservableList<FirstLevelDivisions> getAllFirstLevelDivisions() throws SQLException {
        ObservableList<FirstLevelDivisions> allDivisions = FXCollections.observableArrayList();
        JDBC.openConnection();
        String sql = "SELECT * FROM first_level_divisions";
        Query.makeQuery(sql);
        ResultSet rs = Query.getResult();
        while (rs.next()) {
            int divisionID = rs.getInt("Division_ID");
            String divisionName = rs.getString("Division");
            int countryID = rs.getInt("COUNTRY_ID");
            FirstLevelDivisions divisionResult = new FirstLevelDivisions(divisionID, divisionName, countryID);
            allDivisions.add(divisionResult);
        }
        JDBC.closeConnection();
        return allDivisions;
    }
}

