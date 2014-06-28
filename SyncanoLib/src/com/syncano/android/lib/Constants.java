package com.syncano.android.lib;

public class Constants {

	/** production server url */
	private final static String PRODUCTION_URL = "syncano.com";

	/**
	 * @return server url depending on development switch
	 */
	private static String getServerUrl() {
		return PRODUCTION_URL;
	}

	/** name of the server to connect */
	public final static String SERVER_NAME = getServerUrl() + "/api/jsonrpc";
	/** default timezone for api */
	public final static String DEFAULT_TIMEZONE = "UTC";
	/** address of socket api */
	public final static String SOCKET_ADDRESS = "api." + getServerUrl();
	/** port of socket api */
	public final static int SOCKET_PORT = 8200;

}
