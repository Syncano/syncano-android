package com.syncano.library.api;

public class RequestCount extends Request<Integer> {
    private RequestGetList initialRequest;

    public RequestCount(RequestGetList initialRequest) {
        super(initialRequest.syncano);
        this.initialRequest = initialRequest;
    }

    @Override
    public Response<Integer> send() {
        decorateGetListRequest();
        ResponseGetList responseGetList = initialRequest.send();
        return repackGetResponse(responseGetList);
    }

    private Response<Integer> repackGetResponse(ResponseGetList response) {
        Response<Integer> countResponse = new Response<>();
        countResponse.setData(response.getEstimatedCount());
        countResponse.setResultCode(response.getResultCode());
        countResponse.setHttpResultCode(response.getHttpResultCode());
        countResponse.setError(response.getError());
        return countResponse;
    }

    private void decorateGetListRequest() {
        initialRequest.setLimit(0);
        initialRequest.estimateCount();
    }
}
