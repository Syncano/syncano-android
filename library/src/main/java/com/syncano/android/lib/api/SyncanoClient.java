package com.syncano.android.lib.api;

import com.syncano.android.lib.HttpClient;

public class SyncanoClient
{
    protected HttpClient httpClient;

    public SyncanoClient(HttpClient httpClient)
    {
        this.httpClient = httpClient;
    }
}
