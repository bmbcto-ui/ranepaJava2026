package ru.ranepa;

import ru.ranepa.model.Employee;
import ru.ranepa.repository.EmployeeRepository;
import ru.ranepa.repository.EmployeeRepositoryImpl;
import ru.ranepa.service.HRMService;
import ru.ranepa.service.HRMServiceImpl;

import java.time.LocalDate;

public class HrmApplication {
    public static void main(String[] args) {
        EmployeeRepository repository = new EmployeeRepositoryImpl();
        HRMService service = new HRMServiceImpl(repository);

        System.out.println("Adding employees:");

        var employee1 = new Employee("Ivanov Vitaly Sergeevich",
                "boss",
                25_000.0,
                LocalDate.of(2026, 2, 26));

        var employee2 = new Employee("Kolbasenko Danil Ivanovich",
                "manager",
                20_000.0,
                LocalDate.of(2021, 1, 15));

        var employee3 = new Employee("Krasotka Maria Alekseevna",
                "engineer",
                18_000.0,
                LocalDate.of(2025, 6, 24));

        service.addEmployee(employee1);
        service.addEmployee(employee2);
        service.addEmployee(employee3);

        System.out.println("3 employees added\n");

        //все сотрудники//
        System.out.println("All employees");
        service.getAllEmployees().forEach(System.out::println);
        System.out.println();

        //средняя зп//
        System.out.printf("Average salary: %.2f\n\n",
                service.calculateAverageSalary());

        //самый высокооплачиваемый//
        System.out.println("Highest paid employee: ");
        service.findHighestPaidEmployee()
                .ifPresentOrElse(
                        emp -> System.out.println(emp.getName() + " - " + emp.getSalary()),
                        () -> System.out.println("No employees found")
                );
        System.out.println();

        //фильтрация по должности//
        System.out.println("Filter by position 'engineer': ");
        service.filterByPosition("engineer")
                .forEach(emp -> System.out.println(emp.getName() + " - " + emp.getPosition()));
        System.out.println();

        //поиск сотрудников по имени//
        System.out.println("Search employees by name 'Danil': ");
        service.searchByName("danil")
                .forEach(emp -> System.out.println(emp.getName()));
        System.out.println();

        //удаление сотрудника с ID//
        System.out.println("Delete employee with ID 2: ");
        boolean removed = service.removeEmployee(2L);
        System.out.println("Deletion result: " + (removed ? "successful" : "not found"));
        System.out.println();

        //список после удаления сотрудника//
        System.out.println("All employees after deletion: ");
        service.getAllEmployees().forEach(System.out::println);
        System.out.println();

        //статистика компании//
        System.out.println("Complete company statistics: ");
        System.out.println(service.getCompanyStatistics());
    }
}