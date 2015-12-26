package com.syncano.library.tests;

import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.IncrementBuilder;
import com.syncano.library.api.Response;
import com.syncano.library.data.SyncanoObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IncrementBuilderTest extends SyncanoApplicationTestCase {

    @Before
    public void setUp() throws Exception {
        super.setUp();
        createClass(TestObject.class);
        createTestObjects();
    }

    private void createTestObjects() {
        TestObject testObject = new TestObject();
        Response<TestObject> responseCreate = testObject.save();
        assertTrue(responseCreate.isSuccess());
    }

    @After
    public void tearDown() throws Exception {
        removeClass(TestObject.class);
        super.tearDown();
    }

    @Test
    public void testIncreaseValue() {
        int expectedVersion = 0;
        int expectedRevision = 0;

        Response<List<TestObject>> listResponse = syncano.getObjects(TestObject.class).send();
        TestObject testObject = listResponse.getData().get(0);
        Response<TestObject> response = testObject.increment(TestObject.COLUMN_VERSION, 2).save();

        expectedVersion += 2;
        assertTrue(response.isSuccess());
        assertEquals(expectedVersion, testObject.version);
        assertEquals(expectedRevision, testObject.revision);

        IncrementBuilder a = new IncrementBuilder();
        a.increment(TestObject.COLUMN_VERSION, 2);
        expectedVersion += 2;

        Response<TestObject> response2 = syncano.addition(TestObject.class, testObject.getId(), a).send();
        testObject = response2.getData();

        assertEquals(expectedVersion, testObject.version);

        Response<TestObject> response3 = testObject.increment(TestObject.COLUMN_VERSION, 2)
                .decrement(TestObject.COLUMN_REVISION, 1).save();

        expectedVersion += 2;
        expectedRevision--;
        assertTrue(response3.isSuccess());
        testObject = response3.getData();
        assertEquals(expectedVersion, testObject.version);
        assertEquals(expectedRevision, testObject.revision);
    }


    @SyncanoClass(name = TestObject.TABLE_NAME)
    private static class TestObject extends SyncanoObject {
        public final static String TABLE_NAME = "test_object";
        public final static String COLUMN_VERSION = "obj_version";
        public final static String COLUMN_REVISION = "obj_revision";

        @SyncanoField(name = COLUMN_VERSION)
        public int version = 0;
        @SyncanoField(name = COLUMN_REVISION)
        public int revision = 0;
    }
}
