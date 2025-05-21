package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PayrollSummaryFrame extends JFrame {

    public PayrollSummaryFrame(String employeeName, String employeeId, double basicSalary, List<AttendanceRecord> attendanceRecords, List<Employee> allEmployees) {
        setTitle("Payroll Summary");
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Calculate total days and hours worked
        int totalDays = 0;
        double totalHours = 0.0;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        for (AttendanceRecord record : attendanceRecords) {
            if (record.getTimeIn() != null && !record.getTimeIn().isEmpty()
                    && record.getTimeOut() != null && !record.getTimeOut().isEmpty()) {
                totalDays++;
                try {
                    long diffInMillis = sdf.parse(record.getTimeOut()).getTime() - sdf.parse(record.getTimeIn()).getTime();
                    double hours = TimeUnit.MILLISECONDS.toMinutes(diffInMillis) / 60.0;
                    if (hours < 0) hours = 0;
                    totalHours += hours;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        // Payroll computation: monthly-based
        double standardMonthlyHours = 22 * 8; // 176 hours per month
        double hourlyRate = basicSalary / standardMonthlyHours;
        double grossPay = hourlyRate * totalHours;

        // Updated deductions
        double sss = Math.min(grossPay * 0.045, 1350); // Max employee share
        double philHealthBase = Math.min(100000, Math.max(10000, grossPay));
        double philHealth = (philHealthBase * 0.05) / 2; // 50% employee share
        double pagibig = Math.min(grossPay * 0.02, 100); // Max of 100

        double withholdingTax = computeWithholdingTax(grossPay);

        double totalDeductions = sss + philHealth + pagibig + withholdingTax;
        double netPay = grossPay - totalDeductions;

        // Table setup
        String[] columns = {"Description", "Amount (Php)"};
        Object[][] data = {
                {"Employee Name", employeeName},
                {"Employee ID", employeeId},
                {"Basic Salary", String.format("%.2f", basicSalary)},
                {"Total Days Worked", totalDays},
                {"Total Hours Worked", String.format("%.2f", totalHours)},
                {"Gross Pay", String.format("%.2f", grossPay)},
                {"Deductions", ""},
                {"- SSS", String.format("%.2f", sss)},
                {"- PhilHealth", String.format("%.2f", philHealth)},
                {"- Pag-IBIG", String.format("%.2f", pagibig)},
                {"- Withholding Tax", String.format("%.2f", withholdingTax)},
                {"Total Deductions", String.format("%.2f", totalDeductions)},
                {"Net Pay", String.format("%.2f", netPay)}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);

        // Add "Generate Payslip" button
        JButton generatePayslipButton = new JButton("Generate Payslip");
        generatePayslipButton.addActionListener(e -> openPayslipFrame());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(generatePayslipButton);
        this.add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Withholding tax computation based on BIR TRAIN monthly tax table
    private double computeWithholdingTax(double grossPay) {
        if (grossPay <= 20833) {
            return 0;
        } else if (grossPay <= 33332) {
            return (grossPay - 20833) * 0.20;
        } else if (grossPay <= 66666) {
            return 2500 + (grossPay - 33333) * 0.25;
        } else if (grossPay <= 166666) {
            return 10833.33 + (grossPay - 66667) * 0.30;
        } else if (grossPay <= 666666) {
            return 40833.33 + (grossPay - 166667) * 0.32;
        } else {
            return 200833.33 + (grossPay - 666667) * 0.35;
        }
    }

    // Dummy method to open payslip frame
    private void openPayslipFrame() {
        new PayslipFrame(); // You can modify this to pass payroll data if needed
    }
}
