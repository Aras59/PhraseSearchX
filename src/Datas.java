import java.io.Serializable;

public class Datas implements Serializable {
    private Commands commands;
    private String login;
    private String password;
    private Phrase phrase;

    public Datas(Commands com){
        this.commands=com;
    }

    public Datas(Commands com,String l, String p){
        commands=com;
        login = l;
        password = p;
    }


    public Datas(Commands com,String l){
        commands=com;
        login = l;
    }

    public Datas(Commands com,Phrase phrase){
        commands=com;
        this.phrase = phrase;
    }


    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Commands getCommands() {
        return commands;
    }

    public Phrase getPhrase() {
        return phrase;
    }
}
