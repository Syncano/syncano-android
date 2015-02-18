package syncano.com.library;

import syncano.com.library.api.AccountSyncanoClient;

public class Syncano {

    private HttpClient httpClient;

    public Syncano(String apiKey) {
        httpClient = new HttpClient(apiKey);
    }

    public AccountSyncanoClient account() {

        return new AccountSyncanoClient(httpClient);
    }
}
