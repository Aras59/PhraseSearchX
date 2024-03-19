package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

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
import workers.XpathWorker;

public class ServerThread extends Thread{

    private static final Logger LOGGER = LogManager.getLogger(ServerThread.class);
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final Socket socket;
    private final ObjectOutputStream outputSocketStream;
    private final ObjectInputStream inputSocketStream;
    private final XpathWorker xpathWorker = new XpathWorker();
    private String clientToken;

    public ServerThread(Socket socket) throws IOException {
        this.socket = socket;
        this.outputSocketStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputSocketStream = new ObjectInputStream(this.socket.getInputStream());
    }

    public void run() {
        isRunning.set(true);
        try {
            while (isRunning.get()) {
                Object responseFromClient = inputSocketStream.readObject();
                if (responseFromClient == null) continue;
                if (responseFromClient instanceof LoginRequest) {
                    loginJob(responseFromClient);
                }
                else if (responseFromClient instanceof RegisterRequest) {
                    if(registerJob((RegisterRequest) responseFromClient)) {
                        outputSocketStream.writeObject(new RegisterResponse(200));
                    }
                }
                else if (responseFromClient instanceof RemindRequest) {
                    String password = xpathWorker.getRemindData(((RemindRequest) responseFromClient).getUsername());
                    outputSocketStream.writeObject(new RemindResponse(200, password,
                            ((RemindRequest) responseFromClient).getUsername()));
                }
                else if (responseFromClient instanceof SearchRequest) {
                    HashMap hashNames = xpathWorker.searchPhrase(((SearchRequest) responseFromClient).getSearchPhrase());
                    outputSocketStream.writeObject(new SearchResponse(200, hashNames));
                }
                else if (responseFromClient instanceof LogoutRequest) {
                    LogoutRequest request = (LogoutRequest) responseFromClient;
                    clientToken = null;
                    LogoutResponse logoutResponse = new LogoutResponse(200, "LOGOUT", LocalDateTime.now());
                    outputSocketStream.writeObject(logoutResponse);
                    LOGGER.warn("CLIENT: " + request.getClientName() + " LOGOFF!");
                }
                else if (socket.isClosed()) {
                    isRunning.set(false);
                    LOGGER.error("CLIENT OFF!");
                }
            }
        } catch(IOException | ClassNotFoundException ex) {
            LOGGER.error(ex.getMessage());
        }
    }

    private Socket getSocket() {
        return socket;
    }

    private String getToken(String dateTime, String login, String password) {
        return Base64.getEncoder().encodeToString((dateTime + login + password).getBytes());
    }

    private void loginJob (Object responseFromClient) throws IOException {
        LoginRequest request = (LoginRequest) responseFromClient;
        if (xpathWorker.checkLoginPassword(request.getLogin(), request.getPassword())) {
            LocalDateTime dateTime = LocalDateTime.now();
            clientToken = getToken(dateTime.toString(), request.getLogin(), request.getPassword());
            LocalDateTime tokenExpirationDate = dateTime.plusHours(1);
            LoginResponse response = new LoginResponse(200, "Success", request.getLogin()
                    , dateTime, clientToken, tokenExpirationDate);
            outputSocketStream.writeObject(response);
            LOGGER.info("User: " + request.getLogin() + " login! Expiration at: " + tokenExpirationDate);
        } else {
            LocalDateTime dateTime = LocalDateTime.now();
            LoginResponse response = new LoginResponse(403, "No authorization", null, dateTime, null, null);
            outputSocketStream.writeObject(response);
            LOGGER.info("User: " + request.getLogin() + " not authorized!");
        }
    }

    private boolean registerJob(RegisterRequest responseFromClient) {
        if (xpathWorker.saveUser(responseFromClient.getLogin(), responseFromClient.getPassword(),
                responseFromClient.getCreateDate())) {
            LOGGER.info("User: " + responseFromClient.getLogin() + " successfully registered!");
            return true;
        }
        return false;
    }

    public void closeClientThread() {
        if (!socket.isClosed()) {
            try {
                isRunning.set(false);
                outputSocketStream.close();
                inputSocketStream.close();
                socket.close();
            } catch (IOException ex) {
                LOGGER.error("Client shutdown problem!");
            }
            LOGGER.info("Client shutdown!");
        }
    }

}
