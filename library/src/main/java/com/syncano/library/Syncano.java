package com.syncano.library;

import android.content.Context;

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
import com.syncano.library.data.CodeBox;
import com.syncano.library.data.Notification;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.data.Trace;
import com.syncano.library.data.User;
import com.syncano.library.data.Webhook;
import com.syncano.library.simple.RequestBuilder;
import com.syncano.library.utils.SyncanoClassHelper;
import com.syncano.library.utils.UserMemory;
import com.syncano.library.utils.Validate;

public class Syncano {

    private static Syncano sharedInstance = null;
    protected String customServerUrl;
    protected String apiKey;
    protected AbstractUser user;
    protected String instanceName;
    private Context androidContext = null;
    private boolean strictCheckCertificate = false;
    private boolean useLoggedUserStorage = true;

    /**
     * Create Syncano object. When using this constructor most functions will not work, because api key
     * and instance name are usually required.
     */
    public Syncano() {
        this(null, null);
    }

    /**
     * Create Syncano object. Functions that require api key will not work.
     *
     * @param instanceName Syncano instanceName.
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
        this(null, apiKey, instanceName);
    }

    /**
     * Create Syncano object.
     *
     * @param apiKey         Api key.
     * @param instanceName   Syncano instanceName related with apiKey.
     * @param androidContext used for android specific functions, for example saving logged in user data
     */
    public Syncano(String apiKey, String instanceName, Context androidContext) {
        this(null, apiKey, instanceName, androidContext);
    }

    /**
     * Create Syncano object.
     *
     * @param customServerUrl If not set, production URL will be used.
     * @param apiKey          Api key.
     * @param instanceName    Syncano instanceName related with apiKey.
     */
    public Syncano(String customServerUrl, String apiKey, String instanceName) {
        this(customServerUrl, apiKey, instanceName, null);
    }

    /**
     * Create Syncano object.
     *
     * @param customServerUrl If not set, production URL will be used.
     * @param apiKey          Api key.
     * @param instanceName    Syncano instanceName related with apiKey.
     * @param androidContext  Used for android specific functions, for example saving logged in user data
     */
    public Syncano(String customServerUrl, String apiKey, String instanceName, Context androidContext) {
        this(customServerUrl, apiKey, instanceName, androidContext, null);
    }

    /**
     * Create Syncano object.
     *
     * @param customServerUrl      If not set, production URL will be used.
     * @param apiKey               Api key.
     * @param instanceName         Syncano instanceName related with apiKey.
     * @param androidContext       Used for android specific functions, for example saving logged in user data
     * @param useLoggedUserStorage Will save logged user credentials to storage and keep his user key
     */
    public Syncano(String customServerUrl, String apiKey, String instanceName, Context androidContext, Boolean useLoggedUserStorage) {
        this.customServerUrl = customServerUrl;
        this.apiKey = apiKey;
        this.instanceName = instanceName;
        this.androidContext = androidContext;
        if (useLoggedUserStorage != null) this.useLoggedUserStorage = useLoggedUserStorage;
        if (this.useLoggedUserStorage) this.user = UserMemory.getUserFromStorage(this);
    }

    /**
     * Create static Syncano instance.
     * Use getInstance() to get its reference.
     *
     * @param customServerUrl If not set, production URL will be used.
     * @param apiKey          Api key.
     * @param instanceName    Syncano instanceName related with apiKey.
     * @param androidContext  Used for android specific functions, for example saving logged in user data
     * @return Created instance
     */
    public static Syncano init(String customServerUrl, String apiKey, String instanceName, Context androidContext) {
        sharedInstance = new Syncano(customServerUrl, apiKey, instanceName, androidContext);
        return sharedInstance;
    }

    /**
     * Create static Syncano instance.
     * Use getInstance() to get its reference.
     *
     * @param customServerUrl If not set, production URL will be used.
     * @param apiKey          Api key.
     * @param instanceName    Syncano instanceName related with apiKey.
     * @return Created instance
     */
    public static Syncano init(String customServerUrl, String apiKey, String instanceName) {
        return init(customServerUrl, apiKey, instanceName, null);
    }

