package com.syncano.library;

import com.google.gson.JsonObject;
import com.syncano.library.api.HttpRequest;
import com.syncano.library.api.IncrementBuilder;
import com.syncano.library.api.RequestDelete;
import com.syncano.library.api.RequestGet;
import com.syncano.library.api.RequestGetList;
import com.syncano.library.api.RequestGetOne;
import com.syncano.library.api.RequestPatch;
import com.syncano.library.api.RequestPost;
import com.syncano.library.api.Response;
import com.syncano.library.choice.SocialAuthBackend;
import com.syncano.library.data.AbstractUser;
import com.syncano.library.data.Channel;
import com.syncano.library.data.CodeBox;
import com.syncano.library.data.Group;
import com.syncano.library.data.GroupMembership;
import com.syncano.library.data.Notification;
import com.syncano.library.data.SyncanoClass;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.data.Trace;
import com.syncano.library.data.User;
import com.syncano.library.data.Webhook;
import com.syncano.library.simple.RequestBuilder;
import com.syncano.library.utils.Encryption;
import com.syncano.library.utils.SyncanoClassHelper;

public class Syncano {

    private static Syncano sharedInstance = null;
    protected String customServerUrl;
    protected String apiKey;
    protected String userKey;
    protected String instanceName;

    /**
     * Create Syncano object.
     */
    public Syncano() {
        this(null, null);
    }

    /**
     * Create Syncano object.
     *
     * @param instanceName Syncano instanceName related with apiKey.
     */
    public Syncano(String instanceName) {
        this(null, instanceName);
    }

    /**
     * Create Syncano object.
     *
     * @param apiKey       Api key.
     * @param instanceName Syncano instanceName related with apiKey.
     */
    public Syncano(String apiKey, String instanceName) {
        this.apiKey = apiKey;
        this.instanceName = instanceName;
    }

    /**
     * Create Syncano object.
     *
     * @param customServerUrl If not set, production URL will be used.
     * @param apiKey          Api key.
     * @param instanceName    Syncano instanceName related with apiKey.
     */
    public Syncano(String customServerUrl, String apiKey, String instanceName) {
        this(apiKey, instanceName);
        this.customServerUrl = customServerUrl;
    }

    /**
     * Create static Syncano instance.
     * Use getInstance() to get its reference.
     *
     * @param customServerUrl If not set, production URL will be used.
     * @param apiKey          Api key.
     * @param instanceName    Syncano instanceName related with apiKey.
     */
    public static void init(String customServerUrl, String apiKey, String instanceName) {
        sharedInstance = new Syncano(customServerUrl, apiKey, instanceName);
    }

    /**
     * Create static Syncano instance.
     * Use getInstance() to get its reference.
     *
     * @param apiKey       Api key.
     * @param instanceName Syncano instanceName related with apiKey.
     */
    public static void init(String apiKey, String instanceName) {
        init(null, apiKey, instanceName);
    }

    public static void setStrictCheckCertificate(boolean strictCheck) {
        Encryption.setStrictCheckCertificate(strictCheck);
    }

    public static Syncano getInstance() {
        return sharedInstance;
    }

    public static void setInstance(Syncano syncano) {
        sharedInstance = syncano;
    }

    public static <T extends SyncanoObject> RequestBuilder<T> please(Class<T> clazz) {
        return new RequestBuilder<>(clazz);
    }

    public String getInstanceName() {
        return instanceName;
    }

    public String getNotEmptyInstanceName() {
        if (instanceName == null || instanceName.isEmpty()) {
            throw new RuntimeException("Syncano instance name is not set");
        }
        return instanceName;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    /**
     * Create object on Syncano.
     *
     * @param object Object to create. It has to extend SyncanoObject.
     */
    public <T extends SyncanoObject> RequestPost<T> createObject(T object) {
        return createObject(object, false);
    }

    /**
     * Create object on Syncano.
     *
     * @param object            Object to create. It has to extend SyncanoObject.
     * @param updateGivenObject Should update fields in passed object, or only return the new created object
     */
    public <T extends SyncanoObject> RequestPost<T> createObject(T object, boolean updateGivenObject) {
        Class<T> type = (Class<T>) object.getClass();
        String className = SyncanoClassHelper.getSyncanoClassName(type);
        String url = String.format(Constants.OBJECTS_LIST_URL, getNotEmptyInstanceName(), className);
        RequestPost<T> req = new RequestPost<>(type, url, this, object);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_CREATED);
        req.updateGivenObject(updateGivenObject);
        return req;
    }

