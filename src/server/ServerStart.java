package server;

import java.security.Security;

import com.sun.net.ssl.internal.ssl.Provider;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ServerStart {
    private static final Logger LOGGER = LogManager.getLogger(ServerStart.class);

    public static void main (String[] args) {
        Security.addProvider(new Provider());
        System.setProperty("javax.net.ssl.keyStore","res/keys/server.jks");
        System.setProperty("javax.net.ssl.keyStorePassword","haslo123");
        System.setProperty("java.net.debug","all");
        Server server = new Server();
        server.run();
    }
}
