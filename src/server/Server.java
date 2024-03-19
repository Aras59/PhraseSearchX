package server;

import javax.net.ssl.SSLServerSocketFactory;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Server implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(Server.class);
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final Properties properties = initProperties();
    private final List<ServerThread> clients = new ArrayList<>();

    @Override
    public void run() {
        try {
            isRunning.set(true);
            SSLServerSocketFactory sslServerSocketFactory = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
            ServerSocket sslServerSocket = sslServerSocketFactory.createServerSocket(Integer.parseInt(properties.getProperty("server.port")));
            LOGGER.info("SSL ServerSocket started!");
            LOGGER.info("To disable server type: CLOSE!");
            Runnable serverWorker = () -> {
                BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
                while (isRunning.get()) {
                    try {
                        String inputCommand = consoleReader.readLine();
                        if ("CLOSE".equalsIgnoreCase(inputCommand)) {
                            for (ServerThread client : clients) {
                                client.closeClientThread();
                            }
                            isRunning.set(false);
                            LOGGER.info("SERVER WAS STOPPED!");
                        }
                    } catch (IOException ex) {
                        LOGGER.error(ex.getMessage());
                    }
                }
            };
            Thread serverWorkerThread = new Thread(serverWorker);
            serverWorkerThread.start();

            Runnable clientWorker = () -> {
                while (isRunning.get()) {
                    try {
                        Socket clientSocket = sslServerSocket.accept();
                        ServerThread client = new ServerThread(clientSocket);
                        client.start();
                        clients.add(client);
                        LOGGER.info("New client connected to server! ");
                    } catch (IOException ex) {
                        LOGGER.error(ex.getMessage());
                    }
                }
            };
            Thread clientAcceptThread = new Thread(clientWorker);
            clientAcceptThread.start();

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
