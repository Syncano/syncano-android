package com.syncano.android.lib.modules.collections;

import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.objects.Collection;

/**
 * Response with one collection
 */
public class ResponseCollectionGetOne extends Response {
	/** collection */
	private Collection collection;

	/**
	 * @return requested collection
	 */
	public Collection getCollection() {
		return collection;
	}

	/**
	 * Sets requested collection
	 * 
	 * @param collection
	 */
	public void setCollection(Collection collection) {
		this.collection = collection;
	}
}