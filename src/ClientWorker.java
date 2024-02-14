import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class ClientWorker {
    private Socket socket;

    public ClientWorker(Socket socket){
        this.socket=socket;
    }

    public boolean login(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Logowanie");
        System.out.println("Podaj dane do logowania");
        System.out.println("Podaj login: ");
        String login = scanner.nextLine();
        System.out.println("Podaj haslo: ");
        String password = scanner.nextLine();
        try {
            ObjectOutputStream o = new ObjectOutputStream(socket.getOutputStream());
            Datas datas = new Datas(Commands.LOGIN, login, password);
            o.writeObject(datas);
            o.flush();
            ObjectInputStream i = new ObjectInputStream(socket.getInputStream());
            Commands commands = (Commands)i.readObject();
            if(commands==Commands.SUCCES){
                return true;
            }else{
                return false;
            }
        }catch (IOException | ClassNotFoundException e) {
            e.getMessage();
        }
        return false;
    }



    public boolean register(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Rejestracja");
        System.out.println("Podaj dane do rejestracji");
        System.out.println("Podaj login: ");
        String login = scanner.nextLine();
        System.out.println("Podaj haslo: ");
        String password = scanner.nextLine();
        try {
            ObjectOutputStream o = new ObjectOutputStream(socket.getOutputStream());
            Datas datas = new Datas(Commands.REGISTER, login, password);
            o.writeObject(datas);
            o.flush();
            ObjectInputStream i = new ObjectInputStream(socket.getInputStream());
            Commands commands = (Commands)i.readObject();
            if(commands==Commands.SUCCES){
                return true;
            }
        }catch (IOException | ClassNotFoundException e) {
            e.getMessage();
        }
        return false;
    }

    public boolean remind(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Odzystkiwanie hasła");
        System.out.println("Podaj Login");
        String login = scanner.nextLine();
        try {
            ObjectOutputStream o = new ObjectOutputStream(socket.getOutputStream());
            Datas datas = new Datas(Commands.REMIND, login);
            o.writeObject(datas);
            o.flush();
            ObjectInputStream i = new ObjectInputStream(socket.getInputStream());
            Datas pass = (Datas)i.readObject();
            if(pass.getCommands()==Commands.SUCCES){
                System.out.println("Odzyskane haslo: "+pass.getPassword());
                return true;
            }
        }catch (IOException | ClassNotFoundException e) {
            e.getMessage();
        }
        return false;
    }

    public HashMap<String,String> search(){
        Scanner scanner = new Scanner(System.in);
        HashMap<String,String> hashMap = new HashMap<String, String>();
        System.out.println("Podaj fraze: ");
        String text = scanner.nextLine();
        try {
            ObjectOutputStream o = new ObjectOutputStream(socket.getOutputStream());
            Phrase phrase = new Phrase(text);
            Datas datas = new Datas(Commands.SEARCH, phrase);
            o.writeObject(datas);
            o.flush();
            ObjectInputStream i = new ObjectInputStream(socket.getInputStream());
            hashMap = (HashMap<String,String>)i.readObject();

        }catch (IOException | ClassNotFoundException e) {
            e.getMessage();
        }
        return hashMap;
    }


    public void logout() {
        try {
            ObjectOutputStream o = new ObjectOutputStream(socket.getOutputStream());
            Datas datas = new Datas(Commands.LOGOUT);
            o.writeObject(datas);
            o.flush();
            ObjectInputStream i = new ObjectInputStream(socket.getInputStream());
            Commands commands1 = (Commands)i.readObject();
            if(commands1==Commands.SUCCES){
                System.out.println("Wylogowano!");
            }else{
                System.out.println("Nie udalo sie wylogowac!");
            }
        }catch (IOException | ClassNotFoundException e) {
            e.getMessage();
        }
    }
}
