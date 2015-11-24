package com.syncano.library;

import com.google.gson.JsonObject;
import com.syncano.library.api.RequestDelete;
import com.syncano.library.api.RequestGetList;
import com.syncano.library.api.RequestGetOne;
import com.syncano.library.api.RequestPatch;
import com.syncano.library.api.RequestPost;
import com.syncano.library.choice.SocialAuthBackend;
import com.syncano.library.data.AbstractUser;
import com.syncano.library.data.Channel;
import com.syncano.library.data.CodeBox;
import com.syncano.library.data.Group;
import com.syncano.library.data.GroupMembership;
import com.syncano.library.data.Notification;
import com.syncano.library.data.Trace;
import com.syncano.library.data.SyncanoClass;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.data.User;
import com.syncano.library.data.Webhook;
import com.syncano.library.utils.SyncanoClassHelper;

public class Syncano extends SyncanoBase {

    private static Syncano sharedInstance = null;

    /**
     * Create Syncano object.
     */
    public Syncano() {
        super(null, null);
    }

    /**
     * Create Syncano object.
     *
     * @param instance Syncano instance related with apiKey.
     */
    public Syncano(String instance) {
        super(null, instance);
    }

    /**
     * Create Syncano object.
     *
     * @param apiKey   Api key.
     * @param instance Syncano instance related with apiKey.
     */
    public Syncano(String apiKey, String instance) {
        super(apiKey, instance);
    }

    /**
     * Create Syncano object.
     *
     * @param customServerUrl If not set, production URL will be used.
     * @param apiKey          Api key.
     * @param instance        Syncano instance related with apiKey.
     */
    public Syncano(String customServerUrl, String apiKey, String instance) {
        super(customServerUrl, apiKey, instance);
    }

    /**
     * Create static Syncano instance.
     * Use getSharedInstance() to get its reference.
     *
     * @param apiKey   Api key.
     * @param instance Syncano instance related with apiKey.
     */
    public static void initSharedInstance(String apiKey, String instance) {
        sharedInstance = new Syncano(apiKey, instance);
    }

    public static Syncano getSharedInstance() {
        return sharedInstance;
    }

    // ==================== Objects ==================== //

    /**
     * Create object on Syncano.
     *
     * @param object Object to create. It need to extend SyncanoObject.
     * @param <T>    Result type.
     * @return New DataObject.
     */
    public <T extends SyncanoObject> RequestPost<T> createObject(T object) {

        Class<T> type = (Class<T>) object.getClass();
        String className = SyncanoClassHelper.getSyncanoClassName(type);
        String url = String.format(Constants.OBJECTS_LIST_URL, getInstance(), className);
        return new RequestPost<>(type, url, this, object);
    }

    /**
     * Get details of a Data Object.
     *
     * @param type Type of the object.
     * @param id   Object id.
     * @param <T>  Result type.
     * @return Existing DataObject.
     */
    public <T extends SyncanoObject> RequestGetOne<T> getObject(Class<T> type, int id) {

        String className = SyncanoClassHelper.getSyncanoClassName(type);
        String url = String.format(Constants.OBJECTS_DETAIL_URL, getInstance(), className, id);
        return new RequestGetOne<>(type, url, this);
    }

    /**
     * Get a list of Data Objects associated with a given Class.
     *
     * @param type Type for result List item.
     * @param <T>  Result type.
     * @return List of DataObjects.
     */
    public <T extends SyncanoObject> RequestGetList<T> getObjects(Class<T> type) {

        String className = SyncanoClassHelper.getSyncanoClassName(type);
        String url = String.format(Constants.OBJECTS_LIST_URL, getInstance(), className);
        return new RequestGetList<>(type, url, this);
    }

    /**
     * Update Data Object.
     *
     * @param object Object to update. It need to have id.
     * @param <T>    Result type.
     * @return Updated DataObject.
     */
    public <T extends SyncanoObject> RequestPatch<T> updateObject(T object) {

        if (object.getId() == 0) {
            throw new RuntimeException("Trying to update object without id!");
        }

        Class<T> type = (Class<T>) object.getClass();
        String className = SyncanoClassHelper.getSyncanoClassName(type);
        String url = String.format(Constants.OBJECTS_DETAIL_URL, getInstance(), className, object.getId());
        return new RequestPatch<>(type, url, this, object);
    }

