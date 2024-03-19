package client;

import java.security.Security;

import com.sun.net.ssl.internal.ssl.Provider;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ClientStart {
    private static final Logger LOGGER = LogManager.getLogger(ClientStart.class);
    public static void main(String[] args) {

        Security.addProvider(new Provider());
        System.setProperty("javax.net.ssl.trustStore","res/keys/client.jks");
        System.setProperty("javax.net.ssl.trustStorePassword","haslo123");
        System.setProperty("java.net.debug","all");
        Client client = new Client();
        client.run();
    }

}

