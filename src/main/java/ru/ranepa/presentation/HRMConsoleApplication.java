package ru.ranepa.presentation;

import ru.ranepa.model.Employee;
import ru.ranepa.repository.EmployeeRepository;
import ru.ranepa.repository.EmployeeRepositoryImpl;
import ru.ranepa.service.HRMService;
import ru.ranepa.service.HRMServiceImpl;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class HRMConsoleApplication {
    private static final Scanner scanner = new Scanner(System.in);
    private static final EmployeeRepository repository = new EmployeeRepositoryImpl();
    private static final HRMService service = new HRMServiceImpl(repository);

    public static void main(String[] args) {
        System.out.println("Welcome to HRM System!\n");

        while (true) {
            printMenu();
            int choice = getUserChoice();

            if (choice == 1) {
                displayAllEmployees();
            } else if (choice == 2) {
                addNewEmployee();
            } else if (choice == 3) {
                deleteEmployeeById();
            } else if (choice == 4) {
                findEmployeeById();
            } else if (choice == 5) {
                showStatistics();
            } else if (choice == 6) {
                System.out.println("Goodbye!");
                scanner.close();
                break;
            } else {
                System.out.println("Please enter number from 1 to 6\n");
            }
        }
    }

    private static void printMenu() {
        System.out.println("===== MAIN MENU =====");
        System.out.println("1. Show all employees");
        System.out.println("2. Add new employee");
        System.out.println("3. Delete employee by ID");
        System.out.println("4. Find employee by ID");
        System.out.println("5. Show statistics");
        System.out.println("6. Exit");
        System.out.print("Your choice: ");
    }

    private static int getUserChoice() {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            scanner.nextLine(); // очищаем буфер
            return 0; // возвращаем 0 при ошибке
        }
    }

    private static void displayAllEmployees() {
        System.out.println("\n--- ALL EMPLOYEES ---");
        var employees = service.getAllEmployees();

        if (employees.isEmpty()) {
            System.out.println("No employees found.\n");
            return;
        }

        for (Employee emp : employees) {
            System.out.println(emp);
        }
        System.out.println("Total: " + employees.size() + " employees\n");
    }

    private static void addNewEmployee() {
        System.out.println("\n--- ADD NEW EMPLOYEE ---");

        try {
            scanner.nextLine();

            System.out.print("Enter name: ");
            String name = scanner.nextLine();

            System.out.print("Enter position: ");
            String position = scanner.nextLine();

            System.out.print("Enter salary: ");
            double salary = scanner.nextDouble();

            scanner.nextLine();

            System.out.print("Enter hire date (YYYY-MM-DD): ");
            String dateStr = scanner.nextLine();
            LocalDate hireDate = LocalDate.parse(dateStr);

            Employee employee = new Employee(name, position, salary, hireDate);
            Employee saved = service.addEmployee(employee);

            System.out.println("Employee added! ID: " + saved.getId() + "\n");

        } catch (DateTimeParseException e) {
            System.out.println("Wrong date format! Use YYYY-MM-DD\n");
        } catch (InputMismatchException e) {
            System.out.println("Salary must be a number!\n");
            scanner.nextLine();
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage() + "\n");
        }
    }

    private static void deleteEmployeeById() {
        System.out.println("\n--- DELETE EMPLOYEE ---");

        try {
            System.out.print("Enter ID to delete: ");
            long id = scanner.nextLong();

            if (service.removeEmployee(id)) {
                System.out.println("Employee with ID " + id + " deleted!\n");
            } else {
                System.out.println("Employee with ID " + id + " not found!\n");
            }

        } catch (InputMismatchException e) {
            System.out.println("ID must be a number!\n");
            scanner.nextLine();
        }
    }

    private static void findEmployeeById() {
        System.out.println("\n--- FIND EMPLOYEE ---");

        try {
            System.out.print("Enter ID: ");
            long id = scanner.nextLong();

            var employee = service.findEmployeeById(id);

            if (employee.isPresent()) {
                System.out.println("Found: " + employee.get() + "\n");
            } else {
                System.out.println("Employee with ID " + id + " not found!\n");
            }

        } catch (InputMismatchException e) {
            System.out.println("ID must be a number!\n");
            scanner.nextLine();
        }
    }

    private static void showStatistics() {
        System.out.println("\n--- STATISTICS ---");

        var employees = service.getAllEmployees();

        if (employees.isEmpty()) {
            System.out.println("No employees to show statistics.\n");
            return;
        }

        // Средняя зарплата
        double avgSalary = service.calculateAverageSalary();
        System.out.println("Average salary: " + avgSalary);

        // Самый высокооплачиваемый
        var highest = service.findHighestPaidEmployee();
        if (highest.isPresent()) {
            Employee emp = highest.get();
            System.out.println("Highest paid: " + emp.getName() + " - " + emp.getSalary());
        }

        // Подсчет по должностям
        System.out.println("\nEmployees by position:");
        var allEmployees = service.getAllEmployees();


        java.util.HashMap<String, Integer> positionCount = new java.util.HashMap<>();

        for (Employee emp : allEmployees) {
            String pos = emp.getPosition();
            if (positionCount.containsKey(pos)) {
                positionCount.put(pos, positionCount.get(pos) + 1);
            } else {
                positionCount.put(pos, 1);
            }
        }

        for (String pos : positionCount.keySet()) {
            System.out.println("  " + pos + ": " + positionCount.get(pos) + " people");
        }

        System.out.println();
    }
}