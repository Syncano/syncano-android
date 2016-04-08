package com.syncano.library;

import android.content.Context;

import com.google.gson.JsonObject;
import com.syncano.library.api.Request;
import com.syncano.library.api.RequestDelete;
import com.syncano.library.api.RequestGetList;
import com.syncano.library.api.RequestGetOne;
import com.syncano.library.api.RequestPatch;
import com.syncano.library.api.RequestPost;
import com.syncano.library.api.Response;
import com.syncano.library.data.AbstractUser;
import com.syncano.library.data.Channel;
import com.syncano.library.data.CodeBox;
import com.syncano.library.data.DataEndpoint;
import com.syncano.library.data.Group;
import com.syncano.library.data.GroupMembership;
import com.syncano.library.data.Script;
import com.syncano.library.data.ScriptEndpoint;
import com.syncano.library.data.SyncanoClass;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.data.SyncanoTableView;
import com.syncano.library.data.User;
import com.syncano.library.data.Webhook;
import com.syncano.library.utils.SyncanoClassHelper;
import com.syncano.library.utils.Validate;

public class SyncanoDashboard extends Syncano {
    public SyncanoDashboard() {
        super();
    }

    public SyncanoDashboard(String instanceName) {
        super(instanceName);
    }

    public SyncanoDashboard(String apiKey, String instanceName) {
        super(apiKey, instanceName);
    }

    public SyncanoDashboard(String apiKey, String instanceName, Context androidContext) {
        super(apiKey, instanceName, androidContext);
    }

    public SyncanoDashboard(String customServerUrl, String apiKey, String instanceName) {
        super(customServerUrl, apiKey, instanceName);
    }

    public SyncanoDashboard(String customServerUrl, String apiKey, String instanceName, Context androidContext) {
        super(customServerUrl, apiKey, instanceName, androidContext);
    }

    public SyncanoDashboard(String customServerUrl, String apiKey, String instanceName, Context androidContext, boolean useLoggedUserStorage) {
        super(customServerUrl, apiKey, instanceName, androidContext, useLoggedUserStorage);
    }

    /**
     * Create a CodeBox.
     *
     * @param codeBox CodeBox to create.
     * @return New CodeBox.
     */
    @Deprecated
    public RequestPost<CodeBox> createCodeBox(CodeBox codeBox) {
        String url = String.format(Constants.SCRIPTS_LIST_URL, getNotEmptyInstanceName());
        return new RequestPost<>(CodeBox.class, url, this, codeBox);
    }

    /**
     * Create a Script.
     *
     * @param script Script to create.
     * @return New script.
     */
    public RequestPost<Script> createScript(Script script) {
        String url = String.format(Constants.SCRIPTS_LIST_URL, getNotEmptyInstanceName());
        return new RequestPost<>(Script.class, url, this, script);
    }

    /**
     * Get details of previously created CodeBox.
     *
     * @param id CodeBox id.
     * @return Existing CodeBox.
     */
    @Deprecated
    public RequestGetOne<CodeBox> getCodeBox(int id) {
        String url = String.format(Constants.SCRIPTS_DETAIL_URL, getNotEmptyInstanceName(), id);
        return new RequestGetOne<>(CodeBox.class, url, this);
    }

    /**
     * Get details of previously created Script.
     *
     * @param id Script id.
     * @return Existing Script.
     */
    public RequestGetOne<Script> getScript(int id) {
        String url = String.format(Constants.SCRIPTS_DETAIL_URL, getNotEmptyInstanceName(), id);
        return new RequestGetOne<>(Script.class, url, this);
    }

    /**
     * Get a list of previously created CodeBoxes.
     *
     * @return List of existing CodeBoxes.
     */
    @Deprecated
    public RequestGetList<CodeBox> getCodeBoxes() {
        String url = String.format(Constants.SCRIPTS_LIST_URL, getNotEmptyInstanceName());
        return new RequestGetList<>(CodeBox.class, url, this);
    }

    /**
     * Get a list of previously created Scripts.
     *
     * @return List of existing Scripts.
     */
    public RequestGetList<Script> getScripts() {
        String url = String.format(Constants.SCRIPTS_LIST_URL, getNotEmptyInstanceName());
        return new RequestGetList<>(Script.class, url, this);
    }

