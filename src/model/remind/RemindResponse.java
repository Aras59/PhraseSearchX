package model.remind;

import java.io.Serializable;

public class RemindResponse implements Serializable {
    private int code;
    private String password;
    private String username;

    public RemindResponse(int code, String password, String username) {
        this.code = code;
        this.password = password;
        this.username = username;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
