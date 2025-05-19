package org.example;

import javax.swing.*;
import java.awt.*;

public class PayrollSummaryFrame extends JFrame {

    public PayrollSummaryFrame(String employeeName, double basicSalary, int totalDays, double totalHours) {
        setTitle("Payroll Summary");
        setSize(500, 400);
        setLayout(new GridLayout(10, 2, 10, 10));

        // Fixed deductions (can be made dynamic)
        double sss = 500;
        double philHealth = 300;
        double pagibig = 200;
        double withholdingTax = basicSalary * 0.10; // Sample BIR 10%

        double totalDeductions = sss + philHealth + pagibig + withholdingTax;
        double grossPay = basicSalary; // You can multiply by days or hours here
        double netPay = grossPay - totalDeductions;

        add(new JLabel("Employee:"));
        add(new JLabel(employeeName));
        add(new JLabel("Total Days Worked:"));
        add(new JLabel(String.valueOf(totalDays)));
        add(new JLabel("Total Hours Worked:"));
        add(new JLabel(String.format("%.2f", totalHours)));
        add(new JLabel("Gross Pay:"));
        add(new JLabel(String.format("Php %.2f", grossPay)));

        add(new JLabel("Deductions"));
        add(new JLabel(""));
        add(new JLabel("SSS:"));
        add(new JLabel(String.format("Php %.2f", sss)));
        add(new JLabel("PhilHealth:"));
        add(new JLabel(String.format("Php %.2f", philHealth)));
        add(new JLabel("Pag-IBIG:"));
        add(new JLabel(String.format("Php %.2f", pagibig)));
        add(new JLabel("Withholding Tax:"));
        add(new JLabel(String.format("Php %.2f", withholdingTax)));

        add(new JLabel("Net Salary:"));
        add(new JLabel(String.format("Php %.2f", netPay)));

        setVisible(true);
    }
}