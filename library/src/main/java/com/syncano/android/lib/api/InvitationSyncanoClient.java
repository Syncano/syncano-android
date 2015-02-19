package com.syncano.android.lib.api;

import com.syncano.android.lib.HttpClient;
import com.syncano.android.lib.data.Account;

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
