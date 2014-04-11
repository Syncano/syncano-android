package com.syncano.android.lib;

public class Constants {
	/** production/dev server switch */
	private final static boolean isDevelopment = false;

	/** production server url */
	private final static String PRODUCTION_URL = "syncano.com";
	/** development server url */
	private final static String DEVELOPMENT_URL = "hydraengine.com";

	/**
	 * @return server url depending on development switch
	 */
	private static String getServerUrl() {
		if (isDevelopment) {
			return DEVELOPMENT_URL;
		} else {
			return PRODUCTION_URL;
		}
	}

	/** name of the server to connect */
	public final static String SERVER_NAME = getServerUrl() + "/api/jsonrpc";
	/** default timezone for api */
	public final static String HYDRA_TIMEZONE = "UTC";
	/** address of socket api */
	public final static String SOCKET_ADDRESS = "api." + getServerUrl();
	/** port of socket api */
	public final static int SOCKET_PORT = 8200;

}
