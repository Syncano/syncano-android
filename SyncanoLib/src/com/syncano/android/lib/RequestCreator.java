package com.syncano.android.lib;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.utils.DateTool;


import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestCreator {
	private final static String LOG_TAG = RequestCreator.class.getSimpleName();
	/** json object with params */
	private JSONObject mParamsJson;
	/** method name used to communicate with API */
	private String mMethod;
	/** id for request */
	private String mId = "id_not_set";

	/**
	 * Default constructor
	 * 
	 * @param params
	 *            params that should be added
	 */
	public RequestCreator(Params params) {
		setParams(params);
	}

	/**
	 * Sets method for request
	 * 
	 * @param method
	 *            method name
	 */
	public void setMethod(String method) {
		this.mMethod = method;
	}

	/**
	 * Sets id
	 * 
	 * @param id
	 *            id that should be set
	 */
	protected void setID(String id) {
		this.mId = id;
	}

	/**
	 * Adds new parameter
	 * 
	 * @param name
	 *            name of parameter
	 * @param value
	 *            value of parameter
	 */
	public void addParam(String name, Object value) {
		if (name == null || value == null || name.length() <= 0) {
			Log.e(LOG_TAG, "Can't add param to reuqest: " + name + " " + value);
			return;
		}
		try {
			parseAndAddParam(name, value);
		} catch (JSONException e) {
			Log.e(LOG_TAG, "Error while parsing params: " + e.toString());
		}
	}

	/**
	 * Parses and adds parameter
	 * 
	 * @param name
	 *            name of parameter
	 * @param value
	 *            value of parameter
	 * @throws JSONException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void parseAndAddParam(String name, Object value) throws JSONException {
		// check if is array or list
		if (value instanceof Object[]) {
			Object[] arr = (Object[]) value;
			JSONArray jarr = new JSONArray();
			for (Object val : arr) {
				jarr.put(parse(val));
			}
			mParamsJson.put(name, jarr);
		} else if (value instanceof List) {
			List list = (List) value;
			JSONArray jarr = new JSONArray();
			for (Object val : list) {
				jarr.put(parse(val));
			}
			mParamsJson.put(name, jarr);
		} else if (value instanceof int[]) {
			int[] intarr = (int[]) value;
			JSONArray jarr = new JSONArray();
			for (int val : intarr) {
				jarr.put(parse(val));
			}
			mParamsJson.put(name, jarr);
		} else if (value instanceof HashMap && name.equals("otherParams")) { // check Params
			HashMap<String, String> map = (HashMap<String, String>) value;
			for (Map.Entry<String, String> entry : map.entrySet()) {
				mParamsJson.put(entry.getKey(), entry.getValue());
			}
		} else {
			mParamsJson.put(name, parse(value));

		}
	}

	/**
	 * Special parsing for some object types
	 * 
	 * @param value
	 *            object to parse
	 * @return parsed object
	 */
	private Object parse(Object value) {
		if (value instanceof Date) {
			return DateTool.parseDate((Date) value);
		}
		return value;
	}

	/**
	 * Reads all public fields of provided Object and adds its values as parameters. Name of field is used as the name.
	 */
	private void setParams(Params params) {
		if (params == null) {
			Log.e(LOG_TAG, "params object null");
			return;
		}
		mParamsJson = new JSONObject();

		setMethod(params.getMethodName());

		String paramName = null;
		Object paramValue = null;
		try {

			Field[] fieldsInherited = Params.class.getDeclaredFields();
			for (int i = 0; i < fieldsInherited.length; i++) {
				paramName = fieldsInherited[i].getName();
				fieldsInherited[i].setAccessible(true);
				paramValue = fieldsInherited[i].get(params);
				if (paramValue != null) {
					addParam(paramName, paramValue);
				}
			}
			Field[] fields = params.getClass().getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				paramName = fields[i].getName();
				fields[i].setAccessible(true);
				paramValue = fields[i].get(params);
				if (paramValue != null) {
					addParam(paramName, paramValue);
				}
			}
		} catch (IllegalArgumentException e) {
			Log.e(LOG_TAG, "Adding param " + paramName + " problem. IllegalArgumentException " + e.getMessage());
		} catch (IllegalAccessException e) {
			Log.e(LOG_TAG, "Adding param " + paramName + " problem. IllegalAccessException " + e.getMessage());
		}
	}

	/**
	 * Gets post data JSON string
	 * 
	 * @return postData JSON string
	 */
	protected String getPostData() {
		JSONObject json = new JSONObject();
		try {
			json.put("jsonrpc", "2.0");
			json.put("method", mMethod);
			json.put("id", mId);
			json.put("params", mParamsJson);
			return json.toString();
		} catch (JSONException e) {
			Log.e(LOG_TAG, "Error while adding params: " + e.toString());
		}
		return "";
	}

	/**
	 * Gets paramsJson
	 * 
	 * @return paramsJson
	 */
	public JSONObject getParamsJson() {
		return mParamsJson;
	}

}
