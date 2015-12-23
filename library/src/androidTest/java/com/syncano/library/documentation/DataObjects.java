package com.syncano.library.documentation;

import com.syncano.library.Syncano;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.api.Response;
import com.syncano.library.choice.SortOrder;
import com.syncano.library.data.SyncanoFile;
import com.syncano.library.data.SyncanoObject;

import java.io.File;
import java.util.List;

public class DataObjects extends SyncanoApplicationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        copyAssets();
        createClass(Book.class);

        Book newBook = new Book();
        newBook.author = "Ernest Hemingway";
        newBook.title = "The Old Man and the Sea";

        Response<Book> responseCreateObject = newBook.save();
        assertEquals(Response.HTTP_CODE_CREATED, responseCreateObject.getHttpResultCode());
    }

    public void testCreateClass() {

        // ---------- Creating a Data Object
        Book newBook = new Book();
        newBook.author = "Ernest Hemingway";
        newBook.title = "The Old Man and the Sea";

        Response<Book> responseCreateObject = newBook.save();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_CREATED, responseCreateObject.getHttpResultCode());
        Book createdBook = responseCreateObject.getData();
        assertNotNull(createdBook);
        int serverId = createdBook.getId();

        // ---------- Creating a Data Object with file
        Book bookWithCover = new Book();
        bookWithCover.author = "Ernest Hemingway";
        bookWithCover.title = "The Old Man and the Sea";
        bookWithCover.cover = new SyncanoFile(new File(getContext().getFilesDir(), "blue.png"));

        Response<Book> responseCreateObjectWithFile = bookWithCover.save();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_CREATED, responseCreateObjectWithFile.getHttpResultCode());
        Book createdBookWithCover = responseCreateObjectWithFile.getData();
        assertNotNull(createdBookWithCover);
        assertNotNull(createdBookWithCover.cover);
        assertNotNull(createdBookWithCover.cover.getLink());

        // ---------- Updating a Data Object
        Book book = new Book();
        book.setId(serverId); // object to update has to have id set
        book.author = "Ernest Hemingway";
        book.title = "The Old Man and the Sea";

        Response<Book> responseUpdate = book.save();

        // -----------------------------

        assertEquals(Response.HTTP_CODE_SUCCESS, responseUpdate.getHttpResultCode());

        // ---------- Deleting a Data Object
        book.setId(serverId); // object to delete has to have id set
        Response<Book> responseDelete = book.delete();

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
        Book book = new Book();
        book.setId(serverId);
        Response<Book> response = book.fetch();

        // -----------------------------

        assertEquals(Response.HTTP_CODE_SUCCESS, response.getHttpResultCode());
        assertNotNull(book);

        // ---------- Listing Data Objects
        Response<List<Book>> responseList = Syncano.please(Book.class)
                .orderBy(Book.FIELD_CREATED_AT, SortOrder.DESCENDING).where().neq(Book.FIELD_ID, 5).get();

        // -----------------------------

        assertEquals(Response.HTTP_CODE_SUCCESS, responseList.getHttpResultCode());
        assertNotNull(responseList.getData());

    }

    @SyncanoClass(name = "Book")
    private static class Book extends SyncanoObject {
        @SyncanoField(name = "author")
        public String author;

        @SyncanoField(name = "title")
        public String title;

        @SyncanoField(name = "cover")
        public SyncanoFile cover;
    }
}
