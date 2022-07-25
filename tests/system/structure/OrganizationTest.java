package system.structure;

import org.junit.Before;
import org.junit.Test;
import system.FileManager;
import system.structure.user.AccessLevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class OrganizationTest {
    Organization org = null;

    @Before
    public void setUp() {
        org = FileManager.loadJSON("test.json");
    }

    @Test
    public void getDepartmentByName() {
        Department res = org.getDepartmentByName("отдел продаж");
        assertEquals(res.getName(), "Отдел продаж");
        assertEquals(res.getEmployees().size(), 2);

        assertEquals(res.getEmployees().get(0).getAccount().getLogin(), "VasilievNA");
        assertEquals(res.getEmployees().get(0).getFIO(), "Васильев Николай Алексеевич");

        assertEquals(res.getEmployees().get(1).getAccount().getLogin(), "MorozovaEV");
        assertEquals(res.getEmployees().get(1).getFIO(), "Морозова Елена Васильевна");
    }

    @Test
    public void getDepartmentByLogin() {
        Department res = org.getDepartmentByLogin("VasilievNA");
        assertEquals(res.getName(), "Отдел продаж");
        assertEquals(res.getEmployees().size(), 2);

        assertEquals(res.getEmployees().get(0).getAccount().getLogin(), "VasilievNA");
        assertEquals(res.getEmployees().get(0).getFIO(), "Васильев Николай Алексеевич");

        assertEquals(res.getEmployees().get(1).getAccount().getLogin(), "MorozovaEV");
        assertEquals(res.getEmployees().get(1).getFIO(), "Морозова Елена Васильевна");
    }

    @Test
    public void getEmployeeByLogin() {
        Employee res = org.getEmployeeByLogin("FrolovaEP");
        assertEquals(res.getAccount().getLogin(), "FrolovaEP");
        assertEquals(res.getAccount().getPassword(), "SmartDir89");
        assertEquals(res.getAccount().getAccessLevel(), AccessLevel.USER);
        assertEquals(res.getFIO(), "Фролова Елена Петровна");
        assertEquals(res.getBirthDate().format(Employee.FORMATTER), "15.10.1989");
        assertEquals(res.getGender(), Gender.FEMALE);
        assertEquals(res.getContactNumber(), "+79135973244");
        assertEquals(res.getPosition(), Employee.HEAD_POSITION);
        assertEquals(res.getEmploymentDate().format(Employee.FORMATTER), "25.08.2018");
        assertEquals(res.getSalary(), 130000);
    }

    @Test
    public void transfer() {
        Employee emp = org.getEmployeeByLogin("FrolovaEP");
        Department toDepartment = org.getDepartmentByName("IT-отдел");

        org.transfer(emp, toDepartment);

        assertEquals(org.getDepartmentByLogin("FrolovaEP").getName(), "IT-отдел");
        assertEquals(org.getDepartmentByLogin("FrolovaEP").getEmployees().size(), 4);
        assertNull(emp.getPosition()); //у второго начальника сбрасывается должность, чтобы не было коллизий
    }

    @Test
    public void testTransfer() {
        org.transfer("FrolovaEP", "IT-отдел");

        assertEquals(org.getDepartmentByLogin("FrolovaEP").getName(), "IT-отдел");
        assertEquals(org.getDepartmentByLogin("FrolovaEP").getEmployees().size(), 4);
        //у второго начальника сбрасывается должность, чтобы не было коллизий
        assertNull(org.getEmployeeByLogin("FrolovaEP").getPosition());

        try {
            org.transfer("FrolovXX", "IT-отдел");
            fail("Ошибка. Сотрудник отсутсвует в базе");
        } catch (IllegalArgumentException ignored) {

        }

        try {
            org.transfer("FrolovaEP", "Коммерческий отдел");
            fail("Ошибка. Отдел отсутсвует в базе");
        } catch (IllegalArgumentException ignored) {

        }
    }

    @Test
    public void transferAll() {
        org.transferAll("Отдел продаж", "IT-отдел");
        assertEquals(org.getDepartmentByName("Отдел продаж").getEmployees().size(), 0);
        assertEquals(org.getDepartmentByName("IT-отдел").getEmployees().size(), 5);
        assertEquals(org.getDepartmentByLogin("MorozovaEV").getName(), "IT-отдел");
        assertEquals(org.getDepartmentByLogin("VasilievNA").getName(), "IT-отдел");
    }

    @Test
    public void getAverageSalaryMap() {
        HashMap<String, Integer> res = org.getAverageSalaryMap();
        assertEquals(+res.get("Отдел продаж"), 102500);
        assertEquals(+res.get("IT-отдел"), 90000);
        assertEquals(+res.get("Отдел кадров"), 70000);
    }

    @Test
    public void calcAverageSalary() {
        int res = org.calcAverageSalary();
        assertEquals(res, 87500);
    }

    @Test
    public void getTop10ExpensiveEmployees() {
        ArrayList<Employee> res = org.getTop10ExpensiveEmployees();
        assertEquals(res.size(), 9);
        assertEquals(res.get(0).getFIO(), "Васильев Николай Алексеевич");
        assertEquals(res.get(0).getSalary(), 150000);
        assertEquals(res.get(1).getFIO(), "Володин Дмитрий Сергеевич");
        assertEquals(res.get(1).getSalary(), 130000);
        assertEquals(res.get(2).getFIO(), "Фролова Елена Петровна");
        assertEquals(res.get(2).getSalary(), 130000);
        assertEquals(res.get(3).getFIO(), "Мельников Андрей Генадьевич");
        assertEquals(res.get(3).getSalary(), 70000);
        assertEquals(res.get(4).getFIO(), "Топоров Сергей Никиолаевич");
        assertEquals(res.get(4).getSalary(), 70000);
        assertEquals(res.get(5).getFIO(), "Морозова Елена Васильевна");
        assertEquals(res.get(5).getSalary(), 55000);
        assertEquals(res.get(6).getFIO(), "Антонов Сергей Дмитриевич");
        assertEquals(res.get(6).getSalary(), 50000);
        assertEquals(res.get(7).getFIO(), "Дружкова Ольга Генадьевна");
        assertEquals(res.get(7).getSalary(), 50000);
        assertEquals(res.get(8).getFIO(), "Прокопенко Оксана Серьгеевна");
        assertEquals(res.get(8).getSalary(), 50000);
    }

    @Test
    public void getTop10LoyalEmployees() {
        ArrayList<Employee> res = org.getTop10LoyalEmployees();
        assertEquals(res.size(), 9);
        assertEquals(res.get(0).getFIO(), "Володин Дмитрий Сергеевич");
        assertEquals(res.get(0).getEmploymentDate().format(Employee.FORMATTER), "25.03.2014");
        assertEquals(res.get(1).getFIO(), "Дружкова Ольга Генадьевна");
        assertEquals(res.get(1).getEmploymentDate().format(Employee.FORMATTER), "23.03.2015");
        assertEquals(res.get(2).getFIO(), "Васильев Николай Алексеевич");
        assertEquals(res.get(2).getEmploymentDate().format(Employee.FORMATTER), "30.07.2015");
        assertEquals(res.get(3).getFIO(), "Морозова Елена Васильевна");
        assertEquals(res.get(3).getEmploymentDate().format(Employee.FORMATTER), "23.08.2017");
        assertEquals(res.get(4).getFIO(), "Мельников Андрей Генадьевич");
        assertEquals(res.get(4).getEmploymentDate().format(Employee.FORMATTER), "15.02.2018");
        assertEquals(res.get(5).getFIO(), "Прокопенко Оксана Серьгеевна");
        assertEquals(res.get(5).getEmploymentDate().format(Employee.FORMATTER), "25.03.2018");
        assertEquals(res.get(6).getFIO(), "Топоров Сергей Никиолаевич");
        assertEquals(res.get(6).getEmploymentDate().format(Employee.FORMATTER), "13.05.2018");
        assertEquals(res.get(7).getFIO(), "Фролова Елена Петровна");
        assertEquals(res.get(7).getEmploymentDate().format(Employee.FORMATTER), "25.08.2018");
        assertEquals(res.get(8).getFIO(), "Антонов Сергей Дмитриевич");
        assertEquals(res.get(8).getEmploymentDate().format(Employee.FORMATTER), "24.02.2019");

    }

    @Test
    public void getEmployeesMapByFIO() {
        HashMap<String, ArrayList<Employee>> res = org.getEmployeesMapByFIO("Прокопенко Оксана Серьгеевна");
        assertEquals(res.size(), 1);
        assertEquals(res.get("Отдел кадров").size(), 1);
        assertEquals(res.get("Отдел кадров").get(0).getFIO(), "Прокопенко Оксана Серьгеевна");
        assertEquals(res.get("Отдел кадров").get(0).getAccount().getLogin(), "prank");
    }

    @Test
    public void getEmployeesMapByPosition() {
        HashMap<String, ArrayList<Employee>> res = org.getEmployeesMapByPosition("начальник");
        assertEquals(res.size(), 2);
        assertEquals(res.get("IT-отдел").size(), 1);
        assertEquals(res.get("Отдел кадров").size(), 1);
        assertEquals(res.get("IT-отдел").get(0).getFIO(), "Володин Дмитрий Сергеевич");
        assertEquals(res.get("IT-отдел").get(0).getAccount().getLogin(), "VolodinDS");
        assertEquals(res.get("Отдел кадров").get(0).getFIO(), "Фролова Елена Петровна");
        assertEquals(res.get("Отдел кадров").get(0).getAccount().getLogin(), "FrolovaEP");
    }

    @Test
    public void getEmployeesMapByDepartment() {
        HashMap<String, ArrayList<Employee>> res = org.getEmployeesMapByDepartment("Отдел продаж");
        assertEquals(res.size(), 1);
        assertEquals(res.get("Отдел продаж").size(), 2);
        assertEquals(res.get("Отдел продаж").get(0).getFIO(), "Васильев Николай Алексеевич");
        assertEquals(res.get("Отдел продаж").get(0).getAccount().getLogin(), "VasilievNA");
        assertEquals(res.get("Отдел продаж").get(1).getFIO(), "Морозова Елена Васильевна");
        assertEquals(res.get("Отдел продаж").get(1).getAccount().getLogin(), "MorozovaEV");
    }
}