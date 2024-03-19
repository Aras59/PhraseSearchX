package model.search;

import java.io.Serializable;

public class SearchRequest implements Serializable {
    private String username;
    private String searchPhrase;

    public SearchRequest(String username, String searchPhrase) {
        this.username = username;
        this.searchPhrase = searchPhrase;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSearchPhrase() {
        return searchPhrase;
    }

    public void setSearchPhrase(String searchPhrase) {
        this.searchPhrase = searchPhrase;
    }
}
