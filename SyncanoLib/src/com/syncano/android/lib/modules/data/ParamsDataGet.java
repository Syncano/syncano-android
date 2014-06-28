package com.syncano.android.lib.modules.data;

import java.util.Date;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Get data from collection(s) or whole project with optional additional filtering. All filters, unless explicitly noted
 * otherwise, affect all hierarchy levels. To paginate and to get more data, use since_id or since_time parameter.
 * collection_id/collection_key parameter means that one can use either one of them - collection_id or collection_key.
 */
public class ParamsDataGet extends Params {
	/** project id */
	@Expose
	@SerializedName(value = "project_id")
	private String projectId;
	/** collection id */
	@Expose
	@SerializedName(value = "collection_id")
	private String collectionId;
	/** collection key */
	@Expose
	@SerializedName(value = "collection_key")
	private String collectionKey;
	/** data ids array */
	@Expose
	@SerializedName(value = "data_ids")
	private String[] dataIds;
	/** state */
	@Expose
	private String state;
	/** folders array */
	@Expose
	private String[] folders;
	/** since id */
	@Expose
	@SerializedName(value = "since_id")
	private String sinceId;
	/** since time date */
	@Expose
	@SerializedName(value = "since_time")
	private Date sinceTime;
	/** max id */
	@Expose
	@SerializedName(value = "max_id")
	private String maxId;
	/** limit */
	@Expose
	private Integer limit;
	/** order */
	@Expose
	private String order;
	/** order by */
	@Expose
	@SerializedName(value = "order_by")
	private String orderBy;
	/** filter */
	@Expose
	private String filter;
	/** include children */
	@Expose
	@SerializedName(value = "include_children")
	private Boolean includeChildren;
	/** depth */
	@Expose
	private Integer depth;
	/** children limit */
	@Expose
	@SerializedName(value = "children_limit")
	private Integer childrenLimit;
	/** parent ids array */
	@Expose
	@SerializedName(value = "parent_ids")
	private String[] parentIds;
	/** by user */
	@Expose
	@SerializedName(value = "by_user")
	private String byUser;

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
		return projectId;
	}

	/**
	 * Sets project id
	 * 
	 * @param projectId
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	/**
	 * @return collection key
	 */
	public String getCollectionKey() {
		return collectionKey;
	}

	/**
	 * Sets collection key
	 * 
	 * @param collectionKey
	 */
	public void setCollectionKey(String collectionKey) {
		this.collectionKey = collectionKey;
	}

	/**
	 * @return collection id
	 */
	public String getCollectionId() {
		return collectionId;
	}

	/**
	 * Sets collection id
	 * 
	 * @param collectionId
	 */
	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
	}

	/**
	 * @return data ids array
	 */
	public String[] getDataIds() {
		return dataIds;
	}

	/**
	 * Sets data ids array
	 * 
	 * @param dataIds
	 */
	public void setDataIds(String[] dataIds) {
		this.dataIds = dataIds;
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
		return sinceId;
	}

	/**
	 * Sets since id
	 * 
	 * @param sinceId
	 */
	public void setSinceId(String sinceId) {
		this.sinceId = sinceId;
	}

	/**
	 * @return since time date
	 */
	public Date getSinceTime() {
		return sinceTime;
	}

	/**
	 * Sets since time date
	 * 
	 * @param sinceTime
	 */
	public void setSinceTime(Date sinceTime) {
		this.sinceTime = sinceTime;
	}

	/**
	 * 
	 * @return max id
	 */
	public String getMaxId() {
		return maxId;
	}

	/**
	 * Sets max id
	 * 
	 * @param maxId
	 */
	public void setMaxId(String maxId) {
		this.maxId = maxId;
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
		return orderBy;
	}

	/**
	 * Sets order by
	 * 
	 * @param orderBy
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
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
		return includeChildren;
	}

	/**
	 * Sets include children
	 * 
	 * @param includeChildren
	 */
	public void setIncludeChildren(Boolean includeChildren) {
		this.includeChildren = includeChildren;
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
		return childrenLimit;
	}

	/**
	 * Sets children limit
	 * 
	 * @param childrenLimit
	 */
	public void setChildrenLimit(Integer childrenLimit) {
		this.childrenLimit = childrenLimit;
	}

	/**
	 * @return parent ids array
	 */
	public String[] getParentIds() {
		return parentIds;
	}

	/**
	 * Sets parent ids array
	 * 
	 * @param parentIds
	 */
	public void setParentIds(String[] parentIds) {
		this.parentIds = parentIds;
	}

	/**
	 * @return by_user
	 */
	public String getByUser() {
		return byUser;
	}

	/**
	 * Sets by_user
	 * 
	 * @param byUser
	 */
	public void setByUser(String byUser) {
		this.byUser = byUser;
	}

}