    /**
     * Get a Data Object from Syncano.
     *
     * @param type Type of the object.
     * @param id   Object id.
     */
    public <T extends SyncanoObject> RequestGetOne<T> getObject(Class<T> type, int id) {
        String className = SyncanoClassHelper.getSyncanoClassName(type);
        String url = String.format(Constants.OBJECTS_DETAIL_URL, getNotEmptyInstanceName(), className, id);
        RequestGetOne<T> req = new RequestGetOne<>(type, url, this);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        return req;
    }

    /**
     * Get a Data Object from Syncano.
     *
     * @param object Object to get. Has to have id. Other fields will be updated.
     */
    public <T extends SyncanoObject> RequestGetOne<T> getObject(T object) {
        if (object.getId() == null) {
            throw new RuntimeException("Can't fetch object without id");
        }
        String className = SyncanoClassHelper.getSyncanoClassName(object.getClass());
        String url = String.format(Constants.OBJECTS_DETAIL_URL, getNotEmptyInstanceName(), className, object.getId());
        RequestGetOne<T> req = new RequestGetOne<>(object, url, this);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        return req;
    }

    /**
     * Get a list of Data Objects associated with a given Class.
     *
     * @param type Type for result List item.
     * @param <T>  Result type.
     */
    public <T extends SyncanoObject> RequestGetList<T> getObjects(Class<T> type) {
        String className = SyncanoClassHelper.getSyncanoClassName(type);
        String url = String.format(Constants.OBJECTS_LIST_URL, getNotEmptyInstanceName(), className);
        RequestGetList<T> req = new RequestGetList<>(type, url, this);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        return req;
    }

    public <T extends SyncanoObject> RequestGetList<T> getObjects(Class<T> type, String pageUrl) {
        RequestGetList<T> req = new RequestGetList<>(type, pageUrl, this);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        return req;
    }

    /**
     * Update Data Object on Syncano.
     *
     * @param object Object to update. It has to have id. Fields that are not null will be updated.
     */
    public <T extends SyncanoObject> RequestPatch<T> updateObject(T object) {
        return updateObject(object, false);
    }

    /**
     * Update Data Object on Syncano.
     *
     * @param object            Object to update. It has to have id. Fields that are not null will be updated.
     * @param updateGivenObject Should update the passed object. Param updatedAt will be changed for example.
     */
    public <T extends SyncanoObject> RequestPatch<T> updateObject(T object, boolean updateGivenObject) {
        if (object.getId() == null || object.getId() == 0) {
            throw new RuntimeException("Trying to update object without id!");
        }
        Class<T> type = (Class<T>) object.getClass();
        String className = SyncanoClassHelper.getSyncanoClassName(type);
        String url = String.format(Constants.OBJECTS_DETAIL_URL, getNotEmptyInstanceName(), className, object.getId());
        RequestPatch<T> req = new RequestPatch<>(type, url, this, object);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        req.updateGivenObject(updateGivenObject);
        return req;
    }

    /**
     * Change counter field in object.
     * We could do it by updating it's value but to avoid conflicts
     * for situations where multiple user change object at the same time
     * it's much safer to user our increase/decrease api.
     *
     * @param object Object to update. It need to have id.
     * @param <T>    Result type.
     * @return Updated DataObject.
     */
    public <T extends SyncanoObject> RequestPatch<T> addition(T object, IncrementBuilder incrementBuilder) {
        if (object.getId() == null || object.getId() == 0) {
            throw new RuntimeException("Trying to update object without id!");
        }
        if (incrementBuilder.isAdditionFields()) {
            throw new RuntimeException("Cannot create increment query without specify fields to increment/decrement!");
        }
        Class<T> type = (Class<T>) object.getClass();
        String className = SyncanoClassHelper.getSyncanoClassName(type);
        String url = String.format(Constants.OBJECTS_DETAIL_URL, getNotEmptyInstanceName(), className, object.getId());
        JsonObject additionQuery = new JsonObject();
        incrementBuilder.build(additionQuery);
        RequestPatch<T> req = new RequestPatch<>(type, url, this, additionQuery);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        return req;
    }

