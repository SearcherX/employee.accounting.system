package system.structure;

import org.junit.Before;
import org.junit.Test;
import system.FileManager;
import system.structure.user.AccessLevel;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class DepartmentTest {
    Organization org = null;

    @Before
    public void setUp() {
        org = FileManager.loadJSON("test.json");
    }

    @Test
    public void calcAverageSalary() {
        Department ITDepartment = org.getDepartmentByName("IT-отдел");
        int res = ITDepartment.calcAverageSalary();
        assertEquals(res, 90000);
    }

    @Test
    public void getEmployeeByLogin() {
        Department department = org.getDepartmentByName("отдел кадров");
        Employee res = department.getEmployeeByLogin("FrolovaEP");
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
    public void getEmployeesByFIO() {
        Department department = org.getDepartmentByName("отдел кадров");
        ArrayList<Employee> res = department.getEmployeesByFIO("Фролова Елена Петровна");
        assertEquals(res.size(), 1);
        assertEquals(res.get(0).getAccount().getLogin(), "FrolovaEP");
        assertEquals(res.get(0).getAccount().getPassword(), "SmartDir89");
        assertEquals(res.get(0).getAccount().getAccessLevel(), AccessLevel.USER);
        assertEquals(res.get(0).getFIO(), "Фролова Елена Петровна");
        assertEquals(res.get(0).getBirthDate().format(Employee.FORMATTER), "15.10.1989");
        assertEquals(res.get(0).getGender(), Gender.FEMALE);
        assertEquals(res.get(0).getContactNumber(), "+79135973244");
        assertEquals(res.get(0).getPosition(), Employee.HEAD_POSITION);
        assertEquals(res.get(0).getEmploymentDate().format(Employee.FORMATTER), "25.08.2018");
        assertEquals(res.get(0).getSalary(), 130000);
    }

    @Test
    public void getEmployeesByPosition() {
        Department department = org.getDepartmentByName("Отдел кадров");
        ArrayList<Employee> res = department.getEmployeesByPosition("бухгалтер");

        assertEquals(res.size(), 2);
        assertEquals(res.get(0).getAccount().getLogin(), "trex");
        assertEquals(res.get(0).getFIO(), "Антонов Сергей Дмитриевич");
        assertEquals(res.get(0).getBirthDate().format(Employee.FORMATTER), "21.03.2000");

        assertEquals(res.size(), 2);
        assertEquals(res.get(1).getAccount().getLogin(), "fdsf");
        assertEquals(res.get(1).getFIO(), "Дружкова Ольга Генадьевна");
        assertEquals(res.get(1).getBirthDate().format(Employee.FORMATTER), "21.03.1999");
    }
}