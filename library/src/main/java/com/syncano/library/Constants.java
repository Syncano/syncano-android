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

    // ==================== Classes ==================== //
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_DESCRIPTION = "description";

    public static final String FIELD_ORDER_INDEX = "order_index";
    public static final String FIELD_FILTER_INDEX = "filter_index";
    public static final String FIELD_TARGET = "target";
    public static final String FIELD_TARGET_SELF = "self";

    public static final String BATCH_URL = "/v1/instances/%s/batch/";
    // ==================== Objects ==================== //
    public static final String OBJECTS_LIST_URL = "/v1/instances/%s/classes/%s/objects/";
    public static final String OBJECTS_DETAIL_URL = "/v1/instances/%s/classes/%s/objects/%d/";

    // ==================== Views ==================== //
    public static final String OBJECTS_VIEW = "/v1/instances/%s/api/objects/%s/get/";
    public static final String OBJECTS_VIEW_CREATE = "/v1/instances/%s/api/objects/";
    public static final String OBJECTS_VIEW_REMOVE = "/v1/instances/%s/api/objects/%s/";

    public static final String OBJECTS_VIEW_PARAM_CLASS = "class";

    // ==================== CodeBoxes ==================== //
    public static final String CODEBOXES_LIST_URL = "/v1/instances/%s/codeboxes/";
    public static final String CODEBOXES_DETAIL_URL = "/v1/instances/%s/codeboxes/%d/";
    public static final String CODEBOXES_RUN_URL = "/v1/instances/%s/codeboxes/%d/run/";
    public static final String TRACE_DETAIL_URL = "/v1/instances/%s/codeboxes/%d/traces/%d/";

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
    public static final String USER_SOCIAL_AUTH = "/v1/instances/%s/user/auth/%s/";

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

    // ==================== PUSH ==================== //
    public static final String PUSH_GCM_DEVICES_URL = "/v1/instances/%s/push_notifications/gcm/devices/";
    public static final String PUSH_GCM_DEVICE_URL = "/v1/instances/%s/push_notifications/gcm/devices/%s/";
    public static final String PUSH_GCM_MESSAGES_URL = "/v1/instances/%s/push_notifications/gcm/messages/";
    public static final String PUSH_GCM_MESSAGE_URL = "/v1/instances/%s/push_notifications/gcm/messages/%s/";

}