    /**
     * Delete a Data Object.
     *
     * @param type Type of object to delete.
     * @param id   Object id.
     * @param <T>  Result type.
     * @return null
     */
    public <T extends SyncanoObject> RequestDelete<T> deleteObject(Class<T> type, int id) {

        String className = SyncanoClassHelper.getSyncanoClassName(type);
        String url = String.format(Constants.OBJECTS_DETAIL_URL, getInstance(), className, id);
        return new RequestDelete<>(type, url, this);
    }

    // ==================== CodeBoxes ==================== //

    /**
     * Create a CodeBox.
     *
     * @param codeBox CodeBox to create.
     * @return New CodeBox.
     */
    public RequestPost<CodeBox> createCodeBox(CodeBox codeBox) {

        String url = String.format(Constants.CODEBOXES_LIST_URL, getInstance());
        return new RequestPost<>(CodeBox.class, url, this, codeBox);
    }

    /**
     * Get details of previously created CodeBox.
     *
     * @param id CodeBox id.
     * @return Existing CodeBox.
     */
    public RequestGetOne<CodeBox> getCodeBox(int id) {

        String url = String.format(Constants.CODEBOXES_DETAIL_URL, getInstance(), id);
        return new RequestGetOne<>(CodeBox.class, url, this);
    }

    /**
     * Get a list of previously created CodeBoxes.
     *
     * @return List of existing CodeBoxes.
     */
    public RequestGetList<CodeBox> getCodeBoxes() {

        String url = String.format(Constants.CODEBOXES_LIST_URL, getInstance());
        return new RequestGetList<>(CodeBox.class, url, this);
    }

    /**
     * Update a CodeBox.
     *
     * @param codeBox CodeBox to update. It need to have id.
     * @return Updated CodeBox.
     */
    public RequestPatch<CodeBox> updateCodeBox(CodeBox codeBox) {

        if (codeBox.getId() == 0) {
            throw new RuntimeException("Trying to update object without id!");
        }

        String url = String.format(Constants.CODEBOXES_DETAIL_URL, getInstance(), codeBox.getId());
        return new RequestPatch<>(CodeBox.class, url, this, codeBox);
    }

    /**
     * Delete previously created CodeBox.
     *
     * @param id CodeBox id.
     * @return null
     */
    public RequestDelete<CodeBox> deleteCodeBox(int id) {

        String url = String.format(Constants.CODEBOXES_DETAIL_URL, getInstance(), id);
        return new RequestDelete<>(CodeBox.class, url, this);
    }

    /**
     * Run CodeBox asynchronous. Result of this request is not result of the CodeBox.
     * Result will be stored in associated Trace.
     *
     * @param id CodeBox id.
     * @return Result with link do Trace.
     */
    public RequestPost<Trace> runCodeBox(int id) {
        return runCodeBox(id, null);
    }

    /**
     * Run CodeBox asynchronous. Result of this request is not result of the CodeBox.
     * Result will be stored in associated Trace.
     *
     * @param id     CodeBox id.
     * @param params CodeBox params.
     * @return Result with link do Trace.
     */
    public RequestPost<Trace> runCodeBox(int id, JsonObject params) {

        String url = String.format(Constants.CODEBOXES_RUN_URL, getInstance(), id);
        return new RequestPost<>(Trace.class, url, this, params);
    }

    // ==================== Webhooks ==================== //

    /**
     * Create a new Webhook
     *
     * @param webhook Webhook to create.
     * @return New Webhook.
     */
    public RequestPost<Webhook> createWebhook(Webhook webhook) {

        String url = String.format(Constants.WEBHOOKS_LIST_URL, getInstance());
        return new RequestPost<>(Webhook.class, url, this, webhook);
    }

    /**
     * Get details of previously created Webhook.
     *
     * @param name Webhook id.
     * @return Existing Webhook.
     */
    public RequestGetOne<Webhook> getWebhook(String name) {

        String url = String.format(Constants.WEBHOOKS_DETAIL_URL, getInstance(), name);
        return new RequestGetOne<>(Webhook.class, url, this);
    }

