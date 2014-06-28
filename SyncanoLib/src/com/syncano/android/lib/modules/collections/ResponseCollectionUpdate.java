package com.syncano.android.lib.modules.collections;

import com.google.gson.annotations.Expose;
import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.objects.Collection;

/**
 * Response for collection update request
 */
public class ResponseCollectionUpdate extends Response {
	/** updated collection */
	@Expose
	private Collection collection;

	/**
	 * @return updated collection
	 */
	public Collection getCollection() {
		return collection;
	}

	/**
	 * Sets updated collection
	 * 
	 * @param collection
	 */
	public void setCollection(Collection collection) {
		this.collection = collection;
	}
}