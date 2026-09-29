package com.ems.model;

public class PartTimeEmployee extends Employee {

    private final double hourlyRate;
    private final double hoursWorked;

    public PartTimeEmployee(String id, String name, String email, String phone,
                             double hourlyRate, double hoursWorked) {
        super(id, name, email, phone);
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public double getHoursWorked() {
        return hoursWorked;
    }

    @Override
    public String getEmployeeType() {
        return "Part-Time";
    }
}