    /**
     * Get a list of previously created Webhooks.
     *
     * @return List of existing Webhooks.
     */
    public RequestGetList<Webhook> getWebhooks() {

        String url = String.format(Constants.WEBHOOKS_LIST_URL, getInstance());
        return new RequestGetList<>(Webhook.class, url, this);
    }

    /**
     * Update a Webhook.
     *
     * @param webhook Webhook to update. It need to have name.
     * @return Updated Webhook.
     */
    public RequestPatch<Webhook> updateWebhook(Webhook webhook) {

        if (webhook.getName() == null || webhook.getName().isEmpty()) {
            throw new RuntimeException("Trying to update Webhook without name!");
        }

        String url = String.format(Constants.WEBHOOKS_DETAIL_URL, getInstance(), webhook.getName());
        return new RequestPatch<>(Webhook.class, url, this, webhook);
    }

    /**
     * Delete a Webhook.
     *
     * @param name Webhook id.
     * @return null
     */
    public RequestDelete<Webhook> deleteWebhook(String name) {

        String url = String.format(Constants.WEBHOOKS_DETAIL_URL, getInstance(), name);
        return new RequestDelete<>(Webhook.class, url, this);
    }

    /**
     * Run a Webhook synchronous. It should contain result of associated CodeBox.
     *
     * @param name Webhook id.
     * @return Result of executed CodeBox.
     */
    public RequestPost<Trace> runWebhook(String name) {

        return runWebhook(name, null);
    }

    /**
     * Run a Webhook synchronous. It should contain result of associated CodeBox.
     *
     * @param name Webhook id.
     * @return Result of executed CodeBox.
     */
    public RequestPost<Trace> runWebhook(String name, JsonObject payload) {

        String url = String.format(Constants.WEBHOOKS_RUN_URL, getInstance(), name);
        return new RequestPost<>(Trace.class, url, this, payload);
    }

    public RequestPost<Trace> runWebhookUrl(String url) {
        return runWebhookUrl(url, null);
    }

    public RequestPost<Trace> runWebhookUrl(String url, JsonObject payload) {
        RequestPost<Trace> request = new RequestPost<>(Trace.class, null, this, payload);
        request.setCompleteCustomUrl(url);
        return request;
    }

    // ==================== Classes ==================== //

    /**
     * Create a class.
     *
     * @param clazz Class to create.
     * @return Created class.
     */
    public RequestPost<SyncanoClass> createSyncanoClass(SyncanoClass clazz) {

        String url = String.format(Constants.CLASSES_LIST_URL, getInstance());
        return new RequestPost<>(SyncanoClass.class, url, this, clazz);
    }

    /**
     * Get an information on a selected Class.
     *
     * @param name Class name.
     * @return Existing class.
     */
    public RequestGetOne<SyncanoClass> getSyncanoClass(String name) {

        String url = String.format(Constants.CLASSES_DETAIL_URL, getInstance(), name);
        return new RequestGetOne<>(SyncanoClass.class, url, this);
    }

    /**
     * Get a list of Classes associated with an Instance.
     *
     * @return List of classes.
     */
    public RequestGetList<SyncanoClass> getSyncanoClasses() {

        String url = String.format(Constants.CLASSES_LIST_URL, getInstance());
        return new RequestGetList<>(SyncanoClass.class, url, this);
    }

    /**
     * Update a Class.
     *
     * @param clazz SyncanoClass to update. It need to have name.
     * @return Updated Class.
     */
    public RequestPatch<SyncanoClass> updateSyncanoClass(SyncanoClass clazz) {

        if (clazz.getName() == null || clazz.getName().isEmpty()) {
            throw new RuntimeException("Trying to update Class without name!");
        }

        String url = String.format(Constants.CLASSES_DETAIL_URL, getInstance(), clazz.getName());
        return new RequestPatch<>(SyncanoClass.class, url, this, clazz);
    }

    /**
     * Delete a Class.
     *
     * @param name Class to delete.
     * @return RequestDelete<SyncanoClass>
     */
    public RequestDelete<SyncanoClass> deleteSyncanoClass(String name) {

        String url = String.format(Constants.CLASSES_DETAIL_URL, getInstance(), name);
        return new RequestDelete<>(SyncanoClass.class, url, this);
    }

