package org.example;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class mainFrame extends JFrame {
    private static final String EMPLOYEES_COLLECTION = "employees";

    Attendance attendanceFrame = new Attendance();
    JTextField idTextField, nameTextField, positionTextField, salaryTextField;
    JLabel idLabel, nameLabel, positionLabel, salaryLabel;
    JButton addButton, updateButton, deleteButton, ownerCostsButton;
    JTable table;
    DefaultTableModel tableModel;
    List<Employee> employeesList = new ArrayList<>();
    private Firestore db;

    public mainFrame() {
        this.setTitle("Payroll System");
        this.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Employee Details"));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Options"));

        idLabel = new JLabel("ID:");
        idTextField = new JTextField();
        nameLabel = new JLabel("Name:");
        nameTextField = new JTextField();
        positionLabel = new JLabel("Position:");
        positionTextField = new JTextField();
        salaryLabel = new JLabel("Salary:");
        salaryTextField = new JTextField();

        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        ownerCostsButton = new JButton("Owner Costs");
        JButton payrollSummaryButton = new JButton("Payroll Summary");

        formPanel.add(idLabel);
        formPanel.add(idTextField);
        formPanel.add(nameLabel);
        formPanel.add(nameTextField);
        formPanel.add(positionLabel);
        formPanel.add(positionTextField);
        formPanel.add(salaryLabel);
        formPanel.add(salaryTextField);

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(ownerCostsButton);
        buttonPanel.add(payrollSummaryButton);

        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Position", "Salary"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        addButton.addActionListener(e -> addEmployee());
        updateButton.addActionListener(e -> updateEmployee());
        deleteButton.addActionListener(e -> deleteEmployee());
        ownerCostsButton.addActionListener(e -> showOwnerCosts());
        payrollSummaryButton.addActionListener(e -> showPayrollSummary());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0 && selectedRow < tableModel.getRowCount()) {
                    idTextField.setText(table.getValueAt(selectedRow, 0).toString());
                    nameTextField.setText(table.getValueAt(selectedRow, 1).toString());
                    positionTextField.setText(table.getValueAt(selectedRow, 2).toString());
                    salaryTextField.setText(table.getValueAt(selectedRow, 3).toString());
                }
            }
        });

        this.setSize(600, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        attendanceFrame.setVisible(true);

        db = FirebaseUtil.getDb();
        if (db == null) {
            JOptionPane.showMessageDialog(this, "Firestore is not initialized. Please check your Firebase setup.");
            return;
        }
        loadEmployeesFromFirestore();
    }

    private void clearFields() {
        idTextField.setText("");
        nameTextField.setText("");
        positionTextField.setText("");
        salaryTextField.setText("");
        table.clearSelection();
    }

    private boolean isInputValid() {
        return !idTextField.getText().trim().isEmpty() &&
                !nameTextField.getText().trim().isEmpty() &&
                !positionTextField.getText().trim().isEmpty() &&
                !salaryTextField.getText().trim().isEmpty();
    }

    public void addEmployee() {
        if (!isInputValid()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String id = idTextField.getText().trim();
            String name = nameTextField.getText().trim();
            String position = positionTextField.getText().trim();
            double salary = Double.parseDouble(salaryTextField.getText().trim());

            if (salary < 0) {
                JOptionPane.showMessageDialog(this, "Salary cannot be negative.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Employee employee = new Employee(id, name, position, salary);
            db.collection(EMPLOYEES_COLLECTION).document(id).set(employee).get();
            clearFields();
            loadEmployeesFromFirestore();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid salary format.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (InterruptedException | ExecutionException e) {
            JOptionPane.showMessageDialog(this, "Error adding employee: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private void loadEmployeesFromFirestore() {
        try {
            ApiFuture<QuerySnapshot> future = db.collection(EMPLOYEES_COLLECTION).get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            employeesList.clear();
            tableModel.setRowCount(0);

            for (QueryDocumentSnapshot doc : documents) {
                Employee employee = doc.toObject(Employee.class);
                employeesList.add(employee);
                tableModel.addRow(new Object[]{
                        employee.getId(),
                        employee.getName(),
                        employee.getPosition(),
                        employee.getBasicSalary()
                });
            }
        } catch (InterruptedException | ExecutionException e) {
            JOptionPane.showMessageDialog(this, "Error loading employees: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    public void updateEmployee() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1 || !isInputValid()) {
            JOptionPane.showMessageDialog(this, "Please select and fill in all fields.", "Update Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String id = idTextField.getText().trim();
            String name = nameTextField.getText().trim();
            String position = positionTextField.getText().trim();
            double salary = Double.parseDouble(salaryTextField.getText().trim());

            Employee employee = new Employee(id, name, position, salary);
            db.collection(EMPLOYEES_COLLECTION).document(id).set(employee).get();
            clearFields();
            loadEmployeesFromFirestore();
            JOptionPane.showMessageDialog(this, "Employee updated successfully!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid salary format.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (InterruptedException | ExecutionException e) {
            JOptionPane.showMessageDialog(this, "Error updating employee: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    public void deleteEmployee() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select an employee to delete.", "Delete Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this employee?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String employeeId = table.getValueAt(selectedRow, 0).toString();
                db.collection(EMPLOYEES_COLLECTION).document(employeeId).delete().get();
                clearFields();
                loadEmployeesFromFirestore();
                JOptionPane.showMessageDialog(this, "Employee deleted successfully!");
            } catch (InterruptedException | ExecutionException e) {
                JOptionPane.showMessageDialog(this, "Error deleting employee: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    public void showPayrollSummary() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select an employee to view payroll summary.");
            return;
        }

        try {
            String id = table.getValueAt(selectedRow, 0).toString();
            String name = table.getValueAt(selectedRow, 1).toString();
            String position = table.getValueAt(selectedRow, 2).toString();
            double salary = Double.parseDouble(table.getValueAt(selectedRow, 3).toString());

            List<AttendanceRecord> attendanceData = attendanceFrame.getAttendanceDataById(id);
            if (attendanceData == null || attendanceData.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No attendance records found for employee ID: " + id);
                return;
            }

            new PayrollSummaryFrame(name, id, salary, attendanceData, employeesList);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid salary format.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void showOwnerCosts() {
        if (employeesList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No employees available to display.");
            return;
        }
        new OwnerLiquidationFrame(employeesList, attendanceFrame);
    }
}