    /**
     * Change counter field in object.
     * We could do it by updating it's value but to avoid conflicts
     * for situations where multiple user change object at the same time
     * it's much safer to user our increase/decrease api.
     *
     * @param type Type of the object.
     * @param id   Object id.
     * @param <T>  Result type.
     * @return Updated DataObject.
     */
    public <T extends SyncanoObject> RequestPatch<T> addition(Class<T> type, int id, IncrementBuilder incrementBuilder) {
        if (incrementBuilder.isAdditionFields()) {
            throw new RuntimeException("Cannot create increment query without specify fields to increment/decrement!");
        }
        String className = SyncanoClassHelper.getSyncanoClassName(type);
        String url = String.format(Constants.OBJECTS_DETAIL_URL, getNotEmptyInstanceName(), className, id);
        JsonObject additionQuery = new JsonObject();
        incrementBuilder.build(additionQuery);
        RequestPatch<T> req = new RequestPatch<>(type, url, this, additionQuery);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        return req;
    }

    /**
     * Delete a Data Object on Syncano.
     *
     * @param type Type of object to delete.
     * @param id   Object id.
     */
    public <T extends SyncanoObject> RequestDelete<T> deleteObject(Class<T> type, int id) {
        String className = SyncanoClassHelper.getSyncanoClassName(type);
        String url = String.format(Constants.OBJECTS_DETAIL_URL, getNotEmptyInstanceName(), className, id);
        RequestDelete<T> req = new RequestDelete<>(type, url, this);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_NO_CONTENT);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_NOT_FOUND);
        return req;
    }

    /**
     * Delete a Data Object.
     *
     * @param object Object to delete. it has to have id set.
     */
    public <T extends SyncanoObject> RequestDelete<T> deleteObject(T object) {
        if (object.getId() == null || object.getId() == 0) {
            throw new RuntimeException("Trying to delete object without id!");
        }
        return deleteObject((Class<T>) object.getClass(), object.getId());
    }

    /**
     * Create a CodeBox.
     *
     * @param codeBox CodeBox to create.
     * @return New CodeBox.
     */
    public RequestPost<CodeBox> createCodeBox(CodeBox codeBox) {
        String url = String.format(Constants.CODEBOXES_LIST_URL, getNotEmptyInstanceName());
        RequestPost<CodeBox> req = new RequestPost<>(CodeBox.class, url, this, codeBox);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_CREATED);
        return req;
    }

    /**
     * Get details of previously created CodeBox.
     *
     * @param id CodeBox id.
     * @return Existing CodeBox.
     */
    public RequestGetOne<CodeBox> getCodeBox(int id) {
        String url = String.format(Constants.CODEBOXES_DETAIL_URL, getNotEmptyInstanceName(), id);
        RequestGetOne<CodeBox> req = new RequestGetOne<>(CodeBox.class, url, this);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        return req;
    }

    /**
     * Get a list of previously created CodeBoxes.
     *
     * @return List of existing CodeBoxes.
     */
    public RequestGetList<CodeBox> getCodeBoxes() {
        String url = String.format(Constants.CODEBOXES_LIST_URL, getNotEmptyInstanceName());
        RequestGetList<CodeBox> req = new RequestGetList<>(CodeBox.class, url, this);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        return req;
    }

    /**
     * Update a CodeBox.
     *
     * @param codeBox CodeBox to update. It need to have id.
     * @return Updated CodeBox.
     */
    public RequestPatch<CodeBox> updateCodeBox(CodeBox codeBox) {
        if (codeBox.getId() == null || codeBox.getId() == 0) {
            throw new RuntimeException("Trying to update object without id!");
        }
        String url = String.format(Constants.CODEBOXES_DETAIL_URL, getNotEmptyInstanceName(), codeBox.getId());
        RequestPatch<CodeBox> req = new RequestPatch<>(CodeBox.class, url, this, codeBox);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        return req;
    }

    /**
     * Delete previously created CodeBox.
     *
     * @param id CodeBox id.
     * @return Deleted CodeBox
     */
    public RequestDelete<CodeBox> deleteCodeBox(int id) {
        String url = String.format(Constants.CODEBOXES_DETAIL_URL, getNotEmptyInstanceName(), id);
        RequestDelete<CodeBox> req = new RequestDelete<>(CodeBox.class, url, this);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_NO_CONTENT);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_NOT_FOUND);
        return req;
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
        String url = String.format(Constants.CODEBOXES_RUN_URL, getNotEmptyInstanceName(), id);
        JsonObject payload = new JsonObject();
        payload.add("payload", params);
        RequestPost<Trace> req = new RequestPost<>(Trace.class, url, this, payload);
        addCodeboxIdAfterCall(req, id);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        return req;
    }

    /**
     * Run CodeBox asynchronous. Result of this request is not result of the CodeBox.
     * Result will be stored in associated Trace.
     *
     * @param codeBox CodeBox to run.
     * @return Result with link do Trace.
     */
    public RequestPost<Trace> runCodeBox(final CodeBox codeBox) {
        return runCodeBox(codeBox, null);
    }

    /**
     * Run CodeBox asynchronous. Result of this request is not result of the CodeBox.
     * Result will be stored in associated Trace.
     *
     * @param codeBox CodeBox to run.
     * @param params  CodeBox params.
     * @return Result with link do Trace.
     */
    public RequestPost<Trace> runCodeBox(final CodeBox codeBox, JsonObject params) {
        if (codeBox.getId() == null) {
            throw new RuntimeException("Can't run codebox without giving it's id");
        }
        RequestPost<Trace> req = runCodeBox(codeBox.getId(), params);
        req.setRunAfter(new HttpRequest.RunAfter<Trace>() {
            @Override
            public void run(Response<Trace> response) {
                Trace trace = response.getData();
                if (trace == null) return;
                trace.setCodeBoxId(codeBox.getId());
                codeBox.setTrace(trace);
            }
        });
        return req;
    }

    /**
     * Get trace, result of asynchronous CodeBox execution.
     *
     * @param codeboxId CodeBox id.
     * @param traceId   Trace id.
     * @return Trace, it may be still pending, then try to get trace again.
     */
    public RequestGet<Trace> getTrace(int codeboxId, int traceId) {
        String url = String.format(Constants.TRACE_DETAIL_URL, getNotEmptyInstanceName(), codeboxId, traceId);
        RequestGet<Trace> req = new RequestGetOne<>(Trace.class, url, this);
        addCodeboxIdAfterCall(req, codeboxId);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        return req;
    }

    /**
     * Get trace, result of CodeBox execution. Refreshes values in given trace object.
     *
     * @param trace Trace that should be refreshed. It has to have id and codebox id set.
     * @return Trace, it may be still pending, then try to get trace again.
     */
    public RequestGet<Trace> getTrace(Trace trace) {
        if (trace.getCodeBoxId() == null) {
            throw new RuntimeException("Fetching trace result without codebox id. If run from webhook, result is already known.");
        }
        String url = String.format(Constants.TRACE_DETAIL_URL, getNotEmptyInstanceName(), trace.getCodeBoxId(), trace.getId());
        RequestGet<Trace> req = new RequestGetOne<>(trace, url, this);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        return req;
    }

    private void addCodeboxIdAfterCall(HttpRequest<Trace> req, final int codeboxId) {
        req.setRunAfter(new HttpRequest.RunAfter<Trace>() {
            @Override
            public void run(Response<Trace> response) {
                Trace trace = response.getData();
                if (trace != null) {
                    trace.setCodeBoxId(codeboxId);
                }
            }
        });
    }

    /**
     * Create a new Webhook
     *
     * @param webhook Webhook to create.
     * @return New Webhook.
     */
    public RequestPost<Webhook> createWebhook(Webhook webhook) {
        String url = String.format(Constants.WEBHOOKS_LIST_URL, getNotEmptyInstanceName());
        RequestPost<Webhook> req = new RequestPost<>(Webhook.class, url, this, webhook);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_CREATED);
        return req;
    }

    /**
     * Get details of previously created Webhook.
     *
     * @param name Webhook id.
     * @return Existing Webhook.
     */
    public RequestGetOne<Webhook> getWebhook(String name) {
        String url = String.format(Constants.WEBHOOKS_DETAIL_URL, getNotEmptyInstanceName(), name);
        RequestGetOne<Webhook> req = new RequestGetOne<>(Webhook.class, url, this);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        return req;
    }

    /**
     * Get a list of previously created Webhooks.
     *
     * @return List of existing Webhooks.
     */
    public RequestGetList<Webhook> getWebhooks() {
        String url = String.format(Constants.WEBHOOKS_LIST_URL, getNotEmptyInstanceName());
        RequestGetList<Webhook> req = new RequestGetList<>(Webhook.class, url, this);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        return req;
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
        String url = String.format(Constants.WEBHOOKS_DETAIL_URL, getNotEmptyInstanceName(), webhook.getName());
        RequestPatch<Webhook> req = new RequestPatch<>(Webhook.class, url, this, webhook);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        return req;
    }

    /**
     * Delete a Webhook.
     *
     * @param name Webhook id.
     * @return null
     */
    public RequestDelete<Webhook> deleteWebhook(String name) {
        String url = String.format(Constants.WEBHOOKS_DETAIL_URL, getNotEmptyInstanceName(), name);
        RequestDelete<Webhook> req = new RequestDelete<>(Webhook.class, url, this);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_NO_CONTENT);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_NOT_FOUND);
        return req;
    }

    private <T> RequestPost<T> makeRunWebhook(Class<T> resposneType, String name, JsonObject payload) {
        String url = String.format(Constants.WEBHOOKS_RUN_URL, getNotEmptyInstanceName(), name);
        RequestPost<T> req = new RequestPost<>(resposneType, url, this, payload);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        return req;
    }

    private <T> RequestPost<T> makeRunWebhook(final Class<T> responseType, final Webhook webhook, JsonObject payload) {
        if (webhook.getName() == null) {
            throw new RuntimeException("Can't run webhook without a name.");
        }
        RequestPost<T> req = makeRunWebhook(responseType, webhook.getName(), payload);
        req.setRunAfter(new HttpRequest.RunAfter<T>() {
            @Override
            public void run(Response<T> response) {
                if (responseType.equals(Trace.class)) {
                    webhook.setTrace((Trace) response.getData());
                } else if (responseType.equals(String.class)) {
                    webhook.setCustomResponse((String) response.getData());
                }
            }
        });
        return req;
    }

    private <T> RequestPost<T> makeRunWebhookUrl(Class<T> responseType, String url, JsonObject payload) {
        RequestPost<T> req = new RequestPost<>(responseType, null, this, payload);
        req.setCompleteCustomUrl(url);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        return req;
    }

    /**
     * Run a Webhook.
     *
     * @param name Webhook id.
     * @return Result of executed Webhook.
     */
    public RequestPost<Trace> runWebhook(String name) {
        return runWebhook(name, null);
    }

    /**
     * Run a Webhook.
     *
     * @param name Webhook id.
     * @return Result of executed Webhook.
     */
    public RequestPost<String> runWebhookCustomResponse(String name) {
        return runWebhookCustomResponse(name, null);
    }

    /**
     * Run a Webhook.
     *
     * @param name    Webhook id.
     * @param payload Params to pass to webhook.
     * @return Result of executed Webhook.
     */
    public RequestPost<Trace> runWebhook(String name, JsonObject payload) {
        return makeRunWebhook(Trace.class, name, payload);
    }

    /**
     * Run a Webhook.
     *
     * @param name    Webhook id.
     * @param payload Params to pass to webhook.
     * @return Result of executed Webhook.
     */
    public RequestPost<String> runWebhookCustomResponse(String name, JsonObject payload) {
        return makeRunWebhook(String.class, name, payload);
    }

    /**
     * Run a Webhook.
     *
     * @param webhook Webhook to run.
     * @return Result of executed Webhook.
     */
    public RequestPost<Trace> runWebhook(Webhook webhook) {
        return runWebhook(webhook, null);
    }

    /**
     * Run a Webhook.
     *
     * @param webhook Webhook to run.
     * @return Result of executed Webhook.
     */
    public RequestPost<String> runWebhookCustomResponse(Webhook webhook) {
        return runWebhookCustomResponse(webhook, null);
    }

    /**
     * Run a Webhook.
     *
     * @param webhook Webhook to run.
     * @param payload Params to pass to webhook.
     * @return Result of executed Webhook.
     */
    public RequestPost<Trace> runWebhook(final Webhook webhook, JsonObject payload) {
        return makeRunWebhook(Trace.class, webhook, payload);
    }

    /**
     * Run a Webhook.
     *
     * @param webhook Webhook to run.
     * @param payload Params to pass to webhook.
     * @return Result of executed Webhook.
     */
    public RequestPost<String> runWebhookCustomResponse(final Webhook webhook, JsonObject payload) {
        return makeRunWebhook(String.class, webhook, payload);
    }


    /**
     * Run a public Webhook.
     *
     * @param url Public webhook url.
     * @return Result of executed Webhook.
     */
    public RequestPost<Trace> runWebhookUrl(String url) {
        return runWebhookUrl(url, null);
    }

    /**
     * Run a public Webhook.
     *
     * @param url Public webhook url.
     * @return Result of executed Webhook.
     */
    public RequestPost<String> runWebhookUrlCustomResponse(String url) {
        return runWebhookUrlCustomResponse(url, null);
    }

    /**
     * Run a public Webhook.
     *
     * @param url     Public webhook url.
     * @param payload Params to pass to webhook.
     * @return Result of executed Webhook.
     */
    public RequestPost<Trace> runWebhookUrl(String url, JsonObject payload) {
        return makeRunWebhookUrl(Trace.class, url, payload);
    }

    /**
     * Run a public Webhook.
     *
     * @param url     Public webhook url.
     * @param payload Params to pass to webhook.
     * @return Result of executed Webhook.
     */
    public RequestPost<String> runWebhookUrlCustomResponse(String url, JsonObject payload) {
        return makeRunWebhookUrl(String.class, url, payload);
    }

    /**
     * Create a class.
     *
     * @param clazz Class to create.
     * @return Created class.
     */
    public RequestPost<SyncanoClass> createSyncanoClass(SyncanoClass clazz) {
        String url = String.format(Constants.CLASSES_LIST_URL, getNotEmptyInstanceName());
        RequestPost<SyncanoClass> req = new RequestPost<>(SyncanoClass.class, url, this, clazz);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_CREATED);
        return req;
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
        RequestGetOne<SyncanoClass> req = new RequestGetOne<>(SyncanoClass.class, url, this);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        return req;
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
        RequestGetList<SyncanoClass> req = new RequestGetList<>(SyncanoClass.class, url, this);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        return req;
    }

    /**
     * Update a Class.
     *
     * @param clazz SyncanoClass to update. It need to have name.
     * @return Updated Class.
     */
    public RequestPatch<SyncanoClass> updateSyncanoClass(SyncanoClass clazz) {
        if (clazz.getName() == null || clazz.getName().isEmpty()) {
            throw new RuntimeException("Trying to update SyncanoClass without giving name!");
        }
        String url = String.format(Constants.CLASSES_DETAIL_URL, getNotEmptyInstanceName(), clazz.getName());
        RequestPatch<SyncanoClass> req = new RequestPatch<>(SyncanoClass.class, url, this, clazz);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        return req;
    }

    /**
     * Delete a Class.
     *
     * @param name Class to delete.
     * @return RequestDelete<SyncanoClass>
     */
    public RequestDelete<SyncanoClass> deleteSyncanoClass(String name) {
        String url = String.format(Constants.CLASSES_DETAIL_URL, getNotEmptyInstanceName(), name);
        RequestDelete<SyncanoClass> req = new RequestDelete<>(SyncanoClass.class, url, this);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_NO_CONTENT);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_NOT_FOUND);
        return req;
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
     * Create a new User.
     * To be able to register Users you'll have to create an API Key that has allow_user_create flag set to true.
     *
     * @param user User to create.
     * @return created User
     */
    public RequestPost<User> registerUser(User user) {
        return registerCustomUser(user);
    }

    /**
     * Create a new custom User.
     * To be able to register Users you'll have to create an API Key that has allow_user_create flag set to true.
     *
     * @param user User to create.
     * @return created User
     */
    public <T extends AbstractUser> RequestPost<T> registerCustomUser(T user) {
        Class<T> type = (Class<T>) user.getClass();
        String url = String.format(Constants.USERS_LIST_URL, getNotEmptyInstanceName());
        RequestPost<T> req = new RequestPost<>(type, url, this, user);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_CREATED);
        return req;
    }

    /**
     * Get details of previously created User.
     *
     * @param id Id of existing User.
     * @return requested user
     */
    public RequestGetOne<User> getUser(int id) {
        return getUser(User.class, id);
    }

    /**
     * Get details of previously created User.
     *
     * @param id Id of existing User.
     * @return requested user
     */
    public <T extends AbstractUser> RequestGetOne<T> getUser(Class<T> type, int id) {
        String url = String.format(Constants.USERS_DETAIL_URL, getNotEmptyInstanceName(), id);
        RequestGetOne<T> req = new RequestGetOne<>(type, url, this);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        return req;
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
        RequestGetList<T> req = new RequestGetList<>(type, url, this);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        return req;
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
        if (user.getId() == null || user.getId() == 0) {
            throw new RuntimeException("Trying to update User without giving id!");
        }
        Class<T> type = (Class<T>) user.getClass();
        String url = String.format(Constants.USERS_DETAIL_URL, getNotEmptyInstanceName(), user.getId());
        RequestPatch<T> req = new RequestPatch<>(type, url, this, user);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        return req;
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
        RequestDelete<T> req = new RequestDelete<>(type, url, this);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_NO_CONTENT);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_NOT_FOUND);
        return req;
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
        String url = String.format(Constants.USER_AUTH, getNotEmptyInstanceName());

        JsonObject jsonParams = new JsonObject();
        jsonParams.addProperty(User.FIELD_USER_NAME, username);
        jsonParams.addProperty(User.FIELD_PASSWORD, password);

        RequestPost<T> req = new RequestPost<>(type, url, this, jsonParams);
        saveUserApikeyIfSuccess(req);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        return req;
    }

    /**
     * Authenticate a social user.
     *
     * @param social    Social network authentication backend.
     * @param authToken Authentication token.
     * @return user
     */
    public RequestPost<User> loginSocialUser(SocialAuthBackend social, String authToken) {
        return loginSocialUser(User.class, social, authToken);
    }

    /**
     * Authenticate a social user.
     *
     * @param social    Social network authentication backend.
     * @param authToken Authentication token.
     * @return user
     */
    public <T extends AbstractUser> RequestPost<T> loginSocialUser(Class<T> type, SocialAuthBackend social, String authToken) {
        String url = String.format(Constants.USER_SOCIAL_AUTH, getNotEmptyInstanceName(), social.toString());

        JsonObject jsonParams = new JsonObject();
        jsonParams.addProperty(Constants.POST_PARAM_SOCIAL_TOKEN, authToken);

        RequestPost<T> req = new RequestPost<>(type, url, this, jsonParams);
        saveUserApikeyIfSuccess(req);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        return req;
    }

    private <T extends AbstractUser> void saveUserApikeyIfSuccess(HttpRequest<T> request) {
        request.setRunAfter(new HttpRequest.RunAfter<T>() {
            @Override
            public void run(Response<T> response) {
                if (!response.isSuccess() || response.getData() == null) {
                    return;
                }
                AbstractUser user = response.getData();
                Syncano.this.setUserKey(user.getUserKey());
            }
        });
    }

    /**
     * Create a new Group.
     *
     * @param group Group to create.
     * @return Created group
     */
    public RequestPost<Group> createGroup(Group group) {
        String url = String.format(Constants.GROUPS_LIST_URL, getNotEmptyInstanceName());
        RequestPost<Group> req = new RequestPost<>(Group.class, url, this, group);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_CREATED);
        return req;
    }

    /**
     * Get details of previously created Group.
     *
     * @param id Id of existing Group.
     * @return Requested group
     */
    public RequestGetOne<Group> getGroup(int id) {
        String url = String.format(Constants.GROUPS_DETAIL_URL, getNotEmptyInstanceName(), id);
        RequestGetOne<Group> req = new RequestGetOne<>(Group.class, url, this);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        return req;
    }

    /**
     * Get a list of previously created Groups.
     *
     * @return All groups
     */
    public RequestGetList<Group> getGroups() {
        String url = String.format(Constants.GROUPS_LIST_URL, getNotEmptyInstanceName());
        RequestGetList<Group> req = new RequestGetList<>(Group.class, url, this);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        return req;
    }

    /**
     * Update a Group.
     *
     * @param group Group to update. It need to have id.
     * @return updated group
     */
    public RequestPatch<Group> updateGroup(Group group) {
        if (group.getId() == null || group.getId() == 0) {
            throw new RuntimeException("Trying to update Group without id!");
        }
        String url = String.format(Constants.GROUPS_DETAIL_URL, getNotEmptyInstanceName(), group.getId());
        RequestPatch<Group> req = new RequestPatch<>(Group.class, url, this, group);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        return req;
    }

    /**
     * Delete a Group.
     *
     * @param id Id of existing Group.
     * @return Success or error information
     */
    public RequestDelete<Group> deleteGroup(int id) {
        String url = String.format(Constants.GROUPS_DETAIL_URL, getNotEmptyInstanceName(), id);
        RequestDelete<Group> req = new RequestDelete<>(Group.class, url, this);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_NOT_FOUND);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_NO_CONTENT);
        return req;
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
        RequestGetOne<GroupMembership> req = new RequestGetOne<>(GroupMembership.class, url, this);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        return req;
    }

    /**
     * Get a list of previously created Memberships.
     *
     * @param groupId Id of existing Group.
     * @return Users wrapped id a memberships objects
     */
    public RequestGetList<GroupMembership> getGroupMemberships(int groupId) {
        String url = String.format(Constants.GROUPS_USERS_LIST_URL, getNotEmptyInstanceName(), groupId);
        RequestGetList<GroupMembership> req = new RequestGetList<>(GroupMembership.class, url, this);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        return req;
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

        RequestPost<GroupMembership> req = new RequestPost<>(GroupMembership.class, url, this, jsonParams);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_CREATED);
        return req;
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
        RequestDelete<GroupMembership> req = new RequestDelete<>(GroupMembership.class, url, this);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_NOT_FOUND);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_NO_CONTENT);
        return req;
    }

    /**
     * Create a new Channel
     *
     * @param channel Channel to create.
     * @return New Channel.
     */
    public RequestPost<Channel> createChannel(Channel channel) {
        String url = String.format(Constants.CHANNELS_LIST_URL, getNotEmptyInstanceName());
        RequestPost<Channel> req = new RequestPost<>(Channel.class, url, this, channel);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_CREATED);
        return req;
    }

    /**
     * Get details of previously created Channel.
     *
     * @param channelName Channel id.
     * @return Existing Channel.
     */
    public RequestGetOne<Channel> getChannel(String channelName) {
        String url = String.format(Constants.CHANNELS_DETAIL_URL, getNotEmptyInstanceName(), channelName);
        RequestGetOne<Channel> req = new RequestGetOne<>(Channel.class, url, this);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        return req;
    }

    /**
     * Get a list of previously created Channels.
     *
     * @return List of existing Channels.
     */
    public RequestGetList<Channel> getChannels() {
        String url = String.format(Constants.CHANNELS_LIST_URL, getNotEmptyInstanceName());
        RequestGetList<Channel> req = new RequestGetList<>(Channel.class, url, this);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        return req;
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
        String url = String.format(Constants.CHANNELS_DETAIL_URL, getNotEmptyInstanceName(), channel.getName());
        RequestPatch<Channel> req = new RequestPatch<>(Channel.class, url, this, channel);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        return req;
    }

    /**
     * Delete a Channel.
     *
     * @param channelName Channel name.
     * @return null
     */
    public RequestDelete<Channel> deleteChannel(String channelName) {
        String url = String.format(Constants.CHANNELS_DETAIL_URL, getNotEmptyInstanceName(), channelName);
        RequestDelete<Channel> req = new RequestDelete<>(Channel.class, url, this);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_NOT_FOUND);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_NO_CONTENT);
        return req;
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
        String url = String.format(Constants.CHANNELS_HISTORY_URL, getNotEmptyInstanceName(), channelName);
        RequestGetList<Notification> req = new RequestGetList<>(Notification.class, url, this);
        if (room != null) {
            req.addUrlParam(Constants.URL_PARAM_ROOM, room);
        }
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        return req;
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
        String url = String.format(Constants.CHANNELS_PUBLISH_URL, getNotEmptyInstanceName(), channelName);
        RequestPost<Notification> req = new RequestPost<>(Notification.class, url, this, notification);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_CREATED);
        return req;
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

        String url = String.format(Constants.CHANNELS_POLL_URL, getNotEmptyInstanceName(), channelName);
        RequestGetOne<Notification> req = new RequestGetOne<>(Notification.class, url, this);

        if (room != null) {
            req.addUrlParam(Constants.URL_PARAM_ROOM, room);
        }
        if (lastId != 0) {
            req.addUrlParam(Constants.URL_PARAM_LAST_ID, String.valueOf(lastId));
        }
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_NO_CONTENT);
        return req;
    }
}