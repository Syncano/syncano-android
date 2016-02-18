package com.syncano.library.documentation;

import com.syncano.library.Syncano;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.api.Response;
import com.syncano.library.api.ResponseGetList;
import com.syncano.library.callbacks.SyncanoListCallback;
import com.syncano.library.choice.FilterType;
import com.syncano.library.data.CodeBox;
import com.syncano.library.data.Trace;
import com.syncano.library.model.Author;
import com.syncano.library.model.Book;
import com.syncano.library.utils.SyncanoLog;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

// ---------- JSON
//      [
//        {"type": "string", "name": "title", "order_index":true, "filter_index":true},
//        {"type": "string", "name": "subtitle", "order_index":true, "filter_index":true}
//      ]
// -----------------------------


public class LibraryQuickStart extends SyncanoApplicationTestCase {
    private static final String TAG = LibraryQuickStart.class.getSimpleName();

    // ---------- Connecting to Syncano
    private Syncano syncano;
    // -----------------------------


    @Before
    public void setUp() throws Exception {
        super.setUp();
        createClass(Author.class);
        createClass(Book.class);
        this.syncano = super.syncano;
    }

    @After
    public void tearDown() throws Exception {
        removeClass(Book.class);
        removeClass(Author.class);
        super.tearDown();
    }

    @Test
    public void testInitiateSyncano() {
        // ---------- Connecting to Syncano
        syncano = Syncano.init("YOUR API KEY", "YOUR INSTANCE");
        // -----------------------------

        this.syncano = super.syncano;
    }

    @Test
    public void testGetObjects() {
        // ---------- Download the most recent Objects Synchronous
        ResponseGetList<Book> responseGetBooks1 = Syncano.please(Book.class).get();
        //or other way
        ResponseGetList<Book> responseGetBooks2 = syncano.getObjects(Book.class).send();

        if (responseGetBooks1.isSuccess()) {
            List<Book> books = responseGetBooks1.getData();
        }
        // -----------------------------

        assertEquals(Response.HTTP_CODE_SUCCESS, responseGetBooks1.getHttpResultCode());

        // ---------- Download the most recent Objects Asynchronous
        SyncanoListCallback<Book> callback = new SyncanoListCallback<Book>() {
            @Override
            public void success(ResponseGetList<Book> response, List<Book> result) {
                // Request succeed.
            }

            @Override
            public void failure(ResponseGetList<Book> response) {
                // Something went wrong. Check error codes in response object.
            }
        };

        Syncano.please(Book.class).get(callback);
        // other way
        syncano.getObjects(Book.class).sendAsync(callback);
        // -----------------------------
    }

    @Test
    public void testGetSingleObject() {
        int id = 0;

        // ---------- Get a single Data Object
        Response<Book> responseGetBook = syncano.getObject(Book.class, id).send();
        Book book = responseGetBook.getData();

        // other way
        book = Syncano.please(Book.class).get(id).getData();
        // -----------------------------
    }

    @Test
    public void testObjects() {
        // ---------- Creating a new Data Object
        Book book = new Book();
        book.title = "New Title";
        book.subtitle = "New Subtitle";

        Response<Book> responseCreateObject = book.save();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_CREATED, responseCreateObject.getHttpResultCode());
        Book receivedBook = responseCreateObject.getData();
        assertNotNull(receivedBook);
        assertEquals(book.title, receivedBook.title);

        // ---------- Modify an Object
        // id has to be set in the object
        book.title = "New Title";
        book.subtitle = "New Subtitle";

        Response<Book> responseUpdate = book.save();
        // -----------------------------

        assertTrue(responseUpdate.isSuccess());

        // ---------- Where and OrderBy
        ResponseGetList<Book> response = Syncano.please(Book.class).orderBy(Book.FIELD_TITLE)
                .where().gte(Book.FIELD_ID, 10).lte(Book.FIELD_ID, 15).get();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_SUCCESS, response.getHttpResultCode());

        // ---------- Pages
        // Get with bigger id - next page
        ResponseGetList<Book> responseFirst = Syncano.please(Book.class).limit(10).get();
        ResponseGetList<Book> responseNextPage = responseFirst.getNextPage();

        // Get with smaller id - previous page
        ResponseGetList<Book> responsePreviousPage = responseNextPage.getPreviousPage();
        // -----------------------------


        // ---------- Fields filtering
        ResponseGetList<Book> responseFilters =
                Syncano.please(Book.class).selectFields(FilterType.INCLUDE_FIELDS, Book.FIELD_ID, Book.FIELD_TITLE).get();

        // -----------------------------

        assertEquals(Response.HTTP_CODE_SUCCESS, responseFilters.getHttpResultCode());

        // ---------- Delete a Data Object
        Response<Book> responseDeleteObject = book.delete();
        // -----------------------------

        assertTrue(responseDeleteObject.isSuccess());
    }

    @Test
    public void testAdditionalFeatures() {
        CodeBox codeBox = new CodeBox();
        codeBox.setId(0);

        // ---------- CodeBoxes
        Response<Trace> responseRunCodeBox = syncano.runCodeBox(codeBox.getId()).send();
        // -----------------------------

        // ---------- Webhooks
        Response<Trace> responseRunWebhook = syncano.runWebhook("webhook_name").send();
        // -----------------------------
    }

    @Test
    public void testErrors() {
        // ---------- Response codes and error messages
        ResponseGetList<Book> response = Syncano.please(Book.class).get();

        if (response.isSuccess()) {
            // Success
        } else if (response.getResultCode() == Response.CODE_HTTP_ERROR) {
            SyncanoLog.d(TAG, "Result Code: " + response.getHttpResultCode());
            SyncanoLog.d(TAG, "Reason Phrase: " + response.getHttpReasonPhrase());
        }
        // -----------------------------

        // ---------- Response codes and error messages
        response.getResultCode(); // Library error codes
        response.getError(); // Library error message

        response.getHttpResultCode(); // Http result code
        response.getHttpReasonPhrase(); // Http error message
        // -----------------------------
    }
}
