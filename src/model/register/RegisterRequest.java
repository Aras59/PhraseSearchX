package model.register;

import java.io.Serializable;
import java.time.LocalDateTime;

public class RegisterRequest implements Serializable {

    private String login;
    private String password;
    private LocalDateTime createDate;
    private String userRole;

    public RegisterRequest(String login, String password, LocalDateTime createDate) {
        this.login = login;
        this.password = password;
        this.createDate = createDate;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
