package com.syncano.android.lib.api;

public class SyncanoException extends Exception {
    /**
     * Status code when response is ok.
     */
    public final static int HTTP_CODE_SUCCESS = 200;

    /**
     * Status code when response is ok.
     */
    public final static int CODE_SUCCESS = 0;

    /**
     * Status code when Http error appeared.
     */
    public final static int CODE_HTTP_ERROR = 1;

    /**
     * Internal results code.
     */
    private Integer resultCode;

    /**
     * Http results code.
     */
    private Integer httpResultCode;

    /**
     * Error when something went wrong.
     */
    private String error;

    /**
     * Error when something went wrong.
     */
    private String httpError;

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    public Integer getHttpResultCode() {
        return httpResultCode;
    }

    public void setHttpResultCode(Integer httpResultCode) {
        this.httpResultCode = httpResultCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getHttpError() {
        return httpError;
    }

    public void setHttpError(String httpError) {
        this.httpError = httpError;
    }
}