    /**
     * Update a CodeBox.
     *
     * @param codeBox CodeBox to update. It has to have id.
     * @return Updated CodeBox.
     */
    @Deprecated
    public RequestPatch<CodeBox> updateCodeBox(CodeBox codeBox) {
        Validate.checkNotNullAndZero(codeBox.getId(), "Trying to update object without id!");
        String url = String.format(Constants.SCRIPTS_DETAIL_URL, getNotEmptyInstanceName(), codeBox.getId());
        return new RequestPatch<>(CodeBox.class, url, this, codeBox);
    }

    /**
     * Update a Script.
     *
     * @param script Script to update. It has to have id.
     * @return Updated Script.
     */
    public RequestPatch<Script> updateScript(Script script) {
        Validate.checkNotNullAndZero(script.getId(), "Trying to update object without id!");
        String url = String.format(Constants.SCRIPTS_DETAIL_URL, getNotEmptyInstanceName(), script.getId());
        return new RequestPatch<>(Script.class, url, this, script);
    }

    /**
     * Delete previously created CodeBox.
     *
     * @param id CodeBox id.
     * @return Deleted CodeBox
     */
    @Deprecated
    public RequestDelete<CodeBox> deleteCodeBox(int id) {
        String url = String.format(Constants.SCRIPTS_DETAIL_URL, getNotEmptyInstanceName(), id);
        return new RequestDelete<>(CodeBox.class, url, this);
    }

    /**
     * Delete previously created Script.
     *
     * @param id Script id.
     * @return Deleted Script
     */
    public RequestDelete<Script> deleteScript(int id) {
        String url = String.format(Constants.SCRIPTS_DETAIL_URL, getNotEmptyInstanceName(), id);
        return new RequestDelete<>(Script.class, url, this);
    }

    /**
     * Create a new Webhook
     *
     * @param webhook Webhook to create. It's params will be updated.
     * @return New Webhook.
     */
    @Deprecated
    public RequestPost<Webhook> createWebhook(final Webhook webhook) {
        String url = String.format(Constants.SCRIPT_ENDPOINTS_LIST_URL, getNotEmptyInstanceName());
        RequestPost<Webhook> req = new RequestPost<>(Webhook.class, url, this, webhook);
        req.updateGivenObject(true);
        req.setRunAfter(new Request.RunAfter<Webhook>() {
            @Override
            public void run(Response<Webhook> response) {
                webhook.on(SyncanoDashboard.this);
            }
        });
        return req;
    }

    /**
     * Create a new ScriptEndpoint
     *
     * @param scriptEndpoint ScriptEndpoint to create. It's params will be updated.
     * @return New ScriptEndpoint.
     */
    public RequestPost<ScriptEndpoint> createScriptEndpoint(final ScriptEndpoint scriptEndpoint) {
        String url = String.format(Constants.SCRIPT_ENDPOINTS_LIST_URL, getNotEmptyInstanceName());
        RequestPost<ScriptEndpoint> req = new RequestPost<>(ScriptEndpoint.class, url, this, scriptEndpoint);
        req.updateGivenObject(true);
        req.setRunAfter(new Request.RunAfter<ScriptEndpoint>() {
            @Override
            public void run(Response<ScriptEndpoint> response) {
                scriptEndpoint.on(SyncanoDashboard.this);
            }
        });
        return req;
    }


    /**
     * Get details of previously created Webhook.
     *
     * @param name Webhook id.
     * @return Existing Webhook.
     */
    @Deprecated
    public RequestGetOne<Webhook> getWebhook(String name) {
        String url = String.format(Constants.SCRIPT_ENDPOINTS_DETAIL_URL, getNotEmptyInstanceName(), name);
        return new RequestGetOne<>(Webhook.class, url, this);
    }

    /**
     * Get details of previously created ScriptEndpoint.
     *
     * @param name ScriptEndpoint id.
     * @return Existing ScriptEndpoint.
     */
    public RequestGetOne<ScriptEndpoint> getScriptEndpoint(String name) {
        String url = String.format(Constants.SCRIPT_ENDPOINTS_DETAIL_URL, getNotEmptyInstanceName(), name);
        return new RequestGetOne<>(ScriptEndpoint.class, url, this);
    }

