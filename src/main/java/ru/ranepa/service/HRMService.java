package ru.ranepa.service;

import ru.ranepa.CompanyStatistics;
import ru.ranepa.model.Employee;

import java.util.List;
import java.util.Optional;

public interface HRMService {
    //добавление сотрудника//
    Employee addEmployee(Employee employee);
    //удаление//
    boolean removeEmployee(Long id);
    //оиск сотрудника по id//
    Optional<Employee> findEmployeeById(Long id);
    //все сотрудники//
    List<Employee> getAllEmployees();
    //расчет средней зп//
    double calculateAverageSalary();
    //поиск самого высокооплачиваемого//
    Optional<Employee> findHighestPaidEmployee();
    //фильтрация по должности//
    List<Employee> filterByPosition(String position);
    //поиск по имени//
    List<Employee> searchByName(String namePart);
    //статистика по компании//
    CompanyStatistics getCompanyStatistics();
}
