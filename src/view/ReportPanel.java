package view;

import controller.ReportController;
import model.Report;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReportPanel extends JPanel {
    private ReportController controller;

    // UI Components
    private JComboBox<String> reportTypeComboBox;
    private JTextField txtStartPeriod, txtEndPeriod;
    private JButton btnGenerateReport, btnViewReport;
    private JTable reportTable;
    private DefaultTableModel tableModel;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public ReportPanel(ReportController controller) {
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

        // Report Generation Section
        JPanel reportGenPanel = new JPanel(new GridBagLayout());
        reportGenPanel.setBorder(BorderFactory.createTitledBorder("Generate Report"));
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(5, 5, 5, 5);
        gbc2.anchor = GridBagConstraints.WEST;

        // Report Type
        gbc2.gridx = 0;
        gbc2.gridy = 0;
        reportGenPanel.add(new JLabel("Report Type:"), gbc2);
        gbc2.gridx = 1;
        String[] reportTypes = {"BIR", "SSS", "PhilHealth", "Pag-IBIG"};
        reportTypeComboBox = new JComboBox<>(reportTypes);
        reportGenPanel.add(reportTypeComboBox, gbc2);

        // Start Period
        gbc2.gridx = 0;
        gbc2.gridy = 1;
        reportGenPanel.add(new JLabel("Start Period (yyyy-MM-dd):"), gbc2);
        gbc2.gridx = 1;
        txtStartPeriod = new JTextField(15);
        reportGenPanel.add(txtStartPeriod, gbc2);

        // End Period
        gbc2.gridx = 0;
        gbc2.gridy = 2;
        reportGenPanel.add(new JLabel("End Period (yyyy-MM-dd):"), gbc2);
        gbc2.gridx = 1;
        txtEndPeriod = new JTextField(15);
        reportGenPanel.add(txtEndPeriod, gbc2);

        // Add section to form panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(reportGenPanel, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        btnGenerateReport = new JButton("Generate Report");
        btnViewReport = new JButton("View Report");

        buttonPanel.add(btnGenerateReport);
        buttonPanel.add(btnViewReport);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(buttonPanel, gbc);

        // Table
        String[] columns = {"Report ID", "Report Type", "Period", "Generated Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        reportTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(reportTable);

        // Add components to panel
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Add event listeners
        btnGenerateReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }
        });

        btnViewReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewReport();
            }
        });

        // Initialize table data
        refreshReportTable();
    }

    private void generateReport() {
        try {
            String reportType = (String) reportTypeComboBox.getSelectedItem();
            Date startPeriod = dateFormat.parse(txtStartPeriod.getText());
            Date endPeriod = dateFormat.parse(txtEndPeriod.getText());

            Report report = null;

            switch (reportType) {
                case "BIR":
                    report = controller.generateBIRReport(startPeriod, endPeriod);
                    break;
                case "SSS":
                    report = controller.generateSSSReport(startPeriod, endPeriod);
                    break;
                case "PhilHealth":
                    report = controller.generatePhilHealthReport(startPeriod, endPeriod);
                    break;
                case "Pag-IBIG":
                    report = controller.generatePagIbigReport(startPeriod, endPeriod);
                    break;
            }

            if (report != null) {
                refreshReportTable();
                JOptionPane.showMessageDialog(this, reportType + " report generated successfully!");
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use yyyy-MM-dd format.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void viewReport() {
        int selectedRow = reportTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a report to view.");
            return;
        }

        int reportId = (int) reportTable.getValueAt(selectedRow, 0);
        Report report = controller.getReport(reportId);

        if (report != null) {
            JTextArea textArea = new JTextArea(report.getContent());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(600, 400));

            JOptionPane.showMessageDialog(this, scrollPane, report.getReportType() + " Report", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void refreshReportTable() {
        tableModel.setRowCount(0);
        List<Report> reports = controller.getAllReports();
        for (Report report : reports) {
            String period = dateFormat.format(report.getStartPeriod()) + " to " +
                    dateFormat.format(report.getEndPeriod());

            Object[] row = {
                    report.getId(),
                    report.getReportType(),
                    period,
                    dateFormat.format(report.getGeneratedDate()),
                    report.getStatus()
            };
            tableModel.addRow(row);
        }
    }
}