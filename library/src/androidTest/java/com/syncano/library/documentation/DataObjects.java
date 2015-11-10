package com.syncano.library.documentation;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.RequestGetList;
import com.syncano.library.api.Response;
import com.syncano.library.api.Where;
import com.syncano.library.data.SyncanoClass;
import com.syncano.library.data.SyncanoObject;

import java.util.List;

public class DataObjects extends SyncanoApplicationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        String className = "Book";
        syncano.deleteSyncanoClass(className).send();

        SyncanoClass clazz = new SyncanoClass(className, Book.getSchema());
        Response<SyncanoClass> classResponse = syncano.createSyncanoClass(clazz).send();

        assertEquals(Response.HTTP_CODE_CREATED, classResponse.getHttpResultCode());

        Book newBook = new Book();
        newBook.author = "Ernest Hemingway";
        newBook.title = "The Old Man and the Sea";

        Response<Book> responseCreateObject = syncano.createObject(newBook).send();
        assertEquals(Response.HTTP_CODE_CREATED, responseCreateObject.getHttpResultCode());
    }

    public void testCreateClass() {

        // ---------- Creating a Data Object
        Book newBook = new Book();
        newBook.author = "Ernest Hemingway";
        newBook.title = "The Old Man and the Sea";

        Response<Book> responseCreateObject = syncano.createObject(newBook).send();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_CREATED, responseCreateObject.getHttpResultCode());
        Book createdBook = responseCreateObject.getData();
        assertNotNull(createdBook);
        int serverId = createdBook.getId();

        // ---------- Updating a Data Object
        Book book = new Book();
        book.setId(serverId);
        book.author = "Ernest Hemingway";
        book.title = "The Old Man and the Sea";

        Response<Book> responseUpdate = syncano.updateObject(book).send();

        // -----------------------------

        assertEquals(Response.HTTP_CODE_SUCCESS, responseUpdate.getHttpResultCode());

        // ---------- Deleting a Data Object
        Response<Book> responseDelete = syncano.deleteObject(Book.class, serverId).send();

        // -----------------------------

        assertEquals(Response.HTTP_CODE_NO_CONTENT, responseDelete.getHttpResultCode());
    }


    public void testGetDetails() {
        Response<List<Book>> resp = syncano.getObjects(Book.class).send();
        assertEquals(Response.HTTP_CODE_SUCCESS, resp.getHttpResultCode());
        List<Book> books = resp.getData();
        assertNotNull(books);
        assertTrue(books.size() > 0);
        Book exampleBook = books.get(0);
        int serverId = exampleBook.getId();

        // ---------- Getting Data Object details
        Response<Book> response = syncano.getObject(Book.class, serverId).send();
        Book book = response.getData();

        // -----------------------------

        assertEquals(Response.HTTP_CODE_SUCCESS, response.getHttpResultCode());
        assertNotNull(book);

        // ---------- Listing Data Objects
        Where where = new Where();
        where.neq(Book.FIELD_ID, 5);

        RequestGetList<Book> requestGetList = syncano.getObjects(Book.class);
        requestGetList.setWhereFilter(where);
        requestGetList.setOrderBy(Book.FIELD_CREATED_AT, false);
        Response<List<Book>> responseList = requestGetList.send();

        // -----------------------------

        assertEquals(Response.HTTP_CODE_SUCCESS, responseList.getHttpResultCode());
        assertNotNull(responseList.getData());

    }

    @com.syncano.library.annotation.SyncanoClass(name = "Book")
    private static class Book extends SyncanoObject {
        @SyncanoField(name = "author")
        public String author;

        @SyncanoField(name = "title")
        public String title;

        public static JsonArray getSchema() {
        /*
        [
            {"type": "string","name": "author"},
            {"type": "string","name": "title"}
        ]
         */

            JsonObject fieldOne = new JsonObject();
            fieldOne.addProperty("type", "string");
            fieldOne.addProperty("name", "author");

            JsonObject fieldTwo = new JsonObject();
            fieldTwo.addProperty("type", "string");
            fieldTwo.addProperty("name", "title");

            JsonArray array = new JsonArray();
            array.add(fieldOne);
            array.add(fieldTwo);

            return array;
        }
    }
}