    /**
     * Create static Syncano instance.
     * Use getInstance() to get its reference.
     *
     * @param apiKey         Api key.
     * @param instanceName   Syncano instanceName related with apiKey.
     * @param androidContext Used for android specific functions, for example saving logged in user data
     * @return Created instance
     */
    public static Syncano init(String apiKey, String instanceName, Context androidContext) {
        return init(null, apiKey, instanceName, androidContext);
    }

    /**
     * Create static Syncano instance.
     * Use getInstance() to get its reference.
     *
     * @param apiKey       Api key.
     * @param instanceName Syncano instanceName related with apiKey.
     * @return Created instance
     */
    public static Syncano init(String apiKey, String instanceName) {
        return init(null, apiKey, instanceName);
    }


    public boolean isStrictCheckedCertificate() {
        return strictCheckCertificate;
    }

    /**
     * By default this library checks if certificate that is used for https encryption is exactly
     * the right one.
     *
     * @param strictCheckCertificate If set to false, https certificate will be still checked, if it's trusted.
     *                               It will not check if it's exactly the one built into this library.
     */
    public Syncano setStrictCheckCertificate(boolean strictCheckCertificate) {
        this.strictCheckCertificate = strictCheckCertificate;
        return this;
    }

    /**
     * @return Static instance created by init() or setInstance(). This instance is used by some functions
     * as SyncanoObject.save(), User.login().
     */
    public static Syncano getInstance() {
        return sharedInstance;
    }

    /**
     * @param syncano Instance that will be available from getInstance(). This instance is used by
     *                some functions as SyncanoObject.save(), User.login().
     */
    public static void setInstance(Syncano syncano) {
        sharedInstance = syncano;
    }

    /**
     * @param clazz Syncano class that will be requested.
     * @return Builder object that makes it easy to configure a request in one line.
     */
    public static <T extends SyncanoObject> RequestBuilder<T> please(Class<T> clazz) {
        return new RequestBuilder<>(clazz);
    }

    /**
     * @return Url to syncano API
     */
    public String getUrl() {
        if (customServerUrl != null && !customServerUrl.isEmpty()) {
            return customServerUrl;
        }
        return Constants.PRODUCTION_SERVER_URL;
    }

    /**
     * @return instance name
     */
    public String getInstanceName() {
        return instanceName;
    }

    /**
     * @return Returns instance name. If not set, it throws RuntimeException.
     */
    public String getNotEmptyInstanceName() {
        if (instanceName == null || instanceName.isEmpty()) {
            throw new RuntimeException("Syncano instance name is not set");
        }
        return instanceName;
    }

