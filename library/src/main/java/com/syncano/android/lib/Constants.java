package com.syncano.android.lib;

public class Constants {

    /** Server url. */
    public static final String SERVER_URL = "https://syncanotest1-env.elasticbeanstalk.com";

    /** User agent name */
    public final static String USER_AGENT = "syncano-android-4.0";

    public final static String URL_PARAM_QUERY = "query";
    public final static String URL_PARAM_ORDER_BY = "order_by";
    public final static String URL_PARAM_FIELDS = "fields";
    public final static String URL_PARAM_EXCLUDED_FIELDS = "excluded_fields";

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
}
