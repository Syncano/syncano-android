package com.syncano.android.lib.modules.collections;

import com.google.gson.annotations.Expose;
import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.objects.Collection;

/**
 * Response with new collection
 */
public class ResponseCollectionNew extends Response {
	/** new collection */
	@Expose
	private Collection collection;

	/**
	 * @return new collection
	 */
	public Collection getCollection() {
		return collection;
	}

	/**
	 * Sets new collection
	 * 
	 * @param collection
	 */
	public void setCollection(Collection collection) {
		this.collection = collection;
	}
}