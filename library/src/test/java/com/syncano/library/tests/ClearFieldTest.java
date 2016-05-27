package com.syncano.library.tests;

import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.model.AllTypesObject;
import com.syncano.library.model.Author;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ClearFieldTest extends SyncanoApplicationTestCase {
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testClearing() throws InterruptedException {
        createClass(Author.class);
        createClass(AllTypesObject.class);

        // create new object to get id
        AllTypesObject obj = new AllTypesObject();
        assertTrue(obj.save().isSuccess());

        // fill the object with data
        AllTypesObject.generateObject(obj);
        assertTrue(obj.someReference.save().isSuccess());
        obj.reference = obj;
        assertTrue(obj.save().isSuccess());

        // clear all data
        obj.clearAll();
        assertTrue(obj.save().isSuccess());
        obj.checkEquals(new AllTypesObject(), false);

        assertFalse(obj.hasAnyFieldsToClear());
    }
}
