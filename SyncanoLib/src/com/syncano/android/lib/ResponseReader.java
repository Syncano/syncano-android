package com.syncano.android.lib;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.utils.DateTool;


import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class ResponseReader {

	private static final String LOG_TAG = ResponseReader.class.getSimpleName();

	/**
	 * Method parses json object to response
	 * 
	 * @param r
	 *            response to fill
	 * @param json
	 *            json with data
	 */
	public static void parseResponse(Response r, JSONObject json) {
		if (r == null) {
			Log.e(LOG_TAG, "Response object null");
			return;
		}
		try {
			parseFields(r, json);
		} catch (Exception e) {
			Log.e(LOG_TAG, "Error while parsing fields: " + e.toString());
		}
	}

	/**
	 * Method that parse json and inserts values into object
	 * 
	 * @param o
	 *            object that should be filled
	 * @param json
	 *            json from which data will be read
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static void parseFields(Object o, JSONObject json) throws IllegalArgumentException, IllegalAccessException,
			InstantiationException {
		Field[] fieldsNormal = o.getClass().getDeclaredFields();
		Field[] fieldsInherited = new Field[0];
		if (o instanceof Response) {
			fieldsInherited = Response.class.getDeclaredFields();
		}
		Field[] fields = new Field[fieldsInherited.length + fieldsNormal.length];
		for (int f = 0; f < fieldsInherited.length; f++) {
			fields[f] = fieldsInherited[f];
		}
		for (int g = fieldsInherited.length; g < fields.length; g++) {
			fields[g] = fieldsNormal[g - fieldsInherited.length];
		}
		for (int i = 0; i < fields.length; i++) {
			fields[i].setAccessible(true);
			if (fields[i].getType().isPrimitive()) {
				continue;
			}
			// fix for system forbidden names
			String fieldName = fields[i].getName();
			if (fieldName.startsWith("____")) {
				fieldName = fieldName.substring(4);
			}
			// start checking types
			if (fields[i].getType().isArray()) {
				JSONArray jarray = json.optJSONArray(fieldName);
				if (jarray == null) continue;
				int size = jarray.length();
				Object[] array = (Object[]) Array.newInstance(fields[i].getType().getComponentType(), size);
				for (int n = 0; n < size; n++) {
					array[n] = fields[i].getType().getComponentType().newInstance();
					parseFields(array[n], jarray.optJSONObject(n));
				}
				fields[i].set(o, array);
			} else if (jsonObjectToJava(json, fields[i], o)) {
				// object successfully parsed
			} else {
				JSONObject jsonobject = json.optJSONObject(fieldName);
				if (jsonobject == null) continue;
				Class<?> clazz = fields[i].getType();
				Object object = clazz.newInstance();
				parseFields(object, jsonobject);
				fields[i].set(o, object);
			}
		}
	}

	/**
	 * Method to fill object with data
	 * 
	 * @param json
	 *            json object from which data will be loaded
	 * @param field
	 *            Field from object that will be filled
	 * @param o
	 *            object which will be inflated with data from json
	 * @return returns true if success
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static boolean jsonObjectToJava(JSONObject json, Field field, Object o) throws IllegalArgumentException,
			IllegalAccessException {
		if (String.class.equals(field.getType())) {
			String string = json.optString(field.getName());
			field.set(o, string);
		} else if (Integer.class.equals(field.getType())) {
			Integer integer = json.optInt(field.getName());
			field.set(o, integer);
		} else if (Boolean.class.equals(field.getType())) {
			Boolean bool = json.optBoolean(field.getName());
			field.set(o, bool);
		} else if (Date.class.equals(field.getType())) {
			String dateString = json.optString(field.getName());
			field.set(o, DateTool.parseString(dateString));
		} else if (Double.class.equals(field.getType())) {
			Double doubleVal = json.optDouble(field.getName());
			field.set(o, doubleVal);
		} else if (HashMap.class.equals(field.getType())) {
			JSONObject jMap = json.optJSONObject(field.getName());
			if (jMap == null) return false;
			HashMap<String, String> map = new HashMap<String, String>();
			@SuppressWarnings("rawtypes")
			Iterator itr = jMap.keys();
			while (itr.hasNext()) {
				String key = (String) itr.next();
				map.put(key, jMap.optString(key));
			}
			field.set(o, map);
		} else {
			return false;
		}
		return true;
	}

	/**
	 * Sets result code to specified response depending on result
	 * 
	 * @param response
	 *            response on which will be set result code
	 */
	public static void setResultCode(Response response) {
		if ("NOK".equals(response.getResult())) {
			response.setResultCode(Response.CODE_ERROR_RECEIVED_FROM_SERVER);
		} else if ("OK".equals(response.getResult())) {
			response.setResultCode(Response.CODE_SUCCESS);
		}
	}
}