    /**
     * Get a list of previously created Webhooks.
     *
     * @return List of existing Webhooks.
     */
    @Deprecated
    public RequestGetList<Webhook> getWebhooks() {
        String url = String.format(Constants.SCRIPT_ENDPOINTS_LIST_URL, getNotEmptyInstanceName());
        return new RequestGetList<>(Webhook.class, url, this);
    }

    /**
     * Get a list of previously created ScriptEndpoints.
     *
     * @return List of existing ScriptEndpoints.
     */
    public RequestGetList<ScriptEndpoint> getScriptEndpoints() {
        String url = String.format(Constants.SCRIPT_ENDPOINTS_LIST_URL, getNotEmptyInstanceName());
        return new RequestGetList<>(ScriptEndpoint.class, url, this);
    }

    /**
     * Update a Webhook.
     *
     * @param webhook Webhook to update. It has to have a name.
     * @return Updated Webhook.
     */
    @Deprecated
    public RequestPatch<Webhook> updateWebhook(Webhook webhook) {
        Validate.checkNotNullAndNotEmpty(webhook.getName(), "Trying to update Webhook without name!");
        String url = String.format(Constants.SCRIPT_ENDPOINTS_DETAIL_URL, getNotEmptyInstanceName(), webhook.getName());
        return new RequestPatch<>(Webhook.class, url, this, webhook);
    }

    /**
     * Update a ScriptEndpoint.
     *
     * @param scriptEndpoint ScriptEndpoint to update. It has to have a name.
     * @return Updated ScriptEndpoint.
     */
    public RequestPatch<ScriptEndpoint> updateScriptEndpoint(ScriptEndpoint scriptEndpoint) {
        Validate.checkNotNullAndNotEmpty(scriptEndpoint.getName(), "Trying to update ScriptEndpoint without name!");
        String url = String.format(Constants.SCRIPT_ENDPOINTS_DETAIL_URL, getNotEmptyInstanceName(), scriptEndpoint.getName());
        return new RequestPatch<>(ScriptEndpoint.class, url, this, scriptEndpoint);
    }

    /**
     * Delete a Webhook.
     *
     * @param name Webhook name.
     * @return Deleted webhook
     */
    @Deprecated
    public RequestDelete<Webhook> deleteWebhook(String name) {
        String url = String.format(Constants.SCRIPT_ENDPOINTS_DETAIL_URL, getNotEmptyInstanceName(), name);
        return new RequestDelete<>(Webhook.class, url, this);
    }

    /**
     * Delete a ScriptEndpoint.
     *
     * @param name ScriptEndpoint name.
     * @return Deleted ScriptEndpoint
     */
    public RequestDelete<ScriptEndpoint> deleteScriptEndpoint(String name) {
        String url = String.format(Constants.SCRIPT_ENDPOINTS_DETAIL_URL, getNotEmptyInstanceName(), name);
        return new RequestDelete<>(ScriptEndpoint.class, url, this);
    }

    /**
     * Create a class.
     *
     * @param clazz Class to create.
     * @return Created class.
     */
    public RequestPost<SyncanoClass> createSyncanoClass(SyncanoClass clazz) {
        String url = String.format(Constants.CLASSES_LIST_URL, getNotEmptyInstanceName());
        return new RequestPost<>(SyncanoClass.class, url, this, clazz);
    }

    /**
     * Create a virtual view.
     *
     * @param clazz View class to create.
     * @return Created class.
     */
    @Deprecated
    public RequestPost<SyncanoTableView> createTableView(SyncanoTableView clazz) {
        String url = String.format(Constants.DATA_ENDPOINT_CREATE, getNotEmptyInstanceName());
        return new RequestPost<>(SyncanoTableView.class, url, this, clazz);
    }

    /**
     * Create a data endpoint
     *
     * @param clazz Data endpoint to create.
     * @return Created endpoint.
     */
    public RequestPost<DataEndpoint> createDataEndpoint(DataEndpoint clazz) {
        String url = String.format(Constants.DATA_ENDPOINT_CREATE, getNotEmptyInstanceName());
        return new RequestPost<>(DataEndpoint.class, url, this, clazz);
    }

    /**
     * Delete a virtual view.
     *
     * @param name View to delete.
     * @return RequestDelete<SyncanoTableView>
     */
    @Deprecated
    public RequestDelete<SyncanoTableView> deleteTableView(String name) {
        String url = String.format(Constants.DATA_ENDPOINT_REMOVE, getNotEmptyInstanceName(), name);
        return new RequestDelete<>(SyncanoTableView.class, url, this);
    }

