package controller;

import model.Employee;
import model.Payroll;
import model.Report;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportController {
    private List<Report> reports;
    private EmployeeController employeeController;
    private PayrollController payrollController;
    private int nextReportId;

    public ReportController(EmployeeController employeeController, PayrollController payrollController) {
        this.employeeController = employeeController;
        this.payrollController = payrollController;
        this.reports = new ArrayList<>();
        this.nextReportId = 1;
    }

    public Report generateBIRReport(Date startPeriod, Date endPeriod) {
        // Generate BIR reports content (simplified for example)
        StringBuilder content = new StringBuilder();
        content.append("BIR Report for period: ").append(startPeriod).append(" to ").append(endPeriod).append("\n\n");

        double totalTaxes = 0;

        List<Employee> employees = employeeController.getAllEmployees();
        for (Employee employee : employees) {
            content.append("Employee: ").append(employee.getFullName()).append("\n");
            content.append("TIN: ").append("123-456-789-000").append("\n"); // Placeholder TIN

            double employeeTaxes = 0;
            List<Payroll> payrolls = payrollController.getEmployeePayrolls(employee.getId());
            for (Payroll payroll : payrolls) {
                if (payroll.getPayPeriodStart().after(startPeriod) && payroll.getPayPeriodEnd().before(endPeriod)) {
                    employeeTaxes += payroll.getTaxDeduction();
                }
            }

            content.append("Total Tax Withheld: ₱").append(String.format("%.2f", employeeTaxes)).append("\n\n");
            totalTaxes += employeeTaxes;
        }

        content.append("Total Tax Remittance: ₱").append(String.format("%.2f", totalTaxes));

        Report report = new Report(nextReportId++, "BIR", startPeriod, endPeriod, new Date(), content.toString(), "Draft");
        reports.add(report);
        return report;
    }

    public Report generateSSSReport(Date startPeriod, Date endPeriod) {
        // Generate SSS reports content (simplified for example)
        StringBuilder content = new StringBuilder();
        content.append("SSS Report for period: ").append(startPeriod).append(" to ").append(endPeriod).append("\n\n");

        double totalContributions = 0;

        List<Employee> employees = employeeController.getAllEmployees();
        for (Employee employee : employees) {
            content.append("Employee: ").append(employee.getFullName()).append("\n");
            content.append("SSS Number: ").append("12-3456789-0").append("\n"); // Placeholder SSS number

            double employeeContributions = 0;
            List<Payroll> payrolls = payrollController.getEmployeePayrolls(employee.getId());
            for (Payroll payroll : payrolls) {
                if (payroll.getPayPeriodStart().after(startPeriod) && payroll.getPayPeriodEnd().before(endPeriod)) {
                    employeeContributions += payroll.getSssDeduction();
                }
            }

            content.append("SSS Contribution: ₱").append(String.format("%.2f", employeeContributions)).append("\n\n");
            totalContributions += employeeContributions;
        }

        content.append("Total SSS Remittance: ₱").append(String.format("%.2f", totalContributions));

        Report report = new Report(nextReportId++, "SSS", startPeriod, endPeriod, new Date(), content.toString(), "Draft");
        reports.add(report);
        return report;
    }

    public Report generatePhilHealthReport(Date startPeriod, Date endPeriod) {
        // Similar to SSS report but for PhilHealth
        StringBuilder content = new StringBuilder();
        content.append("PhilHealth Report for period: ").append(startPeriod).append(" to ").append(endPeriod).append("\n\n");

        double totalContributions = 0;

        List<Employee> employees = employeeController.getAllEmployees();
        for (Employee employee : employees) {
            content.append("Employee: ").append(employee.getFullName()).append("\n");
            content.append("PhilHealth Number: ").append("12-345678901-2").append("\n"); // Placeholder number

            double employeeContributions = 0;
            List<Payroll> payrolls = payrollController.getEmployeePayrolls(employee.getId());
            for (Payroll payroll : payrolls) {
                if (payroll.getPayPeriodStart().after(startPeriod) && payroll.getPayPeriodEnd().before(endPeriod)) {
                    employeeContributions += payroll.getPhilHealthDeduction();
                }
            }

            content.append("PhilHealth Contribution: ₱").append(String.format("%.2f", employeeContributions)).append("\n\n");
            totalContributions += employeeContributions;
        }

        content.append("Total PhilHealth Remittance: ₱").append(String.format("%.2f", totalContributions));

        Report report = new Report(nextReportId++, "PhilHealth", startPeriod, endPeriod, new Date(), content.toString(), "Draft");
        reports.add(report);
        return report;
    }

    public Report generatePagIbigReport(Date startPeriod, Date endPeriod) {
        // Similar to SSS report but for Pag-IBIG
        StringBuilder content = new StringBuilder();
        content.append("Pag-IBIG Report for period: ").append(startPeriod).append(" to ").append(endPeriod).append("\n\n");

        double totalContributions = 0;

        List<Employee> employees = employeeController.getAllEmployees();
        for (Employee employee : employees) {
            content.append("Employee: ").append(employee.getFullName()).append("\n");
            content.append("Pag-IBIG Number: ").append("1234-5678-9012").append("\n"); // Placeholder number

            double employeeContributions = 0;
            List<Payroll> payrolls = payrollController.getEmployeePayrolls(employee.getId());
            for (Payroll payroll : payrolls) {
                if (payroll.getPayPeriodStart().after(startPeriod) && payroll.getPayPeriodEnd().before(endPeriod)) {
                    employeeContributions += payroll.getPagIbigDeduction();
                }
            }

            content.append("Pag-IBIG Contribution: ₱").append(String.format("%.2f", employeeContributions)).append("\n\n");
            totalContributions += employeeContributions;
        }

        content.append("Total Pag-IBIG Remittance: ₱").append(String.format("%.2f", totalContributions));

        Report report = new Report(nextReportId++, "Pag-IBIG", startPeriod, endPeriod, new Date(), content.toString(), "Draft");
        reports.add(report);
        return report;
    }

    public List<Report> getAllReports() {
        return new ArrayList<>(reports);
    }

    public Report getReport(int reportId) {
        for (Report report : reports) {
            if (report.getId() == reportId) {
                return report;
            }
        }
        return null;
    }
}