package controller;

import model.Employee;
import model.Payroll;
import model.Deduction;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PayrollController {
    private List<Payroll> payrolls;
    private EmployeeController employeeController;
    private Deduction deduction;
    private int nextPayrollId;

    public PayrollController(EmployeeController employeeController) {
        this.employeeController = employeeController;
        this.payrolls = new ArrayList<>();
        this.deduction = new Deduction();
        this.nextPayrollId = 1;
    }

    public Payroll generatePayroll(int employeeId, Date payPeriodStart, Date payPeriodEnd, Date paymentDate) {
        Employee employee = employeeController.getEmployee(employeeId);
        if (employee == null) {
            return null;
        }

        double baseSalary = employee.getBaseSalary();

        // Calculate deductions
        double sssDeduction = this.deduction.calculateSSS(baseSalary);
        double philHealthDeduction = this.deduction.calculatePhilHealth(baseSalary);
        double pagIbigDeduction = this.deduction.calculatePagIbig(baseSalary);

        // Calculate taxable income
        double taxableIncome = baseSalary - sssDeduction - philHealthDeduction - pagIbigDeduction;
        double taxDeduction = this.deduction.calculateIncomeTax(taxableIncome);

        // Calculate net pay
        double netPay = baseSalary - sssDeduction - philHealthDeduction - pagIbigDeduction - taxDeduction;

        // Create payroll record
        Payroll payroll = new Payroll(nextPayrollId++, employeeId, payPeriodStart, payPeriodEnd, paymentDate);
        payroll.setGrossPay(baseSalary);
        payroll.setSssDeduction(sssDeduction);
        payroll.setPhilHealthDeduction(philHealthDeduction);
        payroll.setPagIbigDeduction(pagIbigDeduction);
        payroll.setTaxDeduction(taxDeduction);
        payroll.setNetPay(netPay);

        payrolls.add(payroll);
        return payroll;
    }

    public List<Payroll> getEmployeePayrolls(int employeeId) {
        List<Payroll> employeePayrolls = new ArrayList<>();
        for (Payroll payroll : payrolls) {
            if (payroll.getEmployeeId() == employeeId) {
                employeePayrolls.add(payroll);
            }
        }
        return employeePayrolls;
    }

    public Payroll getPayroll(int payrollId) {
        for (Payroll payroll : payrolls) {
            if (payroll.getId() == payrollId) {
                return payroll;
            }
        }
        return null;
    }

    public List<Payroll> getAllPayrolls() {
        return new ArrayList<>(payrolls);
    }
}