# Employee Management System (Java)

A small, IntelliJ-ready Maven project demonstrating a SOLID-compliant
design for a simple Employee Management System.


## Project structure

```
src/main/java/com/ems/
├── Main.java                      Composition root — wires everything together
├── model/
│   ├── Employee.java               Abstract base class
│   ├── FullTimeEmployee.java
│   ├── PartTimeEmployee.java
│   └── InternEmployee.java
├── salary/
│   ├── SalaryCalculator.java       Strategy interface
│   ├── FullTimeSalaryCalculator.java
│   ├── PartTimeSalaryCalculator.java
│   ├── InternSalaryCalculator.java
│   └── SalaryCalculatorRegistry.java   Resolves employee -> correct strategy
├── repository/
│   ├── EmployeeRepository.java     Persistence interface
│   └── InMemoryEmployeeRepository.java
├── notification/
│   ├── NotificationService.java    Notification interface
│   ├── EmailNotificationService.java
│   └── SmsNotificationService.java
└── service/
    └── EmployeeService.java        High-level orchestrator (the 5 features)
```

## Where each SOLID principle is applied

**S — Single Responsibility**
Each class has exactly one reason to change:
- `Employee` and its subclasses only hold employee data.
- `SalaryCalculator` implementations only know how to turn one employee
  type into a salary number.
- `EmployeeRepository` implementations only know how to store/retrieve
  employees.
- `NotificationService` implementations only know how to deliver a
  message through one channel.
- `EmployeeService` only coordinates the workflow (add/display/calculate/
  save/notify) — it contains no salary math, no storage logic, and no
  message-formatting/delivery logic itself.

**O — Open/Closed**
Adding a new employee type (e.g. `ContractEmployee`) never requires
editing `FullTimeSalaryCalculator`, `PartTimeSalaryCalculator`,
`InternSalaryCalculator`, or `EmployeeService`. You only *add* new code:
a new `Employee` subclass, a new `SalaryCalculator` implementation, and
one `registry.register(...)` call. There is no `if (employee instanceof
FullTimeEmployee) ... else if (...)` chain anywhere that would need to
grow — `SalaryCalculatorRegistry` dispatches by looking up the
employee's runtime class in a map. The same applies to notifications:
`SmsNotificationService` was added alongside `EmailNotificationService`
without touching either.

**L — Liskov Substitution**
`FullTimeEmployee`, `PartTimeEmployee`, and `InternEmployee` are all
usable anywhere an `Employee` is expected (`EmployeeService.addEmployee`,
`EmployeeRepository.save`, `NotificationService.sendNotification`, the
arrays in `Main`, etc.). Subclasses only *add* fields — they never
override `getDetails()`/`getEmployeeType()` in a way that violates the
base class's contract, throw unexpected exceptions, or require callers
to know the concrete type to use them safely.

**I — Interface Segregation**
Instead of one fat interface (e.g. an `EmployeeManager` with save,
calculateSalary, and notify all mixed together), there are three small,
focused interfaces:
- `EmployeeRepository` (persistence only)
- `SalaryCalculator` (salary math only)
- `NotificationService` (notification only)

A class that only needs to persist employees (like a future reporting
module) can depend on `EmployeeRepository` alone, without being forced
to implement or depend on notification or salary methods it doesn't
need.

**D — Dependency Inversion**
`EmployeeService` (the high-level module) depends only on the
`EmployeeRepository`, `NotificationService`, and `SalaryCalculator`
*interfaces* (via `SalaryCalculatorRegistry`), all supplied through its
constructor. It never references `InMemoryEmployeeRepository` or
`EmailNotificationService` directly. The only place concrete classes are
instantiated and wired together is `Main.java` (the composition root).
This means you can swap the in-memory repository for a database-backed
one, or email for SMS, by changing a single line in `Main`.

## Why interfaces were used

Interfaces (`EmployeeRepository`, `SalaryCalculator`, `NotificationService`)
decouple *what* the system needs to do from *how* it's done:

- They let `EmployeeService` be written and tested against abstractions,
  so unit tests can supply fake/mock implementations without touching a
  real database or sending real emails.
- They let each concern (persistence, salary rules, notification
  delivery) evolve and grow new implementations independently, satisfying
  Open/Closed at the architecture level, not just inside single classes.
- They keep responsibilities from leaking into each other — a repository
  implementation can never accidentally end up also formatting salary
  numbers, because `EmployeeRepository` simply has no method for that.

## How a new employee type can be added

To add, say, a `ContractEmployee` paid a fixed contract fee:

1. **Create the model class** in `model/`:
   ```java
   public class ContractEmployee extends Employee {
       private final double contractFee;
       // constructor, getter, getEmployeeType() -> "Contract"
   }
   ```
2. **Create its salary strategy** in `salary/`:
   ```java
   public class ContractSalaryCalculator implements SalaryCalculator<ContractEmployee> {
       public double calculateSalary(ContractEmployee employee) {
           return employee.getContractFee();
       }
   }
   ```
3. **Register it** where the registry is built (`SalaryCalculatorRegistry.withDefaults()`
   or directly in `Main`):
   ```java
   registry.register(ContractEmployee.class, new ContractSalaryCalculator());
   ```

That's it — `EmployeeService`, `EmployeeRepository`,
`NotificationService`, and every existing salary calculator remain
completely unchanged, which is exactly what the Open/Closed Principle
requires.
