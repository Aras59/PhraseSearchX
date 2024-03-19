package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;

import model.login.LoginRequest;
import model.login.LoginResponse;
import model.logout.LogoutRequest;
import model.logout.LogoutResponse;
import model.register.RegisterRequest;
import model.register.RegisterResponse;
import model.remind.RemindRequest;
import model.remind.RemindResponse;
import model.search.SearchRequest;
import model.search.SearchResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ClientWorker {

    private static final Logger LOGGER = LogManager.getLogger(ClientWorker.class);
    private final Socket socket;
    private final ObjectOutputStream outputSocketStream;
    private final ObjectInputStream inputSocketStream;

    public ClientWorker(Socket socket) throws IOException {
        this.socket = socket;
        this.outputSocketStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputSocketStream = new ObjectInputStream(socket.getInputStream());
    }

    public LoginResponse login(String username, String password) {
        Object loginResponse = null;
        try {
            LoginRequest loginRequest = new LoginRequest(username, password);
            outputSocketStream.writeObject(loginRequest);
            loginResponse = inputSocketStream.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            LOGGER.error(ex.getMessage());
        }
        return loginResponse instanceof LoginResponse && ((LoginResponse) loginResponse).getCode() == 200
                ? (LoginResponse) loginResponse : null;
    }

    public LogoutResponse logout(String clientName) {
        Object response = null;
        try {
            LogoutRequest request = new LogoutRequest(clientName, LocalDateTime.now());
            outputSocketStream.writeObject(request);
            response = inputSocketStream.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            LOGGER.error(ex.getMessage());
        }
        return response instanceof LogoutResponse && ((LogoutResponse) response).getCode() == 200
                ? (LogoutResponse)response : null;
    }

    public boolean register(String username, String password) throws IOException {
        try {
            RegisterRequest request = new RegisterRequest(username, password, LocalDateTime.now());
            outputSocketStream.writeObject(request);
            Object response = inputSocketStream.readObject();
            if (response instanceof RegisterResponse && ((RegisterResponse) response).getCode() == 200) {
                return true;
            }
        }catch (IOException | ClassNotFoundException ex) {
            LOGGER.error(ex.getMessage());
        }
        return false;
    }

    public RemindResponse remind(String username) {
        Object response = null;
        try {
            outputSocketStream.writeObject(new RemindRequest(username));
            response = inputSocketStream.readObject();
        }catch (IOException | ClassNotFoundException ex) {
            LOGGER.error(ex.getMessage());
        }
        return response instanceof RemindResponse && ((RemindResponse) response).getCode() == 200
                ? (RemindResponse) response : null;
    }

    public SearchResponse search(String clientName,String searchPhrase) {
        Object response = null;
        try {
            outputSocketStream.writeObject(new SearchRequest(clientName, searchPhrase));
            response = inputSocketStream.readObject();
        }catch (IOException | ClassNotFoundException ex) {
            LOGGER.error(ex.getMessage());
        }
        return response instanceof SearchResponse && ((SearchResponse) response).getCode() == 200
                ? (SearchResponse) response : null;
    }


}
