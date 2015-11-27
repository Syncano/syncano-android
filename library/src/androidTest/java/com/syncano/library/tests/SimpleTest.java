package com.syncano.library.tests;

import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.Response;
import com.syncano.library.data.SyncanoClass;
import com.syncano.library.data.SyncanoObject;

import java.util.List;

public class SimpleTest extends SyncanoApplicationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        syncano.deleteSyncanoClass(Item.class).send();

        Response<SyncanoClass> respClass = syncano.createSyncanoClass(Item.class).send();
        assertEquals(Response.HTTP_CODE_CREATED, respClass.getHttpResultCode());
    }

    @Override
    protected void tearDown() throws Exception {
        syncano.deleteSyncanoClass(Item.class).send();
        super.tearDown();
    }

    public void testSimple() {
        Response<Item> resp1 = (new Item()).save();
        assertEquals(Response.HTTP_CODE_CREATED, resp1.getHttpResultCode());

        Response<Item> resp2 = (new Item()).save();
        assertEquals(Response.HTTP_CODE_CREATED, resp2.getHttpResultCode());

        Item toUpdate = resp2.getData();
        toUpdate.text += "---";
        Response<Item> respUpdate = toUpdate.save();
        assertEquals(Response.HTTP_CODE_SUCCESS, respUpdate.getHttpResultCode());
        assertEquals(toUpdate.text, respUpdate.getData().text);

        Response<List<Item>> respList = SyncanoObject.please(Item.class).on(syncano).sortBy("text").limit(2).
                where().lte("number", 12).gt("number", 10).get();
        assertEquals(Response.HTTP_CODE_SUCCESS, respList.getHttpResultCode());
        assertEquals(2, respList.getData().size());

        Response<Item> respDelete = toUpdate.delete();
        assertEquals(Response.HTTP_CODE_NO_CONTENT, respDelete.getHttpResultCode());
    }

    @com.syncano.library.annotation.SyncanoClass(name = "item")
    private static class Item extends SyncanoObject {
        @SyncanoField(name = "text", orderIndex = true)
        public String text = "example value";
        @SyncanoField(name = "number", filterIndex = true, orderIndex = true)
        public int number = 12;
    }
}