    /**
     * Delete a Class.
     *
     * @param clazz Class to delete.
     * @return RequestDelete<SyncanoClass>
     */
    public RequestDelete<SyncanoClass> deleteSyncanoClass(Class<? extends SyncanoObject> clazz) {

        return deleteSyncanoClass(SyncanoClassHelper.getSyncanoClassName(clazz));
    }

    // ==================== Users ==================== //

    /**
     * Create a new User.
     * To be able to register Users you'll have to create an API Key that has allow_user_create flag set to true.
     *
     * @param user User to create.
     * @return created User
     */
    public RequestPost<User> createUser(User user) {

        return createCustomUser(user);
    }

    /**
     * Create a new custom User.
     * To be able to register Users you'll have to create an API Key that has allow_user_create flag set to true.
     *
     * @param user User to create.
     * @return created User
     */
    public <T extends AbstractUser> RequestPost<T> createCustomUser(T user) {
        Class<T> type = (Class<T>) user.getClass();
        String url = String.format(Constants.USERS_LIST_URL, getInstance());
        return new RequestPost<>(type, url, this, user);
    }

    /**
     * Get details of previously created User.
     *
     * @param id Id of existing User.
     * @return requested user
     */
    public RequestGetOne<User> getUser(int id) {

        return getCustomUser(User.class, id);
    }

    /**
     * Get details of previously created User.
     *
     * @param id Id of existing User.
     * @return requested user
     */
    public <T extends AbstractUser> RequestGetOne<T> getCustomUser(Class<T> type, int id) {

        String url = String.format(Constants.USERS_DETAIL_URL, getInstance(), id);
        return new RequestGetOne<>(type, url, this);
    }

    /**
     * Get a list of previously created Users.
     *
     * @return requested users
     */
    public RequestGetList<User> getUsers() {

        return getCustomUsers(User.class);
    }

    /**
     * Get a list of previously created custom Users.
     *
     * @return requested users
     */
    public <T extends AbstractUser> RequestGetList<T> getCustomUsers(Class<T> type) {

        String url = String.format(Constants.USERS_LIST_URL, getInstance());
        return new RequestGetList<>(type, url, this);
    }

    /**
     * Update a User.
     *
     * @param user User to update. It need to have id.
     * @return updated user
     */
    public RequestPatch<User> updateUser(User user) {

        return updateCustomUser(user);
    }

    /**
     * Update a custom User.
     *
     * @param user User to update. It need to have id.
     * @return updated user
     */
    public <T extends AbstractUser> RequestPatch<T> updateCustomUser(T user) {

        if (user.getId() == 0) {
            throw new RuntimeException("Trying to update User without id!");
        }

        Class<T> type = (Class<T>) user.getClass();
        String url = String.format(Constants.USERS_DETAIL_URL, getInstance(), user.getId());
        return new RequestPatch<>(type, url, this, user);
    }

    /**
     * Delete a User.
     *
     * @param id Id of existing User.
     * @return Information about success or error
     */
    public RequestDelete<User> deleteUser(int id) {

        return deleteCustomUser(User.class, id);
    }

    /**
     * Delete a custom User.
     *
     * @param id Id of existing User.
     * @return Information about success or error
     */
    public <T extends AbstractUser> RequestDelete<T> deleteCustomUser(Class<T> type, int id) {

        String url = String.format(Constants.USERS_DETAIL_URL, getInstance(), id);
        return new RequestDelete<>(type, url, this);
    }

    /**
     * Authenticate a User.
     *
     * @param username User name from registration.
     * @param password User password.
     * @return user
     */
    public RequestPost<User> loginUser(String username, String password) {

        return loginUser(User.class, username, password);
    }

    /**
     * Authenticate a custom User.
     *
     * @param username User name from registration.
     * @param password User password.
     * @return user
     */
    public <T extends AbstractUser> RequestPost<T> loginUser(Class<T> type, String username, String password) {

        String url = String.format(Constants.USER_AUTH, getInstance());

        JsonObject jsonParams = new JsonObject();
        jsonParams.addProperty(User.FIELD_USER_NAME, username);
        jsonParams.addProperty(User.FIELD_PASSWORD, password);

        return new RequestPost<>(type, url, this, jsonParams);
    }

    /**
     * Authenticate a social user.
     *
     * @param social    Social network authentication backend.
     * @param authToken Authentication token.
     * @return user
     */
    public RequestPost<User> authSocialUser(SocialAuthBackend social, String authToken) {

        return authSocialCustomUser(User.class, social, authToken);
    }

