package com.ems.service;

import com.ems.model.Employee;
import com.ems.notification.NotificationService;
import com.ems.repository.EmployeeRepository;
import com.ems.salary.SalaryCalculatorRegistry;
import java.util.Optional;

import java.util.List;

public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final NotificationService notificationService;
    private final SalaryCalculatorRegistry salaryCalculatorRegistry;

    public EmployeeService(EmployeeRepository employeeRepository,
                            NotificationService notificationService,
                            SalaryCalculatorRegistry salaryCalculatorRegistry) {
        this.employeeRepository = employeeRepository;
        this.notificationService = notificationService;
        this.salaryCalculatorRegistry = salaryCalculatorRegistry;
    }

    /** Adds (and persists) a new employee. Works for ANY Employee subtype (LSP). */
    public void addEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    /** Displays a single employee's details. */
    public void displayEmployee(String employeeId) {
        employeeRepository.findById(employeeId)
                .ifPresentOrElse(
                        e -> System.out.println(e.getDetails()),
                        () -> System.out.println("No employee found with ID: " + employeeId)
                );
    }

    /** Displays every employee currently stored. */
    public void displayAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        if (employees.isEmpty()) {
            System.out.println("No employees on record.");
            return;
        }
        employees.forEach(e -> System.out.println(e.getDetails()));
    }

    public double calculateSalary(Employee employee) {
        return salaryCalculatorRegistry.calculate(employee);
    }

    public void saveEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    public void notifyEmployee(Employee employee, String message) {
        notificationService.sendNotification(employee, message);
    }

    public Optional<Employee> findEmployee(String employeeId) {
        return employeeRepository.findById(employeeId);
    }
}
