package system.structure;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Department {
    private String name;
    private final ArrayList<Employee> employees = new ArrayList<>();

    public Department(@JsonProperty("name") String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Employee> getEmployees() {
        return employees;
    }

    @JsonIgnore
    public Employee getHeadOfDepartment() {
        for (Employee employee : employees) {
            if (employee.getPosition().equalsIgnoreCase("начальник"))
                return employee;
        }
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int calcAverageSalary() {
        double avg = 0.0;
        for (Employee employee : employees) {
            avg += employee.getSalary() * 1.0 / employees.size();
        }

        return (int) Math.round(avg);
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public void delEmployee(Employee employee) {
        employees.remove(employee);
    }

    public void delEmployee(int index) {
        employees.remove(index);
    }

    //метод поиска сотрудника по логину
    public Employee getEmployeeByLogin(String loginName) {
        for (Employee employee : getEmployees()) {
            if (employee.getAccount().getLogin().equals(loginName))
                return employee;
        }

        return null;
    }

    //метод возврата сотрудников по фио
    public ArrayList<Employee> getEmployeesByFIO(String FIO) {
        return getEmployees().stream()
                .filter(employee -> employee.getFIO().equals(FIO))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    //метод возврата сотрудников по должности
    public ArrayList<Employee> getEmployeesByPosition(String position) {
        return getEmployees().stream()
                .filter(employee -> employee.getPosition().equals(position))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public String toString() {
        return name;
    }

    public String toFullString() {
        return "Название отдела: " + name + "\n" +
                "ФИО начальника: " + (getHeadOfDepartment() == null ? "нет" : getHeadOfDepartment().getFIO());
    }
}
