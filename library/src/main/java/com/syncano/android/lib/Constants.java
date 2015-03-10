package com.syncano.android.lib;

public class Constants {

    /** Server url. */
    public static final String SERVER_URL = "https://syncanotest1-env.elasticbeanstalk.com";

    /** User agent name */
    public final static String USER_AGENT = "syncano-android-4.0";

    // ==================== Objects ==================== //
    public static final String OBJECTS_LIST_URL = "/v1/instances/%s/classes/%s/objects/";
    public static final String OBJECTS_DETAIL_URL = "/v1/instances/%s/classes/%s/objects/%d/";

    // ==================== CodeBoxes ==================== //
    public static final String CODEBOXES_LIST_URL = "/v1/instances/%s/codeboxes/";
    public static final String CODEBOXES_DETAIL_URL = "/v1/instances/%s/codeboxes/%d/";
}
