package ru.ranepa.repository;

import ru.ranepa.model.Employee;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {
    Employee save(Employee employee);
    Optional<Employee> findById(long id);
    List<Employee> findAll();
    boolean delete(long id);
    void update(Employee employee);
}