package model;

import java.util.Date;

public class Report {
    private int id;
    private String reportType; // BIR, SSS, PhilHealth, Pag-IBIG
    private Date startPeriod;
    private Date endPeriod;
    private Date generatedDate;
    private String content;
    private String status; // Draft, Submitted, etc.

    public Report() {
    }

    public Report(int id, String reportType, Date startPeriod, Date endPeriod,
                  Date generatedDate, String content, String status) {
        this.id = id;
        this.reportType = reportType;
        this.startPeriod = startPeriod;
        this.endPeriod = endPeriod;
        this.generatedDate = generatedDate;
        this.content = content;
        this.status = status;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public Date getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(Date startPeriod) {
        this.startPeriod = startPeriod;
    }

    public Date getEndPeriod() {
        return endPeriod;
    }

    public void setEndPeriod(Date endPeriod) {
        this.endPeriod = endPeriod;
    }

    public Date getGeneratedDate() {
        return generatedDate;
    }

    public void setGeneratedDate(Date generatedDate) {
        this.generatedDate = generatedDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}