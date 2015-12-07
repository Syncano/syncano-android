package com.syncano.library.tests;

import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.Response;
import com.syncano.library.data.SyncanoClass;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.data.User;

import java.util.List;

public class SimpleTest extends SyncanoApplicationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // ----------------- Delete class -----------------
        syncano.deleteSyncanoClass(Item.class).send();

        // ----------------- Create class -----------------
        Response<SyncanoClass> respClass = syncano.createSyncanoClass(Item.class).send();
        assertEquals(Response.HTTP_CODE_CREATED, respClass.getHttpResultCode());

        // ----------------- Delete users -----------------
        Response<List<User>> usersResp = syncano.getUsers().send();
        assertEquals(Response.HTTP_CODE_SUCCESS, usersResp.getHttpResultCode());
        for (User u : usersResp.getData()) {
            syncano.deleteUser(u.getId()).send();
        }
    }

    @Override
    protected void tearDown() throws Exception {
        // ----------------- Delete class -----------------
        syncano.deleteSyncanoClass(Item.class).send();
        super.tearDown();
    }

    public void testSimple() {
        // ----------------- Create object 1 -----------------
        Response<Item> resp1 = (new Item()).on(syncano).save();
        assertEquals(Response.HTTP_CODE_CREATED, resp1.getHttpResultCode());

        // ----------------- Create object 2 -----------------
        Response<Item> resp2 = (new Item()).save();
        assertEquals(Response.HTTP_CODE_CREATED, resp2.getHttpResultCode());

        // ----------------- Update object 2 -----------------
        Item toUpdate = resp2.getData();
        toUpdate.text += "---";
        toUpdate.number = 11;
        Response<Item> respUpdate = toUpdate.save();
        assertEquals(Response.HTTP_CODE_SUCCESS, respUpdate.getHttpResultCode());
        assertEquals(toUpdate.text, respUpdate.getData().text);

        // ----------------- Get list of objects -----------------
        Response<List<Item>> respList = SyncanoObject.please(Item.class).on(syncano).sortBy("text").limit(2).
                where().lte("number", 12).gt("number", 10).get();
        assertEquals(Response.HTTP_CODE_SUCCESS, respList.getHttpResultCode());
        assertEquals(2, respList.getData().size());

        // ----------------- Get object 2 using fetch() -----------------
        Item toFetch = new Item(toUpdate.getId());
        toFetch.fetch();
        assertEquals(toUpdate.text, toFetch.text);
        assertEquals(toUpdate.number, toFetch.number);

        // ----------------- Delete object 2 -----------------
        Response<Item> respDelete = toUpdate.delete();
        assertEquals(Response.HTTP_CODE_NO_CONTENT, respDelete.getHttpResultCode());
    }

    public void testUsers() {
        // ----------------- Register user -----------------
        User user = new User();
        user.setUserName("userabc");
        user.setPassword("userabc");
        Response<User> regResp = user.register();
        assertEquals(Response.HTTP_CODE_CREATED, regResp.getHttpResultCode());

        // ----------------- Login user -----------------
        user.on(syncano).login();
        Response<User> respLogin = user.login();
        assertEquals(Response.HTTP_CODE_SUCCESS, respLogin.getHttpResultCode());
    }

    @com.syncano.library.annotation.SyncanoClass(name = "item")
    private static class Item extends SyncanoObject {
        public Item() {
        }

        public Item(int id) {
            setId(id);
        }


        @SyncanoField(name = "text", orderIndex = true)
        public String text = "example value";
        @SyncanoField(name = "number", filterIndex = true, orderIndex = true)
        public int number = 12;
    }
}
