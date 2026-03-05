package ru.ranepa;

import ru.ranepa.model.Employee;

import java.math.BigDecimal;
import java.util.Map;

public class CompanyStatistics {
    private final long totalEmployees;
    private final double averageSalary;
    private final BigDecimal totalSalary;
    private final Map<String, Long> employeesByPosition;
    private final Employee highestPaidEmployee;

    // Конструктор
    public CompanyStatistics(long totalEmployees, double averageSalary,
                             BigDecimal totalSalary, Map<String, Long> employeesByPosition,
                             Employee highestPaidEmployee) {
        this.totalEmployees = totalEmployees;
        this.averageSalary = averageSalary;
        this.totalSalary = totalSalary;
        this.employeesByPosition = employeesByPosition;
        this.highestPaidEmployee = highestPaidEmployee;
    }

    // Геттеры
    public long getTotalEmployees() {
        return totalEmployees;
    }

    public double getAverageSalary() {
        return averageSalary;
    }

    public BigDecimal getTotalSalary() {
        return totalSalary;
    }

    public Map<String, Long> getEmployeesByPosition() {
        return employeesByPosition;
    }

    public Employee getHighestPaidEmployee() {
        return highestPaidEmployee;
    }
    @Override
    public String toString() {
        return String.format("""
                        Total employees: %d
                        Total salary: %.2f
                        Average salary: %.2f
                        Distribution by position: %s
                        Highest paid employee: %s
                """, totalEmployees, totalSalary, averageSalary,
                employeesByPosition,
                highestPaidEmployee != null ? highestPaidEmployee.getName() : "нет");
    }
}
