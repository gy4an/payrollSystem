package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class mainFrame extends JFrame {
    Attendance attendanceFrame = new Attendance();
    JTextField idTextField, nameTextField, positionTextField, salaryTextField;
    JLabel idLabel, nameLabel, positionLabel, salaryLabel;
    JButton addButton, updateButton, deleteButton, computeButton;
    JTable table;
    DefaultTableModel tableModel;
    List<Employee> employeesList = new ArrayList<>();


    public mainFrame() {
        this.setTitle("Payroll System");
        this.setLayout(new BorderLayout());


        JPanel formPanel = new JPanel(new GridLayout(4,2,5,5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Add Employee"));
        JPanel buttonPanel = new JPanel(new GridLayout(1,3,5,5));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Options"));

        idLabel = new JLabel("ID");
        idTextField = new JTextField();
        nameLabel = new JLabel("Name");
        nameTextField = new JTextField();
        positionLabel = new JLabel("Position");
        positionTextField = new JTextField();
        salaryLabel = new JLabel("Salary");
        salaryTextField = new JTextField();
//        computeButton = new JButton("Compute Payroll");
        addButton = new JButton("Add Employee");
        updateButton = new JButton("Update Employee");
        deleteButton = new JButton("Delete Employee");




        formPanel.add(idLabel);
        formPanel.add(idTextField);
        formPanel.add(nameLabel);
        formPanel.add(nameTextField);
        formPanel.add(positionLabel);
        formPanel.add(positionTextField);
        formPanel.add(salaryLabel);
        formPanel.add(salaryTextField);
//        formPanel.add(computeButton);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);


        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Position", "Salary"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        addButton.addActionListener(e -> addEmployee());
//        computeButton.addActionListener(e -> computePayroll());
        JButton payrollSummaryButton = new JButton("View Payroll Summary");
        buttonPanel.add(payrollSummaryButton);

        payrollSummaryButton.addActionListener(e -> showPayrollSummary());

        this.setSize(500, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        attendanceFrame.setVisible(true);
    }
    public void addEmployee() {
        String id = idTextField.getText().trim();
        String name = nameTextField.getText().trim();
        String position = positionTextField.getText().trim();
        String salaryStr = salaryTextField.getText().trim();

        // Check for empty fields
        if (id.isEmpty() || name.isEmpty() || position.isEmpty() || salaryStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if salary is a valid number
        double salary;
        try {
            salary = Double.parseDouble(salaryStr);
            if (salary < 0) {
                JOptionPane.showMessageDialog(this, "Salary cannot be negative.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Salary must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check for duplicate ID
        for (Employee emp : employeesList) {
            if (emp.getId().equals(id)) {
                JOptionPane.showMessageDialog(this, "Employee ID already exists.", "Duplicate ID", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Add employee if all inputs are valid
        Employee employee = new Employee(id, name, position, salary);
        employeesList.add(employee);
        tableModel.addRow(new Object[]{id, name, position, salary});

        attendanceFrame.addEmployee(id, name);

        // Clear fields
        idTextField.setText("");
        nameTextField.setText("");
        positionTextField.setText("");
        salaryTextField.setText("");
    }

    public void computePayroll() {
        int row = table.getSelectedRow();
        if (row == -1) {
            return;
        }

        double basic = Double.parseDouble(table.getValueAt(row, 3).toString());
        double sss = 500;
        double philHealth = 300;
        double pagibig = 200;
        double incomeTax = basic * 0.10;

        double totalDeductions = sss + philHealth + pagibig + incomeTax;
        double netPay = basic - totalDeductions;

        String message = String.format("Gross Pay: %.2f\nSSS: %.2f\nPhilHealth: %.2f\nPag-IBIG: %.2f\nIncome Tax: %.2f\nNet Pay: %.2f",
                basic, sss, philHealth, pagibig, incomeTax, netPay);

        JOptionPane.showMessageDialog(this, message, "Payroll Computation", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showPayrollSummary() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select an employee to view payroll summary.");
            return;
        }

        String id = table.getValueAt(selectedRow, 0).toString();
        String name = table.getValueAt(selectedRow, 1).toString();
        double salary = Double.parseDouble(table.getValueAt(selectedRow, 3).toString());

        // Find the employee object to get the correct basic salary (if needed later)
        Employee selectedEmployee = null;
        for (Employee emp : employeesList) {
            if (emp.getId().equals(id)) {
                selectedEmployee = emp;
                break;
            }
        }

        new PayrollSummaryFrame(name, id, salary, attendanceFrame.getAttendanceDataById(id), employeesList); // Pass employeesList
    }
}
