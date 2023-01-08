package model;

/** Creates a Division Reports Model. */
public class DivisionReports {
    private String country;
    private String division;
    private int total;

    /** Creates a constructor for the DivisionReports class. */
    public DivisionReports(String country, String division, int total) {
        this.country = country;
        this.division = division;
        this.total = total;
    }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getDivision() { return division; }
    public void setDivision(String division) { this.division = division; }
    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }
}
