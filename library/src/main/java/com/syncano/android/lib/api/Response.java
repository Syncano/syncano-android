package com.syncano.android.lib.api;

public class Response <T> {
    /**
     * status code when response is ok
     */
    public final static int CODE_SUCCESS = 200;

    /**
     * result code for last request
     */
    private Integer resultCode;

    /**
     * result for last request
     */
    private T data;

    /**
     * error when something went wrong
     */
    private String error;

    /**
     * @return error for last request
     */
    public String getError() {
        return error;
    }

    /**
     * @return result for last request
     */
    public T getData() {
        return data;
    }

    /**
     * @return result code
     */
    public Integer getResultCode() {
        return resultCode;
    }

    /**
     * @param data result of last request
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * @param resultCode result code of last request
     */
    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    /**
     * @param error error if occurred for last request
     */
    public void setError(String error) {
        this.error = error;
    }
}