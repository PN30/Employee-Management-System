package com.ems.model;

public class FullTimeEmployee extends Employee {

    private final double monthlyBaseSalary;
    private final double fixedBonus;

    public FullTimeEmployee(String id, String name, String email, String phone,
                             double monthlyBaseSalary, double fixedBonus) {
        super(id, name, email, phone);
        this.monthlyBaseSalary = monthlyBaseSalary;
        this.fixedBonus = fixedBonus;
    }

    public double getMonthlyBaseSalary() {
        return monthlyBaseSalary;
    }

    public double getFixedBonus() {
        return fixedBonus;
    }

    @Override
    public String getEmployeeType() {
        return "Full-Time";
    }
}
