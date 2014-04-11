package com.syncano.android.lib.modules.collections;

import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.objects.Collection;

/**
 * Response to collection get, contains array of collections
 */
public class ResponseCollectionGet extends Response {
	/** array of collections */
	private Collection[] collection;

	/**
	 * @return array of collections
	 */
	public Collection[] getCollection() {
		return collection;
	}

	/**
	 * Sets array of collections
	 * 
	 * @param collection
	 */
	public void setCollection(Collection[] collection) {
		this.collection = collection;
	}
}