package controller;

import model.Employee;
import model.Attendance;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmployeeController {
    private List<Employee> employees;
    private int nextEmployeeId;
    private int nextAttendanceId;

    public EmployeeController() {
        employees = new ArrayList<>();
        nextEmployeeId = 1;
        nextAttendanceId = 1;
    }

    public Employee addEmployee(String firstName, String lastName, Date birthDate,
                                String address, String contactNumber, String email,
                                Date hireDate, String position, String department,
                                double baseSalary) {
        Employee employee = new Employee(nextEmployeeId++, firstName, lastName, birthDate,
                address, contactNumber, email, hireDate, position,
                department, baseSalary);
        employees.add(employee);
        return employee;
    }

    public boolean updateEmployee(Employee employee) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId() == employee.getId()) {
                employees.set(i, employee);
                return true;
            }
        }
        return false;
    }

    public boolean deleteEmployee(int employeeId) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId() == employeeId) {
                employees.remove(i);
                return true;
            }
        }
        return false;
    }

    public Employee getEmployee(int employeeId) {
        for (Employee employee : employees) {
            if (employee.getId() == employeeId) {
                return employee;
            }
        }
        return null;
    }

    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees);
    }

    public Attendance recordAttendance(int employeeId, Date date, Date timeIn, Date timeOut, String status) {
        Employee employee = getEmployee(employeeId);
        if (employee != null) {
            Attendance attendance = new Attendance(nextAttendanceId++, employeeId, date, timeIn, timeOut, status);
            employee.addAttendanceRecord(attendance);
            return attendance;
        }
        return null;
    }

    public List<Attendance> getEmployeeAttendance(int employeeId) {
        Employee employee = getEmployee(employeeId);
        if (employee != null) {
            return employee.getAttendanceRecords();
        }
        return new ArrayList<>();
    }
}