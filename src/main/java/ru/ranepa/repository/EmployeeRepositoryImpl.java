package ru.ranepa.repository;

import ru.ranepa.model.Employee;
import java.util.*;

public class EmployeeRepositoryImpl implements EmployeeRepository {
    private final Map<Long, Employee> employees = new HashMap<>();
    private long nextId = 1;

    @Override
    public Employee save(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }

        if (employee.getId() == null) {
            employee.setId(nextId++);
            employees.put(employee.getId(), employee);
        } else {
            employees.put(employee.getId(), employee);
        }

        return employee;
    }

    @Override
    public Optional<Employee> findById(long id) {
        return Optional.ofNullable(employees.get(id));
    }

    @Override
    public List<Employee> findAll() {
        return new ArrayList<>(employees.values());
    }

    @Override
    public boolean delete(long id) {
        if (employees.containsKey(id)) {
            employees.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public void update(Employee employee) {
        if (employee == null || employee.getId() == null) {
            throw new IllegalArgumentException("Employee or employee ID cannot be null");
        }

        if (!employees.containsKey(employee.getId())) {
            throw new NoSuchElementException("Employee with id " + employee.getId() + " not found");
        }

        employees.put(employee.getId(), employee);
    }
}