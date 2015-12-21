package com.syncano.library.documentation;

import android.util.Log;

import com.syncano.library.Syncano;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.FieldsFilter;
import com.syncano.library.api.RequestGetList;
import com.syncano.library.api.Response;
import com.syncano.library.api.ResponseGetList;
import com.syncano.library.callbacks.SyncanoCallback;
import com.syncano.library.choice.FilterType;
import com.syncano.library.data.CodeBox;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.data.Trace;

import java.util.List;

// ---------- Adding your class
@SyncanoClass(name = "book")
class Book extends SyncanoObject {

    public static final String FIELD_TITLE = "title";
    public static final String FIELD_SUBTITLE = "subtitle";

    @SyncanoField(name = FIELD_TITLE, filterIndex = true, orderIndex = true)
    public String title;

    @SyncanoField(name = FIELD_SUBTITLE, filterIndex = true, orderIndex = true)
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
        createClass(Book.class);
        this.syncano = super.syncano;
    }

    @Override
    protected void tearDown() throws Exception {
        removeClass(Book.class);
        super.tearDown();
    }

    public void testInitiateSyncano() {
        // ---------- Connecting to Syncano
        syncano = new Syncano("YOUR API KEY", "YOUR INSTANCE");
        // -----------------------------

        this.syncano = super.syncano;
    }


    public void testGetObjects() {
        // ---------- Download the most recent Objects Synchronous
        Response<List<Book>> responseGetBooks = Syncano.please(Book.class).get();
        //or other way
        Response<List<Book>> responseGetBooks2 = syncano.getObjects(Book.class).send();

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

        Syncano.please(Book.class).getAsync(callback);
        // other way
        syncano.getObjects(Book.class).sendAsync(callback);
        // -----------------------------
    }

    public void testGetSingleObject() {
        int id = 0;

        // ---------- Get a single Data Object
        Response<Book> responseGetBook = syncano.getObject(Book.class, id).send();
        Book book = Syncano.please(Book.class).get(id).getData();
        // other way
        book = responseGetBook.getData();
        // -----------------------------
    }

    public void testObjects() {
        // ---------- Creating a new Data Object
        Book newBook = new Book();
        newBook.title = "New Title";
        newBook.subtitle = "New Subtitle";

        Response<Book> responseCreateObject = newBook.save();

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

        Response<Book> responseUpdate = book.save();
        Book updatedBook = responseUpdate.getData(); // book with updated fields (like updatedAt).
        // -----------------------------

        assertNotNull(updatedBook);

        // ---------- Where and OrderBy
        Response<List<Book>> response = Syncano.please(Book.class).orderBy(Book.FIELD_TITLE)
                .where().gte(Book.FIELD_ID, 10).lte(Book.FIELD_ID, 15).get();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_SUCCESS, response.getHttpResultCode());

        ResponseGetList<Book> responseFirst = Syncano.please(Book.class).limit(10).get();
        // ---------- Page size and LastPk
        // Get with bigger id - next page
        ResponseGetList<Book> responseNextPage = syncano.getObjects(Book.class, responseFirst.getLinkNext()).send();

        // Get with smaller id - previous page
        ResponseGetList<Book> responsePreviousPage = syncano.getObjects(Book.class, responseFirst.getLinkPrevious()).send();
        // -----------------------------

        // ---------- Fields filtering
        RequestGetList<Book> requestFilters = syncano.getObjects(Book.class);

        FieldsFilter filter = new FieldsFilter(FilterType.INCLUDE_FIELDS, Book.FIELD_ID, Book.FIELD_TITLE);
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
        codeBox.setId(0);

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
}
