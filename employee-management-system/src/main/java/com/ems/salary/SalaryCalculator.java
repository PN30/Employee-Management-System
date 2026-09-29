package com.ems.salary;

import com.ems.model.Employee;


public interface SalaryCalculator<T extends Employee> {
    double calculateSalary(T employee);
}
