package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class OwnerLiquidationFrame extends JFrame {
    private DefaultTableModel model;

    public OwnerLiquidationFrame(List<Employee> employeesList, Attendance attendanceFrame) {
        setTitle("Owner Costs Summary");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        String[] columns = {"ID", "Name", "Basic Salary", "Total Hours Worked", "SSS", "PhilHealth", "Pag-IBIG", "Net Pay"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        double totalNetPay = 0.0;
        double totalSSS = 0.0;
        double totalPhilHealth = 0.0;
        double totalPagIbig = 0.0;

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
            totalSSS += sss;
            totalPhilHealth += philHealth;
            totalPagIbig += pagibig;

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
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Summary panel with horizontal layout
        JPanel summaryPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 12);
        Font boldFont = new Font("Segoe UI", Font.BOLD, 13);

//        JLabel sssLabel = new JLabel("Total SSS: ₱" + String.format("%.2f", totalSSS));
//        sssLabel.setFont(labelFont);
//        JLabel philLabel = new JLabel("Total PhilHealth: ₱" + String.format("%.2f", totalPhilHealth));
//        philLabel.setFont(labelFont);
//        JLabel pagibigLabel = new JLabel("Total Pag-IBIG: ₱" + String.format("%.2f", totalPagIbig));
//        pagibigLabel.setFont(labelFont);
//        JLabel netPayLabel = new JLabel("Total Net Pay: ₱" + String.format("%.2f", totalNetPay));
//        netPayLabel.setFont(boldFont);
//
//        summaryPanel.add(sssLabel);
//        summaryPanel.add(philLabel);
//        summaryPanel.add(pagibigLabel);
//        summaryPanel.add(netPayLabel);

        // Generate button on right side
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton generateButton = new JButton("Generate Report");
        generateButton.setPreferredSize(new Dimension(150, 30));
        generateButton.setFocusPainted(false);
        buttonPanel.add(generateButton);

        bottomPanel.add(summaryPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        // Action for generate report
        double finalTotalSSS1 = totalSSS;
        double finalTotalPhilHealth1 = totalPhilHealth;
        double finalTotalPagIbig1 = totalPagIbig;
        double finalTotalNetPay1 = totalNetPay;
        generateButton.addActionListener(e -> {
            // Get column names safely
            String[] columnNames = new String[model.getColumnCount()];
            for (int i = 0; i < model.getColumnCount(); i++) {
                columnNames[i] = model.getColumnName(i);
            }

            // Clone data from original table
            DefaultTableModel reportModel = new DefaultTableModel(columnNames, 0);
            for (int i = 0; i < model.getRowCount(); i++) {
                Object[] row = new Object[model.getColumnCount()];
                for (int j = 0; j < model.getColumnCount(); j++) {
                    row[j] = model.getValueAt(i, j);
                }
                reportModel.addRow(row);
            }

            JTable reportTable = new JTable(reportModel);
            reportTable.setEnabled(false);
            JScrollPane reportScrollPane = new JScrollPane(reportTable);
            reportScrollPane.setPreferredSize(new Dimension(780, 250));

            // Summary table
            String[] summaryColumns = {"Deduction Type", "Total Amount"};
            Object[][] summaryData = {
                    {"Total SSS", String.format("₱%.2f", finalTotalSSS1)},
                    {"Total PhilHealth", String.format("₱%.2f", finalTotalPhilHealth1)},
                    {"Total Pag-IBIG", String.format("₱%.2f", finalTotalPagIbig1)},
                    {"Total Net Pay", String.format("₱%.2f", finalTotalNetPay1)}
            };
            JTable summaryTable = new JTable(summaryData, summaryColumns);
            summaryTable.setEnabled(false);
            JScrollPane summaryScrollPane = new JScrollPane(summaryTable);
            summaryScrollPane.setPreferredSize(new Dimension(780, 100));

            JPanel reportPanel = new JPanel();
            reportPanel.setLayout(new BoxLayout(reportPanel, BoxLayout.Y_AXIS));
            reportPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
            reportPanel.add(reportScrollPane);
            reportPanel.add(Box.createVerticalStrut(10));
            reportPanel.add(summaryScrollPane);

            JOptionPane.showMessageDialog(this, reportPanel, "Generated Payroll Report", JOptionPane.INFORMATION_MESSAGE);
        });


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

    private double computePagIbigContribution(double grossMonthly) {
        return Math.min(grossMonthly * 0.02, 100);
    }
}
