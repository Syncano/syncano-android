package com.syncano.library.documentation;

import com.syncano.library.Syncano;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.api.Response;
import com.syncano.library.api.ResponseGetList;
import com.syncano.library.data.SyncanoFile;
import com.syncano.library.data.SyncanoObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DataObjects extends SyncanoApplicationTestCase {

    @Before
    public void setUp() throws Exception {
        super.setUp();
        createClass(Book.class);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void createObject() {
        //
        Book newBook = new Book();
        newBook.author = "Ernest Hemingway";
        newBook.title = "The Old Man and the Sea";

        Response<Book> response = newBook.save();
        //
        assertEquals(Response.HTTP_CODE_CREATED, response.getHttpResultCode());
        Book createdBook = response.getData();
        assertNotNull(createdBook);
    }

    @Test
    public void createObjectWithFile() {
        //
        Book bookWithCover = new Book();
        bookWithCover.author = "Ernest Hemingway";
        bookWithCover.title = "The Old Man and the Sea";
        bookWithCover.cover = new SyncanoFile(new File(getAssetsDir(), "blue.png"));

        Response<Book> response = bookWithCover.save();
        //

        assertTrue(response.isSuccess());
    }

    @Test
    public void updateObject() {
        int serverId = 1;
        //
        Book book = new Book();
        // object to update has to have id set
        book.setId(serverId);
        book.author = "Ernest Hemingway";
        book.title = "The Old Man and the Sea";
        book.save();
        //
    }

    @Test
    public void increment() {
        int serverId = 1;
        //
        Book book = new Book();
        // object to update has to have id set
        book.setId(serverId);
        book.increment("number_of_games", 10);
        book.save();
        //
    }

    @Test
    public void delete() {
        int serverId = 1;
        //
        Book book = new Book();
        // object to delete has to have id set
        book.setId(serverId);
        book.delete();
        //
    }

    @Test
    public void getDetails() {
        int serverId = 1;
        //
        Book book = new Book();
        // object to delete has to have id set
        book.setId(serverId);
        // values in the object will be updated
        book.fetch();
        //
    }

    @Test
    public void getList() {
        //
        ResponseGetList<Book> response = Syncano.please(Book.class).get();
        List<Book> books = response.getData();
        //
    }

    @Test
    public void getCount() {
        //
        Response<Integer> response = Syncano.please(Book.class).getCountEstimation();
        Integer count = response.getData();
        //
    }

    @SyncanoClass(name = "Book")
    private static class Book extends SyncanoObject {
        @SyncanoField(name = "author")
        public String author;

        @SyncanoField(name = "title")
        public String title;

        @SyncanoField(name = "cover")
        public SyncanoFile cover;

        @SyncanoField(name = "number_of_games")
        public Integer numberOfGames = 10;
    }
}
