package com.ems.salary;

import com.ems.model.FullTimeEmployee;

public class FullTimeSalaryCalculator implements SalaryCalculator<FullTimeEmployee> {

    @Override
    public double calculateSalary(FullTimeEmployee employee) {
        return employee.getMonthlyBaseSalary() + employee.getFixedBonus();
    }
}
