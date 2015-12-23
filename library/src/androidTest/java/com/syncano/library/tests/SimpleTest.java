package com.syncano.library.tests;

import com.syncano.library.Syncano;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.Response;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.data.User;

import java.util.Date;
import java.util.List;

public class SimpleTest extends SyncanoApplicationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createClass(Item.class);

        // ----------------- Delete users -----------------
        Response<List<User>> usersResp = syncano.getUsers().send();
        assertEquals(Response.HTTP_CODE_SUCCESS, usersResp.getHttpResultCode());
        for (User u : usersResp.getData()) {
            Response<User> resp = syncano.deleteUser(u.getId()).send();
            assertTrue(resp.isSuccess());
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        removeClass(Item.class);
    }

    public void testSimple() {
        // ----------------- Create object 1 -----------------
        Item item1 = new Item();
        assertNull(item1.getId());
        Response<Item> resp1 = item1.on(syncano).save();
        assertTrue(resp1.isSuccess());
        assertNotNull(item1.getId());
        Date firstUpdatedAt = item1.getUpdatedAt();
        assertNotNull(firstUpdatedAt);

        // ----------------- Create object 2 -----------------
        Response<Item> resp2 = (new Item()).save();
        assertTrue(resp2.isSuccess());

        // ----------------- Update object 1 -----------------
        item1.text += "---";
        item1.number = 11;
        Response<Item> respUpdate = item1.save();
        assertTrue(respUpdate.isSuccess());
        assertEquals(item1.text, respUpdate.getData().text);
        assertTrue(item1.getUpdatedAt().after(firstUpdatedAt));

        // ----------------- Get list of objects -----------------
        Response<List<Item>> respList = Syncano.please(Item.class).on(syncano).orderBy("text").limit(2).
                where().lte("number", 12).gt("number", 10).get();
        assertTrue(respList.isSuccess());
        assertEquals(2, respList.getData().size());

        // ----------------- Get object 1 using fetch() -----------------
        Item toFetch = new Item(item1.getId());
        toFetch.fetch();
        assertEquals(item1.text, toFetch.text);
        assertEquals(item1.number, toFetch.number);

        // ----------------- Delete object 2 -----------------
        Response<Item> respDelete = item1.delete();
        assertTrue(respDelete.isSuccess());
    }

    public void testUsers() {
        // ----------------- Register user -----------------
        User user = new User();
        user.setUserName("userabc");
        user.setPassword("userabc");
        Response<User> regResp = user.register();
        assertTrue(regResp.isSuccess());

        // ----------------- Login user -----------------
        user.on(syncano).login();
        Response<User> respLogin = user.login();
        assertTrue(respLogin.isSuccess());
    }


    @com.syncano.library.annotation.SyncanoClass(name = Item.TABLE_NAME)
    private static class Item extends SyncanoObject {
        public final static String TABLE_NAME = "item";
        public final static String COLUMN_TEXT = "text";
        public final static String COLUMN_NUMBER = "number";

        @SyncanoField(name = COLUMN_TEXT, orderIndex = true)
        public String text = "example value";
        @SyncanoField(name = COLUMN_NUMBER, filterIndex = true, orderIndex = true)
        public int number = 12;

        public Item() {
        }

        public Item(int id) {
            setId(id);
        }
    }
}
