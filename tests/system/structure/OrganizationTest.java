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
    }

    @Test
    public void getTop10LoyalEmployees() {
    }

    @Test
    public void getEmployeesMapByFIO() {
    }

    @Test
    public void getEmployeesMapByPosition() {
    }

    @Test
    public void getEmployeesMapByDepartment() {
    }
}