package system.structure.user;

public enum AccessLevel {
    USER("пользователь"),
    REDACTOR("редактор"),
    ADMIN("администратор");

    private final String name;

    AccessLevel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
