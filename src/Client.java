import com.sun.net.ssl.internal.ssl.Provider;

import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.security.Security;
import java.util.HashMap;
import java.util.Scanner;

public class Client {

    static final int port = 8000;

    public static void main(String[] args) {
        Security.addProvider(new Provider());
        System.setProperty("javax.net.ssl.trustStore","client.jks");
        System.setProperty("javax.net.ssl.trustStorePassword","haslo123");
        System.setProperty("java.net.debug","all");
        SSLSocketFactory sslSocketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
        try {
            Socket socket = sslSocketFactory.createSocket("localhost", port);
            boolean islogin=false;
            while(true) {
                Scanner scanner = new Scanner(System.in);
                String inputLine;
                if (islogin == false) {
                    System.out.println("Podaj Polcenie(z aby zalogować)(r aby zarejestrowac)(ph aby przypomniec haslo): ");
                    inputLine = scanner.nextLine();
                    if (inputLine.equals("z")) {
                        ClientWorker clientWorker = new ClientWorker(socket);
                        if (clientWorker.login()) {
                            System.out.println("Zalogowano!");
                            islogin = true;
                        } else {
                            System.out.println("Nie udalo sie zalogowac!");
                        }

                    }
                    if (inputLine.equals("r")) {
                        ClientWorker clientWorker = new ClientWorker(socket);
                        if (clientWorker.register()) {
                            System.out.println("Zarejestrowano");
                        } else {
                            System.out.println("Nie udalo sie zarejestrowac");
                        }
                    }
                    if (inputLine.equals("ph")) {
                        ClientWorker clientWorker = new ClientWorker(socket);
                        clientWorker.remind();
                    }
                }else {
                    System.out.println("Podaj polecenie(w - wyloguj)(s - szukaj frazy): ");
                    inputLine = scanner.nextLine();
                    if (inputLine.equals("s")) {
                        ClientWorker clientWorker = new ClientWorker(socket);
                        HashMap<String, String> hashName = new HashMap<String, String>(clientWorker.search());
                        System.out.println("Znaleziono wynikow: " + hashName.size());
                        for (String key : hashName.keySet()) {
                            System.out.println("Tytul: " + key );
                            System.out.println("Autor: " + hashName.get(key));
                            System.out.println();
                        }
                    }
                    if (inputLine.equals("w")) {
                        ClientWorker clientWorker = new ClientWorker(socket);
                        islogin = false;
                        clientWorker.logout();

                    }
                }
            }

        } catch (IOException e) {
            e.getMessage();
        }
    }


}

