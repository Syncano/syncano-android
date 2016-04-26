package com.syncano.library.tests;

import com.syncano.library.Syncano;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.api.ResponseGetList;
import com.syncano.library.api.Where;
import com.syncano.library.choice.Case;
import com.syncano.library.data.DataEndpoint;
import com.syncano.library.model.Author;
import com.syncano.library.model.Book;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DataEndpoints extends SyncanoApplicationTestCase {
    private static final String endpointName = "book_endpoints";
    private static final String endpointDescription = "Sample data endpoint";

    @Before
    public void setUp() throws Exception {
        super.setUp();
        createClass(Author.class);
        createClass(Book.class);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testGetFromEndpoint() {
        createSampleData();

        //Remove endpoint if exist and create new instance
        assertTrue(syncano.deleteDataEndpoint(endpointName).send().isSuccess());

        // create endpoint
        DataEndpoint de = new DataEndpoint(endpointName, Book.class);
        de.addExpandField(Book.FIELD_AUTHOR);
        de.setDescription(endpointDescription);
        de.setOrderBy(Book.FIELD_PAGES);
        Where query = new Where().startsWith(Book.FIELD_SUBTITLE, "a", Case.INSENSITIVE);
        de.setQuery(query);
        de.addExcludedField(Book.FIELD_TITLE);
        assertTrue(syncano.createDataEndpoint(de).send().isSuccess());

        // get on syncano object
        ResponseGetList<Book> resp = syncano.getObjectsDataEndpoint(Book.class, endpointName).send();
        assertTrue(resp.isSuccess());
        checkResponse(resp.getData());
        // get with please
        resp = Syncano.please(Book.class).dataEndpoint(endpointName).get();
        assertTrue(resp.isSuccess());
        checkResponse(resp.getData());
    }

    private void checkResponse(List<Book> books) {
        assertNotNull(books);
        assertEquals(2, books.size());
        assertTrue(books.get(0).subtitle.toLowerCase().startsWith("a"));
        assertTrue(books.get(1).subtitle.toLowerCase().startsWith("a"));
        assertTrue(books.get(0).pages < books.get(1).pages);
        assertEquals("Paul", books.get(0).author.name);
        assertEquals("John", books.get(1).author.name);
        assertNull(books.get(0).title);
        assertNull(books.get(1).title);
    }

    private void createSampleData() {
        Author a1 = new Author();
        a1.name = "Paul";
        a1.birthDate = new Date();
        assertTrue(a1.save().isSuccess());

        Author a2 = new Author();
        a2.name = "John";
        a2.birthDate = new Date();
        assertTrue(a2.save().isSuccess());

        Book b1 = new Book();
        b1.title = "Paul's book";
        b1.author = a1;
        b1.pages = 4;
        b1.subtitle = "aaa";
        assertTrue(b1.save().isSuccess());

        Book b2 = new Book();
        b2.title = "John's book";
        b2.pages = 10;
        b2.subtitle = "Aaa";
        b2.author = a2;
        assertTrue(b2.save().isSuccess());

        Book b3 = new Book();
        b3.title = "p letter book";
        b3.pages = 1;
        b3.subtitle = "baa";
        assertTrue(b3.save().isSuccess());
    }
}
