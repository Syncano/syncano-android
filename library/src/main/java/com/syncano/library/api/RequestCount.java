package com.syncano.library.api;

public class RequestCount extends Request {
    private RequestGetList initialRequest;

    public RequestCount(RequestGetList initialRequest) {
        super(initialRequest.syncano);
        this.initialRequest = initialRequest;
    }

    @Override
    public Response send() {
        decorateCountRequest();
        ResponseGetList responseGetList = initialRequest.send();
        if (!responseGetList.isSuccess())
            return responseGetList;

        return createCountResponse(responseGetList.getEstimatedCount());
    }

    private Response<Integer> createCountResponse(Integer estimatedCount) {
        Response<Integer> countResponse = new Response<>();
        countResponse.setData(estimatedCount);
        countResponse.setResultCode(Response.CODE_SUCCESS);
        countResponse.setHttpResultCode(Response.HTTP_CODE_SUCCESS);
        return countResponse;
    }

    private void decorateCountRequest() {
        initialRequest.setLimit(0);
        initialRequest.estimateCount();
    }
}
