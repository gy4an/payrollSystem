package model;

import java.util.Date;

public class Payroll {
    private int id;
    private int employeeId;
    private Date payPeriodStart;
    private Date payPeriodEnd;
    private Date paymentDate;
    private double grossPay;
    private double sssDeduction;
    private double philHealthDeduction;
    private double pagIbigDeduction;
    private double taxDeduction;
    private double netPay;

    public Payroll() {
    }

    public Payroll(int id, int employeeId, Date payPeriodStart, Date payPeriodEnd,
                   Date paymentDate) {
        this.id = id;
        this.employeeId = employeeId;
        this.payPeriodStart = payPeriodStart;
        this.payPeriodEnd = payPeriodEnd;
        this.paymentDate = paymentDate;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public Date getPayPeriodStart() {
        return payPeriodStart;
    }

    public void setPayPeriodStart(Date payPeriodStart) {
        this.payPeriodStart = payPeriodStart;
    }

    public Date getPayPeriodEnd() {
        return payPeriodEnd;
    }

    public void setPayPeriodEnd(Date payPeriodEnd) {
        this.payPeriodEnd = payPeriodEnd;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getGrossPay() {
        return grossPay;
    }

    public void setGrossPay(double grossPay) {
        this.grossPay = grossPay;
    }

    public double getSssDeduction() {
        return sssDeduction;
    }

    public void setSssDeduction(double sssDeduction) {
        this.sssDeduction = sssDeduction;
    }

    public double getPhilHealthDeduction() {
        return philHealthDeduction;
    }

    public void setPhilHealthDeduction(double philHealthDeduction) {
        this.philHealthDeduction = philHealthDeduction;
    }

    public double getPagIbigDeduction() {
        return pagIbigDeduction;
    }

    public void setPagIbigDeduction(double pagIbigDeduction) {
        this.pagIbigDeduction = pagIbigDeduction;
    }

    public double getTaxDeduction() {
        return taxDeduction;
    }

    public void setTaxDeduction(double taxDeduction) {
        this.taxDeduction = taxDeduction;
    }

    public double getNetPay() {
        return netPay;
    }

    public void setNetPay(double netPay) {
        this.netPay = netPay;
    }

    public double calculateTotalDeductions() {
        return sssDeduction + philHealthDeduction + pagIbigDeduction + taxDeduction;
    }
}