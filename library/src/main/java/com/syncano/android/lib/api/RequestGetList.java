package com.syncano.android.lib.api;


import com.google.gson.JsonElement;
import com.syncano.android.lib.Syncano;


import java.util.ArrayList;
import java.util.List;

public class RequestGetList<T> extends ListRequest<T> {

    public RequestGetList(Class<T> resultType, String url, Syncano syncano) {
        super(resultType, url, syncano);
    }

    @Override
    public String getRequestMethod() {
        return null;
    }

    @Override
    public String prepareParams() {
        return null;
    }

    @Override
    public List<T> parseResult(String json) {

        PageInternal pageInternal = gson.fromJson(json, PageInternal.class);
        List<T> resultList = new ArrayList<>();

        if (pageInternal.getObjects() != null) {

            for (JsonElement element : pageInternal.getObjects()) {
                resultList.add(gson.fromJson(element, resultType));
            }
        }

        return resultList;
    }
}
