package ru.ranepa.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import ru.ranepa.model.Employee;
import ru.ranepa.repository.EmployeeRepository;
import ru.ranepa.repository.EmployeeRepositoryImpl;

import java.time.LocalDate;
import java.util.Optional;

class HRMServiceTest {

    private HRMService service;

    @BeforeEach
    void setUp() {
        // Создаем новый репозиторий перед каждым тестом
        EmployeeRepository repository = new EmployeeRepositoryImpl();
        service = new HRMServiceImpl(repository);
    }

    @Test
    void testCalculateAverageSalary() {
        // Добавляем сотрудников
        service.addEmployee(new Employee("Ivanov", "Dev", 100.0, LocalDate.now()));
        service.addEmployee(new Employee("Petrov", "Dev", 200.0, LocalDate.now()));
        service.addEmployee(new Employee("Sidorov", "Dev", 300.0, LocalDate.now()));

        // Проверяем среднюю зарплату
        double result = service.calculateAverageSalary();
        assertEquals(200.0, result, 0.001);
    }

    @Test
    void testFindHighestPaidEmployee() {
        // Добавляем сотрудников
        service.addEmployee(new Employee("Ivanov", "Dev", 100.0, LocalDate.now()));
        service.addEmployee(new Employee("Petrov", "Manager", 500.0, LocalDate.now()));
        service.addEmployee(new Employee("Sidorov", "QA", 300.0, LocalDate.now()));

        // Проверяем самого высокооплачиваемого
        Optional<Employee> result = service.findHighestPaidEmployee();

        assertTrue(result.isPresent());
        assertEquals("Petrov", result.get().getName());
        assertEquals(500.0, result.get().getSalary().doubleValue());
    }

    @Test
    void testEmptyList() {
        // Не добавляем сотрудников
        Optional<Employee> result = service.findHighestPaidEmployee();

        // Проверяем, что результат пустой
        assertFalse(result.isPresent());

        double avg = service.calculateAverageSalary();
        assertEquals(0.0, avg);
    }
}