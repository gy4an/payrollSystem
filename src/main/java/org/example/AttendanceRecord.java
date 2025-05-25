package org.example;

public class AttendanceRecord {
    private String date;
    private String timeIn;
    private String timeOut;
    private String dateOut;

    public AttendanceRecord(String date, String timeIn, String timeOut, String dateOut) {
        this.date = date;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.dateOut = dateOut;
    }

    public String getDate() { return date; }
    public String getTimeIn() { return timeIn; }
    public String getTimeOut() { return timeOut; }

    public String getDateOut() {
        return dateOut;
    }
}