package com.syncano.android.lib.api;

public class Response <T> {

    /**
     * Everything is ok!
     */
    public final static int HTTP_CODE_SUCCESS = 200;

    /**
     * The request was successful and a resource was created.
     */
    public final static int HTTP_CODE_CREATED = 201;

    /**
     * The request was successful but there is no data to
     * return (usually after successful DELETE request).
     */
    public final static int HTTP_CODE_NO_CONTENT = 204;

    /**
     * There was no new data to return.
     */
    public final static int HTTP_CODE_NOT_MODIFIED = 304;

    /**
     * The request was invalid or cannot be otherwise served.
     * An accompanying error message will explain further.
     */
    public final static int HTTP_CODE_BAD_REUEST = 400;

    /**
     * Authentication credentials were missing or incorrect.
     */
    public final static int HTTP_CODE_UNAUTHORIZED = 401;

    /**
     * The request is understood, but it has been refused or
     * access is not allowed. An accompanying error message will explain why.
     */
    public final static int HTTP_CODE_FORBIDDEN = 403;

    /**
     * The URI requested is invalid or the resource requested, such as a user, does not exists.
     * Also returned when the requested format is not supported by the requested method.
     */
    public final static int HTTP_CODE_NOT_FOUND = 404;

    /**
     * Requested method is not supported for this resource.
     */
    public final static int HTTP_CODE_METHOD_NOT_ALLOWED = 405;

    /**
     * Something is broken. Please contact Syncano support.
     */
    public final static int HTTP_CODE_INTERNAL_SERVER_ERROR = 500;

    /**
     * Syncano is down. Contact support.
     */
    public final static int HTTP_CODE_BAD_GATEWAY = 502;

    /**
     * Status code when response is ok.
     */
    public final static int CODE_SUCCESS = 0;

    /**
     * Status code when Http error appeared.
     */
    public final static int CODE_HTTP_ERROR = 1;

    /**
     * Client Protocol Exception
     */
    public final static int CODE_CLIENT_PROTOCOL_EXCEPTION = 2;

    /**
     * Illegal State Exception.
     */
    public final static int CODE_ILLEGAL_STATE_EXCEPTION = 3;

    /**
     * IOException.
     */
    public final static int CODE_ILLEGAL_IO_EXCEPTION = 4;

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
    private String httpReasonPhrase;

    /**
     * Result from server.
     */
    private T data;

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

    public String getHttpReasonPhrase() {
        return httpReasonPhrase;
    }

    public void setHttpReasonPhrase(String httpReasonPhrase) {
        this.httpReasonPhrase = httpReasonPhrase;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