    /**
     * Authenticate a social user.
     *
     * @param social    Social network authentication backend.
     * @param authToken Authentication token.
     * @return user
     */
    public <T extends AbstractUser> RequestPost<T> authSocialCustomUser(Class<T> type, SocialAuthBackend social, String authToken) {

        String url = String.format(Constants.USER_SOCIAL_AUTH, getInstance(), social.toString());

        JsonObject jsonParams = new JsonObject();
        jsonParams.addProperty(Constants.POST_PARAM_SOCIAL_TOKEN, authToken);

        return new RequestPost<>(type, url, this, jsonParams);
    }

    // ==================== Groups ==================== //

    /**
     * Create a new Group.
     *
     * @param group Group to create.
     * @return Created group
     */
    public RequestPost<Group> createGroup(Group group) {

        String url = String.format(Constants.GROUPS_LIST_URL, getInstance());
        return new RequestPost<>(Group.class, url, this, group);
    }

    /**
     * Get details of previously created Group.
     *
     * @param id Id of existing Group.
     * @return Requested group
     */
    public RequestGetOne<Group> getGroup(int id) {

        String url = String.format(Constants.GROUPS_DETAIL_URL, getInstance(), id);
        return new RequestGetOne<>(Group.class, url, this);
    }

    /**
     * Get a list of previously created Groups.
     *
     * @return All groups
     */
    public RequestGetList<Group> getGroups() {

        String url = String.format(Constants.GROUPS_LIST_URL, getInstance());
        return new RequestGetList<>(Group.class, url, this);
    }

    /**
     * Update a Group.
     *
     * @param group Group to update. It need to have id.
     * @return updated group
     */
    public RequestPatch<Group> updateGroup(Group group) {

        if (group.getId() == 0) {
            throw new RuntimeException("Trying to update Group without id!");
        }

        String url = String.format(Constants.GROUPS_DETAIL_URL, getInstance(), group.getId());
        return new RequestPatch<>(Group.class, url, this, group);
    }

    /**
     * Delete a Group.
     *
     * @param id Id of existing Group.
     * @return Success or error information
     */
    public RequestDelete<Group> deleteGroup(int id) {

        String url = String.format(Constants.GROUPS_DETAIL_URL, getInstance(), id);
        return new RequestDelete<>(Group.class, url, this);
    }

    /**
     * Get details of previously created Membership.
     *
     * @param groupId Id of existing Group.
     * @param userId  Membership id.
     * @return User wrapped id a membership object
     */
    public RequestGetOne<GroupMembership> getGroupMembership(int groupId, int userId) {

        String url = String.format(Constants.GROUPS_USERS_DETAIL_URL, getInstance(), groupId, userId);
        return new RequestGetOne<>(GroupMembership.class, url, this);
    }

    /**
     * Get a list of previously created Memberships.
     *
     * @param groupId Id of existing Group.
     * @return Users wrapped id a memberships objects
     */
    public RequestGetList<GroupMembership> getGroupMemberships(int groupId) {

        String url = String.format(Constants.GROUPS_USERS_LIST_URL, getInstance(), groupId);
        return new RequestGetList<>(GroupMembership.class, url, this);
    }

    /**
     * Add user to a Group.
     *
     * @param groupId Id of existing Group.
     * @param userId  Id of existing User.
     * @return Object representing Membership.
     */
    public RequestPost<GroupMembership> addUserToGroup(int groupId, int userId) {
        String url = String.format(Constants.GROUPS_USERS_LIST_URL, getInstance(), groupId);

        JsonObject jsonParams = new JsonObject();
        jsonParams.addProperty(Constants.POST_PARAM_USER, userId);

        return new RequestPost<>(GroupMembership.class, url, this, jsonParams);
    }

    /**
     * Delete a Group mambership.
     *
     * @param groupId Id of existing Group.
     * @param userId  Membership id.
     * @return Information about success.
     */
    public RequestDelete<GroupMembership> deleteUserFromGroup(int groupId, int userId) {

        String url = String.format(Constants.GROUPS_USERS_DETAIL_URL, getInstance(), groupId, userId);
        return new RequestDelete<>(GroupMembership.class, url, this);
    }

