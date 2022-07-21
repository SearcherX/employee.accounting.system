package system.structure;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import system.structure.user.UserAccount;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Employee {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final UserAccount account;
    private String FIO;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthDate;
    private Gender gender;
    private String contactNumber;
    private String position;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate employmentDate;
    private int salary;

    public Employee(@JsonProperty("login") String login) {
        account = new UserAccount(login);
    }

    public UserAccount getAccount() {
        return account;
    }

    public String getFIO() {
        return FIO;
    }

    public void setFIO(String FIO) {
        this.FIO = FIO;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = LocalDate.parse(birthDate, FORMATTER);
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public LocalDate getEmploymentDate() {
        return employmentDate;
    }

    public void setEmploymentDate(String employmentDate) {
        this.employmentDate = LocalDate.parse(employmentDate, FORMATTER);
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Сотрудник " + FIO + ", дата рождения: " + birthDate.format(FORMATTER)
                + ", должность: " + position;

    }

    public String toFullString() {
        return this + ", дата приёма на работу: " + employmentDate.format(FORMATTER)
                + ", логин: " + getAccount().getLogin();

    }
}
