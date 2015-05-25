package com.syncano.library;

import com.google.gson.JsonObject;
import com.syncano.library.api.RequestDelete;
import com.syncano.library.api.RequestGetList;
import com.syncano.library.api.RequestGetOne;
import com.syncano.library.api.RequestPatch;
import com.syncano.library.api.RequestPost;
import com.syncano.library.data.CodeBox;
import com.syncano.library.data.RunCodeBoxResult;
import com.syncano.library.data.SyncanoClass;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.data.User;
import com.syncano.library.data.Webhook;

public class Syncano extends SyncanoBase {

    private static Syncano sharedInstance = null;

    /**
     * Create Syncano object.
     * @param apiKey Api key.
     * @param instance Syncano instance related with apiKey.
     */
    public Syncano(String apiKey, String instance) {
        super(apiKey, instance);
    }

    /**
     * Create static Syncano instance.
     * Use getSharedInstance() to get its reference.
     * @param apiKey Api key.
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
     * @param object Object to create. It need to extend SyncanoObject.
     * @param <T> Result type.
     * @return New DataObject.
     */
    public <T extends SyncanoObject> RequestPost createObject(T object) {

        Class<T> type = (Class<T>) object.getClass();
        String className = getSyncanoClassName(type);
        String url = String.format(Constants.OBJECTS_LIST_URL, getInstance(), className);
        return new RequestPost(type, url, this, object);
    }

    /**
     * Get details of a Data Object.
     * @param type Type of the object.
     * @param id Object id.
     * @param <T> Result type.
     * @return Existing DataObject.
     */
    public <T extends SyncanoObject> RequestGetOne getObject(Class<T> type, int id) {

        String className = getSyncanoClassName(type);
        String url = String.format(Constants.OBJECTS_DETAIL_URL, getInstance(), className, id);
        return new RequestGetOne(type, url, this);
    }

    /**
     * Get a list of Data Objects associated with a given Class.
     * @param type Type for result List item.
     * @param <T> Result type.
     * @return List of DataObjects.
     */
    public <T extends SyncanoObject> RequestGetList getObjects(Class<T> type) {
        String className = getSyncanoClassName(type);
        String url = String.format(Constants.OBJECTS_LIST_URL, getInstance(), className);
        return new RequestGetList(type, url, this);
    }

    /**
     * Update Data Object.
     * @param object Object to update. It need to have id.
     * @param <T> Result type.
     * @return Updated DataObject.
     */
    public <T extends SyncanoObject> RequestPatch updateObject(T object) {

        if (object.getId() == 0) {
            throw new RuntimeException("Trying to update object without id!");
        }

        Class<T> type = (Class<T>) object.getClass();
        String className = getSyncanoClassName(type);
        String url = String.format(Constants.OBJECTS_DETAIL_URL, getInstance(), className, object.getId());
        return new RequestPatch(type, url, this, object);
    }

    /**
     * Delete a Data Object.
     * @param type Type of object to delete.
     * @param id Object id.
     * @param <T> Result type.
     * @return null
     */
    public <T extends SyncanoObject> RequestDelete deleteObject(Class<T> type, int id) {

        String className = getSyncanoClassName(type);
        String url = String.format(Constants.OBJECTS_DETAIL_URL, getInstance(), className, id);
        return new RequestDelete(type, url, this);
    }

    // ==================== CodeBoxes ==================== //

    /**
     * Create a CodeBox.
     * @param codeBox CodeBox to create.
     * @return New CodeBox.
     */
    public RequestPost createCodeBox(CodeBox codeBox) {
        String url = String.format(Constants.CODEBOXES_LIST_URL, getInstance());
        return new RequestPost(CodeBox.class, url, this, codeBox);
    }

    /**
     * Get details of previously created CodeBox.
     * @param id CodeBox id.
     * @return Existing CodeBox.
     */
    public RequestGetOne getCodeBox(int id) {
        String url = String.format(Constants.CODEBOXES_DETAIL_URL, getInstance(), id);
        return new RequestGetOne(CodeBox.class, url, this);
    }

    /**
     * Get a list of previously created CodeBoxes.
     * @return List of existing CodeBoxes.
     */
    public RequestGetList getCodeBoxes() {
        String url = String.format(Constants.CODEBOXES_LIST_URL, getInstance());
        return new RequestGetList(CodeBox.class, url, this);
    }

    /**
     * Update a CodeBox.
     * @param codeBox CodeBox to update. It need to have id.
     * @return Updated CodeBox.
     */
    public RequestPatch updateCodeBox(CodeBox codeBox) {

        if (codeBox.getId() == 0) {
            throw new RuntimeException("Trying to update object without id!");
        }

        String url = String.format(Constants.CODEBOXES_DETAIL_URL, getInstance(), codeBox.getId());
        return new RequestPatch(CodeBox.class, url, this, codeBox);
    }

    /**
     * Delete previously created CodeBox.
     * @param id CodeBox id.
     * @return null
     */
    public RequestDelete deleteCodeBox(int id) {

        String url = String.format(Constants.CODEBOXES_DETAIL_URL, getInstance(), id);
        return new RequestDelete(CodeBox.class, url, this);
    }

    /**
     * Run CodeBox asynchronous. Result of this request is not result of the CodeBox.
     * Result will be stored in associated Trace.
     * @param id CodeBox id.
     * @return Result with link do Trace.
     */
    public RequestPost runCodeBox(int id) {
        String url = String.format(Constants.CODEBOXES_RUN_URL, getInstance(), id);
        return new RequestPost(CodeBox.class, url, this, null);
    }

