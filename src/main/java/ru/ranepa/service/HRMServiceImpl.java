package ru.ranepa.service;

import ru.ranepa.CompanyStatistics;
import ru.ranepa.model.Employee;
import ru.ranepa.repository.EmployeeRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class HRMServiceImpl implements HRMService {

    private final EmployeeRepository repository;

    // Внедрение зависимости через конструктор
    public HRMServiceImpl(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public Employee addEmployee(Employee employee) {
        validateEmployee(employee);
        return repository.save(employee);
    }

    @Override
    public boolean removeEmployee(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID can not be a null");
        }
        return repository.delete(id);
    }

    @Override
    public Optional<Employee> findEmployeeById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return repository.findById(id);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    @Override
    public double calculateAverageSalary() {
        List<Employee> employees = repository.findAll();

        if (employees.isEmpty()) {
            return 0.0;
        }

        double sum = 0.0;
        for (Employee emp : employees) {
            sum += emp.getSalary().doubleValue();
        }
        return sum / employees.size();

    }

    @Override
    public Optional<Employee> findHighestPaidEmployee() {
        List<Employee> employees = repository.findAll();

        if (employees.isEmpty()) {
            return Optional.empty();
        }

        Employee highest = employees.get(0);
        for (Employee emp : employees) {
            if (emp.getSalary().compareTo(highest.getSalary()) > 0) {
                highest = emp;
            }
        }
        return Optional.of(highest);

    }

    @Override
    public List<Employee> filterByPosition(String position) {
        if (position == null || position.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String searchPosition = position.trim().toLowerCase();
        List<Employee> result = new ArrayList<>();
        for (Employee emp : repository.findAll()) {
            if (emp.getPosition().toLowerCase().contains(searchPosition)) {
                result.add(emp);
            }
        }
        return result;
    }

    @Override
    public List<Employee> searchByName(String namePart) {
        if (namePart == null || namePart.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String searchName = namePart.trim().toLowerCase();

        return repository.findAll().stream()
                .filter(emp -> emp.getName() != null)
                .filter(emp -> emp.getName().toLowerCase().contains(searchName))
                .collect(Collectors.toList());
    }

    @Override
    public CompanyStatistics getCompanyStatistics() {
        List<Employee> employees = repository.findAll();

        if (employees.isEmpty()) {
            return new CompanyStatistics(0, 0.0, BigDecimal.ZERO,
                    Collections.emptyMap(), null);
        }

        // Общее количество
        long totalEmployees = employees.size();

        // Суммарная зарплата
        BigDecimal totalSalary = employees.stream()
                .map(Employee::getSalary)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Средняя зарплата
        double averageSalary = totalSalary.doubleValue() / totalEmployees;

        // Распределение по должностям
        Map<String, Long> employeesByPosition = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getPosition,
                        Collectors.counting()
                ));

        // Самый высокооплачиваемый
        Employee highestPaid = findHighestPaidEmployee().orElse(null);

        return new CompanyStatistics(
                totalEmployees,
                averageSalary,
                totalSalary,
                employeesByPosition,
                highestPaid
        );
    }

    // Валидация данных сотрудника//
    private void validateEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }

        if (employee.getName() == null || employee.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Employee name is required");
        }

        if (employee.getPosition() == null || employee.getPosition().trim().isEmpty()) {
            throw new IllegalArgumentException("Employee position is required");
        }

        if (employee.getSalary() == null ||
                employee.getSalary().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Salary must be positive");
        }

        if (employee.getHireDate() == null) {
            throw new IllegalArgumentException("Hire date is required");
        }
    }
}