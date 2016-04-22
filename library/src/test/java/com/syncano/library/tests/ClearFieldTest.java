package com.syncano.library.tests;


import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.model.Author;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ClearFieldTest extends SyncanoApplicationTestCase {
    @Before
    public void setUp() throws Exception {
        super.setUp();
        createClass(Author.class);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testClearing() {
        Author a = new Author();
        // TODO implement clearField
        // TODO check what happens when setting to empty values
    }
}
