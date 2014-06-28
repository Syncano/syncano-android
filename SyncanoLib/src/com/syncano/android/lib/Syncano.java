package com.syncano.android.lib;

import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.modules.administrators.ParamsAdminDelete;
import com.syncano.android.lib.modules.administrators.ParamsAdminGet;
import com.syncano.android.lib.modules.administrators.ParamsAdminGetOne;
import com.syncano.android.lib.modules.administrators.ParamsAdminNew;
import com.syncano.android.lib.modules.administrators.ParamsAdminUpdate;
import com.syncano.android.lib.modules.administrators.ResponseAdminGet;
import com.syncano.android.lib.modules.administrators.ResponseAdminGetOne;
import com.syncano.android.lib.modules.administrators.ResponseAdminNew;
import com.syncano.android.lib.modules.administrators.ResponseAdminUpdate;
import com.syncano.android.lib.modules.apikeys.ParamsApiKeyAuthorize;
import com.syncano.android.lib.modules.apikeys.ParamsApiKeyDeauthorize;
import com.syncano.android.lib.modules.apikeys.ParamsApiKeyDelete;
import com.syncano.android.lib.modules.apikeys.ParamsApiKeyGet;
import com.syncano.android.lib.modules.apikeys.ParamsApiKeyGetOne;
import com.syncano.android.lib.modules.apikeys.ParamsApiKeyNew;
import com.syncano.android.lib.modules.apikeys.ParamsApiKeyStartSession;
import com.syncano.android.lib.modules.apikeys.ParamsApiKeyUpdateDescription;
import com.syncano.android.lib.modules.apikeys.ResponseApiKeyGet;
import com.syncano.android.lib.modules.apikeys.ResponseApiKeyGetOne;
import com.syncano.android.lib.modules.apikeys.ResponseApiKeyNew;
import com.syncano.android.lib.modules.apikeys.ResponseApiKeyStartSession;
import com.syncano.android.lib.modules.apikeys.ResponseApiKeyUpdateDescription;
import com.syncano.android.lib.modules.collections.ParamsCollectionActivate;
import com.syncano.android.lib.modules.collections.ParamsCollectionAddTag;
import com.syncano.android.lib.modules.collections.ParamsCollectionAuthorize;
import com.syncano.android.lib.modules.collections.ParamsCollectionDeactivate;
import com.syncano.android.lib.modules.collections.ParamsCollectionDeauthorize;
import com.syncano.android.lib.modules.collections.ParamsCollectionDelete;
import com.syncano.android.lib.modules.collections.ParamsCollectionDeleteTag;
import com.syncano.android.lib.modules.collections.ParamsCollectionGet;
import com.syncano.android.lib.modules.collections.ParamsCollectionGetOne;
import com.syncano.android.lib.modules.collections.ParamsCollectionNew;
import com.syncano.android.lib.modules.collections.ParamsCollectionUpdate;
import com.syncano.android.lib.modules.collections.ResponseCollectionGet;
import com.syncano.android.lib.modules.collections.ResponseCollectionGetOne;
import com.syncano.android.lib.modules.collections.ResponseCollectionNew;
import com.syncano.android.lib.modules.collections.ResponseCollectionUpdate;
import com.syncano.android.lib.modules.data.ParamsDataAddChild;
import com.syncano.android.lib.modules.data.ParamsDataAddParent;
import com.syncano.android.lib.modules.data.ParamsDataCopy;
import com.syncano.android.lib.modules.data.ParamsDataCount;
import com.syncano.android.lib.modules.data.ParamsDataDelete;
import com.syncano.android.lib.modules.data.ParamsDataGet;
import com.syncano.android.lib.modules.data.ParamsDataGetOne;
import com.syncano.android.lib.modules.data.ParamsDataMove;
import com.syncano.android.lib.modules.data.ParamsDataNew;
import com.syncano.android.lib.modules.data.ParamsDataRemoveChild;
import com.syncano.android.lib.modules.data.ParamsDataRemoveParent;
import com.syncano.android.lib.modules.data.ParamsDataUpdate;
import com.syncano.android.lib.modules.data.ResponseDataCopy;
import com.syncano.android.lib.modules.data.ResponseDataCount;
import com.syncano.android.lib.modules.data.ResponseDataGet;
import com.syncano.android.lib.modules.data.ResponseDataGetOne;
import com.syncano.android.lib.modules.data.ResponseDataNew;
import com.syncano.android.lib.modules.data.ResponseDataUpdate;
import com.syncano.android.lib.modules.folders.ParamsFolderAuthorize;
import com.syncano.android.lib.modules.folders.ParamsFolderDeauthorize;
import com.syncano.android.lib.modules.folders.ParamsFolderDelete;
import com.syncano.android.lib.modules.folders.ParamsFolderGet;
import com.syncano.android.lib.modules.folders.ParamsFolderGetOne;
import com.syncano.android.lib.modules.folders.ParamsFolderNew;
import com.syncano.android.lib.modules.folders.ParamsFolderUpdate;
import com.syncano.android.lib.modules.folders.ResponseFolderGet;
import com.syncano.android.lib.modules.folders.ResponseFolderGetOne;
import com.syncano.android.lib.modules.folders.ResponseFolderNew;
import com.syncano.android.lib.modules.identities.ParamsIdentityGet;
import com.syncano.android.lib.modules.identities.ParamsIdentityUpdate;
import com.syncano.android.lib.modules.identities.ResponseIdentityGet;
import com.syncano.android.lib.modules.identities.ResponseIdentityUpdate;
import com.syncano.android.lib.modules.notification.ParamsNotificationGetHistory;
import com.syncano.android.lib.modules.notification.ParamsNotificationSend;
import com.syncano.android.lib.modules.projects.ParamsProjectAuthorize;
import com.syncano.android.lib.modules.projects.ParamsProjectDeauthorize;
import com.syncano.android.lib.modules.projects.ParamsProjectDelete;
import com.syncano.android.lib.modules.projects.ParamsProjectGet;
import com.syncano.android.lib.modules.projects.ParamsProjectGetOne;
import com.syncano.android.lib.modules.projects.ParamsProjectNew;
import com.syncano.android.lib.modules.projects.ParamsProjectUpdate;
import com.syncano.android.lib.modules.projects.ResponseProjectGet;
import com.syncano.android.lib.modules.projects.ResponseProjectGetOne;
import com.syncano.android.lib.modules.projects.ResponseProjectNew;
import com.syncano.android.lib.modules.projects.ResponseProjectUpdate;
import com.syncano.android.lib.modules.roles.ParamsRoleGet;
import com.syncano.android.lib.modules.roles.ResponseRoleGet;
import com.syncano.android.lib.modules.subscriptions.ParamsSubscriptionGet;
import com.syncano.android.lib.modules.subscriptions.ParamsSubscriptionSubscribeCollection;
import com.syncano.android.lib.modules.subscriptions.ParamsSubscriptionSubscribeProject;
import com.syncano.android.lib.modules.subscriptions.ParamsSubscriptionUnsubscribeCollection;
import com.syncano.android.lib.modules.subscriptions.ParamsSubscriptionUnsubscribeProject;
import com.syncano.android.lib.modules.subscriptions.ResponseSubscriptionGet;
import com.syncano.android.lib.modules.users.ParamsUserCount;
import com.syncano.android.lib.modules.users.ParamsUserDelete;
import com.syncano.android.lib.modules.users.ParamsUserGet;
import com.syncano.android.lib.modules.users.ParamsUserGetAll;
import com.syncano.android.lib.modules.users.ParamsUserGetOne;
import com.syncano.android.lib.modules.users.ParamsUserLogin;
import com.syncano.android.lib.modules.users.ParamsUserNew;
import com.syncano.android.lib.modules.users.ParamsUserUpdate;
import com.syncano.android.lib.modules.users.ResponseUserCount;
import com.syncano.android.lib.modules.users.ResponseUserGet;
import com.syncano.android.lib.modules.users.ResponseUserGetAll;
import com.syncano.android.lib.modules.users.ResponseUserGetOne;
import com.syncano.android.lib.modules.users.ResponseUserLogin;
import com.syncano.android.lib.modules.users.ResponseUserNew;
import com.syncano.android.lib.modules.users.ResponseUserUpdate;

