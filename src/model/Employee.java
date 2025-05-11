package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Employee {
    private int id;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String address;
    private String contactNumber;
    private String email;
    private Date hireDate;
    private String position;
    private String department;
    private double baseSalary;
    private List<Attendance> attendanceRecords;

    public Employee() {
        attendanceRecords = new ArrayList<>();
    }

    public Employee(int id, String firstName, String lastName, Date birthDate,
                    String address, String contactNumber, String email,
                    Date hireDate, String position, String department,
                    double baseSalary) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.address = address;
        this.contactNumber = contactNumber;
        this.email = email;
        this.hireDate = hireDate;
        this.position = position;
        this.department = department;
        this.baseSalary = baseSalary;
        attendanceRecords = new ArrayList<>();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(double baseSalary) {
        this.baseSalary = baseSalary;
    }

    public List<Attendance> getAttendanceRecords() {
        return attendanceRecords;
    }

    public void addAttendanceRecord(Attendance attendance) {
        attendanceRecords.add(attendance);
    }
}