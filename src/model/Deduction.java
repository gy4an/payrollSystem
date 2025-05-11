package model;

public class Deduction {
    private double sssRate = 0.0363; // 3.63% SSS contribution rate
    private double philHealthRate = 0.035; // 3.5% PhilHealth contribution rate
    private double pagIbigRate = 0.02; // 2% Pag-IBIG contribution rate
    private double sssMax = 1125.0; // Maximum SSS contribution
    private double philHealthMax = 1800.0; // Maximum PhilHealth contribution
    private double pagIbigMax = 100.0; // Maximum Pag-IBIG contribution

    public double calculateSSS(double salary) {
        double contribution = salary * sssRate;
        return Math.min(contribution, sssMax);
    }

    public double calculatePhilHealth(double salary) {
        double contribution = salary * philHealthRate;
        return Math.min(contribution, philHealthMax);
    }

    public double calculatePagIbig(double salary) {
        double contribution = salary * pagIbigRate;
        return Math.min(contribution, pagIbigMax);
    }

    public double calculateIncomeTax(double taxableIncome) {
        // Philippine Tax Brackets (simplified for example)
        if (taxableIncome <= 20833) {
            return 0; // 0%
        } else if (taxableIncome <= 33332) {
            return (taxableIncome - 20833) * 0.20; // 20% of excess over 20,833
        } else if (taxableIncome <= 66666) {
            return 2500 + (taxableIncome - 33333) * 0.25; // 2,500 + 25% of excess over 33,333
        } else if (taxableIncome <= 166666) {
            return 10833 + (taxableIncome - 66667) * 0.30; // 10,833 + 30% of excess over 66,667
        } else if (taxableIncome <= 666666) {
            return 40833.33 + (taxableIncome - 166667) * 0.32; // 40,833.33 + 32% of excess over 166,667
        } else {
            return 200833.33 + (taxableIncome - 666667) * 0.35; // 200,833.33 + 35% of excess over 666,667
        }
    }

    // Getters and setters for rates
    public double getSssRate() {
        return sssRate;
    }

    public void setSssRate(double sssRate) {
        this.sssRate = sssRate;
    }

    public double getPhilHealthRate() {
        return philHealthRate;
    }

    public void setPhilHealthRate(double philHealthRate) {
        this.philHealthRate = philHealthRate;
    }

    public double getPagIbigRate() {
        return pagIbigRate;
    }

    public void setPagIbigRate(double pagIbigRate) {
        this.pagIbigRate = pagIbigRate;
    }
}