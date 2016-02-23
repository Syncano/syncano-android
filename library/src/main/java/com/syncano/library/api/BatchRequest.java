package com.syncano.library.api;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.syncano.library.Syncano;
import com.syncano.library.data.BatchAnswer;
import com.syncano.library.parser.GsonParser;
import com.syncano.library.utils.SyncanoHttpClient;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class BatchRequest extends HttpRequest<List<BatchAnswer>> {

    private JsonElement data;
    private Gson gson;

    protected BatchRequest(String url, Syncano syncano, JsonElement data) {
        super(url, syncano);
        this.data = data;
        addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        gson = GsonParser.createGson(data);
    }

    @Override
    public String getRequestMethod() {
        return SyncanoHttpClient.METHOD_POST;
    }

    @Override
    public List<BatchAnswer> parseResult(Response<List<BatchAnswer>> response, String json) {
        return gson.fromJson(json, new TypeToken<List<BatchAnswer>>() {
        }.getType());
    }

    @Override
    public HttpEntity prepareParams() {
        if (data == null) {
            return null;
        }
        String text = gson.toJson(data);
        try {
            return new StringEntity(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
