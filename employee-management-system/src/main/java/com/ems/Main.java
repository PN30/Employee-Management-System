package com.ems;

import com.ems.model.Employee;
import com.ems.model.FullTimeEmployee;
import com.ems.model.InternEmployee;
import com.ems.model.PartTimeEmployee;
import com.ems.notification.EmailNotificationService;
import com.ems.notification.NotificationService;
import com.ems.repository.EmployeeRepository;
import com.ems.repository.InMemoryEmployeeRepository;
import com.ems.salary.SalaryCalculatorRegistry;
import com.ems.service.EmployeeService;

import java.util.Locale;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        EmployeeRepository repository = new InMemoryEmployeeRepository();
        NotificationService notificationService = new EmailNotificationService();
        SalaryCalculatorRegistry salaryRegistry = SalaryCalculatorRegistry.withDefaults();

        EmployeeService employeeService =
                new EmployeeService(repository, notificationService, salaryRegistry);

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> addEmployee(employeeService);
                case "2" -> displayEmployee(employeeService);
                case "3" -> displayAllEmployees(employeeService);
                case "4" -> calculateSalary(employeeService);
                case "5" -> notifyEmployee(employeeService);
                case "6" -> running = false;
                default -> System.out.println("Invalid choice. Please try again.");
            }
            System.out.println();
        }

        System.out.println("Goodbye!");
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("===== Employee Management System =====");
        System.out.println("1. Add Employee");
        System.out.println("2. Display Employee (by ID)");
        System.out.println("3. Display All Employees");
        System.out.println("4. Calculate Salary");
        System.out.println("5. Send Notification");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addEmployee(EmployeeService employeeService) {
        System.out.println("\nSelect employee type:");
        System.out.println("1. Full-Time");
        System.out.println("2. Part-Time");
        System.out.println("3. Intern");
        System.out.print("Choice: ");
        String typeChoice = scanner.nextLine().trim();

        System.out.print("Employee ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Phone: ");
        String phone = scanner.nextLine().trim();

        Employee employee = switch (typeChoice) {
            case "1" -> {
                double baseSalary = readDouble("Monthly Base Salary: ");
                double bonus = readDouble("Fixed Bonus: ");
                yield new FullTimeEmployee(id, name, email, phone, baseSalary, bonus);
            }
            case "2" -> {
                double hourlyRate = readDouble("Hourly Rate: ");
                double hoursWorked = readDouble("Hours Worked: ");
                yield new PartTimeEmployee(id, name, email, phone, hourlyRate, hoursWorked);
            }
            case "3" -> {
                double stipend = readDouble("Monthly Stipend: ");
                yield new InternEmployee(id, name, email, phone, stipend);
            }
            default -> null;
        };

        if (employee == null) {
            System.out.println("Invalid employee type. Employee not added.");
            return;
        }

        employeeService.addEmployee(employee);
        System.out.println("Employee added and saved: " + employee.getDetails());
    }

    private static void displayEmployee(EmployeeService employeeService) {
        System.out.print("Enter Employee ID: ");
        String id = scanner.nextLine().trim();
        employeeService.displayEmployee(id);
    }

    private static void displayAllEmployees(EmployeeService employeeService) {
        employeeService.displayAllEmployees();
    }

    private static void calculateSalary(EmployeeService employeeService) {
        System.out.print("Enter Employee ID: ");
        String id = scanner.nextLine().trim();
        employeeService.findEmployee(id).ifPresentOrElse(
                employee -> {
                    double salary = employeeService.calculateSalary(employee);
                    System.out.printf(Locale.US, "Salary for %s (%s): Rs. %.2f%n",
                            employee.getName(), employee.getEmployeeType(), salary);
                },
                () -> System.out.println("No employee found with ID: " + id)
        );
    }

    private static void notifyEmployee(EmployeeService employeeService) {
        System.out.print("Enter Employee ID: ");
        String id = scanner.nextLine().trim();
        employeeService.findEmployee(id).ifPresentOrElse(
                employee -> {
                    System.out.print("Message: ");
                    String message = scanner.nextLine();
                    employeeService.notifyEmployee(employee, message);
                },
                () -> System.out.println("No employee found with ID: " + id)
        );
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}