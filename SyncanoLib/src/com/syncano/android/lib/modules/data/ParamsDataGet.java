package com.syncano.android.lib.modules.data;


import java.util.Date;

import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Get data from collection(s) or whole project with optional additional filtering. All filters, unless explicitly noted
 * otherwise, affect all hierarchy levels. To paginate and to get more data, use since_id or since_time parameter.
 * collection_id/collection_key parameter means that one can use either one of them - collection_id or collection_key.
 */
public class ParamsDataGet extends Params {
	/** project id */
	private String project_id;
	/** collection id */
	private String collection_id;
	/** collection key */
	private String collection_key;
	/** data ids array */
	private String[] data_ids;
	/** state */
	private String state;
	/** folders array */
	private String[] folders;
	/** since id */
	private String since_id;
	/** since time date */
	private Date since_time;
	/** max id */
	private String max_id;
	/** limit */
	private Integer limit;
	/** order */
	private String order;
	/** order by */
	private String order_by;
	/** filter */
	private String filter;
	/** include children */
	private Boolean include_children;
	/** depth */
	private Integer depth;
	/** children limit */
	private Integer children_limit;
	/** parent ids array */
	private String[] parent_ids;
	/** by user */
	private String by_user;

	public ParamsDataGet(String projectId, String collectionId, String collectionKey) {
		setProjectId(projectId);
		setCollectionId(collectionId);
		setCollectionKey(collectionKey);
	}

	@Override
	public String getMethodName() {
		return "data.get";
	}

	public Response instantiateResponse() {
		return new ResponseDataGet();
	}

	/**
	 * @return project id
	 */
	public String getProjectId() {
		return project_id;
	}

	/**
	 * Sets project id
	 * 
	 * @param projectId
	 */
	public void setProjectId(String projectId) {
		this.project_id = projectId;
	}

	/**
	 * @return collection key
	 */
	public String getCollectionKey() {
		return collection_key;
	}

	/**
	 * Sets collection key
	 * 
	 * @param collectionKey
	 */
	public void setCollectionKey(String collectionKey) {
		this.collection_key = collectionKey;
	}

	/**
	 * @return collection id
	 */
	public String getCollectionId() {
		return collection_id;
	}

	/**
	 * Sets collection id
	 * 
	 * @param collectionId
	 */
	public void setCollectionId(String collectionId) {
		this.collection_id = collectionId;
	}

	/**
	 * @return data ids array
	 */
	public String[] getDataIds() {
		return data_ids;
	}

	/**
	 * Sets data ids array
	 * 
	 * @param dataIds
	 */
	public void setDataIds(String[] dataIds) {
		this.data_ids = dataIds;
	}

	/**
	 * @return state
	 */
	public String getState() {
		return state;
	}

	/**
	 * Sets state
	 * 
	 * @param state
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return folders array
	 */
	public String[] getFolders() {
		return folders;
	}

	/**
	 * Set folders array
	 * 
	 * @param folders
	 */
	public void setFolders(String[] folders) {
		this.folders = folders;
	}

	/**
	 * @return since id
	 */
	public String getSinceId() {
		return since_id;
	}

	/**
	 * Sets since id
	 * 
	 * @param sinceId
	 */
	public void setSinceId(String sinceId) {
		this.since_id = sinceId;
	}

	/**
	 * @return since time date
	 */
	public Date getSinceTime() {
		return since_time;
	}

	/**
	 * Sets since time date
	 * 
	 * @param sinceTime
	 */
	public void setSinceTime(Date sinceTime) {
		this.since_time = sinceTime;
	}

	/**
	 * 
	 * @return max id
	 */
	public String getMaxId() {
		return max_id;
	}

	/**
	 * Sets max id
	 * 
	 * @param maxId
	 */
	public void setMaxId(String maxId) {
		this.max_id = maxId;
	}

	/**
	 * @return limit
	 */
	public Integer getLimit() {
		return limit;
	}

	/**
	 * Sets limit
	 * 
	 * @param limit
	 */
	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	/**
	 * @return order
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * Sets order
	 * 
	 * @param order
	 */
	public void setOrder(String order) {
		this.order = order;
	}

	/**
	 * @return order by
	 */
	public String getOrderBy() {
		return order_by;
	}

	/**
	 * Sets order by
	 * 
	 * @param orderBy
	 */
	public void setOrderBy(String orderBy) {
		this.order_by = orderBy;
	}

	/**
	 * @return filter
	 */
	public String getFilter() {
		return filter;
	}

	/**
	 * Sets filter
	 * 
	 * @param filter
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * @return include children
	 */
	public Boolean getIncludeChildren() {
		return include_children;
	}

	/**
	 * Sets include children
	 * 
	 * @param includeChildren
	 */
	public void setIncludeChildren(Boolean includeChildren) {
		this.include_children = includeChildren;
	}

	/**
	 * @return depth
	 */
	public Integer getDepth() {
		return depth;
	}

	/**
	 * Sets depth
	 * 
	 * @param depth
	 */
	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	/**
	 * @return children limit
	 */
	public Integer getChildrenLimit() {
		return children_limit;
	}

	/**
	 * Sets children limit
	 * 
	 * @param childrenLimit
	 */
	public void setChildrenLimit(Integer childrenLimit) {
		this.children_limit = childrenLimit;
	}

	/**
	 * @return parent ids array
	 */
	public String[] getParentIds() {
		return parent_ids;
	}

	/**
	 * Sets parent ids array
	 * 
	 * @param parentIds
	 */
	public void setParent_ids(String[] parentIds) {
		this.parent_ids = parentIds;
	}

	/**
	 * @return by_user
	 */
	public String getByUser() {
		return by_user;
	}

	/**
	 * Sets by_user
	 * 
	 * @param byUser
	 */
	public void setByUser(String byUser) {
		this.by_user = byUser;
	}

}