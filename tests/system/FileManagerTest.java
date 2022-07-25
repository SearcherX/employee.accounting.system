package system;

import org.junit.Test;
import system.report.CompanyReportMaker;
import system.structure.Department;
import system.structure.Employee;
import system.structure.Gender;
import system.structure.Organization;
import system.structure.user.AccessLevel;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class FileManagerTest {

    @Test
    public void loadJSON() {
    }

    @Test
    public void saveJSON() {
        Organization newOrg = new Organization("Тестовая организация");
        Department ITDepartment = new Department("IT-отдел");
        Employee admin = new Employee("root");
        admin.getAccount().setPassword("123");
        admin.getAccount().setAccessLevel(AccessLevel.ADMIN);
        admin.setFIO("Админ");
        admin.setPosition("админ");
        admin.setContactNumber("111111");
        admin.setSalary(200000);
        admin.setGender(Gender.MALE);
        admin.setBirthDate(LocalDate.now().format(Employee.FORMATTER));
        admin.setEmploymentDate(LocalDate.now().format(Employee.FORMATTER));
        ITDepartment.addEmployee(admin);
        newOrg.addDepartment(ITDepartment);

        FileManager.saveJSON("newOrg.json", newOrg);
        Organization res = FileManager.loadJSON("newOrg.json");

        assertEquals(newOrg.getName(), res.getName());
    }

    @Test
    public void writeReportToFile() throws IOException {
        Organization org = FileManager.loadJSON("test.json");
        CompanyReportMaker maker = new CompanyReportMaker(org);
        String report = maker.createReportAverageSalaries();
        FileManager.writeReportToFile("testInfo.txt", report);

        File file = new File("testInfo.txt");
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();

        String res = new String(data, StandardCharsets.UTF_8);
        assertEquals(report, res);
    }
}