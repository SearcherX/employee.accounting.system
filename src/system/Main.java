package system;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import system.console.CompanyConsoleApp;
import system.structure.Department;
import system.structure.Employee;
import system.structure.Gender;
import system.structure.Organization;
import system.structure.user.AccessLevel;

import java.nio.file.Paths;
import java.text.SimpleDateFormat;

public class Main {

    public static void main(String[] args) throws DatabindException {
        Organization organization = readInfoFromFile();
        CompanyConsoleApp app = new CompanyConsoleApp(organization);

        app.start();

    }

    public static Organization readInfoFromFile() throws DatabindException {
        Organization organization = null;
        try {
            ObjectMapper mapper = new ObjectMapper();

            organization = mapper.readValue(Paths.get("SmartElectronics.json").toFile(), Organization.class);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return organization;
    }

    public static void writeInfoToFile() {
        Organization org = new Organization("Smart Electronics");

        Department department1 = new Department("Отдел продаж");

        Employee emp1 = new Employee("VasilievNA");
        emp1.getAccount().setPassword("BestDir83");
        emp1.getAccount().setAccessLevel(AccessLevel.REDACTOR);
        emp1.setFIO("Васильев Николай Алексеевич");
        emp1.setBirthDate("10.09.1983");
        emp1.setGender(Gender.MALE);
        emp1.setContactNumber("+79235873145");
        emp1.setPosition("Начальник");
        emp1.setEmploymentDate("30.07.2015");
        emp1.setSalary(150000);
        department1.addEmployee(emp1);

        org.addDepartment(department1);
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            mapper.setDateFormat(new SimpleDateFormat("dd.MM.yyyy"));
            mapper.writeValue(Paths.get("SmartElectronics.json").toFile(), org);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