    // ==================== Channels ==================== //

    /**
     * Create a new Channel
     *
     * @param channel Channel to create.
     * @return New Channel.
     */
    public RequestPost<Channel> createChannel(Channel channel) {

        String url = String.format(Constants.CHANNELS_LIST_URL, getInstance());
        return new RequestPost<>(Channel.class, url, this, channel);
    }

    /**
     * Get details of previously created Channel.
     *
     * @param channelName Channel id.
     * @return Existing Channel.
     */
    public RequestGetOne<Channel> getChannel(String channelName) {

        String url = String.format(Constants.CHANNELS_DETAIL_URL, getInstance(), channelName);
        return new RequestGetOne<>(Channel.class, url, this);
    }

    /**
     * Get a list of previously created Channels.
     *
     * @return List of existing Channels.
     */
    public RequestGetList<Channel> getChannels() {
        String url = String.format(Constants.CHANNELS_LIST_URL, getInstance());
        return new RequestGetList<>(Channel.class, url, this);
    }

    /**
     * Update a Channel.
     *
     * @param channel Channel to update. It need to have name.
     * @return Updated Channel.
     */
    public RequestPatch<Channel> updateChannel(Channel channel) {

        if (channel.getName() == null || channel.getName().isEmpty()) {
            throw new RuntimeException("Trying to update Channel without name!");
        }

        String url = String.format(Constants.CHANNELS_DETAIL_URL, getInstance(), channel.getName());
        return new RequestPatch<>(Channel.class, url, this, channel);
    }

    /**
     * Delete a Channel.
     *
     * @param channelName Channel id.
     * @return null
     */
    public RequestDelete<Channel> deleteChannel(String channelName) {

        String url = String.format(Constants.CHANNELS_DETAIL_URL, getInstance(), channelName);
        return new RequestDelete<>(Channel.class, url, this);
    }

    /**
     * Get a list of Notifications.
     *
     * @param channelName Channel id.
     * @return Notification list.
     */
    public RequestGetList<Notification> getChannelsHistory(String channelName) {
        return getChannelsHistory(channelName, null);
    }

    /**
     * Get a list of Notifications.
     *
     * @param channelName Channel id.
     * @param room        Room to get history of. Might be null.
     * @return Notification list.
     */
    public RequestGetList<Notification> getChannelsHistory(String channelName, String room) {

        String url = String.format(Constants.CHANNELS_HISTORY_URL, getInstance(), channelName);
        RequestGetList<Notification> request = new RequestGetList<>(Notification.class, url, this);

        if (room != null) {
            request.addUrlParam(Constants.URL_PARAM_ROOM, room);
        }

        return request;
    }

    /**
     * Publish custom message.
     * To allow your Users to send custom messages, you'll have to create a channel that:
     * Has appropriate permissions - publish permission type for either group_permissions or other_permissions (depending on which Users should be granted the ability to send the messages)
     * Has custom_publish flag set to true
     *
     * @param channelName  Channel to publish on.
     * @param notification Notification to publish. Payload and Room are required. Room might be null.
     * @return Published Notification.
     */
    public RequestPost<Notification> publishOnChannel(String channelName, Notification notification) {

        String url = String.format(Constants.CHANNELS_PUBLISH_URL, getInstance(), channelName);

        return new RequestPost<>(Notification.class, url, this, notification);
    }

    /**
     * Poll realtime notifications. Timeout for a request to a channel is 5 minutes.
     * When the request times out it returns 200 OK empty request.
     * To handle this simply repeat the previous request with an unchanged last_id.
     *
     * @param channelName Channel to poll from.
     * @param room        Room to poll from.
     * @param lastId      Last notification id.
     * @return Notification.
     */
    /* package */ RequestGetOne<Notification> pollChannel(String channelName, String room, int lastId) {

        String url = String.format(Constants.CHANNELS_POLL_URL, getInstance(), channelName);
        RequestGetOne<Notification> request = new RequestGetOne<>(Notification.class, url, this);

        if (room != null) {
            request.addUrlParam(Constants.URL_PARAM_ROOM, room);
        }

        if (lastId != 0) {
            request.addUrlParam(Constants.URL_PARAM_LAST_ID, String.valueOf(lastId));
        }

        return request;
    }
}