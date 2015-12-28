package com.syncano.library.documentation;

import com.syncano.library.Syncano;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.Response;
import com.syncano.library.choice.SortOrder;
import com.syncano.library.data.SyncanoFile;
import com.syncano.library.data.SyncanoObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataObjectsFilteringOrdering extends SyncanoApplicationTestCase {

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
    public void testFilteringOrdering() {

        // ---------- Filtering Data Objects

        Response<List<Book>> responseList = Syncano.please(Book.class)
                .where().gt("release_year", 1990).get();

        // -----------------------------

        assertEquals(Response.HTTP_CODE_SUCCESS, responseList.getHttpResultCode());
        assertNotNull(responseList.getData());

        // ---------- Complex Filtering Data Objects

        Response<List<Book>> responseListComplex = Syncano.please(Book.class)
                .where().gt("release_year", 1990).lte("release_year", 2000).get();

        // -----------------------------

        assertEquals(Response.HTTP_CODE_SUCCESS, responseListComplex.getHttpResultCode());
        assertNotNull(responseListComplex.getData());

        // ---------- Filtering on multiple fields

        Response<List<Book>> responseListMultiple = Syncano.please(Book.class)
                .where().gt("release_year", 1990).lte("release_year", 2000).gt("pages", 199).get();

        // -----------------------------

        assertEquals(Response.HTTP_CODE_SUCCESS, responseListMultiple.getHttpResultCode());
        assertNotNull(responseListMultiple.getData());

        // ---------- Ordering Data Objects

        Response<List<Book>> responseOrdered = Syncano.please(Book.class)
                .orderBy("release_year").get();

        // -----------------------------

        assertEquals(Response.HTTP_CODE_SUCCESS, responseOrdered.getHttpResultCode());
        assertNotNull(responseOrdered.getData());

        // ---------- Reversed Ordering Data Objects

        Response<List<Book>> responseOrderedReversed = Syncano.please(Book.class)
                .orderBy("release_year", SortOrder.DESCENDING).get();

        // -----------------------------

        assertEquals(Response.HTTP_CODE_SUCCESS, responseOrderedReversed.getHttpResultCode());
        assertNotNull(responseOrderedReversed.getData());

        // ---------- Ordering with Filtering Data Objects

        Response<List<Book>> responseOrderedFiltered = Syncano.please(Book.class)
                .orderBy("release_year").where().gt("release_year", 1990).get();

        // -----------------------------

        assertEquals(Response.HTTP_CODE_SUCCESS, responseOrderedFiltered.getHttpResultCode());
        assertNotNull(responseOrderedFiltered.getData());
    }

    @SyncanoClass(name = "Book")
    private static class Book extends SyncanoObject {
        @SyncanoField(name = "author")
        public String author;

        @SyncanoField(name = "title")
        public String title;

        @SyncanoField(name = "cover")
        public SyncanoFile cover;

        @SyncanoField(name = "release_year", orderIndex = true, filterIndex = true)
        public Integer releaseYear;

        @SyncanoField(name = "pages", orderIndex = true, filterIndex = true)
        public Integer pages;
    }
}
