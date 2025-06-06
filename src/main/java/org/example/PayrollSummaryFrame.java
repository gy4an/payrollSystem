package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PayrollSummaryFrame extends JFrame {

    // Payroll variables
    private String employeeName;
    private String employeeId;
    private double basicSalary;
    private int totalDays;
    private double totalHours;
    private double hourlyRate;
    private double grossPay;
    private double sss;
    private double philHealth;
    private double pagibig;
    private double withholdingTax;
    private double totalDeductions;
    private double netPay;

    public PayrollSummaryFrame(String employeeName, String employeeId, double basicSalary,
                               List<AttendanceRecord> attendanceRecords, List<Employee> allEmployees) {
        this.setTitle("Payroll Summary");
        this.setSize(600, 400);
        this.setLocationRelativeTo(null);


        this.employeeName = employeeName;
        this.employeeId = employeeId;
        this.basicSalary = basicSalary;

        // Compute total hours and days
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        totalDays = 0;
        totalHours = 0.0;

        for (AttendanceRecord record : attendanceRecords) {
            if (record.getTimeIn() != null && !record.getTimeIn().isEmpty()
                    && record.getTimeOut() != null && !record.getTimeOut().isEmpty()) {
                totalDays++;
                try {
                    long timeInMillis = sdf.parse(record.getTimeIn()).getTime();
                    long timeOutMillis = sdf.parse(record.getTimeOut()).getTime();

                    if (timeOutMillis < timeInMillis) {
                        timeOutMillis += TimeUnit.HOURS.toMillis(24);
                    }

                    long diffInMillis = timeOutMillis - timeInMillis;
                    double hours = TimeUnit.MILLISECONDS.toMinutes(diffInMillis) / 60.0;
                    totalHours += hours;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        // Payroll computations
        double standardMonthlyHours = 22 * 8.0;
        hourlyRate = basicSalary / standardMonthlyHours;
        grossPay = hourlyRate * totalHours;

        Contribution sssContrib = Contribution.computeSSSContribution(grossPay);
        Contribution philContrib = Contribution.computePhilHealthContribution(grossPay);
        Contribution pagibigContrib = Contribution.computePagIbigContribution(grossPay);

        sss = sssContrib.employeeShare;
        philHealth = philContrib.employeeShare;
        pagibig = pagibigContrib.employeeShare;

        withholdingTax = computeWithholdingTax(grossPay);

        totalDeductions = sss + philHealth + pagibig + withholdingTax;
        netPay = grossPay - totalDeductions;

        // Table display
        String[] columns = {"Description", "Amount (Php)"};
        Object[][] data = {
                {"Employee Name", employeeName},
                {"Employee ID", employeeId},
                {"Basic Salary", String.format("%.2f", basicSalary)},
                {"Total Days Worked", totalDays},
                {"Total Hours Worked", String.format("%.2f", totalHours)},
                {"Gross Pay", String.format("%.2f", grossPay)},
                {"Deductions", ""},
                {"- SSS (Employee)", String.format("%.2f", sss)},
                {"- PhilHealth (Employee)", String.format("%.2f", philHealth)},
                {"- Pag-IBIG (Employee)", String.format("%.2f", pagibig)},
                {"- Withholding Tax", String.format("%.2f", withholdingTax)},
                {"Total Deductions", String.format("%.2f", totalDeductions)},
                {"Employer Contributions", ""},
                {"- SSS (Employer)", String.format("%.2f", sssContrib.employerShare)},
                {"- PhilHealth (Employer)", String.format("%.2f", philContrib.employerShare)},
                {"- Pag-IBIG (Employer)", String.format("%.2f", pagibigContrib.employerShare)},
                {"Net Pay", String.format("%.2f", netPay)},
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

        // Payslip Button
        JButton generatePayslipButton = new JButton("Generate Payslip");
        generatePayslipButton.addActionListener(e -> openPayslipFrame(sssContrib, philContrib, pagibigContrib));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(generatePayslipButton);
        this.add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private double computeWithholdingTax(double grossPay) {
        if (grossPay <= 20833) {
            return 0.0;
        } else if (grossPay <= 33333) {
            return (grossPay - 20833) * 0.20;
        } else if (grossPay <= 66667) {
            return 2500 + (grossPay - 33333) * 0.25;
        } else if (grossPay <= 166667) {
            return 10833.33 + (grossPay - 66667) * 0.30;
        } else if (grossPay <= 666667) {
            return 40833.33 + (grossPay - 166667) * 0.32;
        } else {
            return 200833.33 + (grossPay - 666667) * 0.35;
        }
    }



    private void openPayslipFrame(Contribution sssContrib, Contribution philContrib, Contribution pagibigContrib) {
        StringBuilder payslipText = new StringBuilder();
        payslipText.append("=========== PAYSLIP ===========\n");
        payslipText.append("Employee Name     : ").append(employeeName).append("\n");
        payslipText.append("Employee ID       : ").append(employeeId).append("\n");
        payslipText.append("--------------------------------\n");
        payslipText.append(String.format("Basic Salary      : Php %.2f\n", basicSalary));
        payslipText.append(String.format("Total Days Worked : %d\n", totalDays));
        payslipText.append(String.format("Total Hours Worked: %.2f\n", totalHours));
        payslipText.append(String.format("Hourly Rate       : Php %.2f\n", hourlyRate));
        payslipText.append(String.format("Gross Pay         : Php %.2f\n", grossPay));
        payslipText.append("\n-------- Deductions --------\n");
        payslipText.append(String.format("SSS (Employee)    : Php %.2f\n", sss));
        payslipText.append(String.format("PhilHealth (Emp.) : Php %.2f\n", philHealth));
        payslipText.append(String.format("Pag-IBIG (Emp.)   : Php %.2f\n", pagibig));
        payslipText.append(String.format("Withholding Tax   : Php %.2f\n", withholdingTax));
        payslipText.append(String.format("Total Deductions  : Php %.2f\n", totalDeductions));
        payslipText.append("\n-- Employer Contributions --\n");
        payslipText.append(String.format("SSS (Employer)    : Php %.2f\n", sssContrib.employerShare));
        payslipText.append(String.format("PhilHealth (Emp.) : Php %.2f\n", philContrib.employerShare));
        payslipText.append(String.format("Pag-IBIG (Emp.)   : Php %.2f\n", pagibigContrib.employerShare));
        payslipText.append("\n-----------------------------\n");
        payslipText.append(String.format("NET PAY           : Php %.2f\n", netPay));
        payslipText.append("================================\n");

        new PayslipFrame(payslipText.toString());
    }
}
