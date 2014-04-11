package com.syncano.android.lib.syncserver;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.syncano.android.lib.BuildConfig;
import com.syncano.android.lib.RequestCreator;
import com.syncano.android.lib.ResponseReader;
import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.objects.Data;
import com.syncano.android.lib.syncserver.SocketConnection.DataListener;

import java.util.ArrayList;
import java.util.Iterator;

public class SyncServerConnection implements DataListener {

	private final static String LOG_TAG = SyncServerConnection.class.getSimpleName();

	private final static int STATE_NOT_CONNECTED = 0;
	private final static int STATE_CONNECTING = 1;
	private final static int STATE_CONNECTED = 2;

	private static final String OPT_RESULT = "result";
	private static final String OPT_TYPE = "type";
	private static final String OPT_OBJECT = "object";
	private static final String OPT_UUID = "uuid";
	private static final String AUTH = "auth";
	private static final String METHOD = "method";
	private static final String OK = "OK";
	private static final String PARAMS = "params";
	private static final String MESSAGE_ID = "message_id";

	private static final String TYPE_CALLRESPONSE = "callresponse";
	private static final String TYPE_MESSAGE = "message";
	private static final String TYPE_NEW = "new";
	private static final String TYPE_CHANGE = "change";
	private static final String TYPE_DELETE = "delete";
	private static final String TYPE_REPLACE = "replace";
	private static final String TYPE_ADD = "add";
	private static final String OBJECT_TYPE_DATA = "data";
	private static final String ARRAY_ID = "id";

	/** Sync server listener */
	private SyncServerListener mListener;
	/** Subscription listener */
	private SubscriptionListener mSubscriptionListener;
	/** Subdomain for desired instance */
	private String mInstanceSubdomain;
	/** Current socket connection */
	private SocketConnection mConnection;
	/** Api key */
	private String mApiKey;
	/** Uuid */
	private String mUuid;
	/** Context from application */
	private Context mContext;
	/** Current call id */
	private int mCurrentCallID;
	/** Current state */
	private int mState = STATE_NOT_CONNECTED;
	/** Array of waiting calls */
	private SparseArray<CallItem> waitingCalls = new SparseArray<CallItem>(5);

	/**
	 * Default constructor
	 * 
	 * @param context
	 *            context from application
	 * @param instanceSubdomain
	 *            subdomain name
	 * @param login
	 *            login to api
	 * @param password
	 *            password to api
	 * @param listener
	 *            server listener
	 */
	public SyncServerConnection(Context context, String instanceSubdomain, String apiKey, SyncServerListener listener) {
		mListener = listener;
		mApiKey = apiKey;
		mInstanceSubdomain = instanceSubdomain;
		mContext = context;
	}

	/**
	 * Constructor with subscription listener added
	 * 
	 * @param context
	 *            context from application
	 * @param instanceSubdomain
	 *            subdomain name
	 * @param login
	 *            login to api
	 * @param password
	 *            password to api
	 * @param sslistener
	 *            server listener
	 * @param subscriptionListener
	 *            subscription listener
	 */
	public SyncServerConnection(Context context, String instanceSubdomain, String apiKey,
			SyncServerListener sslistener, SubscriptionListener subscriptionListener) {
		this(context, instanceSubdomain, apiKey, sslistener);
		setSubscriptionListener(subscriptionListener);
	}

	/**
	 * Method sets new subscription listener
	 * 
	 * @param subscriptionListener
	 */
	public void setSubscriptionListener(SubscriptionListener subscriptionListener) {
		this.mSubscriptionListener = subscriptionListener;
	}

	/**
	 * Method starts new connection
	 */
	public void start() {
		mCurrentCallID = 0;
		mState = STATE_CONNECTING;
		mConnection = new SocketConnection(mContext, this);
		mConnection.start();
	}

	/**
	 * Method stops connection
	 */
	public void stop() {
		mConnection.stop();
	}

	/**
	 * Method that passes message to different method depending on state of connection
	 */
	public void message(String data) {
		if (BuildConfig.DEBUG) {
			Log.d(LOG_TAG, "State: " + mState + " Message: " + data);
		}
		switch (mState) {
			case STATE_CONNECTING:
				messageReceivedConnecting(data);
				break;
			case STATE_CONNECTED:
				messageReceivedConnected(data);
				break;
			case STATE_NOT_CONNECTED:
				Log.e(LOG_TAG, "Miracle. You received message when disconnected.");
				break;
		}
	}

