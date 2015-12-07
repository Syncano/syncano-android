package com.syncano.library.documentation;

import com.syncano.library.Syncano;
import com.syncano.library.SyncanoApplicationTestCase;

public class GettingStarted extends SyncanoApplicationTestCase {

    public void testMakeInstance() {

        // ---------- Connecting from a Library
        Syncano.init("api_key", "instance_name");
        Syncano syncano = Syncano.getInstance();
        // -----------------------------
    }
}
