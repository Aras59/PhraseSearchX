import com.sun.net.ssl.internal.ssl.Provider;

import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Security;

public class Server {
    static final int port = 8000;

    public static void main(String[] args) {
	// write your code here

        Security.addProvider(new Provider());
        System.setProperty("javax.net.ssl.keyStore","server.jks");
        System.setProperty("javax.net.ssl.keyStorePassword","haslo123");
        System.setProperty("java.net.debug","all");
        SSLServerSocketFactory sslServerSocketFactory = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();

        try {
            ServerSocket sslServerSocket = sslServerSocketFactory.createServerSocket(port);
            System.out.println("SSL ServerSocket started");

            while(true){
                Socket clientSocket = sslServerSocket.accept();
                System.out.println("Nowy klient!");
                ServerThread st= new ServerThread(clientSocket);
                st.start();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
