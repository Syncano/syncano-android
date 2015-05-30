package com.syncano.library;

public class Constants {

    /** Server url. */
    public static final String SERVER_URL = "https://api.syncano.io";

    /** User agent name */
    public final static String USER_AGENT = "syncano-android-" + BuildConfig.VERSION_NAME;

    public final static String URL_PARAM_QUERY = "query";
    public final static String URL_PARAM_ORDER_BY = "order_by";
    public final static String URL_PARAM_FIELDS = "fields";
    public final static String URL_PARAM_EXCLUDED_FIELDS = "excluded_fields";
    public final static String URL_PARAM_PAGE_SIZE = "page_size";
    public final static String URL_PARAM_PAGE_DIRECTION = "direction";
    public final static String URL_PARAM_PAGE_LAST_PK = "last_pk";
    public final static String URL_PARAM_ROOM = "room";
    public final static String URL_PARAM_LAST_ID = "last_id";

    public final static String POST_PARAM_USER = "user";
    public final static String POST_PARAM_ROOM = "room";
    public final static String POST_PARAM_PAYLOAD = "payload";

    // ==================== Objects ==================== //
    public static final String OBJECTS_LIST_URL = "/v1/instances/%s/classes/%s/objects/";
    public static final String OBJECTS_DETAIL_URL = "/v1/instances/%s/classes/%s/objects/%d/";

    // ==================== CodeBoxes ==================== //
    public static final String CODEBOXES_LIST_URL = "/v1/instances/%s/codeboxes/";
    public static final String CODEBOXES_DETAIL_URL = "/v1/instances/%s/codeboxes/%d/";
    public static final String CODEBOXES_RUN_URL = "/v1/instances/%s/codeboxes/%d/run/";

    // ==================== Webhooks ==================== //
    public static final String WEBHOOKS_LIST_URL = "/v1/instances/%s/webhooks/";
    public static final String WEBHOOKS_DETAIL_URL = "/v1/instances/%s/webhooks/%s/";
    public static final String WEBHOOKS_RUN_URL = "/v1/instances/%s/webhooks/%s/run/";

    // ==================== Classes ==================== //
    public static final String USER_PROFILE_CLASS_NAME = "user_profile";
    public static final String CLASSES_LIST_URL = "/v1/instances/%s/classes/";
    public static final String CLASSES_DETAIL_URL = "/v1/instances/%s/classes/%s/";

    // ==================== Users ==================== //
    public static final String USERS_LIST_URL = "/v1/instances/%s/users/";
    public static final String USERS_DETAIL_URL = "/v1/instances/%s/users/%d/";
    public static final String USER_AUTH = "/v1/instances/%s/user/auth/";

    // ==================== Groups ==================== //
    public static final String GROUPS_LIST_URL = "/v1/instances/%s/groups/";
    public static final String GROUPS_DETAIL_URL = "/v1/instances/%s/groups/%d/";
    public static final String GROUPS_USERS_LIST_URL = "/v1/instances/%s/groups/%d/users/";
    public static final String GROUPS_USERS_DETAIL_URL = "/v1/instances/%s/groups/%d/users/%d/";

    // ==================== Channels ==================== //
    public static final String CHANNELS_LIST_URL = "/v1/instances/%s/channels/";
    public static final String CHANNELS_DETAIL_URL = "/v1/instances/%s/channels/%s/";
    public static final String CHANNELS_HISTORY_URL = "/v1/instances/%s/channels/%s/history/";
    public static final String CHANNELS_PUBLISH_URL = "/v1/instances/%s/channels/%s/publish/";
    public static final String CHANNELS_POLL_URL = "/v1/instances/%s/channels/%s/poll/";
}