    /**
     * Delete a data endpoint.
     *
     * @param name Data endpoint to delete.
     * @return RequestDelete<DataEndpoint>
     */
    public RequestDelete<DataEndpoint> deleteDataEndpoint(String name) {
        String url = String.format(Constants.DATA_ENDPOINT_REMOVE, getNotEmptyInstanceName(), name);
        return new RequestDelete<>(DataEndpoint.class, url, this);
    }

    /**
     * Create a class.
     *
     * @param clazz Class to create.
     * @return Created class.
     */
    public RequestPost<SyncanoClass> createSyncanoClass(Class<? extends SyncanoObject> clazz) {
        return createSyncanoClass(new SyncanoClass(clazz));
    }

    /**
     * Get an information about selected Class.
     *
     * @param name Class name.
     * @return Existing class.
     */
    public RequestGetOne<SyncanoClass> getSyncanoClass(String name) {
        String url = String.format(Constants.CLASSES_DETAIL_URL, getNotEmptyInstanceName(), name);
        return new RequestGetOne<>(SyncanoClass.class, url, this);
    }

    /**
     * Get an information about selected Class.
     *
     * @param clazz Class.
     * @return Existing class.
     */
    public RequestGetOne<SyncanoClass> getSyncanoClass(Class<? extends SyncanoObject> clazz) {
        return getSyncanoClass(SyncanoClassHelper.getSyncanoClassName(clazz));
    }

    /**
     * Get a list of Classes associated with an Instance.
     *
     * @return List of classes.
     */
    public RequestGetList<SyncanoClass> getSyncanoClasses() {
        String url = String.format(Constants.CLASSES_LIST_URL, getNotEmptyInstanceName());
        return new RequestGetList<>(SyncanoClass.class, url, this);
    }

    /**
     * Update a Class.
     *
     * @param clazz SyncanoClass to update. It has to have a name.
     * @return Updated Class.
     */
    public RequestPatch<SyncanoClass> updateSyncanoClass(Class<? extends SyncanoObject> clazz) {
        return updateSyncanoClass(new SyncanoClass(clazz));
    }

