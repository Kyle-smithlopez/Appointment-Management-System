package model;

/** Creates a model for Reports. */
public class AppointmentReports {

    private String month;
    private String type;
    private int total;

    /** Creates a constructor for the AppointmentReports class. */
    public AppointmentReports(String month, String type, int total) {
        this.month = month;
        this.type = type;
        this.total = total;
    }

    /** Creates getter and setters. */
    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
