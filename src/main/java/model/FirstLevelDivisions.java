package model;

public class FirstLevelDivisions {
    private int divisionID;
    private String divisionName;
    private int countryId;



    public FirstLevelDivisions(int divisionID, String divisionName, int countryId) {
        this.divisionID = divisionID;
        this.divisionName = divisionName;
        this.countryId = countryId;
    }

    public int getDivisionID() {
        return divisionID;
    }

    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
}
