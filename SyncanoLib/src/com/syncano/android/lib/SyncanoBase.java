package com.syncano.android.lib;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.syncano.android.lib.BuildConfig;
import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.utils.DownloadTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;

public class SyncanoBase {

	private final static String LOG_TAG = SyncanoBase.class.getSimpleName();
	/** Context for hydra */
	private Context mContext;
	/** Subdomain name string */
	private String mInstanceSubdomain;
	/** Api key string */
	private String mApiKey;

	/** Max number of retries when something goes wrong */
	private static final int NO_OF_TRIES = 3;
	/** Response OK */
	private static final String OK = "OK";
	/** Response Not OK */
	private static final String NOK = "NOK";

	/**
	 * Default constructor
	 * 
	 * @param context
	 *            Context for hydra
	 * @param instanceSubdomain
	 *            subdomain name
	 * @param login
	 *            login to API
	 * @param pass
	 *            password to API
	 */
	protected SyncanoBase(Context context, String instanceSubdomain, String apiKey) {
		mContext = context;
		mInstanceSubdomain = instanceSubdomain;
		mApiKey = apiKey;
	}

	/**
	 * @return returns subdomain name
	 */
	public String getInstanceSubdomain() {
		return mInstanceSubdomain;
	}

	/**
	 * 
	 * @return returns url to server
	 */
	private String getUrl() {
		return "https://" + mInstanceSubdomain + '.' + Constants.SERVER_NAME;
	}

	/**
	 * Sends request to api
	 * 
	 * @param params
	 *            parameters to send
	 * @return response from server
	 */
	public Response sendRequest(Params params) {
		setApiKey(params);
		RequestCreator request = new RequestCreator(params);
		Response response = params.instantiateResponse();
		sendRequest(getUrl(), request.getPostData(), response);
		return response;
	}

	/**
	 * Sends batch request
	 * 
	 * @param params
	 *            array of parameters to send
	 * @return array of responses
	 */
	public Response[] sendBatchRequests(Params[] params) {
		Response[] responses = new Response[params.length];
		StringBuilder sb = new StringBuilder();
		sb.append("[");

		for (int i = 0; i < params.length; i++) {
			setApiKey(params[i]);
			RequestCreator request = new RequestCreator(params[i]);
			request.setID(i + "");
			sb.append(request.getPostData());
			if (i != params.length - 1) sb.append(",");
			responses[i] = params[i].instantiateResponse();
		}
		sb.append("]");

		sendBatchRequest(getUrl(), sb.toString(), responses);
		return responses;
	}

	/**
	 * Sets api key for specified params
	 * 
	 * @param params
	 *            parameters that will be have set api key
	 */
	private void setApiKey(Params params) {
		if (params.getApiKey() == null) {
			params.setApiKey(mApiKey);
		}
	}

	/**
	 * Sends asynchronous request
	 * 
	 * @param params
	 *            array of parameters
	 * @param listener
	 *            listener that listens for response
	 */
	public void sendAsyncRequest(final Params[] params, final BatchCallback listener) {
		(new AsyncTask<Void, Void, Response[]>() {
			@Override
			protected Response[] doInBackground(Void... p) {
				Response[] response = sendBatchRequests(params);
				listener.finishedWorkerThread(response);
				return response;
			}

			@Override
			protected void onPostExecute(Response[] result) {
				listener.finished(result);
			}
		}).execute();
	}

	/**
	 * Sends asynchronous batch request - will be added later
	 */
	public void sendAsyncBatchRequest(final Params params, final Callback listener) {
		// TODO ?
	}

	/**
	 * Sends request
	 * 
	 * @param url
	 *            source url
	 * @param data
	 *            data to send
	 * @param result
	 *            result that will be filled with response data
	 */
	private void sendRequest(String url, String data, Response result) {
		if (BuildConfig.DEBUG) {
			Log.d(LOG_TAG, "Sending: " + data);
		}
		if (!DownloadTool.connectionAvailable(mContext)) {
			result.setResultCode(Response.CODE_ERROR_CONNECTION_OFF);
			Log.e(LOG_TAG, "RequestSender, Connection off");
			return;
		}
		// download
		byte[] response = DownloadTool.download(mContext, url, null, data, NO_OF_TRIES, 0);
		if (response == null) {
			Log.e(LOG_TAG, "RequestSender, Connection error");
			result.setResultCode(Response.CODE_ERROR_DOWNLOADED_DATA_NULL);
			return;
		}
		// parse
		try {
			String s = new String(response);
			if (BuildConfig.DEBUG) {
				Log.d(LOG_TAG, "Received: " + s);
			}
			JSONObject jObject = new JSONObject(s);
			ResponseReader.parseResponse(result, jObject.getJSONObject("result"));
		} catch (JSONException e) {
			Log.e(LOG_TAG, "RequestSender, JSONException, Parser error\n" + e.getMessage());
			result.setResultCode(Response.CODE_ERROR_PARSER);
			return;
		}

		ResponseReader.setResultCode(result);
	}

	/**
	 * Sends batch request
	 * 
	 * @param url
	 *            source url
	 * @param data
	 *            data to send
	 * @param result
	 *            result array that will be filled with response data
	 */
	private void sendBatchRequest(String url, String data, Response[] responses) {
		if (!DownloadTool.connectionAvailable(mContext)) {
			setResponseCode(responses, Response.CODE_ERROR_CONNECTION_OFF);
			Log.e(LOG_TAG, "RequestSender, Connection off");
			return;
		}
		// download
		byte[] response = DownloadTool.download(mContext, url, null, data, NO_OF_TRIES, 0);
		if (response == null) {
			Log.e(LOG_TAG, "RequestSender, Connection error");
			setResponseCode(responses, Response.CODE_ERROR_DOWNLOADED_DATA_NULL);
			return;
		}
		// parse
		try {
			String s = new String(response);
			JSONArray jArray = new JSONArray(s);

			HashMap<String, JSONObject> map = new HashMap<String, JSONObject>(responses.length);
			for (int i = 0; i < responses.length; i++) {
				JSONObject jObject = jArray.getJSONObject(i);
				String id = jObject.optString("id");
				map.put(id, jObject.getJSONObject("result"));
			}
			for (int i = 0; i < responses.length; i++) {
				ResponseReader.parseResponse(responses[i], map.get(i + ""));
			}
		} catch (JSONException e) {
			Log.e(LOG_TAG, "RequestSender, JSONException, Parser error\n" + e.getMessage());
			setResponseCode(responses, Response.CODE_ERROR_PARSER);
			return;
		}

		for (Response result : responses) {
			if (NOK.equals(result.getResult())) {
				result.setResultCode(Response.CODE_ERROR_RECEIVED_FROM_SERVER);
			} else if (OK.equals(result.getResult())) {
				result.setResultCode(Response.CODE_SUCCESS);
			}
		}
	}

	/**
	 * Sets response code for all responses
	 * 
	 * @param responses
	 *            array of responses
	 * @param code
	 *            code that will be set for responses
	 */
	private void setResponseCode(Response[] responses, int code) {
		for (Response response : responses) {
			response.setResultCode(code);
		}
	}

	/**
	 * Callback for asynchronous requests
	 */
	public abstract static class Callback {
		public abstract void finished(Response response);

		public void finishedWorkerThread(Response response) {
		}
	}

	/**
	 * Callback for asynchronous batch requests
	 * 
	 */
	public abstract static class BatchCallback {
		public abstract void finished(Response[] response);

		public void finishedWorkerThread(Response[] response) {
		}
	}

}
