package com.syncano.android.lib;

import com.google.gson.annotations.Expose;
import com.syncano.android.lib.modules.Params;

public class JSONRPCRequest {

	public final static String DEFAULT_ID = "default_request_id";

	public JSONRPCRequest(Params p) {
		this(p, DEFAULT_ID);
	}

	public JSONRPCRequest(Params p, String id) {
		method = p.getMethodName();
		this.id = id;
		params = p;
	}

	@Expose
	String jsonrpc = "2.0";
	@Expose
	String method;
	@Expose
	String id;
	@Expose
	Params params;
}
