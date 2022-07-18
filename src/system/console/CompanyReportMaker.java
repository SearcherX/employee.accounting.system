package system.console;

import system.structure.Employee;
import system.structure.Organization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CompanyReportMaker {
    private final Organization org;

    public CompanyReportMaker(Organization org) {
        this.org = org;
    }

    //метод создания отчета о средней зарплате по отделам
    public String createReportAverageSalaries() {
        StringBuilder sb = new StringBuilder("Средняя зарплата по отделам:\n");
        HashMap<String, Integer> avgMap = org.getAverageSalaryMap();
        int i = 1;

        for (Map.Entry<String, Integer> pair : avgMap.entrySet()) {
            sb.append(i++).append(". ").append(pair.getKey()).append(" - ").append(pair.getValue()).append("\n");
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    //метод создания отчета о средней зарплате по организации
    public String createReportAverageSalary() {
        return "Средняя зарплата по органищации - " + org.calcAverageSalary();
    }

    //метод создания отчета о ТОП-10 самых дорогих сотрудников
    public String createReportTop10ExpensiveEmployees() {
        StringBuilder sb = new StringBuilder("ТОП-10 самых дорогих сотрудников по зарплате:\n");
        ArrayList<Employee> top10 = org.getTop10ExpensiveEmployees();

        for (int i = 0; i < top10.size(); i++) {
            sb.append(i + 1).append(". ").append(top10.get(i).getFIO()).append(" - ")
                    .append(top10.get(i).getSalary()).append("\n");
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    //метод создания отчета о ТОП-10 самых преданных сотрудников
    public String createReportTop10LoyalEmployees() {
        StringBuilder sb = new StringBuilder("ТОП-10 самых дорогих сотрудников по зарплате:\n");
        ArrayList<Employee> top10 = org.getTop10LoyalEmployees();

        for (int i = 0; i < top10.size(); i++) {
            sb.append(i + 1).append(". ").append(top10.get(i).getFIO()).append(", дата найма: ")
                    .append(top10.get(i).getEmploymentDate().format(Employee.FORMATTER)).append("\n");
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    //метод создания отчета вывода сотрудников из словаря
    public String createReportEmployees(HashMap<String, ArrayList<Employee>> info) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, ArrayList<Employee>> pair : info.entrySet()) {
            sb.append(pair.getKey()).append(":").append("\n");
            for (Employee employee : pair.getValue()) {
                sb.append(employee).append("\n");
            }
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }
}
