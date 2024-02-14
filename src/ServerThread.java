import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class ServerThread extends Thread{
    private Socket socket;
    private boolean islogin=false;

    public ServerThread(Socket sc){
        this.socket=sc;
    }

    public void run(){
        try{
            while(true){
                ObjectInputStream i = new ObjectInputStream(this.socket.getInputStream());
                Datas datas = (Datas)i.readObject();
                if(datas.getCommands()==Commands.LOGIN){
                    ObjectOutputStream o = new ObjectOutputStream(socket.getOutputStream());
                    ReadXml readXml= new ReadXml();
                    if(readXml.compareLP("users.xml",datas.getLogin(), datas.getPassword())){
                        System.out.println("Uzytkowanik zalogowany!");
                        Commands commands = Commands.SUCCES;
                        o.writeObject(commands);
                        o.flush();
                        islogin=true;
                    }else{
                        Commands commands = Commands.NOSUCCES;
                        o.writeObject(commands);
                        o.flush();
                    }

                }
                if(datas.getCommands()==Commands.REGISTER){
                    ObjectOutputStream o = new ObjectOutputStream(socket.getOutputStream());
                    WriteXml writeXml= new WriteXml();
                    if(writeXml.register("users.xml",datas.getLogin(), datas.getPassword())){
                        System.out.println("Uzytkowanik zarejestrowany!");
                        Commands commands = Commands.SUCCES;
                        o.writeObject(commands);
                        o.flush();
                    }else{
                        Commands commands = Commands.NOSUCCES;
                        o.writeObject(commands);
                        o.flush();
                    }

                }
                if(datas.getCommands()==Commands.REMIND){
                    ObjectOutputStream o = new ObjectOutputStream(socket.getOutputStream());
                    ReadXml readXml= new ReadXml();
                    String password = readXml.getPassword("users.xml",datas.getLogin());
                    Datas passwordData = new Datas(Commands.SUCCES,datas.getLogin(),password);
                    o.writeObject(passwordData);
                    o.flush();
                }
                if(datas.getCommands()==Commands.SEARCH){
                    SearchPhrase sp=new SearchPhrase();
                    HashMap<String,String> hashNames =sp.searchPhrase("books.xml",datas.getPhrase().getPhrase());
                    ObjectOutputStream o = new ObjectOutputStream(socket.getOutputStream());
                    o.writeObject(hashNames);
                    o.flush();
                }
                if(datas.getCommands()==Commands.LOGOUT){
                    islogin=false;
                    ObjectOutputStream o = new ObjectOutputStream(socket.getOutputStream());
                    Commands commands = Commands.SUCCES;
                    o.writeObject(commands);
                    o.flush();
                    System.out.println("Uzytkownik wylogowany!");
                }

            }
        }catch(IOException | ClassNotFoundException e){
            e.getMessage();
        }
    }

}
