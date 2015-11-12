package com.syncano.library.documentation;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.syncano.library.Syncano;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.api.FieldsFilter;
import com.syncano.library.api.RequestGetList;
import com.syncano.library.api.Response;
import com.syncano.library.api.Where;
import com.syncano.library.callbacks.SyncanoCallback;

import java.util.Arrays;
import java.util.List;

import com.syncano.library.data.Trace;
import com.syncano.library.data.CodeBox;

// ---------- Adding your class
import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.data.SyncanoObject;

@SyncanoClass(name = "book")
class Book extends SyncanoObject {

    public static final String FIELD_TITLE = "title";
    public static final String FIELD_SUBTITLE = "subtitle";

    @SyncanoField(name = FIELD_TITLE)
    public String title;

    @SyncanoField(name = FIELD_SUBTITLE)
    public String subtitle;
}
// -----------------------------


public class LibraryQuickStart extends SyncanoApplicationTestCase {
    private static final String TAG = LibraryQuickStart.class.getSimpleName();

    // ---------- Connecting to Syncano
    private Syncano syncano;
    // -----------------------------


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.syncano = super.syncano;
        Response<com.syncano.library.data.SyncanoClass> respDelete = syncano.deleteSyncanoClass("book").send();
        assertEquals(Response.HTTP_CODE_NO_CONTENT, respDelete.getHttpResultCode());

