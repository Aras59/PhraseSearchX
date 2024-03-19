package model.search;

import java.io.Serializable;
import java.util.HashMap;

public class SearchResponse implements Serializable {
    private int code;
    private HashMap results;

    public SearchResponse(int code, HashMap results) {
        this.code = code;
        this.results = results;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public HashMap getResults() {
        return results;
    }

    public void setResults(HashMap results) {
        this.results = results;
    }
}
