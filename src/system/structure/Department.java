package system.structure;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

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
        for (Employee employee: employees) {
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
        for (Employee employee: employees) {
            avg += employee.getSalary() * 1.0 / employees.size();
        }

        return (int)Math.round(avg);
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

    @Override
    public String toString() {
        return name;
    }

    public String toFullString() {
        return "Название отдела: " + name + "\n" +
                "ФИО начальника: " + (getHeadOfDepartment() == null ? "нет" : getHeadOfDepartment().getFIO());
    }
}
