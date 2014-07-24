package com.syncano.android.lib.syncserver;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.syncano.android.lib.BuildConfig;
import com.syncano.android.lib.GsonHelper;
import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.objects.Data;
import com.syncano.android.lib.syncserver.SocketConnection.DataListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;

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
	private static final String TARGET = "target";

	private static final String TYPE_CALLRESPONSE = "callresponse";
	private static final String TYPE_MESSAGE = "message";
	private static final String TYPE_NEW = "new";
	private static final String TYPE_CHANGE = "change";
	private static final String TYPE_DELETE = "delete";
	private static final String TYPE_REPLACE = "replace";
	private static final String TYPE_ADD = "add";
	private static final String OBJECT_TYPE_DATA = "data";
	private static final String ARRAY_ID = "id";

	private static final int MAX_REQUESTS = 10;

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
	/** Array of sent calls */
	private SparseArray<Call> sentCalls = new SparseArray<Call>(10);

	private Queue<Call> waitingCalls = new LinkedList<Call>();

	private Gson mGson;

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
		mGson = GsonHelper.createGson();
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
     * Method sets new sync server listener
     *
     * @param syncServerListener
     */
    public void setSyncServerListener(SyncServerListener syncServerListener) {
        this.mListener = syncServerListener;
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

		JsonObject json = (new JsonParser()).parse(data).getAsJsonObject();
		if (AUTH.equals(json.get(OPT_TYPE).getAsString()) && OK.equals(json.get(OPT_RESULT).getAsString())) {
			mUuid = json.get(OPT_UUID).getAsString();
			mState = STATE_CONNECTED;
			mListener.connected();
			sendCalls();
			return;
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

		JsonObject json = (new JsonParser()).parse(data).getAsJsonObject();
		String type = json.get(OPT_TYPE).getAsString();
		String objectType = json.get(OPT_OBJECT).getAsString();
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
	}

	/**
	 * Method that calls response on message new data and notifies callback
	 * 
	 * @param json
	 *            json with response
	 */
	private void messageNewData(JsonObject json) {
		Data data = mGson.fromJson(json.get(OBJECT_TYPE_DATA), Data.class);
		mSubscriptionListener.added(data);
	}

	/**
	 * Method that calls response on message deleted data and notifies callback
	 * 
	 * @param json
	 *            json with response
	 */
	private void messageDeletedData(JsonObject json) {
		JsonObject target = json.get(TARGET).getAsJsonObject();
		JsonArray jsonIds = target.get(ARRAY_ID).getAsJsonArray();
		String[] ids = new String[jsonIds.size()];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = jsonIds.get(i).getAsString();
		}
		mSubscriptionListener.deleted(ids);
	}

	/**
	 * Method that calls response on message changed data and notifies callback
	 * 
	 * @param json
	 *            json with response
	 */
	private void messageChangedData(JsonObject json) {
		final DataChanges data = new DataChanges(mGson);

		JsonElement j;
		Iterator<Entry<String, JsonElement>> keys;
		ArrayList<String> ids = new ArrayList<String>();

		// get ids of changed objects
		JsonObject target = json.get(TARGET).getAsJsonObject();
		JsonArray idsArray = target.get(ARRAY_ID).getAsJsonArray();
		for (int i = 0; i < idsArray.size(); i++) {
			String id = idsArray.get(i).getAsString();
			ids.add(id);
		}

		if (ids.size() == 0) return;

		// start reading info about changes
		j = json.get(TYPE_REPLACE);
		if (j != null) {
			keys = j.getAsJsonObject().entrySet().iterator();
			while (keys.hasNext()) {
				Entry<String, JsonElement> entry = keys.next();
				data.setReplacedValue(entry.getKey(), entry.getValue());
			}
		}

		j = json.get(TYPE_DELETE);
		if (j != null) {
			keys = j.getAsJsonObject().entrySet().iterator();
			while (keys.hasNext()) {
				Entry<String, JsonElement> entry = keys.next();
				data.setDeletedValue(entry.getKey());
			}
		}

		j = json.get(TYPE_ADD);
		if (j != null) {
			keys = j.getAsJsonObject().entrySet().iterator();
			while (keys.hasNext()) {
				Entry<String, JsonElement> entry = keys.next();
				data.setAddedValue(entry.getKey(), entry.getValue());
			}
		}

		JsonElement jAdditionals = json.get("additional");
		if (jAdditionals != null) {
			j = jAdditionals.getAsJsonObject().get(TYPE_ADD);
			if (j != null) {
				keys = j.getAsJsonObject().entrySet().iterator();
				while (keys.hasNext()) {
					Entry<String, JsonElement> entry = keys.next();
					data.setAddedAdditionalValue(entry.getKey(), entry.getValue().getAsString());
				}
			}

			j = jAdditionals.getAsJsonObject().get(TYPE_REPLACE);
			if (j != null) {
				keys = j.getAsJsonObject().entrySet().iterator();
				while (keys.hasNext()) {
					Entry<String, JsonElement> entry = keys.next();
					data.setReplacedAdditionalValue(entry.getKey(), entry.getValue().getAsString());
				}
			}

			JsonElement jelarr = jAdditionals.getAsJsonObject().get(TYPE_DELETE);
			if (jelarr != null) {
				JsonArray jarr = jelarr.getAsJsonArray();
				for (int i = 0; i < jarr.size(); i++) {
					data.setDeletedAdditionalValue(jarr.get(i).getAsString());
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
	private void messageCallResponse(JsonObject json) {
		int id = json.get(MESSAGE_ID).getAsInt();
		Call call = sentCalls.get(id);
		if (call == null) {
			mListener.error("Received message for call that wasn't send: " + json.toString());
			return;
		}
		call.response = mGson.fromJson(json.get(OBJECT_TYPE_DATA), call.response.getClass());
		call.response.setResult(json.get(OPT_RESULT).getAsString());
		call.response.refreshResultCode();

		call.callback.result(call.response);
		sentCalls.remove(id);
		sendCalls();
	}

	/**
	 * Method that notifies listener about new message
	 * 
	 * @param json
	 *            json with new message
	 */
	private void messageMessage(JsonObject json) {
		mListener.message(json.get(OPT_OBJECT).getAsString(), json.get(OBJECT_TYPE_DATA).getAsJsonObject());
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
		JsonObject json = new JsonObject();
		json.addProperty("api_key", mApiKey);
		json.addProperty("instance", mInstanceSubdomain);
		mConnection.send(json.toString());
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
		JsonObject json = new JsonObject();
		json.addProperty(OPT_TYPE, "call");
		json.addProperty(METHOD, params.getMethodName());
		mCurrentCallID++;
		json.addProperty(MESSAGE_ID, mCurrentCallID);
		json.add(PARAMS, mGson.toJsonTree(params));

		Call call = new Call();
		call.callback = callback;
		call.data = json.toString();

		call.response = params.instantiateResponse();
		waitingCalls.add(call);

		sendCalls();
	}

	private void sendCalls() {
		if (waitingCalls.size() == 0 || mState != STATE_CONNECTED) {
			return;
		}
		while (sentCalls.size() < MAX_REQUESTS && !waitingCalls.isEmpty()) {
			Call call = waitingCalls.poll();
			sentCalls.append(mCurrentCallID, call);
			mConnection.send(call.data);
			call.data = null;
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

		public void message(String object, JsonObject message);
	}

	/**
	 * Listener used in notifying that subscription has changed or added
	 */

	public interface SubscriptionListener {
		public void deleted(String[] ids);

		public void changed(ArrayList<DataChanges> changes);

		public void added(Data data);
	}

	/**
	 * Class that holds response and callback
	 */
	private class Call {
		String data;
		Response response;
		SyncServerCallback callback;
	}
}