    /**
     * Update a Class.
     *
     * @param clazz SyncanoClass to update. It need to have name.
     * @return Updated Class.
     */
    public RequestPatch<SyncanoClass> updateSyncanoClass(SyncanoClass clazz) {
        Validate.checkNotNullAndNotEmpty(clazz.getName(), "Trying to update SyncanoClass without giving name!");
        String url = String.format(Constants.CLASSES_DETAIL_URL, getNotEmptyInstanceName(), clazz.getName());
        return new RequestPatch<>(SyncanoClass.class, url, this, clazz);
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

    /**
     * Delete a Class.
     *
     * @param name Class to delete.
     * @return RequestDelete<SyncanoClass>
     */
    public RequestDelete<SyncanoClass> deleteSyncanoClass(String name) {
        String url = String.format(Constants.CLASSES_DETAIL_URL, getNotEmptyInstanceName(), name);
        return new RequestDelete<>(SyncanoClass.class, url, this);
    }

    /**
     * Delete a User.
     *
     * @param id Id of existing User.
     * @return Information about success or error
     */
    public RequestDelete<User> deleteUser(int id) {
        return deleteUser(User.class, id);
    }


    /**
     * Delete a custom User.
     *
     * @param id Id of existing User.
     * @return Information about success or error
     */
    public <T extends AbstractUser> RequestDelete<T> deleteUser(Class<T> type, int id) {
        String url = String.format(Constants.USERS_DETAIL_URL, getNotEmptyInstanceName(), id);
        return new RequestDelete<>(type, url, this);
    }


    /**
     * Create a new Group.
     *
     * @param group Group to create.
     * @return Created group
     */
    public RequestPost<Group> createGroup(Group group) {
        String url = String.format(Constants.GROUPS_LIST_URL, getNotEmptyInstanceName());
        return new RequestPost<>(Group.class, url, this, group);
    }

    /**
     * Get details of previously created Group.
     *
     * @param id Id of existing Group.
     * @return Requested group
     */
    public RequestGetOne<Group> getGroup(int id) {
        String url = String.format(Constants.GROUPS_DETAIL_URL, getNotEmptyInstanceName(), id);
        return new RequestGetOne<>(Group.class, url, this);
    }

    /**
     * Get a list of previously created Groups.
     *
     * @return All groups
     */
    public RequestGetList<Group> getGroups() {
        String url = String.format(Constants.GROUPS_LIST_URL, getNotEmptyInstanceName());
        return new RequestGetList<>(Group.class, url, this);
    }

    /**
     * Update a Group.
     *
     * @param group Group to update. It need to have id.
     * @return updated group
     */
    public RequestPatch<Group> updateGroup(Group group) {
        Validate.checkNotNullAndZero(group.getId(), "Trying to update Group without id!");
        String url = String.format(Constants.GROUPS_DETAIL_URL, getNotEmptyInstanceName(), group.getId());
        return new RequestPatch<>(Group.class, url, this, group);
    }

    /**
     * Delete a Group.
     *
     * @param id Id of existing Group.
     * @return Success or error information
     */
    public RequestDelete<Group> deleteGroup(int id) {
        String url = String.format(Constants.GROUPS_DETAIL_URL, getNotEmptyInstanceName(), id);
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
        String url = String.format(Constants.GROUPS_USERS_DETAIL_URL, getNotEmptyInstanceName(), groupId, userId);
        return new RequestGetOne<>(GroupMembership.class, url, this);
    }

    /**
     * Get a list of previously created Memberships.
     *
     * @param groupId Id of existing Group.
     * @return Users wrapped id a memberships objects
     */
    public RequestGetList<GroupMembership> getGroupMemberships(int groupId) {
        String url = String.format(Constants.GROUPS_USERS_LIST_URL, getNotEmptyInstanceName(), groupId);
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
        String url = String.format(Constants.GROUPS_USERS_LIST_URL, getNotEmptyInstanceName(), groupId);
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
        String url = String.format(Constants.GROUPS_USERS_DETAIL_URL, getNotEmptyInstanceName(), groupId, userId);
        return new RequestDelete<>(GroupMembership.class, url, this);
    }


    /**
     * Create a new Channel
     *
     * @param channel Channel to create.
     * @return New Channel.
     */
    public RequestPost<Channel> createChannel(Channel channel) {
        String url = String.format(Constants.CHANNELS_LIST_URL, getNotEmptyInstanceName());
        return new RequestPost<>(Channel.class, url, this, channel);
    }

    /**
     * Get details of previously created Channel.
     *
     * @param channelName Channel id.
     * @return Existing Channel.
     */
    public RequestGetOne<Channel> getChannel(String channelName) {
        String url = String.format(Constants.CHANNELS_DETAIL_URL, getNotEmptyInstanceName(), channelName);
        return new RequestGetOne<>(Channel.class, url, this);
    }

    /**
     * Get a list of previously created Channels.
     *
     * @return List of existing Channels.
     */
    public RequestGetList<Channel> getChannels() {
        String url = String.format(Constants.CHANNELS_LIST_URL, getNotEmptyInstanceName());
        return new RequestGetList<>(Channel.class, url, this);
    }

    /**
     * Update a Channel.
     *
     * @param channel Channel to update. It need to have name.
     * @return Updated Channel.
     */
    public RequestPatch<Channel> updateChannel(Channel channel) {
        Validate.checkNotNullAndNotEmpty(channel.getName(), "Trying to update Channel without name!");
        String url = String.format(Constants.CHANNELS_DETAIL_URL, getNotEmptyInstanceName(), channel.getName());
        return new RequestPatch<>(Channel.class, url, this, channel);
    }

    /**
     * Delete a Channel.
     *
     * @param channelName Channel name.
     * @return null
     */
    public RequestDelete<Channel> deleteChannel(String channelName) {
        String url = String.format(Constants.CHANNELS_DETAIL_URL, getNotEmptyInstanceName(), channelName);
        return new RequestDelete<>(Channel.class, url, this);
    }


    /**
     * Get a list of previously created Users.
     *
     * @return requested users
     */
    public RequestGetList<User> getUsers() {
        return getUsers(User.class);
    }

    /**
     * Get a list of previously created custom Users.
     *
     * @return requested users
     */
    public <T extends AbstractUser> RequestGetList<T> getUsers(Class<T> type) {
        String url = String.format(Constants.USERS_LIST_URL, getNotEmptyInstanceName());
        return new RequestGetList<>(type, url, this);
    }
}
