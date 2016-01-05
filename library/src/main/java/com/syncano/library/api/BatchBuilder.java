package com.syncano.library.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.syncano.library.Constants;
import com.syncano.library.Syncano;
import com.syncano.library.callbacks.SyncanoCallback;
import com.syncano.library.data.BatchAnswer;

import java.util.ArrayList;
import java.util.List;

public class BatchBuilder {
    public static final int MAX_BATCH = 50;
    private ArrayList<HttpRequest> requests = new ArrayList<>();
    private Syncano syncano;

    public BatchBuilder(Syncano syncano) {
        this.syncano = syncano;
    }

    public BatchBuilder add(HttpRequest httpRequest) {
        requests.add(httpRequest);
        return this;
    }

    public BatchRequest buildRequest() {
        if (requests.size() > MAX_BATCH) {
            throw new RuntimeException("Maximum batch size " + MAX_BATCH + " exceeded");
        }
        if (requests.size() == 0) {
            throw new RuntimeException("Trying to send 0 requests in a batch");
        }

        JsonArray array = new JsonArray();
        for (HttpRequest r : requests) {
            if (r.getCompleteCustomUrl() != null) {
                throw new RuntimeException("Can't send completely custom url request in batch");
            }
            if (r.getUrlPath() == null) {
                throw new RuntimeException("Can't send request without url path in batch");
            }

            JsonObject reqJson = new JsonObject();
            reqJson.addProperty("method", r.getRequestMethod());
            reqJson.addProperty("path", r.getUrlPath() + r.getUrlParams());
            JsonElement body = r.prepareJsonParams();
            if (body != null) {
                reqJson.add("body", body);
            }

            array.add(reqJson);
        }
        JsonObject json = new JsonObject();
        json.add("requests", array);

        String url = String.format(Constants.BATCH_URL, syncano.getNotEmptyInstanceName());
        BatchRequest req = new BatchRequest(url, syncano, json);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        return req;
    }

    public Response<List<BatchAnswer>> send() {
        return buildRequest().send();
    }

    public void sendAsync(SyncanoCallback<List<BatchAnswer>> callback) {
        buildRequest().sendAsync(callback);
    }
}
