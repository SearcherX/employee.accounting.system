package system.console;

import system.structure.Department;
import system.structure.Employee;
import system.structure.Gender;
import system.structure.Organization;
import system.structure.user.AccessLevel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

public class CompanyConsoleApp {
    private final Organization org;
    private final CompanyReportMaker reportMaker;

    public CompanyConsoleApp(Organization org) {
        this.org = org;
        reportMaker = new CompanyReportMaker(org);
    }

    //метод, запрашивайший куда вывести отчет и выводящий его
    private boolean askDestination(Scanner in, String report) {
        System.out.println("Выберите куда вывести отчет:");
        System.out.println("1 - на консоль");
        System.out.println("2 - в файл");
        System.out.println("0 - выход");

        int choice3 = in.nextInt();

        if (choice3 == 0)
            return false;

        if (choice3 == 1) {
            System.out.println(report);
            System.out.println();
        }

        if (choice3 == 2) {
            System.out.print("Введите название файла: ");
            String choice4 = in.next();

            File file = new File(choice4);
            try {
                file.createNewFile();
                writeReportToFile(choice4, report);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Отчет записан в файл " + choice4);
        }
        return true;
    }

    public void start() {
        Scanner in = new Scanner(System.in);

        System.out.print("Введите логин: ");
        String login = in.next();
        Employee employee = org.getAccount(login);

        if (employee == null) {
            System.out.println("Логина " + login + " нет в базе");
            return;
        }

        System.out.print("Введите пароль: ");
        String password = in.next();

        if (!employee.getAccount().getPassword().equals(password)) {
            System.out.println("Неверный пароль");
        }

        AccessLevel accessLevel = employee.getAccount().getAccessLevel();

        System.out.println("Добро пожаловать в консольное приложение фирмы " + org.getName() +
                ", " + employee.getFIO());
        System.out.println("Права доступа: " + accessLevel.getName());

        while (true) {
            System.out.println("Выберите пункт меню:");
            System.out.println("1 - найти пользователей");
            System.out.println("2 - вывести отчет");
            if (accessLevel.ordinal() > 0) {
                System.out.println("3 - управление списком сотрудников");
                System.out.println("4 - управление списком отделов");
            }

            System.out.println("0 - выход");

            int choice1 = in.nextInt();

            if (choice1 == 0)
                return;

            //меню управления поиском сотрудников
            if (choice1 == 1) {
                System.out.println("Выберите по какому параметру искать:");
                System.out.println("1 - ФИО");
                System.out.println("2 - должность");
                System.out.println("3 - название отдела");
                System.out.println("0 - выход");

                int choice2 = in.nextInt();

                if (choice2 == 0)
                    return;

                if (choice2 == 1) {
                    System.out.print("Введите ФИО: ");
                    in.nextLine();
                    String choice3 = in.nextLine();
                    System.out.println(reportMaker.createReportEmployees(org.getEmployeesMapByFIO(choice3)));
                    System.out.println();
                }

                if (choice2 == 2) {
                    System.out.print("Введите должность: ");
                    in.nextLine();
                    String choice3 = in.nextLine();
                    System.out.println(reportMaker.createReportEmployees(org.getEmployeesMapByPosition(choice3)));
                    System.out.println();
                }

                if (choice2 == 3) {
                    System.out.print("Введите название отдела: ");
                    in.nextLine();
                    String choice3 = in.nextLine();
                    System.out.println(reportMaker.createReportEmployees(org.getEmployeesMapByDepartment(choice3)));
                    System.out.println();
                }
            //меню вывода отчетов
            } else if (choice1 == 2) {
                System.out.println("Выберите какой вам нужен отчет:");
                System.out.println("1 - структура организации");
                System.out.println("2 - средняя зарплата по организации");
                System.out.println("3 - средняя зарплата по отделам");
                System.out.println("4 - ТОП-10 самых дорогих сотрудников по зарплате");
                System.out.println("5 - ТОП-10 самых преданных сотрудников по количеству лет работы в организации");
                System.out.println("0 - выход");

                int choice2 = in.nextInt();

                if (choice2 == 0)
                    return;

                if (choice2 == 1) {
                    String report = org.toFullString();
                    //спросить куда вывести отчет и вывести его
                    if (!askDestination(in, report))
                        return;
                }

                if (choice2 == 2) {
                    String report = reportMaker.createReportAverageSalary();
                    //спросить куда вывести отчет и вывести его
                    if (!askDestination(in, report))
                        return;
                }

                if (choice2 == 3) {
                    String report = reportMaker.createReportAverageSalaries();
                    //спросить куда вывести отчет и вывести его
                    if (!askDestination(in, report))
                        return;
                }

                if (choice2 == 4) {
                    String report = reportMaker.createReportTop10ExpensiveEmployees();
                    //спросить куда вывести отчет и вывести его
                    if (!askDestination(in, report))
                        return;
                }

                if (choice2 == 5) {
                    String report = reportMaker.createReportTop10LoyalEmployees();
                    //спросить куда вывести отчет и вывести его
                    if (!askDestination(in, report))
                        return;
                }
            //меню управления списком сотрудников
            } else if (accessLevel.ordinal() > 0 && choice1 == 3) {
                System.out.println("Меню управления списком сотрудников:");
                System.out.println("1 - изменить данные сотрудника");
                if (accessLevel == AccessLevel.ADMIN) {
                    System.out.println("2 - добавить данные сотрудника");
                    System.out.println("3 - удалить данные сотрудника");
                }
                System.out.println("0 - выход");

                int choice2 = in.nextInt();

                if (choice2 == 0)
                    return;

                if (choice2 == 1) {
                    System.out.print("Введите логин сотрудника: ");
                    String empName = in.next();
                    Employee foundEmployee = org.getAccount(empName);

                    if (foundEmployee == null)
                        System.out.println("Нет такого логина " + empName);
                    else {
                        while (true) {
                            System.out.println("Выберите поле для редактирования:");
                            System.out.println("1 - ФИО");
                            System.out.println("2 - дата рождения");
                            System.out.println("3 - пол");
                            System.out.println("4 - номер телефона");
                            System.out.println("5 - должность");
                            System.out.println("6 - дата принятия на работу");
                            System.out.println("7 - зарплата");
                            System.out.println("8 - пароль");

                            if (accessLevel == AccessLevel.ADMIN)
                                System.out.println("9 - правда доступа");

                            System.out.println("0 - выход");

                            int choice3 = in.nextInt();

                            if (choice3 == 1) {
                                System.out.print("Введите новое ФИО: ");
                                in.nextLine();
                                String FIO = in.nextLine();

                                System.out.println("ФИО сотрудника " + foundEmployee.getFIO()
                                        + " изменено на " + FIO);
                                foundEmployee.setFIO(FIO);
                            } else if (choice3 == 2) {
                                System.out.print("Введите новую дату рождения: ");
                                String birthDateStr = in.next();

                                System.out.println("Дата рождения "
                                        + foundEmployee.getBirthDate().format(Employee.FORMATTER)
                                        + " сотрудника "
                                        + foundEmployee.getFIO()
                                        + " изменена на "
                                        + birthDateStr);

                                foundEmployee.setBirthDate(birthDateStr);
                            } else if (choice3 == 3) {
                                System.out.print("Выберите пол (м/ж): ");
                                String choice4 = in.next();
                                Gender gender = null;

                                switch (choice4) {
                                    case "м" -> gender = Gender.MALE;
                                    case "ж" -> gender = Gender.FEMALE;
                                }

                                if (gender == null)
                                    System.out.println("Пол " + choice4 + " не обнаружен в базе");
                                else {
                                    if (gender == foundEmployee.getGender()) {
                                        System.out.println("Пол "
                                                + foundEmployee.getGender()
                                                + " сотрудника "
                                                + foundEmployee.getFIO()
                                                + " остался прежним");
                                    } else {
                                        System.out.println("Пол "
                                                + foundEmployee.getGender()
                                                + " сотрудника "
                                                + foundEmployee.getFIO()
                                                + " изменен на "
                                                + gender);

                                        foundEmployee.setGender(gender);
                                    }
                                }
                            } else if (choice3 == 4) {
                                System.out.print("Введите номер телефона: ");
                                String choice4 = in.next();

                                System.out.println("Контактный номер "
                                        + foundEmployee.getContactNumber()
                                        + " сотрудника "
                                        + foundEmployee.getFIO()
                                        + " изменена на "
                                        + choice4);

                                foundEmployee.setContactNumber(choice4);
                            } else if (choice3 == 5) {
                                System.out.print("Введите должность: ");
                                String choice4 = in.next();

                                System.out.println("Должность "
                                        + foundEmployee.getPosition()
                                        + " сотрудника "
                                        + foundEmployee.getFIO()
                                        + " изменена на "
                                        + choice4);

                                foundEmployee.setPosition(choice4);
                            } else if (choice3 == 6) {
                                System.out.print("Введите новую дату принятия на работу: ");
                                String employmentDateStr = in.next();

                                System.out.println("Дата принятия на работу "
                                        + foundEmployee.getEmploymentDate().format(Employee.FORMATTER)
                                        + " сотрудника "
                                        + foundEmployee.getFIO()
                                        + " изменена на "
                                        + employmentDateStr);

                                foundEmployee.setEmploymentDate(employmentDateStr);

                            } else if (choice3 == 7) {
                                System.out.print("Введите новую зарплату: ");
                                int choice4 = in.nextInt();

                                System.out.println("Зарплата "
                                        + foundEmployee.getSalary()
                                        + " сотрудника "
                                        + foundEmployee.getFIO()
                                        + " изменена на "
                                        + choice4);

                                foundEmployee.setSalary(choice4);
                            } else if (choice3 == 8) {
                                System.out.print("Введите новый пароль: ");
                                String choice4 = in.next();

                                System.out.println("Пароль "
                                        + foundEmployee.getAccount().getPassword()
                                        + " сотрудника "
                                        + foundEmployee.getFIO()
                                        + " изменена на "
                                        + choice4);

                                foundEmployee.getAccount().setPassword(choice4);
                            } else if (accessLevel == AccessLevel.ADMIN && choice3 == 9) {
                                System.out.print("Выберите уровень доступа (user, redactor): ");
                                String choice4 = in.next();
                                AccessLevel accessLevel1 = AccessLevel.USER;

                                if (choice4.equals("redactor"))
                                    accessLevel1 = AccessLevel.REDACTOR;

                                System.out.println("Права доступа "
                                        + foundEmployee.getAccount().getAccessLevel()
                                        + " сотрудника "
                                        + foundEmployee.getFIO()
                                        + " изменена на "
                                        + accessLevel1);

                                foundEmployee.getAccount().setAccessLevel(accessLevel1);
                            }

                            System.out.println("Продолжить редактировать? (да/нет)");
                            String choice4 = in.next();

                            if (choice4.equals("нет"))
                                break;

                            if (choice3 == 0 || choice4.equals("0"))
                                return;
                        }
                    }
                } else if (choice2 == 2 && accessLevel == AccessLevel.ADMIN) {
                    System.out.println("Меню добавления сотрудника:");

                    String newLogin;
                    while (true) {
                        System.out.print("Введите логин: ");
                        newLogin = in.next();

                        try {
                            org.getAccount(newLogin);
                            System.out.println("Логин " + newLogin + " уже используется");
                        } catch (IllegalArgumentException ignored) {
                            break;
                        }
                    }

                    Department department;
                    while (true) {
                        System.out.print("Введите название отдела: ");
                        in.nextLine();
                        String choice3 = in.nextLine();

                        try {
                            department = org.getDepartmentByName(choice3);
                            break;
                        } catch (IllegalArgumentException ignored) {
                            System.out.println("Ошибка. " + choice3 + " нет в базе");
                        }
                    }

                    Employee emp = new Employee(newLogin);

                    System.out.print("Введите пароль: ");
                    String newPassword = in.next();
                    emp.getAccount().setPassword(newPassword);

                    System.out.print("Выберите уровень доступа (user, redactor): ");
                    String choice4 = in.next();
                    AccessLevel accessLevel1 = AccessLevel.USER;

                    if (choice4.equals("redactor"))
                        accessLevel1 = AccessLevel.REDACTOR;
                    emp.getAccount().setAccessLevel(accessLevel1);

                    System.out.print("Введите ФИО: ");
                    in.nextLine();
                    String newFIO = in.nextLine();
                    emp.setFIO(newFIO);

                    System.out.print("Введите дату рождения: ");
                    String newBirthDate = in.next();
                    emp.setBirthDate(newBirthDate);

                    while(true) {
                        System.out.print("Выберите пол (м/ж): ");
                        String genderChoice = in.next();
                        Gender gender = null;

                        switch (genderChoice) {
                            case "м" -> gender = Gender.MALE;
                            case "ж" -> gender = Gender.FEMALE;
                        }

                        if (gender == null)
                            System.out.println("Пол " + genderChoice + " не обнаружен в базе");
                        else
                            break;
                    }

                    System.out.print("Введите контактный номер: ");
                    String newContactNumber = in.next();
                    emp.setContactNumber(newContactNumber);

                    while(true) {
                        System.out.print("Введите должность: ");
                        String newPosition = in.next();
                        if (newPosition.equals("начальник") && department.getHeadOfDepartment() != null) {
                            System.out.println("Должность начальника уже занята в " + department);
                        } else {
                            emp.setPosition(newPosition);
                            break;
                        }
                    }

                    emp.setEmploymentDate(LocalDate.now().format(Employee.FORMATTER));

                    System.out.print("Введите зарплату: ");
                    int newSalary = in.nextInt();
                    emp.setSalary(newSalary);

                    department.addEmployee(emp);
                    System.out.println("Сотрудник " + emp.getFIO() + " добавлен в " + department);
                } else if (choice2 == 3 && accessLevel == AccessLevel.ADMIN) {
                    while (true) {
                        System.out.print("Введите логин сотрудника для удаления: ");
                        String delLogin = in.next();

                        try {
                            Employee delEmp = org.getAccount(delLogin);
                            Department department = org.getDepartmentByLogin(delLogin);
                            department.delEmployee(delEmp);
                            System.out.println("Сотрудник " + delEmp.getFIO() + " с логином "
                                    + delLogin + " удален из " + department);
                            break;
                        } catch (IllegalArgumentException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                }
            //меню управления списком отделов
            } else if (accessLevel.ordinal() > 0 && choice1 == 4) {
                System.out.println("Меню управления списком отделов:");
                System.out.println("1 - изменить название отдела");
                if (accessLevel == AccessLevel.ADMIN) {
                    System.out.println("2 - добавить отдел");
                    System.out.println("3 - удалить отдел");
                }
                System.out.println("0 - выход");

                int choice2 = in.nextInt();

                if (choice2 == 0)
                    return;

                if (choice2 == 1) {
                    Department department;
                    in.nextLine();
                    while (true) {
                        System.out.print("Введите название отдела: ");
                        String oldName = in.nextLine();
                        try {
                            department = org.getDepartmentByName(oldName);
                            break;
                        } catch (IllegalArgumentException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }

                    while (true) {
                        System.out.print("Введите новое название отдела: ");
                        String newName = in.nextLine();
                        try {
                            org.getDepartmentByName(newName);
                            System.out.println("Название " + newName + " уже занято");
                        } catch (IllegalArgumentException ex) {
                            System.out.println("Название " + department + " изменено на " + newName);
                            System.out.println();
                            department.setName(newName);
                            break;
                        }
                    }
                //пункт меню добавления отдела
                } else if (choice2 == 2 && accessLevel == AccessLevel.ADMIN) {
                    in.nextLine();
                    while (true) {
                        System.out.print("Введите название отдела для добавления: ");
                        String departmentName = in.nextLine();

                        try {
                            Department department = org.getDepartmentByName(departmentName);
                            System.out.println(department + " уже есть в базе");
                        } catch (IllegalArgumentException ignored) {
                            System.out.println(departmentName + " добавлен в базу");
                            org.addDepartment(new Department(departmentName));
                            break;
                        }
                    }
                //пункт меню удаления отдела
                } else if (choice2 == 3 && accessLevel == AccessLevel.ADMIN) {
                    in.nextLine();
                    while (true) {
                        System.out.print("Введите название отдела для удаления: ");
                        String departmentName = in.nextLine();

                        try {
                            Department department = org.getDepartmentByName(departmentName);
                            if (department.getEmployees().size() > 0) {
                                System.out.println("В " + department + " есть сотрудники. " +
                                        "Сначада сделайте им transfer в другие отделы");
                                continue;
                            }
                            System.out.println(department + " удален из базы");
                            org.delDepartment(department);
                            break;
                        } catch (IllegalArgumentException ex) {
                            System.out.println(departmentName + " не найден в базе");
                        }
                    }
                }
            }
        }


    }

    //метод записи отчета в файл
    public void writeReportToFile(String fileName, String report) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            bw.write(report);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
