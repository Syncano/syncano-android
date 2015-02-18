package syncano.com.library.api;

import syncano.com.library.HttpClient;

public class Account extends  Request
{
    public Account(HttpClient httpClient) {
        super(httpClient);
    }

    public void getInvitations()
    {
        httpClient.sendRequest("GET", "/v1/account/invitations/");
    }
}
