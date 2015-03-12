package com.syncano.android.lib;

import com.syncano.android.lib.api.Page;
import com.syncano.android.lib.api.Params;
import com.syncano.android.lib.callbacks.DeleteCallback;
import com.syncano.android.lib.callbacks.GetCallback;
import com.syncano.android.lib.data.CodeBox;
import com.syncano.android.lib.data.RunCodeBoxResult;
import com.syncano.android.lib.data.SyncanoObject;
import com.syncano.android.lib.data.Webhook;

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
     * @param object Object that will be created.
     *               This object should match class created on Syncano.
     *               Fields should be marked with @SyncanoField annotation.
     * @param callback Notifies about success or fail. Returns created object.
     * @param <T> Type of object to save. It should be marked with @SyncanoClass annotation.
     */
    public <T extends SyncanoObject> void createObject(T object, GetCallback<T> callback) {

        Class<T> type = (Class<T>) object.getClass();
        String className = getSyncanoClassName(type);
        String url = String.format(Constants.OBJECTS_LIST_URL, getInstance(), className);
        requestCreate(type, url, object, callback);
    }

    public <T extends SyncanoObject> void getObject(Class<T> type, int id, Params params, GetCallback<T> callback) {

        String className = getSyncanoClassName(type);
        String url = String.format(Constants.OBJECTS_DETAIL_URL, getInstance(), className, id);
        requestGetOne(type, url, params, callback);
    }

    public <T extends SyncanoObject> void getObjectsPage(Class<T> type, Params params, GetCallback<Page<T>> callback) {

        String className = getSyncanoClassName(type);
        String url = String.format(Constants.OBJECTS_LIST_URL, getInstance(), className);
        requestGetPage(type, url, params, callback);
    }

    public <T extends SyncanoObject> void updateObject(T object, GetCallback<T> callback) {

        if (object.getId() == 0) {
            throw new RuntimeException("Trying to update object without id!");
        }

        Class<T> type = (Class<T>) object.getClass();
        String className = getSyncanoClassName(type);
        String url = String.format(Constants.OBJECTS_DETAIL_URL, getInstance(), className, object.getId());
        requestUpdate(type, url, object, callback);
    }

    public <T extends SyncanoObject> void deleteObject(Class<T> type, int id, DeleteCallback callback) {

        String className = getSyncanoClassName(type);
        String url = String.format(Constants.OBJECTS_DETAIL_URL, getInstance(), className, id);
        requestDelete(url, callback);
    }

    // ==================== CodeBoxes ==================== //

    public void createCodeBox(CodeBox codeBox, GetCallback<CodeBox> callback) {
        String url = String.format(Constants.CODEBOXES_LIST_URL, getInstance());
        requestCreate(CodeBox.class, url, codeBox, callback);
    }

    public void getCodeBox(int id, GetCallback<CodeBox> callback) {
        String url = String.format(Constants.CODEBOXES_DETAIL_URL, getInstance(), id);
        requestGetOne(CodeBox.class, url, null, callback);
    }

    public void getCodeBoxes(GetCallback<Page<CodeBox>> callback) {
        String url = String.format(Constants.CODEBOXES_LIST_URL, getInstance());
        requestGetPage(CodeBox.class, url, null, callback);
    }

    public void updateCodeBox(CodeBox codeBox, GetCallback<CodeBox> callback) {

        if (codeBox.getId() == 0) {
            throw new RuntimeException("Trying to update object without id!");
        }

        String url = String.format(Constants.CODEBOXES_DETAIL_URL, getInstance(), codeBox.getId());
        requestUpdate(CodeBox.class, url, codeBox, callback);
    }

    public void deleteCodeBox(int id, DeleteCallback callback) {

        String url = String.format(Constants.CODEBOXES_DETAIL_URL, getInstance(), id);
        requestDelete(url, callback);
    }

    // ==================== Webhooks ==================== //

    public void createWebhook(Webhook webhook, GetCallback<Webhook> callback) {
        String url = String.format(Constants.WEBHOOKS_LIST_URL, getInstance());
        requestCreate(Webhook.class, url, webhook, callback);
    }

    public void getWebhook(String slug, GetCallback<Webhook> callback) {
        String url = String.format(Constants.WEBHOOKS_DETAIL_URL, getInstance(), slug);
        requestGetOne(Webhook.class, url, null, callback);
    }

    public void getWebhooks(GetCallback<Page<Webhook>> callback) {
        String url = String.format(Constants.WEBHOOKS_LIST_URL, getInstance());
        requestGetPage(Webhook.class, url, null, callback);
    }

    public void updateWebhook(Webhook webhook, GetCallback<Webhook> callback) {

        if (webhook.getSlug() == null || webhook.getSlug().isEmpty()) {
            throw new RuntimeException("Trying to update Webhook without slug!");
        }

        String url = String.format(Constants.WEBHOOKS_DETAIL_URL, getInstance(), webhook.getSlug());
        requestUpdate(Webhook.class, url, webhook, callback);
    }

    public void deleteWebhook(String slug, DeleteCallback callback) {

        String url = String.format(Constants.WEBHOOKS_DETAIL_URL, getInstance(), slug);
        requestDelete(url, callback);
    }

    public void runWebhook(String slug, GetCallback<RunCodeBoxResult> callback) {
        String url = String.format(Constants.WEBHOOKS_RUN_URL, getInstance(), slug);
        requestGetOne(RunCodeBoxResult.class, url, null, callback);
    }
}