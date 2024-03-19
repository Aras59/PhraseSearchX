package model.logout;

import java.io.Serializable;
import java.time.LocalDateTime;

public class LogoutResponse implements Serializable {
    private int code;

    private String message;

    private LocalDateTime dataTime;

    public LogoutResponse(int code, String message, LocalDateTime dataTime) {
        this.code = code;
        this.message = message;
        this.dataTime = dataTime;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDataTime() {
        return dataTime;
    }

    public void setDataTime(LocalDateTime dataTime) {
        this.dataTime = dataTime;
    }
}
