package com.syncano.android.lib;

public class Syncano {
    private static Syncano syncanoInstance = null;

    private HttpClient httpClient = null;

    private Syncano() {
        httpClient = new HttpClient();
    }

    public static Syncano getInstance() {
        if (syncanoInstance == null) {
            syncanoInstance = new Syncano();
        }
        return syncanoInstance;
    }

    public static void init(String apiKey) {
        getInstance().setApiKey(apiKey);
    }

    public void setApiKey(String apiKey) {
        httpClient.setApiKey(apiKey);
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }
}