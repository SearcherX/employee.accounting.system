package system;

import system.structure.Department;
import system.structure.Employee;
import system.structure.Gender;
import system.structure.Organization;
import system.structure.user.AccessLevel;
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
        organization = FileManager.loadJSON(fileName);
        //если загрузка не удалась создать новую фирму с одним админом
        if (organization == null)
            organization = createCompany();
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

    // метод запуска синхронизации
    public void run() {
        // главный цикл фоновой задачи
        while (true) {
            // 1. сохранить инфу об организации в файл
            FileManager.saveJSON(fileName, organization);
            // 2. подождать определенное время
            try {
                Thread.sleep(waitIntervalMS);
            } catch (InterruptedException ex) {
                // если мы попали в этот код, то поток прервали из вне через interrupt
                interrupt();    // установим флаг interrupt для потока еще раз
            }

            // 3. в конце цикла проверяем, а не прервался ли наш поток
            if (isInterrupted()) {
                FileManager.saveJSON(fileName, organization); // сделать последнюю синхронизацию
                System.out.println("Данные сериализованы");
                break;          // завершить главный цикл
            }
        }
    }
}
