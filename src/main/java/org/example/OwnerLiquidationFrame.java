package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class OwnerLiquidationFrame extends JFrame {
    public OwnerLiquidationFrame(List<Employee> employeesList, Attendance attendanceFrame) {
        setTitle("Owner Costs Summary");
        setSize(800, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] columns = {"ID", "Name", "Basic Salary", "Total Hours Worked", "SSS", "PhilHealth", "Pag-IBIG", "Net Pay"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        double totalNetPay = 0.0;

        for (Employee emp : employeesList) {
            List<AttendanceRecord> attendanceRecords = attendanceFrame.getAttendanceDataById(emp.getId());

            double totalHours = 0.0;
            for (AttendanceRecord record : attendanceRecords) {
                if (record.getTimeIn() != null && !record.getTimeIn().isEmpty() &&
                        record.getTimeOut() != null && !record.getTimeOut().isEmpty()) {
                    try {
                        long timeInMillis = sdf.parse(record.getTimeIn()).getTime();
                        long timeOutMillis = sdf.parse(record.getTimeOut()).getTime();

                        if (timeOutMillis < timeInMillis) {
                            timeOutMillis += TimeUnit.HOURS.toMillis(24);
                        }

                        long diffMillis = timeOutMillis - timeInMillis;
                        double hours = TimeUnit.MILLISECONDS.toMinutes(diffMillis) / 60.0;
                        totalHours += hours;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            double standardMonthlyHours = 22 * 8.0;
            double hourlyRate = emp.getBasicSalary() / standardMonthlyHours;
            double grossPay = hourlyRate * totalHours;

            double sss = computeSSSContribution(grossPay);
            double philHealth = computePhilHealthContribution(grossPay);
            double pagibig = computePagIbigContribution(grossPay);
            double withholdingTax = computeWithholdingTax(grossPay);

            double totalDeductions = sss + philHealth + pagibig + withholdingTax;
            double netPay = grossPay - totalDeductions;

            totalNetPay += netPay;

            model.addRow(new Object[]{
                    emp.getId(),
                    emp.getName(),
                    String.format("%.2f", emp.getBasicSalary()),
                    String.format("%.2f", totalHours),
                    String.format("%.2f", sss),
                    String.format("%.2f", philHealth),
                    String.format("%.2f", pagibig),
                    String.format("%.2f", netPay)
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel for total net pay
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel totalNetPayLabel = new JLabel("Total Net Pay: ₱" + String.format("%.2f", totalNetPay));
        totalNetPayLabel.setFont(new Font("Arial", Font.BOLD, 14));
        bottomPanel.add(totalNetPayLabel);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Helper methods (same as before)
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

    private double computeSSSContribution(double grossMonthly) {
        if (grossMonthly <= 3250) return 135.00;
        else if (grossMonthly <= 24750) {
            double msc = Math.ceil(grossMonthly / 500.0) * 500;
            return Math.min(msc * 0.045, 1125.00);
        } else {
            return 1125.00;
        }
    }

    private double computePhilHealthContribution(double grossMonthly) {
        double base = Math.min(100000, Math.max(10000, grossMonthly));
        return (base * 0.05) / 2;
    }

    private double computePagIbigContribution(double grossMonthly)   {
        return Math.min(grossMonthly * 0.02, 100);
    }
}
