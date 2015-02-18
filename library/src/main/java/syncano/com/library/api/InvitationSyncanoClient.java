package syncano.com.library.api;

import syncano.com.library.HttpClient;
import syncano.com.library.data.Account;

public class InvitationSyncanoClient extends SyncanoClient
{
    public InvitationSyncanoClient(HttpClient httpClient) {
        super(httpClient);
    }

    public void getInvitations()
    {
        httpClient.sendRequest("GET", "/v1/account/invitations/", Account.class);
    }


}
