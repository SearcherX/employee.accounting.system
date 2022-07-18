package system.structure.user;

import com.fasterxml.jackson.annotation.JsonProperty;

//класс пользовательского аккаунта
public class UserAccount {
    private final String login;
    private String password = "";
    private AccessLevel accessLevel = AccessLevel.USER;

    public UserAccount(@JsonProperty("login") String login) {
        this.login = login;
    }

    public UserAccount(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public UserAccount(String login, String password, AccessLevel accessLevel) {
        this.login = login;
        this.password = password;
        this.accessLevel = accessLevel;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }
}
