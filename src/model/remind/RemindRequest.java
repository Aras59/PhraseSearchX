package model.remind;

import java.io.Serializable;

public class RemindRequest implements Serializable {

    private String username;

    public RemindRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