        com.syncano.library.data.SyncanoClass syncanoClass = new com.syncano.library.data.SyncanoClass("book", getBookSchema());
        Response<com.syncano.library.data.SyncanoClass> response = syncano.createSyncanoClass(syncanoClass).send();
        assertEquals(Response.HTTP_CODE_CREATED, response.getHttpResultCode());
    }

    public void testInitiateSyncano() {
        // ---------- Connecting to Syncano
        syncano = new Syncano("YOUR API KEY", "YOUR INSTANCE");
        // -----------------------------

        this.syncano = super.syncano;
    }


    public void testGetObjects() {
        // ---------- Download the most recent Objects Synchronous
        Response<List<Book>> responseGetBooks = syncano.getObjects(Book.class).send();

        if (responseGetBooks.getResultCode() == Response.CODE_SUCCESS) {
            List<Book> books = responseGetBooks.getData();
        }
        // -----------------------------

        assertEquals(Response.HTTP_CODE_SUCCESS, responseGetBooks.getHttpResultCode());

        // ---------- Download the most recent Objects Asynchronous
        SyncanoCallback<List<Book>> callback = new SyncanoCallback<List<Book>>() {
            @Override
            public void success(Response<List<Book>> response, List<Book> result) {
                // Request succeed.
            }

            @Override
            public void failure(Response<List<Book>> response) {
                // Something went wrong. Check error codes in response object.
            }
        };

        syncano.getObjects(Book.class).sendAsync(callback);
        // -----------------------------
    }

    public void testGetSingleObject() {
        int id = 0;

        // ---------- Get a single Data Object
        Response<Book> responseGetBook = syncano.getObject(Book.class, id).send();
        Book book = responseGetBook.getData();
        // -----------------------------
    }

    public void testObjects() {
        // ---------- Creating a new Data Object
        Book newBook = new Book();
        newBook.title = "New Title";
        newBook.subtitle = "New Subtitle";

        Response<Book> responseCreateObject = syncano.createObject(newBook).send();

        if (responseCreateObject.getResultCode() == Response.CODE_SUCCESS) {
            // Book with filled id, createdAt, updatedAt and revision.
            Book book = responseCreateObject.getData();
        }
        // -----------------------------

        assertEquals(Response.HTTP_CODE_CREATED, responseCreateObject.getHttpResultCode());
        Book receivedBook = responseCreateObject.getData();
        assertNotNull(receivedBook);
        assertEquals(newBook.title, receivedBook.title);

        Book book = new Book();
        int id = receivedBook.getId();

        // ---------- Modify an Object
        book.setId(id);
        book.title = "New Title";
        book.subtitle = "New Subtitle";

        Response<Book> responseUpdate = syncano.updateObject(book).send();
        Book updatedBook = responseUpdate.getData(); // book with updated fields (like updatedAt).
        // -----------------------------

        assertNotNull(updatedBook);

        // ---------- Where and OrderBy
        Where where = new Where();
        where.gte(Book.FIELD_ID, 10).lte(Book.FIELD_ID, 15);

        RequestGetList<Book> requestGetList = syncano.getObjects(Book.class);
        requestGetList.setWhereFilter(where);
        requestGetList.setOrderBy(Book.FIELD_TITLE, false);
        Response<List<Book>> response = requestGetList.send();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_SUCCESS, response.getHttpResultCode());

        // ---------- Page size and LastPk
        // Get with bigger id - next page
        RequestGetList<Book> requestNextPage = syncano.getObjects(Book.class);
        requestNextPage.setLimit(10);
        requestNextPage.setLastPk(100, true);
        Response<List<Book>> responseNextPage = requestNextPage.send();

        // Get with smaller id - previous page
        RequestGetList<Book> requestPreviousPage = syncano.getObjects(Book.class);
        requestPreviousPage.setLimit(10);
        requestPreviousPage.setLastPk(100, false); // false - reverse direction
        Response<List<Book>> responsePreviousPage = requestPreviousPage.send();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_SUCCESS, responseNextPage.getHttpResultCode());
        assertEquals(Response.HTTP_CODE_SUCCESS, responsePreviousPage.getHttpResultCode());

        // ---------- Fields filtering
        RequestGetList<Book> requestFilters = syncano.getObjects(Book.class);

        FieldsFilter filter = new FieldsFilter(FieldsFilter.FilterType.INCLUDE_FIELDS, Arrays.asList(Book.FIELD_ID, Book.FIELD_TITLE));
        requestFilters.setFieldsFilter(filter);

        Response<List<Book>> responseFilters = requestFilters.send();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_SUCCESS, responseFilters.getHttpResultCode());

        // ---------- Delete a Data Object
        Response<Book> responseDeleteObject = syncano.deleteObject(Book.class, book.getId()).send();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_NO_CONTENT, responseDeleteObject.getHttpResultCode());
    }

    public void testAdditionalFeatures() {
        CodeBox codeBox = new CodeBox();

        // ---------- CodeBoxes
        Response<Trace> responseRunCodeBox = syncano.runCodeBox(codeBox.getId()).send();
        // -----------------------------

        // ---------- Webhooks
        Response<Trace> responseRunWebhook = syncano.runWebhook("webhook_name").send();
        // -----------------------------
    }

    public void testErrors() {
        // ---------- Response codes and error messages
        Response<List<Book>> responseGetBooks = syncano.getObjects(Book.class).send();

        if (responseGetBooks.getResultCode() == Response.CODE_SUCCESS) {
            // Success
        } else if (responseGetBooks.getResultCode() == Response.CODE_HTTP_ERROR) {
            Log.d(TAG, "Result Code: " + responseGetBooks.getHttpResultCode());
            Log.d(TAG, "Reason Phrase: " + responseGetBooks.getHttpReasonPhrase());
        }
        // -----------------------------

        // ---------- Response codes and error messages
        responseGetBooks.getResultCode(); // Library error codes
        responseGetBooks.getError(); // Library error message

        responseGetBooks.getHttpResultCode(); // Http result code
        responseGetBooks.getHttpReasonPhrase(); // Http error message
        // -----------------------------
    }

    private static JsonArray getBookSchema() {
        JsonObject fieldOne = new JsonObject();
        fieldOne.addProperty("type", "string");
        fieldOne.addProperty("name", Book.FIELD_SUBTITLE);
        fieldOne.addProperty("order_index", true);
        fieldOne.addProperty("filter_index", true);

        JsonObject fieldTwo = new JsonObject();
        fieldTwo.addProperty("type", "string");
        fieldTwo.addProperty("name", Book.FIELD_TITLE);
        fieldTwo.addProperty("order_index", true);
        fieldTwo.addProperty("filter_index", true);

        JsonArray array = new JsonArray();
        array.add(fieldOne);
        array.add(fieldTwo);

        return array;
    }
}
