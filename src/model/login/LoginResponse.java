package model.login;

import java.io.Serializable;
import java.time.LocalDateTime;

public class LoginResponse implements Serializable {
    private int code;
    private String message;
    private String username;
    private LocalDateTime dataTime;
    private String accessToken;
    private LocalDateTime accessTokenExpiration;

    public LoginResponse(int code, String message, String userName, LocalDateTime dataTime, String token, LocalDateTime tokenExpiration) {
        this.code = code;
        this.message = message;
        this.username = userName;
        this.dataTime = dataTime;
        this.accessToken = token;
        this.accessTokenExpiration = tokenExpiration;
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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public LocalDateTime getAccessTokenExpiration() {
        return accessTokenExpiration;
    }

    public void setAccessTokenExpiration(LocalDateTime accessTokenExpiration) {
        this.accessTokenExpiration = accessTokenExpiration;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
