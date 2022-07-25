package system.structure;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    //метод возврата отдела по имени
    public Department getDepartmentByName(String departmentName) {
        for (Department department: departments) {
            if (department.getName().equalsIgnoreCase(departmentName))
                return department;
        }
        return null;
    }

    //метод возврата отдела по логину работника
    public Department getDepartmentByLogin(String loginName) {
        for (Department department: departments) {
            Employee employee = department.getEmployeeByLogin(loginName);
            if (employee != null)
                return department;
        }
        return null;
    }

    //метод возврата информации о сотруднике по логину
    public Employee getEmployeeByLogin(String loginName) {
        for (Department department: departments) {
            Employee employee = department.getEmployeeByLogin(loginName);
            if (employee != null)
                return employee;
        }

        return null;
    }

    //метод перевода сотрудника из одного отдела в другой
    public void transfer(Employee emp, Department toDepartment) {
        Department fromDepartment = getDepartmentByLogin(emp.getAccount().getLogin());

        fromDepartment.delEmployee(emp);
        //если сотрудник-начальник, то нужно сбросить должность, чтобы не было
        //коллизий в новом отделе
        if (emp.getPosition().equalsIgnoreCase(Employee.HEAD_POSITION))
            emp.setPosition(null);
        toDepartment.addEmployee(emp);
    }

    //метод перевода сотрудника из одного отдела в другой
    public void transfer(String login, String toDepartmentName) {
        Employee emp = getEmployeeByLogin(login);
        if (emp == null)
            throw new IllegalArgumentException("Ошибка. Сотрудник отсутсвует в базе");

        Department toDepartment = getDepartmentByName(toDepartmentName);
        if (toDepartment == null)
            throw new IllegalArgumentException("Ошибка. Отдел отсутсвует в базе");

        transfer(emp, toDepartment);
    }

    //метод перевода всех сотрудников в другой отдел
    public void transferAll(String fromDepartmentName, String toDepartmentName) {
        Department fromDepartment = getDepartmentByName(fromDepartmentName);
        Department toDepartment = getDepartmentByName(toDepartmentName);

        for (int i = 0; i < fromDepartment.getEmployees().size(); i++) {
            transfer(fromDepartment.getEmployees().get(i), toDepartment);
            i--;
        }

    }

    //метод получения списка средних зарплат по отделам
    @JsonIgnore
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
    @JsonIgnore
    public ArrayList<Employee> getTop10ExpensiveEmployees() {
        return departments.stream()
                .flatMap(department -> department.getEmployees().stream())
                .sorted((o1, o2) -> o2.getSalary() - o1.getSalary())
                .limit(10).collect(Collectors.toCollection(ArrayList::new));
    }

    //метод нахождения топ-10 самых преданных сотрудников по количеству лет работы в организации
    @JsonIgnore
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
            ArrayList<Employee> employees =  department.getEmployeesByFIO(FIO);
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
            ArrayList<Employee> employees =  department.getEmployeesByPosition(position);
            if (employees.size() > 0)
                res.put(department.getName(), employees);
        }

        if (res.size() == 0)
            return null;

        return res;
    }

    //метод возврата сотрудников по должности
    public HashMap<String, ArrayList<Employee>> getEmployeesMapByDepartment(String departmentName) {
        HashMap<String, ArrayList<Employee>> res = new HashMap<>();
        Department department = getDepartmentByName(departmentName);

        if (department != null)
            res.put(department.getName(), department.getEmployees());
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
