package com.syncano.library.documentation;

import com.syncano.library.Syncano;
import com.syncano.library.SyncanoApplicationTestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GettingStarted extends SyncanoApplicationTestCase {

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testMakeInstance() {

        // ---------- Connecting from a Library
        Syncano.init("api_key", "instance_name");
        Syncano syncano = Syncano.getInstance();
        // -----------------------------
    }
}
