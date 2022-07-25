package system.report;

import org.junit.Before;
import org.junit.Test;
import system.FileManager;
import system.structure.Organization;
import system.structure.user.AccessLevel;

import static org.junit.Assert.*;

public class CompanyReportMakerTest {
    Organization org = null;
    CompanyReportMaker maker = null;

    @Before
    public void setUp() {
        org = FileManager.loadJSON("test.json");
        maker = new CompanyReportMaker(org);
    }

    @Test
    public void createReportAverageSalaries() {
        String res = maker.createReportAverageSalaries();
        assertTrue(res.contains("Средняя зарплата по отделам:"));
        assertTrue(res.contains("1. Отдел продаж - 102500"));
        assertTrue(res.contains("2. IT-отдел - 90000"));
        assertTrue(res.contains("3. Отдел кадров - 70000"));
    }

    @Test
    public void createReportAverageSalary() {
        String res = maker.createReportAverageSalary();
        assertEquals(res, "Средняя зарплата по организации - 87500");
    }

    @Test
    public void createReportTop10ExpensiveEmployees() {
        String res = maker.createReportTop10ExpensiveEmployees();
        assertTrue(res.contains("ТОП-10 самых дорогих сотрудников по зарплате:"));
        assertTrue(res.contains("1. Васильев Николай Алексеевич - 150000"));
        assertTrue(res.contains("2. Володин Дмитрий Сергеевич - 130000"));
        assertTrue(res.contains("3. Фролова Елена Петровна - 130000"));
        assertTrue(res.contains("4. Мельников Андрей Генадьевич - 70000"));
        assertTrue(res.contains("5. Топоров Сергей Никиолаевич - 70000"));
        assertTrue(res.contains("6. Морозова Елена Васильевна - 55000"));
        assertTrue(res.contains("7. Антонов Сергей Дмитриевич - 50000"));
        assertTrue(res.contains("8. Дружкова Ольга Генадьевна - 50000"));
        assertTrue(res.contains("9. Прокопенко Оксана Серьгеевна - 50000"));
    }

    @Test
    public void createReportTop10LoyalEmployees() {
        String res = maker.createReportTop10LoyalEmployees();
        assertTrue(res.contains("ТОП-10 самых преданных сотрудников по количеству лет работы в организации:"));
        assertTrue(res.contains("1. Володин Дмитрий Сергеевич, дата найма: 25.03.2014"));
        assertTrue(res.contains("2. Дружкова Ольга Генадьевна, дата найма: 23.03.2015"));
        assertTrue(res.contains("3. Васильев Николай Алексеевич, дата найма: 30.07.2015"));
        assertTrue(res.contains("4. Морозова Елена Васильевна, дата найма: 23.08.2017"));
        assertTrue(res.contains("5. Мельников Андрей Генадьевич, дата найма: 15.02.2018"));
        assertTrue(res.contains("6. Прокопенко Оксана Серьгеевна, дата найма: 25.03.2018"));
        assertTrue(res.contains("7. Топоров Сергей Никиолаевич, дата найма: 13.05.2018"));
        assertTrue(res.contains("8. Фролова Елена Петровна, дата найма: 25.08.2018"));
        assertTrue(res.contains("9. Антонов Сергей Дмитриевич, дата найма: 24.02.2019"));
    }

    @Test
    public void createReportEmployees() {
        String res = maker.createReportEmployees(org.getEmployeesMapByDepartment("Отдел кадров"), AccessLevel.ADMIN);
        assertTrue(res.contains("Отдел кадров:"));
        assertTrue(res.contains("Сотрудник Фролова Елена Петровна, дата рождения: 15.10.1989, " +
                "должность: начальник, дата приёма на работу: 25.08.2018, логин: FrolovaEP"));
        assertTrue(res.contains("Сотрудник Антонов Сергей Дмитриевич, дата рождения: 21.03.2000, " +
                "должность: бухгалтер, дата приёма на работу: 24.02.2019, логин: trex"));
        assertTrue(res.contains("Сотрудник Дружкова Ольга Генадьевна, дата рождения: 21.03.1999, " +
                "должность: бухгалтер, дата приёма на работу: 23.03.2015, логин: fdsf"));
        assertTrue(res.contains("Сотрудник Прокопенко Оксана Серьгеевна, дата рождения: 11.07.2001, " +
                "должность: секретарь, дата приёма на работу: 25.03.2018, логин: prank"));

    }
}