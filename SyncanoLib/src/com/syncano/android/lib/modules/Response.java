package com.syncano.android.lib.modules;

public class Response {
	/** status code when response is ok */
	public final static int CODE_SUCCESS = 0;
	/** status code when connection is unavailable */
	public final static int CODE_ERROR_CONNECTION_OFF = -1;
	/** status code when something went wrong while parsing */
	public final static int CODE_ERROR_PARSER = -2;
	/** status code when no data recieved */
	public final static int CODE_ERROR_DOWNLOADED_DATA_NULL = -3;
	/** status code when server returned error */
	public final static int CODE_ERROR_RECEIVED_FROM_SERVER = -4;
	/** status code when unknown error ocurred */
	public final static int CODE_ERROR_UNKNOWN = -5;
	/** status code when bad request was sent */
	public final static int CODE_ERROR_BAD_REQUEST = -6;

	/** result code for last request */
	private Integer resultCode = CODE_ERROR_UNKNOWN;
	/** result for last request */
	private String result;
	/** error when something went wrong */
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
	public String getResult() {
		return result;
	}

	/**
	 * @return result code
	 */
	public Integer getResultCode() {
		return resultCode;
	}

	/**
	 * 
	 * @param result
	 *            result of last request
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * 
	 * @param resultCode
	 *            result code of last request
	 */
	public void setResultCode(Integer resultCode) {
		this.resultCode = resultCode;
	}

	/**
	 * 
	 * @param error
	 *            error if ocurred for last request
	 */
	public void setError(String error) {
		this.error = error;
	}
}