	/**
	 * Notifies listener if server succesfully connected or not
	 * 
	 * @param data
	 *            data with additional information
	 */
	private void messageReceivedConnecting(String data) {
		if (data == null) {
			mState = STATE_NOT_CONNECTED;
			mListener.error("Null received from server for login request");
			mListener.disconnected();
			return;
		}
		try {
			JSONObject json = new JSONObject(data);
			if (AUTH.equals(json.optString(OPT_TYPE)) && OK.equals(json.optString(OPT_RESULT))) {
				mUuid = json.optString(OPT_UUID);
				mState = STATE_CONNECTED;
				mListener.connected();
				return;
			}
		} catch (JSONException e) {
			Log.e(LOG_TAG, "Error while creating json: " + e.toString());
		}
		mState = STATE_NOT_CONNECTED;
		mListener.error("For login request server returned: " + data);
		mListener.disconnected();
	}

	/**
	 * @return state of socket connection, true if connected
	 */
	public boolean isConnected() {
		if (mState == STATE_CONNECTED) {
			return true;
		}
		return false;
	}

	/**
	 * Method that notifies listener on message recieved and connected
	 * 
	 * @param data
	 *            data with additional info
	 */
	private void messageReceivedConnected(String data) {
		if (data == null) {
			mListener.error("Null message received");
			return;
		}

		try {
			JSONObject json = new JSONObject(data);
			String type = json.optString(OPT_TYPE);
			String objectType = json.optString(OPT_OBJECT);
			if (type == null || objectType == null) {
				mListener.error("Message type or object null. " + data);
				return;
			}
			if (type.equals(TYPE_CALLRESPONSE)) {
				messageCallResponse(json);
				return;
			}

			if (type.equals(TYPE_MESSAGE)) {
				messageMessage(json);
				return;
			}

			if (mSubscriptionListener == null) {
				Log.d(LOG_TAG, "Message received but no listener interested");
				return;
			}

			if (type.equals(TYPE_NEW) && objectType.equals(OBJECT_TYPE_DATA)) {
				messageNewData(json);
			} else if (type.equals(TYPE_CHANGE) && objectType.equals(OBJECT_TYPE_DATA)) {
				messageChangedData(json);
			} else if (type.equals(TYPE_DELETE) && objectType.equals(OBJECT_TYPE_DATA)) {
				messageDeletedData(json);
			}
			return;
		} catch (JSONException e) {
			Log.e(LOG_TAG, "Error while creating json: " + e.toString());
		}
		mListener.error("Json exception for data: " + data);
	}

	/**
	 * Method that calls response on message new data and notifies callback
	 * 
	 * @param json
	 *            json with response
	 */
	private void messageNewData(JSONObject json) {
		try {
			Data data = new Data();
			ResponseReader.parseFields(data, json.optJSONObject(OBJECT_TYPE_DATA));
			mSubscriptionListener.added(data);
		} catch (Exception e) {
			mListener.error("Error parsing message about new data object");
		}
	}

	/**
	 * Method that calls response on message deleted data and notifies callback
	 * 
	 * @param json
	 *            json with response
	 */
	private void messageDeletedData(JSONObject json) {
		try {
			JSONArray jsonIds = json.getJSONArray(ARRAY_ID);
			int[] ids = new int[jsonIds.length()];
			for (int i = 0; i < ids.length; i++) {
				ids[i] = jsonIds.getInt(i);
			}
			mSubscriptionListener.deleted(ids);
		} catch (JSONException e) {
			Log.e(LOG_TAG, "Received message about data removed but can't parse list of ids: " + json.toString());
			mListener.error("Error parsing message about deleted data objects");
		}
	}

