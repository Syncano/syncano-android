package com.syncano.android.lib;

public class Syncano {

    private HttpClient httpClient;

    public Syncano(String apiKey) {
        httpClient = new HttpClient(apiKey);
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }
}
