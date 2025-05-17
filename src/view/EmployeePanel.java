package view;

import controller.EmployeeController;
import model.Employee;
import model.Attendance;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EmployeePanel extends JPanel {
    private EmployeeController controller;

    // UI Components
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JTextField txtFirstName, txtLastName, txtAddress, txtContact, txtEmail, txtPosition, txtDepartment, txtSalary;
    private JTextField txtBirthDate, txtHireDate;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;

    private int selectedEmployeeId = -1;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public EmployeePanel(EmployeeController controller) {
        this.controller = controller;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Personal Info Section
        JPanel personalInfoPanel = new JPanel(new GridBagLayout());
        personalInfoPanel.setBorder(BorderFactory.createTitledBorder("Personal Information"));
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(5, 5, 5, 5);
        gbc2.anchor = GridBagConstraints.WEST;

        // First Name
        gbc2.gridx = 0;
        gbc2.gridy = 0;
        personalInfoPanel.add(new JLabel("First Name:"), gbc2);
        gbc2.gridx = 1;
        txtFirstName = new JTextField(15);
        personalInfoPanel.add(txtFirstName, gbc2);

        // Last Name
        gbc2.gridx = 0;
        gbc2.gridy = 1;
        personalInfoPanel.add(new JLabel("Last Name:"), gbc2);
        gbc2.gridx = 1;
        txtLastName = new JTextField(15);
        personalInfoPanel.add(txtLastName, gbc2);

        // Birth Date
        gbc2.gridx = 0;
        gbc2.gridy = 2;
        personalInfoPanel.add(new JLabel("Birth Date (yyyy-MM-dd):"), gbc2);
        gbc2.gridx = 1;
        txtBirthDate = new JTextField(15);
        personalInfoPanel.add(txtBirthDate, gbc2);

        // Address
        gbc2.gridx = 0;
        gbc2.gridy = 3;
        personalInfoPanel.add(new JLabel("Address:"), gbc2);
        gbc2.gridx = 1;
        txtAddress = new JTextField(15);
        personalInfoPanel.add(txtAddress, gbc2);

        // Contact
        gbc2.gridx = 0;
        gbc2.gridy = 4;
        personalInfoPanel.add(new JLabel("Contact:"), gbc2);
        gbc2.gridx = 1;
        txtContact = new JTextField(15);
        personalInfoPanel.add(txtContact, gbc2);

        // Email
        gbc2.gridx = 0;
        gbc2.gridy = 5;
        personalInfoPanel.add(new JLabel("Email:"), gbc2);
        gbc2.gridx = 1;
        txtEmail = new JTextField(15);
        personalInfoPanel.add(txtEmail, gbc2);

        // Employment Info Section
        JPanel employmentInfoPanel = new JPanel(new GridBagLayout());
        employmentInfoPanel.setBorder(BorderFactory.createTitledBorder("Employment Information"));
        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.insets = new Insets(5, 5, 5, 5);
        gbc3.anchor = GridBagConstraints.WEST;

        // Hire Date
        gbc3.gridx = 0;
        gbc3.gridy = 0;
        employmentInfoPanel.add(new JLabel("Hire Date (yyyy-MM-dd):"), gbc3);
        gbc3.gridx = 1;
        txtHireDate = new JTextField(15);
        employmentInfoPanel.add(txtHireDate, gbc3);

        // Position
        gbc3.gridx = 0;
        gbc3.gridy = 1;
        employmentInfoPanel.add(new JLabel("Position:"), gbc3);
        gbc3.gridx = 1;
        txtPosition = new JTextField(15);
        employmentInfoPanel.add(txtPosition, gbc3);

        // Department
        gbc3.gridx = 0;
        gbc3.gridy = 2;
        employmentInfoPanel.add(new JLabel("Department:"), gbc3);
        gbc3.gridx = 1;
        txtDepartment = new JTextField(15);
        employmentInfoPanel.add(txtDepartment, gbc3);

        // Salary
        gbc3.gridx = 0;
        gbc3.gridy = 3;
        employmentInfoPanel.add(new JLabel("Base Salary:"), gbc3);
        gbc3.gridx = 1;
        txtSalary = new JTextField(15);
        employmentInfoPanel.add(txtSalary, gbc3);

        // Add sections to form panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        formPanel.add(personalInfoPanel, gbc);

        gbc.gridx = 1;
        formPanel.add(employmentInfoPanel, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        // Table
        String[] columns = {"ID", "Name", "Contact", "Email", "Position", "Department", "Salary"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        employeeTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(employeeTable);

        // Add components to panel
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Add event listeners
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEmployee();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateEmployee();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteEmployee();
            }
        });

        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = employeeTable.getSelectedRow();
                if (selectedRow != -1) {
                    selectedEmployeeId = (int) employeeTable.getValueAt(selectedRow, 0);
                    populateForm(selectedEmployeeId);
                }
            }
        });

        // Initialize table data
        refreshEmployeeTable();
    }

    private void addEmployee() {
        try {
            String firstName = txtFirstName.getText();
            String lastName = txtLastName.getText();
            Date birthDate = dateFormat.parse(txtBirthDate.getText());
            String address = txtAddress.getText();
            String contact = txtContact.getText();
            String email = txtEmail.getText();
            Date hireDate = dateFormat.parse(txtHireDate.getText());
            String position = txtPosition.getText();
            String department = txtDepartment.getText();
            double salary = Double.parseDouble(txtSalary.getText());

            controller.addEmployee(firstName, lastName, birthDate, address, contact, email,
                    hireDate, position, department, salary);

            refreshEmployeeTable();
            clearForm();
            JOptionPane.showMessageDialog(this, "Employee added successfully!");
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use yyyy-MM-dd format.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid salary. Please enter a valid number.");
        }
    }

    private void updateEmployee() {
        if (selectedEmployeeId == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to update.");
            return;
        }

        try {
            Employee employee = controller.getEmployee(selectedEmployeeId);
            if (employee != null) {
                employee.setFirstName(txtFirstName.getText());
                employee.setLastName(txtLastName.getText());
                employee.setBirthDate(dateFormat.parse(txtBirthDate.getText()));
                employee.setAddress(txtAddress.getText());
                employee.setContactNumber(txtContact.getText());
                employee.setEmail(txtEmail.getText());
                employee.setHireDate(dateFormat.parse(txtHireDate.getText()));
                employee.setPosition(txtPosition.getText());
                employee.setDepartment(txtDepartment.getText());
                employee.setBaseSalary(Double.parseDouble(txtSalary.getText()));

                controller.updateEmployee(employee);
                refreshEmployeeTable();
                clearForm();
                JOptionPane.showMessageDialog(this, "Employee updated successfully!");
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use yyyy-MM-dd format.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid salary. Please enter a valid number.");
        }
    }

    private void deleteEmployee() {
        if (selectedEmployeeId == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this employee?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            controller.deleteEmployee(selectedEmployeeId);
            refreshEmployeeTable();
            clearForm();
            JOptionPane.showMessageDialog(this, "Employee deleted successfully!");
        }
    }

    private void clearForm() {
        txtFirstName.setText("");
        txtLastName.setText("");
        txtBirthDate.setText("");
        txtAddress.setText("");
        txtContact.setText("");
        txtEmail.setText("");
        txtHireDate.setText("");
        txtPosition.setText("");
        txtDepartment.setText("");
        txtSalary.setText("");
        selectedEmployeeId = -1;
        employeeTable.clearSelection();
    }

    private void populateForm(int employeeId) {
        Employee employee = controller.getEmployee(employeeId);
        if (employee != null) {
            txtFirstName.setText(employee.getFirstName());
            txtLastName.setText(employee.getLastName());
            txtBirthDate.setText(dateFormat.format(employee.getBirthDate()));
            txtAddress.setText(employee.getAddress());
            txtContact.setText(employee.getContactNumber());
            txtEmail.setText(employee.getEmail());
            txtHireDate.setText(dateFormat.format(employee.getHireDate()));
            txtPosition.setText(employee.getPosition());
            txtDepartment.setText(employee.getDepartment());
            txtSalary.setText(String.valueOf(employee.getBaseSalary()));
        }
    }

    private void refreshEmployeeTable() {
        tableModel.setRowCount(0);
        List<Employee> employees = controller.getAllEmployees();
        for (Employee employee : employees) {
            Object[] row = {
                    employee.getId(),
                    employee.getFirstName() + " " + employee.getLastName(),
                    employee.getContactNumber(),
                    employee.getEmail(),
                    employee.getPosition(),
                    employee.getDepartment(),
                    employee.getBaseSalary()
            };
            tableModel.addRow(row);
        }
    }
}