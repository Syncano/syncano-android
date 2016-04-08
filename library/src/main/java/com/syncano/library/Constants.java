package com.syncano.library;

public class Constants {

    /**
     * Server url.
     */
    public static final String PRODUCTION_SERVER_URL = "https://api.syncano.io";

    /**
     * User agent name
     */
    public final static String USER_AGENT = "syncano-android-" + BuildConfig.VERSION_NAME;

    public final static String URL_PARAM_QUERY = "query";
    public final static String URL_PARAM_ORDER_BY = "order_by";
    public final static String URL_PARAM_FIELDS = "fields";
    public final static String URL_PARAM_EXCLUDED_FIELDS = "excluded_fields";
    public final static String URL_PARAM_PAGE_SIZE = "page_size";
    public final static String URL_PARAM_PAGE_DIRECTION = "direction";
    public final static String URL_PARAM_PAGE_LAST_PK = "last_pk";
    public final static String URL_PARAM_ROOM = "room";
    public final static String URL_PARAM_EXPAND = "expand";
    public final static String URL_PARAM_LAST_ID = "last_id";
    public final static String URL_PARAM_INCLUDE_COUNT = "include_count";

    public final static String POST_PARAM_USER = "user";
    public final static String POST_PARAM_SOCIAL_TOKEN = "access_token";
    public final static String POST_PARAM_ROOM = "room";
    public final static String POST_PARAM_PAYLOAD = "payload";

    public static final String SOCIAL_AUTH_FACEBOOK = "facebook";
    public static final String SOCIAL_AUTH_GOOGLE_OAUTH2 = "google-oauth2";
    public static final String SOCIAL_AUTH_LINKEDIN = "linkedin";
    public static final String SOCIAL_AUTH_TWITTER = "twitter";

    // ==================== Classes ==================== //
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_DESCRIPTION = "description";

    public static final String FIELD_ORDER_INDEX = "order_index";
    public static final String FIELD_FILTER_INDEX = "filter_index";
    public static final String FIELD_TARGET = "target";
    public static final String FIELD_TARGET_SELF = "self";

    public static final String BATCH_URL = "/v1.1/instances/%s/batch/";
    // ==================== Objects ==================== //
    public static final String OBJECTS_LIST_URL = "/v1.1/instances/%s/classes/%s/objects/";
    public static final String OBJECTS_DETAIL_URL = "/v1.1/instances/%s/classes/%s/objects/%d/";

    // ==================== Data Endpoints ==================== //
    public static final String DATA_ENDPOINT = "/v1.1/instances/%s/endpoints/data/%s/get/";
    public static final String DATA_ENDPOINT_CREATE = "/v1.1/instances/%s/endpoints/data/";
    public static final String DATA_ENDPOINT_REMOVE = "/v1.1/instances/%s/endpoints/data/%s/";

    public static final String DATA_ENDPOINT_PARAM_CLASS = "class";

    // ==================== Scripts ==================== //
    public static final String SCRIPTS_LIST_URL = "/v1.1/instances/%s/snippets/scripts/";
    public static final String SCRIPTS_DETAIL_URL = "/v1.1/instances/%s/snippets/scripts/%d/";
    public static final String SCRIPTS_RUN_URL = "/v1.1/instances/%s/snippets/scripts/%d/run/";
    public static final String TRACE_DETAIL_URL = "/v1.1/instances/%s/snippets/scripts/%d/traces/%d/";

    // ==================== Script Endpoints ==================== //
    public static final String SCRIPT_ENDPOINTS_LIST_URL = "/v1.1/instances/%s/endpoints/scripts/";
    public static final String SCRIPT_ENDPOINTS_DETAIL_URL = "/v1.1/instances/%s/endpoints/scripts/%s/";
    public static final String SCRIPT_ENDPOINTS_RUN_URL = "/v1.1/instances/%s/endpoints/scripts/%s/run/";

    // ==================== Classes ==================== //
    public static final String USER_PROFILE_CLASS_NAME = "user_profile";
    public static final String CLASSES_LIST_URL = "/v1.1/instances/%s/classes/";
    public static final String CLASSES_DETAIL_URL = "/v1.1/instances/%s/classes/%s/";

    // ==================== Users ==================== //
    public static final String USERS_LIST_URL = "/v1.1/instances/%s/users/";
    public static final String USERS_DETAIL_URL = "/v1.1/instances/%s/users/%d/";
    public static final String USER_AUTH = "/v1.1/instances/%s/user/auth/";
    public static final String USER_SOCIAL_AUTH = "/v1.1/instances/%s/user/auth/%s/";
    public static final String USER_DETAILS = "/v1.1/instances/%s/user/";


    // ==================== Groups ==================== //
    public static final String GROUPS_LIST_URL = "/v1.1/instances/%s/groups/";
    public static final String GROUPS_DETAIL_URL = "/v1.1/instances/%s/groups/%d/";
    public static final String GROUPS_USERS_LIST_URL = "/v1.1/instances/%s/groups/%d/users/";
    public static final String GROUPS_USERS_DETAIL_URL = "/v1.1/instances/%s/groups/%d/users/%d/";

    // ==================== Channels ==================== //
    public static final String CHANNELS_LIST_URL = "/v1.1/instances/%s/channels/";
    public static final String CHANNELS_DETAIL_URL = "/v1.1/instances/%s/channels/%s/";
    public static final String CHANNELS_HISTORY_URL = "/v1.1/instances/%s/channels/%s/history/";
    public static final String CHANNELS_PUBLISH_URL = "/v1.1/instances/%s/channels/%s/publish/";
    public static final String CHANNELS_POLL_URL = "/v1.1/instances/%s/channels/%s/poll/";
}
