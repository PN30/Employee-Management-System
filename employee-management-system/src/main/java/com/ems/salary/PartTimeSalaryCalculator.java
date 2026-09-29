package com.ems.salary;

import com.ems.model.PartTimeEmployee;

public class PartTimeSalaryCalculator implements SalaryCalculator<PartTimeEmployee> {

    @Override
    public double calculateSalary(PartTimeEmployee employee) {
        return employee.getHourlyRate() * employee.getHoursWorked();
    }
}