    /**
     * @return api key
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * @return Returns api key. If not set, it throws RuntimeException.
     */
    public String getNotEmptyApiKey() {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new RuntimeException("Syncano api key is not set");
        }
        return apiKey;
    }

    /**
     * @return User key. It will be set, when user logged in using this instance or set in setUser().
     * If not null, it will be added automatically to requests.
     */
    public String getUserKey() {
        if (user == null) {
            return null;
        }
        return user.getUserKey();
    }

    public Syncano setUserKey(String userKey) {
        User user = new User();
        user.setUserKey(userKey);
        setUser(user);
        return this;
    }

    /**
     * @return User. It will be set, when user logged in using this instance or if it was set in setUser().
     * If not null, its user key will be added automatically to requests.
     */
    public AbstractUser getUser() {
        return user;
    }

    /**
     * If set, this instance will behave as this user is logged in. Its user key will be added automatically to requests.
     */
    public Syncano setUser(AbstractUser user) {
        this.user = user;
        if (useLoggedUserStorage)
            UserMemory.saveUserToStorage(this, user);
        return this;
    }

    /**
     * @return connected context
     */
    public Context getAndroidContext() {
        return androidContext;
    }

    /**
     * @param ctx Connect context to this instance. Functions that require context will work.
     *            For example saving logged in user to persistent memory.
     */
    public Syncano setAndroidContext(Context ctx) {
        androidContext = ctx;
        return this;
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
        return new RequestGetOne<>(type, url, this);
    }

    /**
     * Get a Data Object from Syncano.
     *
     * @param object Object to get. Has to have id. Other fields will be updated.
     */
    public <T extends SyncanoObject> RequestGetOne<T> getObject(T object) {
        Validate.checkNotNullAndZero(object.getId(), "Can't fetch object without id");
        String className = SyncanoClassHelper.getSyncanoClassName(object.getClass());
        String url = String.format(Constants.OBJECTS_DETAIL_URL, getNotEmptyInstanceName(), className, object.getId());
        return new RequestGetOne<>(object, url, this);
    }


    /**
     * Get a list of Data Objects associated with a given Class.
     *
     * @param type Type for result List item.
     * @param <T>  Result type.
     */
    public <T extends SyncanoObject> RequestGetList<T> getViewObjects(Class<T> type, String tableView) {
        String url = String.format(Constants.OBJECTS_VIEW, getNotEmptyInstanceName(), tableView);
        return new RequestGetList<>(type, url, this);
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
        return new RequestGetList<>(type, url, this);
    }

    /**
     * Get a list of Data Objects associated with a given Class from specific page.
     * You can get this url by calling ResponseGetList.getNextPageUrl() or getPreviousPageUrl().
     *
     * @param type    Type for result List item.
     * @param <T>     Result type.
     * @param pageUrl page to request
     */
    public <T extends SyncanoObject> RequestGetList<T> getObjects(Class<T> type, String pageUrl) {
        return new RequestGetList<>(type, pageUrl, this);
    }

    /**
     * Update Data Object on Syncano.
     *
     * @param object Object to update. It has to have id. Fields that are not null will be updated on server.
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
        Validate.checkNotNullAndZero(object.getId(), "Trying to update object without id!");
        Class<T> type = (Class<T>) object.getClass();
        String className = SyncanoClassHelper.getSyncanoClassName(type);
        String url = String.format(Constants.OBJECTS_DETAIL_URL, getNotEmptyInstanceName(), className, object.getId());
        RequestPatch<T> req = new RequestPatch<>(type, url, this, object);
        req.updateGivenObject(updateGivenObject);
        return req;
    }

    /**
     * Change counter field in object.
     * We could do it by updating it's value but to avoid conflicts
     * for situations where multiple user change object at the same time
     * it's much safer to use our increase/decrease api.
     *
     * @param object Object to update. It has to have id. This object will not be changed.
     * @param <T>    Result type.
     * @return Updated DataObject.
     */
    public <T extends SyncanoObject> RequestPatch<T> addition(T object, IncrementBuilder incrementBuilder) {
        Validate.checkNotNullAndZero(object.getId(), "Can't addition field object without id");
        return addition((Class<T>) object.getClass(), object.getId(), incrementBuilder);
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
        if (incrementBuilder.hasAdditionFields()) {
            throw new IllegalArgumentException("Cannot create increment query without specify fields to increment/decrement!");
        }
        String className = SyncanoClassHelper.getSyncanoClassName(type);
        String url = String.format(Constants.OBJECTS_DETAIL_URL, getNotEmptyInstanceName(), className, id);
        JsonObject additionQuery = new JsonObject();
        incrementBuilder.build(additionQuery);
        return new RequestPatch<>(type, url, this, additionQuery);
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
        return new RequestDelete<>(type, url, this);
    }

    /**
     * Delete a Data Object.
     *
     * @param object Object to delete. It has to have id set.
     */
    public <T extends SyncanoObject> RequestDelete<T> deleteObject(T object) {
        Validate.checkNotNullAndZero(object.getId(), "Can't delete object without id");
        return deleteObject((Class<T>) object.getClass(), object.getId());
    }

    /**
     * Run CodeBox asynchronously. Result of this request is not a result of the CodeBox.
     * Result will be stored in associated Trace. You need to call Trace.fetch() to check current
     * status of execution.
     *
     * @param id CodeBox id.
     * @return Result with link do Trace.
     */
    public RequestPost<Trace> runCodeBox(int id) {
        return runCodeBox(id, null);
    }

    /**
     * Run CodeBox asynchronously. Result of this request is not a result of the CodeBox.
     * Result will be stored in associated Trace. You need to call Trace.fetch() to check current
     * status of execution.
     *
     * @param id     CodeBox id.
     * @param params CodeBox params.
     * @return Result with link do Trace.
     */
    public RequestPost<Trace> runCodeBox(int id, JsonObject params) {
        String url = String.format(Constants.CODEBOXES_RUN_URL, getNotEmptyInstanceName(), id);
        JsonObject payload = new JsonObject();
        payload.add(Constants.POST_PARAM_PAYLOAD, params);
        RequestPost<Trace> req = new RequestPost<>(Trace.class, url, this, payload);
        addCodeboxIdAfterCall(req, id);
        return req;
    }

    /**
     * Run CodeBox asynchronously. Result of this request is not a result of the CodeBox.
     * Result will be stored in associated Trace. You need to call Trace.fetch() to check current
     * status of execution.
     *
     * @param codeBox CodeBox to run.
     * @return Result with link do Trace.
     */
    public RequestPost<Trace> runCodeBox(final CodeBox codeBox) {
        return runCodeBox(codeBox, null);
    }

    /**
     * Run CodeBox asynchronously. Result of this request is not a result of the CodeBox.
     * Result will be stored in associated Trace. You need to call Trace.fetch() to check current
     * status of execution.
     *
     * @param codeBox CodeBox to run.
     * @param params  CodeBox params.
     * @return Result with link do Trace.
     */
    public RequestPost<Trace> runCodeBox(final CodeBox codeBox, JsonObject params) {
        Validate.checkNotNullAndZero(codeBox.getId(), "Can't run codebox without giving it's id");
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
        return req;
    }

    /**
     * Get trace, result of CodeBox execution. Refreshes values in given trace object.
     *
     * @param trace Trace that should be refreshed. It has to have id and codebox id set.
     * @return Trace, it may be still pending, then try to get trace again.
     */
    public RequestGet<Trace> getTrace(Trace trace) {
        Validate.checkNotNullAndZero(trace.getCodeBoxId(), "Fetching trace result without codebox id. If run from webhook, result is already known.");
        String url = String.format(Constants.TRACE_DETAIL_URL, getNotEmptyInstanceName(), trace.getCodeBoxId(), trace.getId());
        return new RequestGetOne<>(trace, url, this);
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
     * Run a Webhook.
     *
     * @param name Webhook name.
     * @return Result of executed Webhook.
     */
    public RequestPost<Trace> runWebhook(String name) {
        return runWebhook(name, null);
    }

    /**
     * Run a Webhook.
     *
     * @param name Webhook name.
     * @return Result of executed Webhook.
     */
    public RequestPost<String> runWebhookCustomResponse(String name) {
        return runWebhookCustomResponse(name, String.class);
    }

    /**
     * Run a Webhook.
     *
     * @param name Webhook name.
     * @return Result of executed Webhook.
     */
    public <T> RequestPost<T> runWebhookCustomResponse(String name, Class<T> type) {
        return runWebhookCustomResponse(name, type, null);
    }

    /**
     * Run a Webhook.
     *
     * @param name    Webhook name.
     * @param payload Params to pass to webhook.
     * @return Result of executed Webhook.
     */
    public RequestPost<Trace> runWebhook(String name, JsonObject payload) {
        return runWebhookCustomResponse(name, Trace.class, payload);
    }

    /**
     * Run a Webhook.
     *
     * @param name    Webhook name.
     * @param payload Params to pass to webhook.
     * @return Result of executed Webhook.
     */
    public RequestPost<String> runWebhookCustomResponse(String name, JsonObject payload) {
        return runWebhookCustomResponse(name, String.class, payload);
    }

    /**
     * Run a Webhook.
     *
     * @param name    Webhook name.
     * @param payload Params to pass to webhook.
     * @return Result of executed Webhook.
     */
    public <T> RequestPost<T> runWebhookCustomResponse(String name, Class<T> type, JsonObject payload) {
        String url = String.format(Constants.WEBHOOKS_RUN_URL, getNotEmptyInstanceName(), name);
        return new RequestPost<>(type, url, this, payload);
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
        return runWebhookCustomResponse(webhook, String.class);
    }

    /**
     * Run a Webhook.
     *
     * @param webhook Webhook to run.
     * @return Result of executed Webhook.
     */
    public <T> RequestPost<T> runWebhookCustomResponse(Webhook webhook, Class<T> type) {
        return runWebhookCustomResponse(webhook, type, null);
    }

    /**
     * Run a Webhook.
     *
     * @param webhook Webhook to run.
     * @param payload Params to pass to webhook.
     * @return Result of executed Webhook.
     */
    public RequestPost<Trace> runWebhook(Webhook webhook, JsonObject payload) {
        return runWebhookCustomResponse(webhook, Trace.class, payload);
    }

    /**
     * Run a Webhook.
     *
     * @param webhook Webhook to run.
     * @param payload Params to pass to webhook.
     * @return Result of executed Webhook.
     */
    public RequestPost<String> runWebhookCustomResponse(Webhook webhook, JsonObject payload) {
        return runWebhookCustomResponse(webhook, String.class, payload);
    }

    /**
     * Run a Webhook.
     *
     * @param webhook Webhook to run.
     * @param payload Params to pass to webhook.
     * @return Result of executed Webhook.
     */
    public <T> RequestPost<T> runWebhookCustomResponse(final Webhook webhook, final Class<T> type, JsonObject payload) {
        Validate.checkNotNullAndNotEmpty(webhook.getName(), "Can't run webhook without a name.");
        RequestPost<T> req = runWebhookCustomResponse(webhook.getName(), type, payload);
        req.setRunAfter(new HttpRequest.RunAfter<T>() {
            @Override
            public void run(Response<T> response) {
                if (type.equals(Trace.class)) {
                    webhook.setTrace((Trace) response.getData());
                } else {
                    webhook.setCustomResponse(response.getData());
                }
            }
        });
        return req;
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
        return runWebhookUrlCustomResponse(url, String.class);
    }

    /**
     * Run a public Webhook.
     *
     * @param url Public webhook url.
     * @return Result of executed Webhook.
     */
    public <T> RequestPost<T> runWebhookUrlCustomResponse(String url, Class<T> type) {
        return runWebhookUrlCustomResponse(url, type, null);
    }

    /**
     * Run a public Webhook.
     *
     * @param url     Public webhook url.
     * @param payload Params to pass to webhook.
     * @return Result of executed Webhook.
     */
    public RequestPost<Trace> runWebhookUrl(String url, JsonObject payload) {
        return runWebhookUrlCustomResponse(url, Trace.class, payload);
    }

    /**
     * Run a public Webhook.
     *
     * @param url     Public webhook url.
     * @param payload Params to pass to webhook.
     * @return Result of executed Webhook.
     */
    public RequestPost<String> runWebhookUrlCustomResponse(String url, JsonObject payload) {
        return runWebhookUrlCustomResponse(url, String.class, payload);
    }

    /**
     * Run a public Webhook.
     *
     * @param url     Public webhook url.
     * @param payload Params to pass to webhook.
     * @return Result of executed Webhook.
     */
    public <T> RequestPost<T> runWebhookUrlCustomResponse(String url, Class<T> type, JsonObject payload) {
        RequestPost<T> req = new RequestPost<>(type, null, this, payload);
        req.setCompleteCustomUrl(url);
        return req;
    }


    /**
     * Create a new custom User.
     * To be able to register Users you'll have to create an API Key that has allow_user_create flag set to true.
     *
     * @param user User to create.
     * @return created User
     */
    public <T extends AbstractUser> RequestPost<T> registerUser(T user) {
        Class<T> type = (Class<T>) user.getClass();
        String url = String.format(Constants.USERS_LIST_URL, getNotEmptyInstanceName());
        RequestPost<T> req = new RequestPost<>(type, url, this, user);
        req.updateGivenObject(true);
        saveUserIfSuccess(req);
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
        return new RequestGetOne<>(type, url, this);
    }

    /**
     * Get details of application User.
     *
     * @param user user instance
     * @return requested user
     */
    public <T extends AbstractUser> RequestGetOne<T> fetchCurrentUser(T user) {
        Validate.checkNotNullAndNotEmpty(getUserKey(), "Can't fetch user without login him first.");
        String url = String.format(Constants.USER_DETAILS, getNotEmptyInstanceName());
        return new RequestGetOne<>(user, url, this);
    }

    /**
     * Get details of previously created User.
     *
     * @return requested user
     */
    public <T extends AbstractUser> RequestGetOne<T> fetchCurrentUser(Class<T> type) {
        Validate.checkNotNullAndNotEmpty(getUserKey(), "Can't fetch user without login him first.");
        String url = String.format(Constants.USER_DETAILS, getNotEmptyInstanceName());
        return new RequestGetOne<>(type, url, this);
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
        Validate.checkNotNullAndZero(user.getId(), "Trying to update User without giving id!");
        Class<T> type = (Class<T>) user.getClass();
        String url = String.format(Constants.USERS_DETAIL_URL, getNotEmptyInstanceName(), user.getId());
        return new RequestPatch<>(type, url, this, user);
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
        saveUserIfSuccess(req);
        return req;
    }


    /**
     * Authenticate a User.
     * Password property will be cleared, instead of this you get token in userKey field
     *
     * @return user
     */
    public <T extends AbstractUser> RequestPost<T> loginUser(T user) {
        Validate.checkNotNullAndNotEmpty(user.getUserName(), "Username cannot be empty.");
        Validate.checkNotNullAndNotEmpty(user.getPassword(), "Password cannot be empty.");
        String url = String.format(Constants.USER_AUTH, getNotEmptyInstanceName());
        RequestPost<T> req = new RequestPost<>((Class<T>) user.getClass(), url, this, user);
        req.updateGivenObject(true);
        saveUserIfSuccess(req);
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
        saveUserIfSuccess(req);
        return req;
    }

    /**
     * Authenticate a social user.
     *
     * @return user
     */
    public <T extends AbstractUser> RequestPost<T> loginSocialUser(T user) {
        Validate.checkNotNullAndNotEmpty(user.getAuthToken(), "Auth token cannot be empty.");
        Validate.checkNotNull(user.getSocialAuthBackend(), "You should specify social type backend.");
        String url = String.format(Constants.USER_SOCIAL_AUTH, getNotEmptyInstanceName(), user.getSocialAuthBackend().toString());
        RequestPost<T> req = new RequestPost<>((Class<T>) user.getClass(), url, this, user);
        req.updateGivenObject(true);
        saveUserIfSuccess(req);
        return req;
    }


    private <T extends AbstractUser> void saveUserIfSuccess(HttpRequest<T> request) {
        request.setRunAfter(new HttpRequest.RunAfter<T>() {
            @Override
            public void run(Response<T> response) {
                if (!response.isSuccess() || response.getData() == null) {
                    return;
                }
                Syncano.this.setUser(response.getData());
            }
        });
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

        String url = String.format(Constants.CHANNELS_POLL_URL, getNotEmptyInstanceName(), channelName);
        RequestGetOne<Notification> req = new RequestGetOne<>(Notification.class, url, this);
        req.setLongConnectionTimeout();

        if (room != null) {
            req.addUrlParam(Constants.URL_PARAM_ROOM, room);
        }
        if (lastId != 0) {
            req.addUrlParam(Constants.URL_PARAM_LAST_ID, String.valueOf(lastId));
        }
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
        return req;
    }


}