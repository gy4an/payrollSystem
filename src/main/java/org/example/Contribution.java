package org.example;

public class Contribution {
    double employeeShare;
    double employerShare;

    public Contribution(double employeeShare, double employerShare) {
        this.employeeShare = employeeShare;
        this.employerShare = employerShare;
    }

    public double getTotalContribution() {
        return employeeShare + employerShare;
    }

    public static Contribution computeSSSContribution(double grossMonthly) {
        double total;
        if (grossMonthly <= 3250) {
            total = 135.00 / 0.045 / 0.14 * 0.14; // Recompute full total based on fixed employee share
        } else if (grossMonthly <= 24750) {
            double msc = Math.ceil(grossMonthly / 500.0) * 500;
            total = Math.min(msc, 25000) * 0.14; // total = MSC * 14%
        } else {
            total = 25000 * 0.14;
        }

        double employeeShare = total * 0.045; // 4.5%
        double employerShare = total - employeeShare; // 9.5%
        return new Contribution(employeeShare, employerShare);
    }

    public static Contribution computePhilHealthContribution(double grossMonthly) {
        double base = Math.min(100000, Math.max(10000, grossMonthly));
        double total = base * 0.05;
        return new Contribution(total / 2, total / 2);
    }

    public static Contribution computePagIbigContribution(double grossMonthly) {
        double employeeShare = Math.min(grossMonthly * 0.02, 100.00);
        double employerShare = Math.min(grossMonthly * 0.02, 100.00);
        return new Contribution(employeeShare, employerShare);
    }
}