import android.content.Context;

public class Syncano extends SyncanoBase {
	/**
	 * Default constructor
	 * 
	 * @param context
	 *            Context from application
	 * @param instanceSubdomain
	 *            subdomain of instance
	 * @param apiKey
	 *            api key
	 */
	public Syncano(Context context, String instanceSubdomain, String apiKey) {
		super(context, instanceSubdomain, apiKey);
	}

	/**
	 * Adds a new administrator to current instance. Account with admin_email must exist in Syncano. Only Admin
	 * permission role can add new administrators.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public ResponseAdminNew adminNew(ParamsAdminNew params) {
		return (ResponseAdminNew) sendRequest(params);
	}

	/**
	 * Get all administrators of current instance.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public ResponseAdminGet adminGet(ParamsAdminGet params) {
		return (ResponseAdminGet) sendRequest(params);
	}

	/**
	 * Gets admin info with specific id or email from current instance.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public ResponseAdminGetOne adminGetOne(ParamsAdminGetOne params) {
		return (ResponseAdminGetOne) sendRequest(params);
	}

	/**
	 * Updates specified admin's permission role. Only Admin permission role can edit administrators.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public ResponseAdminUpdate adminUpdate(ParamsAdminUpdate params) {
		return (ResponseAdminUpdate) sendRequest(params);
	}

	/**
	 * Deletes specified administrator from current instance. Only Admin permission role can delete administrators.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public Response adminDelete(ParamsAdminDelete params) {
		return sendRequest(params);
	}

	/**
	 * Logs in API client and returns session_id for session id or cookie-based authentication. See Authorization.
	 * Session is valid for 2 hours and is automatically renewed whenever it is used.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public ResponseApiKeyStartSession apikeyStartSession(ParamsApiKeyStartSession params) {
		return (ResponseApiKeyStartSession) sendRequest(params);
	}

	/**
	 * Creates a new API key client in current instance. Only Admin permission role can create new API clients.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public ResponseApiKeyNew apikeyNew(ParamsApiKeyNew params) {
		return (ResponseApiKeyNew) sendRequest(params);
	}

	/**
	 * Get API clients. Only Admin permission role can view other API clients.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public ResponseApiKeyGet apikeyGet(ParamsApiKeyGet params) {
		return (ResponseApiKeyGet) sendRequest(params);
	}

	/**
	 * Gets info of one specified API client. Only Admin permission role can view other API clients.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public ResponseApiKeyGetOne apikeyGetOne(ParamsApiKeyGetOne params) {
		return (ResponseApiKeyGetOne) sendRequest(params);
	}

	/**
	 * Updates specified API client's info.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public ResponseApiKeyUpdateDescription apikeyUpdateDescription(ParamsApiKeyUpdateDescription params) {
		return (ResponseApiKeyUpdateDescription) sendRequest(params);
	}

	/**
	 * Adds permission to specified User API client. Requires Backend API key with Admin permission role.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public Response apikeyAuthorize(ParamsApiKeyAuthorize params) {
		return sendRequest(params);
	}

	/**
	 * Removes permission from specified User API client. Requires Backend API key with Admin permission role.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public Response apikeyDeauthorize(ParamsApiKeyDeauthorize params) {
		return sendRequest(params);
	}

	/**
	 * Deletes specified API client. Only Admin permission role can delete API clients.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public Response apikeyDelete(ParamsApiKeyDelete params) {
		return sendRequest(params);
	}

	/**
	 * Lists all permission roles of current instance.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public ResponseRoleGet apikeyUpdateDescription(ParamsRoleGet params) {
		return (ResponseRoleGet) sendRequest(params);
	}

	/**
	 * Creates new project.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public ResponseProjectNew projectNew(ParamsProjectNew params) {
		return (ResponseProjectNew) sendRequest(params);
	}

	/**
	 * Gets list of available project
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public ResponseProjectGet projectGet(ParamsProjectGet params) {
		return (ResponseProjectGet) sendRequest(params);
	}

	/**
	 * Gets one project with specified id
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public ResponseProjectGetOne projectGetOne(ParamsProjectGetOne params) {
		return (ResponseProjectGetOne) sendRequest(params);
	}

	/**
	 * Updates specified project.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public ResponseProjectUpdate projectUpdate(ParamsProjectUpdate params) {
		return (ResponseProjectUpdate) sendRequest(params);
	}

	/**
	 * Adds container-level permission to specified User API client. Requires Backend API key with Admin permission
	 * role.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public Response projectAuthorize(ParamsProjectAuthorize params) {
		return sendRequest(params);
	}

	/**
	 * Removes container-level permission from specified User API client. Requires Backend API key with Admin permission
	 * role.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public Response projectDeuthorize(ParamsProjectDeauthorize params) {
		return sendRequest(params);
	}

	/**
	 * Deletes specified project.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public Response projectDelete(ParamsProjectDelete params) {
		return sendRequest(params);
	}

	/**
	 * Creates new collection.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public ResponseCollectionNew collectionNew(ParamsCollectionNew params) {
		return (ResponseCollectionNew) sendRequest(params);
	}

	/**
	 * Gets all collections
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public ResponseCollectionGet collectionGet(ParamsCollectionGet params) {
		return (ResponseCollectionGet) sendRequest(params);
	}

	/**
	 * Gets one collection with specified id
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public ResponseCollectionGetOne collectionGetOne(ParamsCollectionGetOne params) {
		return (ResponseCollectionGetOne) sendRequest(params);
	}

	/**
	 * Activates specified collection.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public Response collectionActivate(ParamsCollectionActivate params) {
		return sendRequest(params);
	}

	/**
	 * Deactivates specified collection.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public Response collectionDeactivate(ParamsCollectionDeactivate params) {
		return sendRequest(params);
	}

	/**
	 * Updates specified collection.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public ResponseCollectionUpdate collectionUpdate(ParamsCollectionUpdate params) {
		return (ResponseCollectionUpdate) sendRequest(params);
	}

	/**
	 * Adds container-level permission to specified User API client. Requires Backend API key with Admin permission
	 * role.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public Response collectionAuthorize(ParamsCollectionAuthorize params) {
		return sendRequest(params);
	}

	/**
	 * Removes container-level permission from specified User API client. Requires Backend API key with Admin permission
	 * role.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public Response collectionDeauthorize(ParamsCollectionDeauthorize params) {
		return sendRequest(params);
	}

	/**
	 * Delete specified collection.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public Response collectionDelete(ParamsCollectionDelete params) {
		return sendRequest(params);
	}

	/**
	 * Adds tag to specified collection.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public Response collectionAddTag(ParamsCollectionAddTag params) {
		return sendRequest(params);
	}

	/**
	 * Delete selected tags from collection.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public Response collectionDeleteTag(ParamsCollectionDeleteTag params) {
		return sendRequest(params);
	}

	/**
	 * Create new folder within specified collection.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public ResponseFolderNew folderNew(ParamsFolderNew params) {
		return (ResponseFolderNew) sendRequest(params);
	}

	/**
	 * Get folders from specified collection.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public ResponseFolderGet folderGet(ParamsFolderGet params) {
		return (ResponseFolderGet) sendRequest(params);
	}

	/**
	 * Get one folder from specified collection.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public ResponseFolderGetOne folderGetOne(ParamsFolderGetOne params) {
		return (ResponseFolderGetOne) sendRequest(params);
	}

	/**
	 * Update existing folder.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public Response folderUpdate(ParamsFolderUpdate params) {
		return sendRequest(params);
	}

	/**
	 * Adds container-level permission to specified User API client. Requires Backend API key with Admin permission
	 * role.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public Response folderAuthorize(ParamsFolderAuthorize params) {
		return sendRequest(params);
	}

	/**
	 * Removes container-level permission from specified User API client. Requires Backend API key with Admin permission
	 * role.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public Response folderDeauthorize(ParamsFolderDeauthorize params) {
		return sendRequest(params);
	}

	/**
	 * Delete (permanently) specified folder and all associated data.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public Response folderDelete(ParamsFolderDelete params) {
		return sendRequest(params);
	}

	/**
	 * Creates a new Data Object.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public ResponseDataNew dataNew(ParamsDataNew params) {
		return (ResponseDataNew) sendRequest(params);
	}

	/**
	 * * Get data from collection(s) or whole project with optional additional filtering. All filters, unless explicitly
	 * noted otherwise, affect all hierarchy levels. To paginate and to get more data, use since_id or since_time
	 * parameter.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public ResponseDataGet dataGet(ParamsDataGet params) {
		return (ResponseDataGet) sendRequest(params);
	}

	/**
	 * Get data by data_id or data_key. Either data_id or data_key has to be specified.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public ResponseDataGetOne dataGetOne(ParamsDataGetOne params) {
		return (ResponseDataGetOne) sendRequest(params);
	}

	/**
	 * Updates existing Data Object if data with specified data_id or data_key already exists. You can specify how to
	 * react if specified Data Object already exists with update_method parameter. Possible options: replace - default
	 * value. Will delete all Data Object fields and create a new one in its place (no previous data will remain). merge
	 * - will not delete/empty previously set data but merge it instead with new data.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public ResponseDataUpdate dataUpdate(ParamsDataUpdate params) {
		return (ResponseDataUpdate) sendRequest(params);
	}

	/**
	 * Moves data to folder and/or state.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public Response dataMove(ParamsDataMove params) {
		return sendRequest(params);
	}

	/**
	 * Copies data with data_id. Copy has data_key cleared.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public ResponseDataCopy dataCopy(ParamsDataCopy params) {
		return (ResponseDataCopy) sendRequest(params);
	}

	/**
	 * Adds additional parent to data with data_id. If remove_other is True, all other relations for this Data Object
	 * will be removed. Note: There can be max 250 parents per Data Object.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public Response dataAddParent(ParamsDataAddParent params) {
		return sendRequest(params);
	}

	/**
	 * Removes a parent from data with data_id.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public Response dataRemoveParent(ParamsDataRemoveParent params) {
		return sendRequest(params);
	}

	/**
	 * Adds additional child to data with specified data_id. If remove_other is True, all other children of specified
	 * Data Object will be removed.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public Response dataAddChild(ParamsDataAddChild params) {
		return sendRequest(params);
	}

	/**
	 * Removes a child (or children) from data with specified data_id.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public Response dataRemoveChild(ParamsDataRemoveChild params) {
		return sendRequest(params);
	}

	/**
	 * Deletes Data Object. If no filters are specified, will process all Data Objects in defined collection(s) (up to
	 * defined limit).
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public Response dataDelete(ParamsDataDelete params) {
		return sendRequest(params);
	}

	/**
	 * Counts data with specified criteria.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public ResponseDataCount dataCount(ParamsDataCount params) {
		return (ResponseDataCount) sendRequest(params);
	}

	/**
	 * Logs in a user.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public ResponseUserLogin userLogin(ParamsUserLogin params) {
		return (ResponseUserLogin) sendRequest(params);
	}

	/**
	 * Create new user.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public ResponseUserNew userNew(ParamsUserNew params) {
		return (ResponseUserNew) sendRequest(params);
	}

	/**
	 * Gets all users.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public ResponseUserGetAll userGetAll(ParamsUserGetAll params) {
		return (ResponseUserGetAll) sendRequest(params);
	}

	/**
	 * Gets users with specified criteria that are associated with Data Objects within specified collection.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public ResponseUserGet userGet(ParamsUserGet params) {
		return (ResponseUserGet) sendRequest(params);
	}

	/**
	 * Gets one user.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public ResponseUserGetOne userGetOne(ParamsUserGetOne params) {
		return (ResponseUserGetOne) sendRequest(params);
	}

	/**
	 * Updates specified user.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public ResponseUserUpdate userUpdate(ParamsUserUpdate params) {
		return (ResponseUserUpdate) sendRequest(params);
	}

	/**
	 * Count users with specified criteria.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public ResponseUserCount userCount(ParamsUserCount params) {
		return (ResponseUserCount) sendRequest(params);
	}

	/**
	 * Deletes specified user
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public Response userDelete(ParamsUserDelete params) {
		return sendRequest(params);
	}

	/**
	 * Subscribes to project.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public Response subscriptionSubscribeProject(ParamsSubscriptionSubscribeProject params) {
		return sendRequest(params);
	}

	/**
	 * Unsubscribes from project subscription.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public Response subscriptionUnsubscribeProject(ParamsSubscriptionUnsubscribeProject params) {
		return sendRequest(params);
	}

	/**
	 * Subscribes to collection within specified project.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public Response subscriptionSubscribeCollection(ParamsSubscriptionSubscribeCollection params) {
		return sendRequest(params);
	}

	/**
	 * Unsubscribes from collection subscription.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public Response subscriptionUnsubscribeCollection(ParamsSubscriptionUnsubscribeCollection params) {
		return sendRequest(params);
	}

	/**
	 * Gets all subscriptions for specified user.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public ResponseSubscriptionGet subscriptionGet(ParamsSubscriptionGet params) {
		return (ResponseSubscriptionGet) sendRequest(params);
	}

	/**
	 * Sends custom notification to client through Sync Server.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public Response notificationSend(ParamsNotificationSend params) {
		return sendRequest(params);
	}

	/**
	 * Gets specified collection history of notifications of current or specified client. History items are stored for
	 * 24 hours.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public Response notificationGetHistory(ParamsNotificationGetHistory params) {
		return sendRequest(params);
	}

	/**
	 * Get currently connected API client identities up to limit (max 100).
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public ResponseIdentityGet identityGet(ParamsIdentityGet params) {
		return (ResponseIdentityGet) sendRequest(params);
	}

	/**
	 * Updates specified API client identity.
	 * 
	 * @param params
	 *            parameters for request
	 * @return response for this request
	 */
	public ResponseIdentityUpdate identityUpdate(ParamsIdentityUpdate params) {
		return (ResponseIdentityUpdate) sendRequest(params);
	}
}
