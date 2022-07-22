package system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import system.console.CompanyConsoleApp;
import system.structure.Department;
import system.structure.Employee;
import system.structure.Gender;
import system.structure.Organization;
import system.structure.user.AccessLevel;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class OrganizationSynchronizer extends Thread {
    // интервал ожидания
    private final static int waitIntervalMS = 5000;
    //имя файла, ассоциированного с данным синхронизатором
    private final String fileName;
    //организация, которую будем синхронизировать
    private Organization organization;

    // конструктор с параметрами
    public OrganizationSynchronizer(String fileName) {
        // вызов конструктора базового класса
        super();
        // инициализация полей
        this.fileName = fileName;
        //загрузить информацию организации
        readInfoFromFile();
    }

    public Organization getOrganization() {
        return organization;
    }

    public static Organization createCompany() {
        System.out.println("Создана новая фирма с админскими правами. Логин: root, пароль: 123");
        Organization org = new Organization("Новая фирма");

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
        org.addDepartment(ITDepartment);

        return org;
    }

    public void readInfoFromFile() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            organization = mapper.readValue(Paths.get(fileName).toFile(), Organization.class);
            if (organization == null)
                organization = createCompany();
        } catch (IOException ex) {
            System.out.println("Не удалось загрузить данные организации");
            System.out.println(ex.getMessage());
            organization = createCompany();
        }
    }

    public void writeInfoToFile() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            mapper.setDateFormat(new SimpleDateFormat("dd.MM.yyyy"));
            mapper.writeValue(Paths.get(fileName).toFile(), organization);

        } catch (Exception ex) {
            System.out.println("Не удалось сохранить данные в файл");
            System.out.println(ex.getMessage());
        }
    }

    // метод запуска синхронизации
    public void run() {
        // главный цикл фоновой задачи
        while (true) {
            // 1. сохранить инфу об организации в файл
            writeInfoToFile();
            // 2. подождать определенное время
            try {
                Thread.sleep(waitIntervalMS);
            } catch (InterruptedException ex) {
                // если мы попали в этот код, то поток прервали из вне через interrupt
                interrupt();    // установим флаг interrupt для потока еще раз
            }

            // 3. в конце цикла проверяем, а не прервался ли наш поток
            if (isInterrupted()) {
                writeInfoToFile(); // сделать последнюю синхронизацию
                System.out.println("Данные сериализованы");
                break;          // завершить главный цикл
            }
        }
    }
}
