package syncano.com.library;

import syncano.com.library.api.Account;

public class Syncano {

    private HttpClient httpClient;

    public Syncano(String apiKey, String authKey) {
        httpClient = new HttpClient(apiKey, authKey);
    }

    public Account account() {

        return new Account(httpClient);
    }
}
