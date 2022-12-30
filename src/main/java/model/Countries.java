package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Countries {

    private int countryId;
    public String country;
//    private ObservableList<Countries> allCountries = FXCollections.observableArrayList();

    public Countries(int countryId, String country) {
        this.countryId = countryId;
        this.country = country;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