	/**
	 * Method that calls response on message changed data and notifies callback
	 * 
	 * @param json
	 *            json with response
	 */
	@SuppressWarnings("unchecked")
	private void messageChangedData(JSONObject json) {
		final DataChanges data = new DataChanges();

		JSONObject j;
		Iterator<String> keys;
		ArrayList<String> ids = new ArrayList<String>();

		// get ids of changed objects
		try {
			JSONObject target = json.getJSONObject("target");
			JSONArray idsArray = target.getJSONArray(ARRAY_ID);
			for (int i = 0; i < idsArray.length(); i++) {
				String id = idsArray.getString(i);
				ids.add(id);
			}

		} catch (JSONException e) {
			Log.e(LOG_TAG, "Information about changed data objects ids corrupted");
		}
		if (ids.size() == 0) return;

		// start reading info about changes
		j = json.optJSONObject(TYPE_REPLACE);
		if (j != null) {
			keys = j.keys();
			while (keys.hasNext()) {
				String key = keys.next();
				data.setReplacedValue(key, j);
			}
		}

		j = json.optJSONObject(TYPE_DELETE);
		if (j != null) {
			keys = j.keys();
			while (keys.hasNext()) {
				String key = keys.next();
				data.setDeletedValue(key);
			}
		}

		j = json.optJSONObject(TYPE_ADD);
		if (j != null) {
			keys = j.keys();
			while (keys.hasNext()) {
				String key = keys.next();
				data.setAddedValue(key, j);
			}
		}

		JSONObject jAdditionals = json.optJSONObject("additional");
		if (jAdditionals != null) {
			j = jAdditionals.optJSONObject(TYPE_ADD);
			if (j != null) {
				keys = j.keys();
				while (keys.hasNext()) {
					String key = keys.next();
					data.setAddedAdditionalValue(key, j.optString(key));
				}
			}

			j = jAdditionals.optJSONObject(TYPE_REPLACE);
			if (j != null) {
				keys = j.keys();
				while (keys.hasNext()) {
					String key = keys.next();
					data.setReplacedAdditionalValue(key, j.optString(key));
				}
			}

			JSONArray jarr = jAdditionals.optJSONArray(TYPE_DELETE);
			if (jarr != null) {
				for (int i = 0; i < jarr.length(); i++) {
					data.setDeletedAdditionalValue(jarr.optString(i));
				}
			}
		}

		ArrayList<DataChanges> changesList = new ArrayList<DataChanges>();
		for (String id : ids) {
			if (id == null) {
				Log.e(LOG_TAG, "Id null in the ids list");
				continue;
			}
			DataChanges item = data.clone();
			item.setId(id);
			changesList.add(item);
		}
		mSubscriptionListener.changed(changesList);
	}

	/**
	 * Method that calls response on message and notifies callback
	 * 
	 * @param json
	 *            json with response
	 */
	private void messageCallResponse(JSONObject json) {
		int id = json.optInt(MESSAGE_ID, -1);
		CallItem call = waitingCalls.get(id);
		if (call == null) {
			mListener.error("Received message for call that wasn't send: " + json.toString());
			return;
		}
		ResponseReader.parseResponse(call.response, json.optJSONObject(OBJECT_TYPE_DATA));
		call.response.setResult(json.optString(OPT_RESULT));
		ResponseReader.setResultCode(call.response);
		call.callback.result(call.response);
		waitingCalls.remove(id);
	}

	/**
	 * Method that notifies listener about new message
	 * 
	 * @param json
	 *            json with new message
	 */
	private void messageMessage(JSONObject json) {
		mListener.message(json.optString(OPT_OBJECT), json.optString(OBJECT_TYPE_DATA));
	}

	/**
	 * Method that is called after disconnecting from socket
	 */
	public void disconnected() {
		mState = STATE_NOT_CONNECTED;
		mListener.disconnected();
	}

	/**
	 * Method that is called after connecting to socket
	 */
	public void connected() {
		try {
			JSONObject json = new JSONObject();
			json.put("api_key", mApiKey);
			json.put("instance", mInstanceSubdomain);
			mConnection.send(json.toString());
		} catch (JSONException e) {
			mListener.error("Wrong login json");
			mListener.disconnected();
			mState = STATE_NOT_CONNECTED;
		}
	}

	/**
	 * Method to send params over sync server and recieve callback
	 * 
	 * @param params
	 *            parameters to send
	 * @param callback
	 *            callback that is called after server response
	 */
	public void call(Params params, SyncServerCallback callback) {
		JSONObject json = new JSONObject();
		try {
			json.put(OPT_TYPE, "call");
			json.put(METHOD, params.getMethodName());
			mCurrentCallID++;
			json.put(MESSAGE_ID, mCurrentCallID);
			RequestCreator creator = new RequestCreator(params);
			json.put(PARAMS, creator.getParamsJson());

			CallItem call = new CallItem();
			call.callback = callback;
			call.response = params.instantiateResponse();
			waitingCalls.append(mCurrentCallID, call);

			mConnection.send(json.toString());
		} catch (JSONException e) {
			mListener.error("Error sending call");
		}
	}

	/**
	 * Callback that notifies when sync server returned result
	 */
	public interface SyncServerCallback {
		public void result(Response response);
	}

	/**
	 * Listener to notify that sync server state has changed
	 */

	public interface SyncServerListener {
		public void disconnected();

		public void error(String why);

		public void connected();

		public void message(String object, String message);
	}

	/**
	 * Listener used in notifying that subscription has changed or added
	 */

	public interface SubscriptionListener {
		public void deleted(int[] ids);

		public void changed(ArrayList<DataChanges> changes);

		public void added(Data data);
	}

	/**
	 * Class that holds response and callback
	 */
	private class CallItem {
		Response response;
		SyncServerCallback callback;
	}

}
