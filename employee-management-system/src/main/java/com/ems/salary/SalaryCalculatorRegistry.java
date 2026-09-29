package com.ems.salary;

import com.ems.model.Employee;

import java.util.HashMap;
import java.util.Map;

public class SalaryCalculatorRegistry {

    @SuppressWarnings("rawtypes")
    private final Map<Class<? extends Employee>, SalaryCalculator> calculators = new HashMap<>();

    public <T extends Employee> void register(Class<T> employeeType, SalaryCalculator<T> calculator) {
        calculators.put(employeeType, calculator);
    }

    @SuppressWarnings("unchecked")
    public double calculate(Employee employee) {
        SalaryCalculator<Employee> calculator = calculators.get(employee.getClass());
        if (calculator == null) {
            throw new IllegalStateException(
                    "No SalaryCalculator registered for employee type: " + employee.getClass().getSimpleName());
        }
        return calculator.calculateSalary(employee);
    }

    public static SalaryCalculatorRegistry withDefaults() {
        SalaryCalculatorRegistry registry = new SalaryCalculatorRegistry();
        registry.register(com.ems.model.FullTimeEmployee.class, new FullTimeSalaryCalculator());
        registry.register(com.ems.model.PartTimeEmployee.class, new PartTimeSalaryCalculator());
        registry.register(com.ems.model.InternEmployee.class, new InternSalaryCalculator());
        return registry;
    }
}
