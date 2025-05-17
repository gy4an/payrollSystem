package view;

import controller.EmployeeController;
import controller.PayrollController;
import controller.ReportController;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private EmployeeController employeeController;
    private PayrollController payrollController;
    private ReportController reportController;

    private JTabbedPane tabbedPane;

    private ReportPanel reportPanel;

    public MainFrame() {
        // Initialize controllers
        employeeController = new EmployeeController();
        payrollController = new PayrollController(employeeController);
        reportController = new ReportController(employeeController, payrollController);

        // Initialize UI
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Employee Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);


        reportPanel = new ReportPanel(reportController);

        // Create tabbed pane
        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Compliance & Reporting", reportPanel);

        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }
}