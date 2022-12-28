package model;

public class Customers {

    private int customerId;
    private String customerName;
    private String customerAddress;
    private String postalCode;
    private String phone;
    private String division;
    private String country;
    public int divisionID;

    public Customers(int customerId, String customerName, String customerAddress, String postalCode, String phone, int divisionID, String division) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionID = divisionID;
        this.division = division;
//        this.country = country;
    }

    public int getDivision_ID() {
        return divisionID;
    }

    public void setDivision_ID(int divisionID) {
        this.divisionID = divisionID;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
