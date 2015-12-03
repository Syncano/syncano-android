package com.syncano.library;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.syncano.library.api.Request;

import java.util.ArrayList;

public class BatchBuilder {
    private static final int MAX_BATCH = 50;
    private ArrayList<Request> requests = new ArrayList<>();

    public BatchBuilder add(Request request) {
        requests.add(request);
        return this;
    }

    public void send() {
        if (requests.size() > MAX_BATCH) {
            throw new RuntimeException("Maximum batch size " + MAX_BATCH + " exceeded");
        }

        JsonArray array = new JsonArray();

        for (Request r : requests) {
            if (r.getCompleteCustomUrl() != null) {
                throw new RuntimeException("Can't send completely custom url request in batch");
            }

            JsonObject reqJson = new JsonObject();
            reqJson.addProperty("method", r.getRequestMethod());
            reqJson.addProperty("path", r.getUrl() + r.getUrlParams());
            //TODO reqJson.addProperty("body", );
        }

        JsonObject json = new JsonObject();
        json.add("requests", array);
    }


}
