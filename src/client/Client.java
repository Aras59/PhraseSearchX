package client;

import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import model.login.LoginResponse;
import model.logout.LogoutResponse;
import model.remind.RemindResponse;
import model.search.SearchResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Client implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(Client.class);
    private final Properties properties = initProperties();
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final AtomicBoolean isLogged = new AtomicBoolean(false);
    private final BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
    private String clientName;
    private String accessToken;
    private LocalDateTime accessTokenExpiration;


    @Override
    public void run() {
        isRunning.set(true);
        try {
            SSLSocketFactory sslSocketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
            Socket socket = sslSocketFactory.createSocket(properties.getProperty("server.url"),
                    Integer.parseInt(properties.getProperty("server.port")));
            ClientWorker clientWorker = new ClientWorker(socket);
            while (isRunning.get()) {
                String commandFromConsole;
                if (!isLogged.get()) {
                    LOGGER.info("Type commenad (l - login) or (r - register) or (p to password recover): ");
                    commandFromConsole = consoleReader.readLine();
                    if (commandFromConsole.equals("l")) {
                        System.out.println("LOGIN!");
                        System.out.println("Username: ");
                        String username = consoleReader.readLine();
                        System.out.println("Password: ");
                        String password = consoleReader.readLine();

                        LoginResponse response = clientWorker.login(username, password);
                        if (response != null) {
                            this.accessToken = response.getAccessToken();
                            this.accessTokenExpiration = response.getAccessTokenExpiration();
                            this.clientName = response.getUsername();
                            LOGGER.info("Client successfully log in!");
                            isLogged.set(true);
                        } else {
                            LOGGER.warn("Cannot successfully log in!");
                        }
                    }
                    else if (commandFromConsole.equals("r")) {
                        System.out.println("REGISTER!");
                        System.out.println("Put username: ");
                        String username = consoleReader.readLine();
                        System.out.println("Put password: ");
                        String password = consoleReader.readLine();
                        if (clientWorker.register(username, password)) {
                            LOGGER.info("Client successfully registered!");
                        } else {
                            LOGGER.warn("Cannot successfully register!");
                        }
                    }
                    else if (commandFromConsole.equals("p")) {
                        System.out.println("Password reminder!");
                        System.out.println("Put your username:");
                        String username = consoleReader.readLine();
                        RemindResponse response = clientWorker.remind(username);
                        if (response != null && response.getCode() == 200) {
                            LOGGER.info("Password for user " + response.getUsername() + " : "
                                    + response.getPassword());
                        }
                    }
                } else {
                    LOGGER.info("Type commenad (lo - logout) or (s - search phrase): ");
                    commandFromConsole = consoleReader.readLine();
                    if (commandFromConsole.equals("s")) {
                        System.out.println("Podaj fraze: ");
                        String searchPhrase = consoleReader.readLine();
                        SearchResponse response = clientWorker.search(clientName, searchPhrase);
                        if (response != null) {
                            HashMap results = response.getResults();
                            System.out.println("Found: " + results.size());
                            for (Object key : results.keySet()) {
                                System.out.println("Title: " + key );
                                System.out.println("Author: " + results.get(key));
                                System.out.println();
                            }
                        }
                    }
                    else if (commandFromConsole.equals("lo")) {
                        LogoutResponse response = clientWorker.logout(clientName);
                        if (200 == response.getCode()) {
                            isLogged.set(false);
                        }

                    }
                }
            }
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
        }
    }

    private Properties initProperties() {
        Properties properties = new Properties();
        try {
            properties = new Properties();
            InputStream inputStream = new FileInputStream("config/project.properties");
            properties.load(inputStream);
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
        }
        return properties;
    }
}
