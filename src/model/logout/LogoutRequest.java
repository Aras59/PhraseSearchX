package model.logout;

import java.io.Serializable;
import java.time.LocalDateTime;

public class LogoutRequest implements Serializable {
    private String clientName;

    private LocalDateTime dateTime;

    public LogoutRequest(String clientName, LocalDateTime dateTime) {
        this.clientName = clientName;
        this.dateTime = dateTime;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