    // ==================== Webhooks ==================== //

    /**
     * Create a new Webhook
     * @param webhook Webhook to create.
     * @return New Webhook.
     */
    public RequestPost createWebhook(Webhook webhook) {
        String url = String.format(Constants.WEBHOOKS_LIST_URL, getInstance());
        return new RequestPost(Webhook.class, url, this, webhook);
    }

    /**
     * Get details of previously created Webhook.
     * @param slug Webhook id.
     * @return Existing Webhook.
     */
    public RequestGetOne getWebhook(String slug) {
        String url = String.format(Constants.WEBHOOKS_DETAIL_URL, getInstance(), slug);
        return new RequestGetOne(Webhook.class, url, this);
    }

    /**
     * Get a list of previously created Webhooks.
     * @return List of existing Webhooks.
     */
    public RequestGetList getWebhooks() {
        String url = String.format(Constants.WEBHOOKS_LIST_URL, getInstance());
        return new RequestGetList(Webhook.class, url, this);
    }

    /**
     * Update a Webhook.
     * @param webhook Webhook to update. It need to have slug.
     * @return Updated Webhook.
     */
    public RequestPatch updateWebhook(Webhook webhook) {

        if (webhook.getSlug() == null || webhook.getSlug().isEmpty()) {
            throw new RuntimeException("Trying to update Webhook without slug!");
        }

        String url = String.format(Constants.WEBHOOKS_DETAIL_URL, getInstance(), webhook.getSlug());
        return new RequestPatch(Webhook.class, url, this, webhook);
    }

    /**
     * Delete a Webhook.
     * @param slug Webhook id.
     * @return null
     */
    public RequestDelete deleteWebhook(String slug) {

        String url = String.format(Constants.WEBHOOKS_DETAIL_URL, getInstance(), slug);
        return new RequestDelete(Webhook.class, url, this);
    }

    /**
     * Run a Webhook synchronous. It should contain result of associated CodeBox.
     * @param slug Webhook id.
     * @return Result of executed CodeBox.
     */
    public RequestGetOne runWebhook(String slug) {
        String url = String.format(Constants.WEBHOOKS_RUN_URL, getInstance(), slug);
        return new RequestGetOne(RunCodeBoxResult.class, url, this);
    }

    // ==================== Classes ==================== //

    /**
     * Create a class.
     * @param clazz Class to create.
     * @return Created class.
     */
    public RequestPost createSyncanoClass(SyncanoClass clazz) {
        String url = String.format(Constants.CLASSES_LIST_URL, getInstance());
        return new RequestPost(SyncanoClass.class, url, this, clazz);
    }

    /**
     * Get an information on a selected Class.
     * @param name Class name.
     * @return Existing class.
     */
    public RequestGetOne getSyncanoClass(String name) {
        String url = String.format(Constants.CLASSES_DETAIL_URL, getInstance(), name);
        return new RequestGetOne(SyncanoClass.class, url, this);
    }

    /**
     * Get a list of Classes associated with an Instance.
     * @return List of classes.
     */
    public RequestGetList getSyncanoClasses() {
        String url = String.format(Constants.CLASSES_LIST_URL, getInstance());
        return new RequestGetList(SyncanoClass.class, url, this);
    }

    /**
     * Delete a Class.
     * @param name Class to delete.
     * @return null
     */
    public RequestDelete deleteSyncanoClass(String name) {

        String url = String.format(Constants.CLASSES_DETAIL_URL, getInstance(), name);
        return new RequestDelete(SyncanoClass.class, url, this);
    }

    // ==================== Users ==================== //

    /**
     * Create a new User.
     * To be able to register Users you'll have to create an API Key that has allow_user_create flag set to true.
     * @param user User to create.
     * @return
     */
    public RequestPost createUser(User user) {
        String url = String.format(Constants.USERS_LIST_URL, getInstance());
        return new RequestPost(User.class, url, this, user);
    }

    /**
     * Get details of previously created User.
     * @param id Id of existing User.
     * @return
     */
    public RequestGetOne getUser(int id) {
        String url = String.format(Constants.USERS_DETAIL_URL, getInstance(), id);
        return new RequestGetOne(User.class, url, this);
    }

    /**
     * Get a list of previously created CodeBoxes.
     * @return
     */
    public RequestGetList getUsers() {
        String url = String.format(Constants.USERS_LIST_URL, getInstance());
        return new RequestGetList(User.class, url, this);
    }

    /**
     * Update a User.
     * @param user User to update. It need to have id.
     * @return
     */
    public RequestPatch updateUser(User user) {

        if (user.getId() == 0 ) {
            throw new RuntimeException("Trying to update User without id!");
        }

        String url = String.format(Constants.USERS_DETAIL_URL, getInstance(), user.getId());
        return new RequestPatch(User.class, url, this, user);
    }

    /**
     * Delete a User.
     * @param id Id of existing User.
     * @return
     */
    public RequestDelete deleteUser(int id) {

        String url = String.format(Constants.USERS_DETAIL_URL, getInstance(), id);
        return new RequestDelete(User.class, url, this);
    }

    /**
     * Authenticate a User.
     * @param username User name from registration.
     * @param password User password.
     * @return
     */
    public RequestPost authUser(String username, String password) {
        String url = String.format(Constants.USER_AUTH, getInstance());

        JsonObject jsonParams = new JsonObject();
        jsonParams.addProperty(User.FIELD_USER_NAME, username);
        jsonParams.addProperty(User.FIELD_PASSWORD, password);

        return new RequestPost(User.class, url, this, jsonParams);
    }

    // ==================== Groups ==================== //

}