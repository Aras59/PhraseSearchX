package model.register;

import java.io.Serializable;

public class RegisterResponse implements Serializable {
    private int code;

    public RegisterResponse(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
