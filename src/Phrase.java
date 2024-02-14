import java.io.Serializable;

public class Phrase implements Serializable {
    private String phrase;

    public Phrase(String phrase){
        this.phrase=phrase;
    }


    public String getPhrase() {
        return phrase;
    }
}
