package org.example;

public class AttendanceRecord {
    private String date;
    private String timeIn;
    private String timeOut;

    public AttendanceRecord(String date, String timeIn, String timeOut) {
        this.date = date;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
    }

    public String getDate() { return date; }
    public String getTimeIn() { return timeIn; }
    public String getTimeOut() { return timeOut; }
}