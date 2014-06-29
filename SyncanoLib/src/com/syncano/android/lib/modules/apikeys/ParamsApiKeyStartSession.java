package com.syncano.android.lib.modules.apikeys;

import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Logs in API client and returns session_id for session id or cookie-based authentication. See Authorization. Session
 * is valid for 2 hours and is automatically renewed whenever it is used.
 */
public class ParamsApiKeyStartSession extends Params {
	// timezone is already declared in Params

	@Override
	public String getMethodName() {
		return "apikey.start_session";
	}

	@Override
	public Response instantiateResponse() {
		return new ResponseApiKeyStartSession();
	}

}