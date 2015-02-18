package syncano.com.library.api;

import syncano.com.library.data.Account;
import syncano.com.library.HttpClient;
import java.util.LinkedHashMap;

public class AccountSyncanoClient extends SyncanoClient
{
    public AccountSyncanoClient(HttpClient httpClient) {
        super(httpClient);
    }

    /**
     * Returns details of the currently logged user
     */
    public Account getAccount () { return httpClient.sendRequest("GET", "/v1/account/", Account.class); }

    public Account createAccount (String email, String password, String firstName, String lastName)
    {
        if (email.isEmpty() || password.isEmpty())
        {
            throw new IllegalArgumentException ();
        }

        LinkedHashMap params = new LinkedHashMap();
        params.put("email", email);
        params.put("password", password);
        params.put("first_name", firstName);
        params.put("last_name", lastName);

        return httpClient.sendRequest("POST", "/v1/account/register/", Account.class, params);
    }

}
