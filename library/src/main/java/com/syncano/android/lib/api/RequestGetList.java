package com.syncano.android.lib.api;


import com.google.gson.JsonElement;
import com.syncano.android.lib.Syncano;
import com.syncano.android.lib.data.PageInternal;


import java.util.ArrayList;
import java.util.List;

public class RequestGetList<T> extends RequestGet<List<T>> {

    protected Class<T> resultType;

    public RequestGetList(Class<T> resultType, String url, Syncano syncano) {
        super(url, syncano);
        this.resultType = resultType;
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
