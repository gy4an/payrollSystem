package view;

import controller.EmployeeController;
import controller.PayrollController;
import model.Employee;
import model.Payroll;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PayrollPanel extends JPanel {
    private EmployeeController employeeController;
    private PayrollController payrollController;

    // UI Components
    private JComboBox<String> employeeComboBox;
    private JTextField txtPayPeriodStart, txtPayPeriodEnd, txtPaymentDate;
    private JButton btnGeneratePayroll, btnClear, btnViewPayslip;
    private JTable payrollTable;
    private DefaultTableModel tableModel;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public PayrollPanel(EmployeeController employeeController, PayrollController payrollController) {
        this.employeeController = employeeController;
        this.payrollController = payrollController;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Payroll Generation Section
        JPanel payrollGenPanel = new JPanel(new GridBagLayout());
        payrollGenPanel.setBorder(BorderFactory.createTitledBorder("Generate Payroll"));
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(5, 5, 5, 5);
        gbc2.anchor = GridBagConstraints.WEST;

        // Employee Selection
        gbc2.gridx = 0;
        gbc2.gridy = 0;
        payrollGenPanel.add(new JLabel("Employee:"), gbc2);
        gbc2.gridx = 1;
        employeeComboBox = new JComboBox<>();
        refreshEmployeeComboBox();
        payrollGenPanel.add(employeeComboBox, gbc2);

        // Pay Period Start
        gbc2.gridx = 0;
        gbc2.gridy = 1;
        payrollGenPanel.add(new JLabel("Pay Period Start (yyyy-MM-dd):"), gbc2);
        gbc2.gridx = 1;
        txtPayPeriodStart = new JTextField(15);
        payrollGenPanel.add(txtPayPeriodStart, gbc2);

        // Pay Period End
        gbc2.gridx = 0;
        gbc2.gridy = 2;
        payrollGenPanel.add(new JLabel("Pay Period End (yyyy-MM-dd):"), gbc2);
        gbc2.gridx = 1;
        txtPayPeriodEnd = new JTextField(15);
        payrollGenPanel.add(txtPayPeriodEnd, gbc2);

        // Payment Date
        gbc2.gridx = 0;
        gbc2.gridy = 3;
        payrollGenPanel.add(new JLabel("Payment Date (yyyy-MM-dd):"), gbc2);
        gbc2.gridx = 1;
        txtPaymentDate = new JTextField(15);
        payrollGenPanel.add(txtPaymentDate, gbc2);

        // Add section to form panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(payrollGenPanel, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        btnGeneratePayroll = new JButton("Generate Payroll");
        btnClear = new JButton("Clear");
        btnViewPayslip = new JButton("View Payslip");

        buttonPanel.add(btnGeneratePayroll);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnViewPayslip);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(buttonPanel, gbc);

        // Table
        String[] columns = {"Payroll ID", "Employee ID", "Employee Name", "Pay Period", "Gross Pay", "Total Deductions", "Net Pay"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        payrollTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(payrollTable);

        // Add components to panel
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Add event listeners
        btnGeneratePayroll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generatePayroll();
            }
        });

        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        btnViewPayslip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewPayslip();
            }
        });

        // Initialize table data
        refreshPayrollTable();
    }

    private void refreshEmployeeComboBox() {
        employeeComboBox.removeAllItems();
        List<Employee> employees = employeeController.getAllEmployees();
        for (Employee employee : employees) {
            employeeComboBox.addItem(employee.getId() + " - " + employee.getFullName());
        }
    }

    private void generatePayroll() {
        try {
            if (employeeComboBox.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "Please select an employee.");
                return;
            }

            String selectedItem = (String) employeeComboBox.getSelectedItem();
            int employeeId = Integer.parseInt(selectedItem.split(" - ")[0]);

            Date payPeriodStart = dateFormat.parse(txtPayPeriodStart.getText());
            Date payPeriodEnd = dateFormat.parse(txtPayPeriodEnd.getText());
            Date paymentDate = dateFormat.parse(txtPaymentDate.getText());

            Payroll payroll = payrollController.generatePayroll(employeeId, payPeriodStart, payPeriodEnd, paymentDate);

            if (payroll != null) {
                refreshPayrollTable();
                clearForm();
                JOptionPane.showMessageDialog(this, "Payroll generated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to generate payroll. Please check employee data.");
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use yyyy-MM-dd format.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void viewPayslip() {
        int selectedRow = payrollTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a payroll record to view payslip.");
            return;
        }

        int payrollId = (int) payrollTable.getValueAt(selectedRow, 0);
        Payroll payroll = payrollController.getPayroll(payrollId);

        if (payroll != null) {
            Employee employee = employeeController.getEmployee(payroll.getEmployeeId());

            if (employee != null) {
                // Create a simple payslip view
                StringBuilder payslip = new StringBuilder();
                payslip.append("EMPLOYEE PAYSLIP\n\n");
                payslip.append("Employee: ").append(employee.getFullName()).append("\n");
                payslip.append("Position: ").append(employee.getPosition()).append("\n");
                payslip.append("Department: ").append(employee.getDepartment()).append("\n\n");

                payslip.append("Pay Period: ").append(dateFormat.format(payroll.getPayPeriodStart()));
                payslip.append(" to ").append(dateFormat.format(payroll.getPayPeriodEnd())).append("\n");
                payslip.append("Payment Date: ").append(dateFormat.format(payroll.getPaymentDate())).append("\n\n");

                payslip.append("EARNINGS\n");
                payslip.append("Basic Salary: ₱").append(String.format("%.2f", payroll.getGrossPay())).append("\n\n");

                payslip.append("DEDUCTIONS\n");
                payslip.append("SSS: ₱").append(String.format("%.2f", payroll.getSssDeduction())).append("\n");
                payslip.append("PhilHealth: ₱").append(String.format("%.2f", payroll.getPhilHealthDeduction())).append("\n");
                payslip.append("Pag-IBIG: ₱").append(String.format("%.2f", payroll.getPagIbigDeduction())).append("\n");
                payslip.append("Tax: ₱").append(String.format("%.2f", payroll.getTaxDeduction())).append("\n");
                payslip.append("Total Deductions: ₱").append(String.format("%.2f", payroll.calculateTotalDeductions())).append("\n\n");

                payslip.append("NET PAY: ₱").append(String.format("%.2f", payroll.getNetPay())).append("\n");

                JTextArea textArea = new JTextArea(payslip.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(400, 400));

                JOptionPane.showMessageDialog(this, scrollPane, "Employee Payslip", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void clearForm() {
        txtPayPeriodStart.setText("");
        txtPayPeriodEnd.setText("");
        txtPaymentDate.setText("");
        payrollTable.clearSelection();
    }

    private void refreshPayrollTable() {
        tableModel.setRowCount(0);
        List<Payroll> payrolls = payrollController.getAllPayrolls();
        for (Payroll payroll : payrolls) {
            Employee employee = employeeController.getEmployee(payroll.getEmployeeId());
            String employeeName = employee != null ? employee.getFullName() : "Unknown";
            String payPeriod = dateFormat.format(payroll.getPayPeriodStart()) + " to " +
                    dateFormat.format(payroll.getPayPeriodEnd());

            Object[] row = {
                    payroll.getId(),
                    payroll.getEmployeeId(),
                    employeeName,
                    payPeriod,
                    String.format("₱%.2f", payroll.getGrossPay()),
                    String.format("₱%.2f", payroll.calculateTotalDeductions()),
                    String.format("₱%.2f", payroll.getNetPay())
            };
            tableModel.addRow(row);
        }
    }
}