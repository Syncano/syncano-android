package syncano.com.library.api;

import syncano.com.library.HttpClient;

public class Request
{
    protected HttpClient httpClient;

    public Request(HttpClient httpClient)
    {
        this.httpClient = httpClient;
    }
}
