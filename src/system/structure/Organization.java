package system.structure;

import com.fasterxml.jackson.annotation.JsonProperty;
import system.structure.Department;
import system.structure.Employee;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Organization {
    private final String name;
    private final ArrayList<Department> departments = new ArrayList<>();

    public Organization(@JsonProperty("name") String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Department> getDepartments() {
        return departments;
    }

    public void addDepartment(Department department) {
        departments.add(department);
    }

    public void delDepartment(Department department) {
        departments.remove(department);
    }

    public void delDepartment(int index) {
        departments.remove(index);
    }

    //метод возврата информации о сотруднике по логину
    public Employee getAccount(String login) {
        for (Department department: departments) {
            for (Employee employee: department.getEmployees()) {
                if (employee.getAccount().getLogin().equals(login))
                    return employee;
            }
        }

        return null;
    }

    //метод получения списка средних зарплат по отделам
    public HashMap<String, Integer> getAverageSalaryMap() {
        HashMap<String, Integer> avgMap = new HashMap<>();

        for (Department department: departments) {
            avgMap.put(department.getName(), department.calcAverageSalary());
        }
        return avgMap;
    }

    //метод получения средней зарплаты по организации
    public int calcAverageSalary() {
        double avg = 0.0;
        HashMap<String, Integer> avgMap = getAverageSalaryMap();

        for (Integer avgDepartment: avgMap.values()) {
            avg += avgDepartment * 1.0 / avgMap.size();
        }

        return (int)Math.round(avg);
    }

    //метод нахождения топ-10 самых дорогих сотрудников по зарплате
    public ArrayList<Employee> getTop10ExpensiveEmployees() {
        return departments.stream()
                .flatMap(department -> department.getEmployees().stream())
                .sorted((o1, o2) -> o2.getSalary() - o1.getSalary())
                .limit(10).collect(Collectors.toCollection(ArrayList::new));
    }

    //метод нахождения топ-10 самых преданных сотрудников по количеству лет работы в организации
    public ArrayList<Employee> getTop10LoyalEmployees() {
        return departments.stream()
                .flatMap(department -> department.getEmployees().stream())
                .sorted(Comparator.comparing(Employee::getEmploymentDate))
                .limit(10).collect(Collectors.toCollection(ArrayList::new));
    }

    //метод возврата сотрудников по фио
    public HashMap<String, ArrayList<Employee>> getEmployeesMapByFIO(String FIO) {
        HashMap<String, ArrayList<Employee>> res = new HashMap<>();

        for (Department department: departments) {
            ArrayList<Employee> employees =  department.getEmployees().stream()
                    .filter(employee -> employee.getFIO().equals(FIO))
                    .collect(Collectors.toCollection(ArrayList::new));
            if (employees.size() > 0)
                res.put(department.getName(), employees);
        }

        if (res.size() == 0)
            return null;

        return res;
    }

    //метод возврата сотрудников по должности
    public HashMap<String, ArrayList<Employee>> getEmployeesMapByPosition(String position) {
        HashMap<String, ArrayList<Employee>> res = new HashMap<>();

        for (Department department: departments) {
            ArrayList<Employee> employees =  department.getEmployees().stream()
                    .filter(employee -> employee.getPosition().equals(position))
                    .collect(Collectors.toCollection(ArrayList::new));
            if (employees.size() > 0)
                res.put(department.getName(), employees);
        }

        if (res.size() == 0)
            return null;

        return res;
    }

    //метод возврата отдела по имени
    public Department getDepartmentByName(String departmentName) {
        for (Department department: departments) {
            if (department.getName().equals(departmentName))
                return department;
        }
        return null;
    }

    //метод возврата сотрудника по логину
    public Employee getEmployeeByLogin(String login) {
        for (Department department: departments) {
            for (Employee employee: department.getEmployees()) {
                if (employee.getAccount().getLogin().equals(login))
                    return employee;
            }
        }
        return null;
    }

    //метод возврата сотрудников по должности
    public HashMap<String, ArrayList<Employee>> getEmployeesMapByDepartment(String departmentName) {
        HashMap<String, ArrayList<Employee>> res = new HashMap<>();
        Department department = getDepartmentByName(departmentName);

        if (department != null)
            res.put(departmentName, department.getEmployees());
        else
            return null;

        return res;
    }

    //метод получения строки, описывающей структуру организации
    public String toFullString() {
        StringBuilder sb = new StringBuilder("Структура организации " + name + ":\n");
        for (int i = 0; i < departments.size(); i++) {
            sb.append(i + 1).append(". ").append(departments.get(i).toFullString()).append("\n");
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    @Override
    public String toString() {
        return "Организация: " + name + "\n" +
                "отделы: " + departments;
    }
}
