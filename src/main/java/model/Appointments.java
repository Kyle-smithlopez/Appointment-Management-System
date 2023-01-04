package model;

import java.sql.Timestamp;

public class Appointments {
    private int apptId;
    private String title;
    private String description;
    private String location;
    private String type;
    private Timestamp start;
    private Timestamp end;
    public int custId;
    public int userId;
    public static int contactId;
    public String custName;
    public String userName;
    public String contactName;

//    private ZonedDateTime start;
//    private ZonedDateTime end;

    public Appointments(int apptId, String title, String description, String location, String type, Timestamp start, Timestamp end, int custId, int userId, int contactId, String contactName) {

        this.apptId = apptId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.custId = custId;
        this.userId = userId;
        this.contactId = contactId;
        this.contactName = contactName;
//        this.start = start.toInstant().atZone(ZoneId.of("UTC"));
//        this.end = end.toInstant().atZone(ZoneId.of("UTC"));
    }

    public int getApptId() {
        return apptId;
    }

    public void setApptId(int apptId) {
        this.apptId = apptId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public static int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

//    public ZonedDateTime getStart() {
//        return start;
//    }
//
//    public void setStart(ZonedDateTime start) {
//        this.start = start;
//    }
//
//    public ZonedDateTime getEnd() {
//        return end;
//    }
//
//    public void setEnd(ZonedDateTime end) {
//        this.end = end;
//    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }


}
