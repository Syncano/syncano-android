package com.syncano.android.lib;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.syncano.android.lib.BuildConfig;
import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.utils.DownloadTool;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class SyncanoBase {

	private final static String LOG_TAG = SyncanoBase.class.getSimpleName();

	private Context mContext;
	/** Subdomain name string */
	private String mInstanceSubdomain;
	/** Api key string */
	private String mApiKey;
	/** User authorization key */
	private String authKey;

	private Gson mGson;

	/** Max number of retries when something goes wrong */
	private static final int NO_OF_TRIES = 3;

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
		mGson = GsonHelper.createGson();
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
		setAuthKey(params);
		JSONRPCRequest req = new JSONRPCRequest(params);

		String json = mGson.toJson(req);
		return sendRequest(getUrl(), json, params.instantiateResponse());
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
		JSONRPCRequest[] requests = new JSONRPCRequest[params.length];
		for (int i = 0; i < params.length; i++) {
			setApiKey(params[i]);
			setAuthKey(params[i]);
			responses[i] = params[i].instantiateResponse();
			requests[i] = new JSONRPCRequest(params[i], Integer.toString(i));
		}

		return sendBatchRequest(getUrl(), mGson.toJson(requests), responses);
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
	 * Sets authorization key for specified params.
	 * If Params already contains value, it will not be replaced.
	 * 
	 * @param params
	 *            parameters that will be have auth key
	 */
	private void setAuthKey(Params params) {
		if (params.getAuthKey() == null) {
			params.setAuthKey(authKey);
		}
	}

	/**
	 * @return User authorization key
	 */
	public String getAuthKey() {
		return authKey;
	}

	/**
	 * To use most of API methods for User API key, auth_key is required for every request parameters. Setting
	 * authorization key here will cause adding it automatically to every parameters just before request is sent. To get
	 * authorization key, use user.login method.
	 * 
	 * @param User
	 *            authorization key
	 */
	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	/**
	 * Sends asynchronous request
	 * 
	 * @param params
	 *            array of parameters
	 * @param listener
	 *            listener that listens for response
	 */
	public void sendAsyncRequest(final Params params, final Callback listener) {
		(new AsyncTask<Void, Void, Response>() {
			@Override
			protected Response doInBackground(Void... p) {
				Response response = sendRequest(params);
				listener.finishedWorkerThread(response);
				return response;
			}

			@Override
			protected void onPostExecute(Response result) {
				listener.finished(result);
			}
		}).execute();
	}

	/**
	 * Sends asynchronous batch request
	 */
	public void sendAsyncBatchRequests(final Params[] params, final BatchCallback listener) {
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
	 * Sends request
	 * 
	 * @param url
	 *            source url
	 * @param data
	 *            data to send
	 * @param result
	 *            result that will be filled with response data
	 */
	private Response sendRequest(String url, String data, Response result) {
		if (BuildConfig.DEBUG) {
			Log.d(LOG_TAG, "Sending: " + data);
		}
		if (!DownloadTool.connectionAvailable(mContext)) {
			result.setResultCode(Response.CODE_ERROR_CONNECTION_OFF);
			Log.e(LOG_TAG, "RequestSender, Connection off");
			return result;
		}
		// download
		byte[] response = DownloadTool.download(mContext, url, null, data, NO_OF_TRIES, 0);
		if (response == null) {
			Log.e(LOG_TAG, "RequestSender, Connection error");
			result.setResultCode(Response.CODE_ERROR_DOWNLOADED_DATA_NULL);
			return result;
		}
		// parse

		String s = new String(response);
		if (BuildConfig.DEBUG) {
			Log.d(LOG_TAG, "Received: " + s);
		}

		JSONRPCResponse<Response> jsonres = new JSONRPCResponse<Response>();
		jsonres.result = result;
		try {
			jsonres = mGson.fromJson(s, JSONRPCResponse.getTypeForParser(result.getClass()));
		} catch (JsonSyntaxException e) {
			jsonres.result.setResultCode(Response.CODE_ERROR_PARSER);
		}

		jsonres.result.refreshResultCode();
		return jsonres.result;
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
	private Response[] sendBatchRequest(String url, String data, Response[] responses) {
		if (!DownloadTool.connectionAvailable(mContext)) {
			setResponseCode(responses, Response.CODE_ERROR_CONNECTION_OFF);
			Log.e(LOG_TAG, "RequestSender, Connection off");
			return responses;
		}
		// download
		byte[] response = DownloadTool.download(mContext, url, null, data, NO_OF_TRIES, 0);
		if (response == null) {
			Log.e(LOG_TAG, "RequestSender, Connection error");
			setResponseCode(responses, Response.CODE_ERROR_DOWNLOADED_DATA_NULL);
			return responses;
		}
		// parse
		String s = new String(response);
		JsonParser parser = new JsonParser();
		JsonArray jArray = parser.parse(s).getAsJsonArray();

		HashMap<String, Response> map = new HashMap<String, Response>(responses.length);
		for (int i = 0; i < responses.length; i++) {
			JSONRPCResponse<Response> jsonres = mGson.fromJson(jArray.get(i),
					JSONRPCResponse.getTypeForParser(responses[i].getClass()));
			map.put(jsonres.id, jsonres.result);
		}

		for (int i = 0; i < responses.length; i++) {
			responses[i] = map.get(Integer.toString(i));
			responses[i].refreshResultCode();
		}

		return responses;
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
	
	/**
	 * Callback for asynchronous image download.
	 */
	public abstract static class GetImageCallback {
		/** Method called on UI Thread. */
		public abstract void finished(Bitmap bitmap);

		public void finishedWorkerThread(Bitmap bitmap) {
		}
	} 

	/**
	 * Download images from given URL. Usually used for downloading images from Data or User Avatars.
	 * 
	 * @param url
	 *            Image URL
	 * @return Null if failed to download
	 */
	public Bitmap getImage(String url) {
		URL urlAdress;
		try {
			urlAdress = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
		return DownloadTool.downloadImage(mContext, urlAdress);
	}

	/**
	 * Start getImage method in AsyncTask. When download finishes, listener will be notified.
	 * 
	 * @param url
	 *            Image URL
	 * @param listener
	 *            Callback notifying about download finish
	 */
	public void getImageAsync(final String url, final GetImageCallback listener) {
		(new AsyncTask<Void, Void, Bitmap>() {
			@Override
			protected Bitmap doInBackground(Void... p) {
				Bitmap response = getImage(url);
				listener.finishedWorkerThread(response);
				return response;
			}

			@Override
			protected void onPostExecute(Bitmap result) {
				listener.finished(result);
			}
		}).execute();
	}
}
