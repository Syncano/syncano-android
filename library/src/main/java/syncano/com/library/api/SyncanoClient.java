package syncano.com.library.api;

import syncano.com.library.HttpClient;

public class SyncanoClient
{
    protected HttpClient httpClient;

    public SyncanoClient(HttpClient httpClient)
    {
        this.httpClient = httpClient;
    }
}
