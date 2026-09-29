package com.ems.model;

public class InternEmployee extends Employee {

    private final double monthlyStipend;

    public InternEmployee(String id, String name, String email, String phone,
                           double monthlyStipend) {
        super(id, name, email, phone);
        this.monthlyStipend = monthlyStipend;
    }

    public double getMonthlyStipend() {
        return monthlyStipend;
    }

    @Override
    public String getEmployeeType() {
        return "Intern";
    }
}
