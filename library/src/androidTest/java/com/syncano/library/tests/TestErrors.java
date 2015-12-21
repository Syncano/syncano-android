package com.syncano.library.tests;

import com.syncano.library.Syncano;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.Response;
import com.syncano.library.data.SyncanoObject;

import java.util.Date;

public class TestErrors extends SyncanoApplicationTestCase {

    public void testWrongTypes() throws InterruptedException {
        createClass(TypeA.class);

        TypeA objA = new TypeA();
        Response<TypeA> respA = objA.save();
        assertEquals(Response.HTTP_CODE_CREATED, respA.getHttpResultCode());

        Exception parseEx = null;
        try {
            Syncano.please(TypeB.class).get();
        } catch (Exception e) {
            parseEx = e;
        }
        assertNotNull(parseEx);
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
