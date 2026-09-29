package com.ems.salary;

import com.ems.model.InternEmployee;

public class InternSalaryCalculator implements SalaryCalculator<InternEmployee> {

    @Override
    public double calculateSalary(InternEmployee employee) {
        return employee.getMonthlyStipend();
    }
}
