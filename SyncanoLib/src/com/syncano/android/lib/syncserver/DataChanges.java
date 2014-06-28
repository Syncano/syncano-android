package com.syncano.android.lib.syncserver;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.BuildConfig;
import com.syncano.android.lib.objects.Data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Set;

public class DataChanges extends Data implements Serializable, Cloneable {

	private static final long serialVersionUID = 944898951645564940L;
	private final static String LOG_TAG = DataChanges.class.getSimpleName();
	/** list of all deleted data */
	private HashMap<String, Object> mDeleted = new HashMap<String, Object>();
	/** list of all replaced data */
	private HashMap<String, Object> mReplaced = new HashMap<String, Object>();
	/** list of all added data */
	private HashMap<String, Object> mAdded = new HashMap<String, Object>();

	private Gson mGson;

	/**
	 * Default constructor
	 * 
	 * @param mGson
	 */
	public DataChanges(Gson gson) {
		mGson = gson;
	}

	/**
	 * Method used to clone whole object
	 */
	@Override
	public DataChanges clone() {
		try {
			return (DataChanges) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Gets change from specified object
	 * 
	 * @param param
	 *            Object that we want to analyze
	 * @return Change result (value from Change enum)
	 */
	public Change getChange(Object param) {
		if (contains(mDeleted, param)) return Change.DELETED;
		if (contains(mReplaced, param)) return Change.REPLACED;
		if (contains(mAdded, param)) return Change.NEW;
		return Change.NO_CHANGE;
	}

	/**
	 * Checks if param had additional change
	 * 
	 * @param param
	 * @return
	 */
	public Change getAdditionalChange(Object param) {

		return Change.NO_CHANGE;
	}

	/**
	 * Gets deleted data
	 * 
	 * @return set of strings with deleted data
	 */
	public Set<String> getDeleted() {
		return mDeleted.keySet();
	}

	/**
	 * Gets replaced data
	 * 
	 * @return set of strings with replaced data
	 */
	public Set<String> getReplaced() {
		return mReplaced.keySet();
	}

	/**
	 * Gets new data
	 * 
	 * @return set of strings with new data
	 */
	public Set<String> getNew() {
		return mAdded.keySet();
	}

	/**
	 * Enum with change possibilities
	 */
	public enum Change {
		DELETED, REPLACED, NEW, NO_CHANGE
	}

	private Field findFieldForKey(String key) {
		Field[] fields = Data.class.getDeclaredFields();
		for (Field f : fields) {
			// check serialized name first
			SerializedName sn = f.getAnnotation(SerializedName.class);
			if (sn != null && key.equals(sn.value())) {
				f.setAccessible(true);
				return f;
			}

			// then check real name
			if (sn == null && key.equals(f.getName())) {
				f.setAccessible(true);
				return f;
			}
		}
		return null;
	}

	/**
	 * Sets replaced value
	 * 
	 * @param key
	 *            key for data
	 * @param json
	 *            json with data
	 */
	protected void setReplacedValue(String key, JsonElement json) {
		if (BuildConfig.DEBUG) Log.d(LOG_TAG, "setReplacedValue: " + key);
		try {
			Field field = findFieldForKey(key);
			Object value = mGson.fromJson(json, field.getType());
			field.set(this, value);
			mReplaced.put(key, field.get(this));
		} catch (Exception e) {
			Log.e(LOG_TAG, "Can't set value for parameter: " + key + ". " + e.getMessage());
		}
	}

	/**
	 * Sets deleted value
	 * 
	 * @param key
	 *            key for data
	 * @param json
	 *            json with data
	 */
	protected void setDeletedValue(String key) {
		if (BuildConfig.DEBUG) Log.d(LOG_TAG, "setDeletedValue: " + key);
		try {
			Field field = findFieldForKey(key);
			Object instance = field.getClass().newInstance();
			field.set(this, instance);
			mDeleted.put(key, instance);
		} catch (Exception e) {
			Log.e(LOG_TAG, "Can't set value for parameter: " + key + ". " + e.getMessage());
		}
	}

	/**
	 * Sets added value
	 * 
	 * @param key
	 *            key for data
	 * @param json
	 *            json with data
	 */
	public void setAddedValue(String key, JsonElement json) {
		if (BuildConfig.DEBUG) Log.d(LOG_TAG, "setAddedValue: " + key);
		try {
			Field field = findFieldForKey(key);
			Object value = mGson.fromJson(json, field.getType());
			field.set(this, value);
			mAdded.put(key, field.get(this));
		} catch (Exception e) {
			Log.e(LOG_TAG, "Can't set value for parameter: " + key + ". " + e.getMessage());
		}
	}

	/**
	 * Sets added additional value
	 * 
	 * @param key
	 *            key for data
	 * @param json
	 *            json with data
	 */
	public void setAddedAdditionalValue(String key, String value) {
		if (BuildConfig.DEBUG) Log.d(LOG_TAG, "setAddedAdditionalValue: " + key);
		if (getAdditional() == null) {
			setAdditional(new HashMap<String, String>());
		}
		getAdditional().put(key, value);
		mAdded.put(key, value);
	}

	/**
	 * Sets replaced additional value
	 * 
	 * @param key
	 *            key for data
	 * @param json
	 *            json with data
	 */
	public void setReplacedAdditionalValue(String key, String value) {
		if (BuildConfig.DEBUG) Log.d(LOG_TAG, "setReplacedAdditionalValue: " + key);
		if (getAdditional() == null) {
			setAdditional(new HashMap<String, String>());
		}
		getAdditional().put(key, value);
		mReplaced.put(key, value);
	}

	/**
	 * Sets deleted additional value
	 * 
	 * @param key
	 *            key for data
	 * @param json
	 *            json with data
	 */
	public void setDeletedAdditionalValue(String key) {
		if (BuildConfig.DEBUG) Log.d(LOG_TAG, "setDeletedAdditionalValue: " + key);
		mDeleted.put(key, "");
	}

	/**
	 * Checks if hash map contains object
	 * 
	 * @param map
	 *            map to check
	 * @param o
	 *            object to check
	 * @return returns true if map contains object o
	 */
	private boolean contains(HashMap<String, Object> map, Object o) {
		for (Object obj : map.values()) {
			if (obj == o) {
				return true;
			}
		}
		return false;
	}
}
