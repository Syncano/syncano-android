package com.syncano.android.lib;

import com.syncano.android.lib.api.RequestDelete;
import com.syncano.android.lib.api.RequestGet;
import com.syncano.android.lib.api.RequestGetList;
import com.syncano.android.lib.api.RequestGetOne;
import com.syncano.android.lib.api.RequestPatch;
import com.syncano.android.lib.api.RequestPost;
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
     */
    public <T extends SyncanoObject> RequestPost createObject(T object) {

        Class<T> type = (Class<T>) object.getClass();
        String className = getSyncanoClassName(type);
        String url = String.format(Constants.OBJECTS_LIST_URL, getInstance(), className);
        return new RequestPost(type, url, this, object);
    }

    public <T extends SyncanoObject> RequestGetOne getObject(Class<T> type, int id) {

        String className = getSyncanoClassName(type);
        String url = String.format(Constants.OBJECTS_DETAIL_URL, getInstance(), className, id);
        return new RequestGetOne(type, url, this);
    }

    public <T extends SyncanoObject> RequestGetList getObjects(Class<T> type) {
        String className = getSyncanoClassName(type);
        String url = String.format(Constants.OBJECTS_LIST_URL, getInstance(), className);
        return new RequestGetList(type, url, this);
    }

    public <T extends SyncanoObject> RequestPatch updateObject(T object) {

        if (object.getId() == 0) {
            throw new RuntimeException("Trying to update object without id!");
        }

        Class<T> type = (Class<T>) object.getClass();
        String className = getSyncanoClassName(type);
        String url = String.format(Constants.OBJECTS_DETAIL_URL, getInstance(), className, object.getId());
        return new RequestPatch(type, url, this, object);
    }

    public <T extends SyncanoObject> RequestDelete deleteObject(Class<T> type, int id) {

        String className = getSyncanoClassName(type);
        String url = String.format(Constants.OBJECTS_DETAIL_URL, getInstance(), className, id);
        return new RequestDelete(type, url, this);
    }

    // ==================== CodeBoxes ==================== //

    public RequestPost createCodeBox(CodeBox codeBox) {
        String url = String.format(Constants.CODEBOXES_LIST_URL, getInstance());
        return new RequestPost(CodeBox.class, url, this, codeBox);
    }

    public RequestGetOne getCodeBox(int id) {
        String url = String.format(Constants.CODEBOXES_DETAIL_URL, getInstance(), id);
        return new RequestGetOne(CodeBox.class, url, this);
    }

    public RequestGetList getCodeBoxes() {
        String url = String.format(Constants.CODEBOXES_LIST_URL, getInstance());
        return new RequestGetList(CodeBox.class, url, this);
    }

    public RequestPatch updateCodeBox(CodeBox codeBox) {

        if (codeBox.getId() == 0) {
            throw new RuntimeException("Trying to update object without id!");
        }

        String url = String.format(Constants.CODEBOXES_DETAIL_URL, getInstance(), codeBox.getId());
        return new RequestPatch(CodeBox.class, url, this, codeBox);
    }

    public RequestDelete deleteCodeBox(int id) {

        String url = String.format(Constants.CODEBOXES_DETAIL_URL, getInstance(), id);
        return new RequestDelete(CodeBox.class, url, this);
    }

    public RequestPost runCodeBox(int id) {
        String url = String.format(Constants.CODEBOXES_RUN_URL, getInstance(), id);
        return new RequestPost(CodeBox.class, url, this, null);
    }

    // ==================== Webhooks ==================== //

    public RequestPost createWebhook(Webhook webhook) {
        String url = String.format(Constants.WEBHOOKS_LIST_URL, getInstance());
        return new RequestPost(Webhook.class, url, this, webhook);
    }

    public RequestGetOne getWebhook(String slug) {
        String url = String.format(Constants.WEBHOOKS_DETAIL_URL, getInstance(), slug);
        return new RequestGetOne(Webhook.class, url, this);
    }

    public RequestGetList getWebhooks() {
        String url = String.format(Constants.WEBHOOKS_LIST_URL, getInstance());
        return new RequestGetList(Webhook.class, url, this);
    }

    public RequestPatch updateWebhook(Webhook webhook) {

        if (webhook.getSlug() == null || webhook.getSlug().isEmpty()) {
            throw new RuntimeException("Trying to update Webhook without slug!");
        }

        String url = String.format(Constants.WEBHOOKS_DETAIL_URL, getInstance(), webhook.getSlug());
        return new RequestPatch(Webhook.class, url, this, webhook);
    }

    public RequestDelete deleteWebhook(String slug) {

        String url = String.format(Constants.WEBHOOKS_DETAIL_URL, getInstance(), slug);
        return new RequestDelete(Webhook.class, url, this);
    }

    public RequestGetOne runWebhook(String slug) {
        String url = String.format(Constants.WEBHOOKS_RUN_URL, getInstance(), slug);
        return new RequestGetOne(RunCodeBoxResult.class, url, this);
    }
}