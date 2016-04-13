package com.syncano.library.api;

import com.syncano.library.Constants;
import com.syncano.library.utils.SyncanoHttpClient;

public class RequestTemplate extends HttpRequest<String> {

    private RequestGet requestGet;

    public RequestTemplate(RequestGet requestGet, String templateName) {
        super(requestGet.getUrlPath(), requestGet.syncano);
        this.requestGet = requestGet;
        requestGet.addUrlParam(Constants.URL_PARAM_TEMPLATE, templateName);
        addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
    }

    @Override
    public String getRequestMethod() {
        return SyncanoHttpClient.METHOD_GET;
    }

    @Override
    public String parseResult(Response<String> response, String value) {
        return value;
    }

    @Override
    public String getUrl() {
        return requestGet.getUrl();
    }
}
