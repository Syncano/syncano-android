package com.syncano.library.tests;

import com.syncano.library.Syncano;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.Response;
import com.syncano.library.data.SyncanoObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestErrors extends SyncanoApplicationTestCase {

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testWrongTypes() throws InterruptedException {
        createClass(TypeA.class);

        TypeA objA = new TypeA();
        Response<TypeA> respA = objA.save();
        assertEquals(Response.HTTP_CODE_CREATED, respA.getHttpResultCode());
        Response<List<TypeB>> respB = Syncano.please(TypeB.class).get();
        assertEquals(Response.CODE_PARSING_RESPONSE_EXCEPTION, respB.getResultCode());
    }

    @SyncanoClass(name = "myclass")
    private static class TypeA extends SyncanoObject {
        @SyncanoField(name = "date")
        public Date date = new Date();
        @SyncanoField(name = "name")
        public String name = "";
    }

    @SyncanoClass(name = "myclass")
    private static class TypeB extends SyncanoObject {
        @SyncanoField(name = "date")
        public String name = "";
        @SyncanoField(name = "name")
        public Date date = new Date();
    }
}
