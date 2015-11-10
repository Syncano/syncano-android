package com.syncano.library.documentation;

import com.syncano.library.Syncano;
import com.syncano.library.SyncanoApplicationTestCase;

public class AuthenticationAPIKeys extends SyncanoApplicationTestCase {

    public void testMakeInstance() {

        // ---------- API KEY CONNECTION
        Syncano syncano = new Syncano("api_key", "instance_name");
        // -----------------------------
    }

}
