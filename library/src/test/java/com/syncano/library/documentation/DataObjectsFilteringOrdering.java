package com.syncano.library.documentation;

import com.syncano.library.Syncano;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.ResponseGetList;
import com.syncano.library.choice.FilterType;
import com.syncano.library.choice.SortOrder;
import com.syncano.library.data.SyncanoFile;
import com.syncano.library.data.SyncanoObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

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

        ResponseGetList<Book> responseList = Syncano.please(Book.class)
                .where().gt("release_year", 1990).get();

        // -----------------------------

        assertTrue(responseList.isSuccess());

        // ---------- Fields skipping

        ResponseGetList<Book> responseSkip = Syncano.please(Book.class)
                .selectFields(FilterType.INCLUDE_FIELDS, "release_year", "id", "pages").get();

        // -----------------------------

        assertTrue(responseSkip.isSuccess());

        // ---------- Complex Filtering Data Objects

        ResponseGetList<Book> responseSimple = Syncano.please(Book.class)
                .where().gt("release_year", 1990).get();

        // -----------------------------

        assertTrue(responseSimple.isSuccess());

        // ---------- Complex filtering on one field

        ResponseGetList<Book> responseComplex = Syncano.please(Book.class)
                .selectFields(FilterType.INCLUDE_FIELDS, "release_year", "id", "pages")
                .where().gt("release_year", 1900).lte("release_year", 2000).get();

        // -----------------------------

        assertTrue(responseComplex.isSuccess());

        // ---------- Filtering on multiple fields

        ResponseGetList<Book> responseMultiple = Syncano.please(Book.class)
                .selectFields(FilterType.INCLUDE_FIELDS, "release_year", "id", "pages")
                .where().gt("release_year", 1900).lte("release_year", 2000).gt("pages", 199).get();

        // -----------------------------

        assertTrue(responseMultiple.isSuccess());

        // ---------- Ordering Data Objects

        ResponseGetList<Book> responseOrdered = Syncano.please(Book.class)
                .orderBy("release_year").get();

        // -----------------------------

        assertTrue(responseOrdered.isSuccess());

        // ---------- Reversed Ordering Data Objects

        ResponseGetList<Book> responseOrderedReversed = Syncano.please(Book.class)
                .orderBy("release_year", SortOrder.DESCENDING).get();

        // -----------------------------

        assertTrue(responseOrderedReversed.isSuccess());

        // ---------- Ordering with Filtering Data Objects

        ResponseGetList<Book> responseOrderedFiltered = Syncano.please(Book.class)
                .selectFields(FilterType.INCLUDE_FIELDS, "release_year", "id", "pages")
                .orderBy("release_year").where().gt("release_year", 1990).get();

        // -----------------------------

        assertTrue(responseOrderedFiltered.isSuccess());

        // ---------- Using paging with filtering and ordering

        ResponseGetList<Book> responsePage = Syncano.please(Book.class).limit(1)
                .selectFields(FilterType.INCLUDE_FIELDS, "release_year", "id", "pages")
                .where().gt("release_year", 1900).lte("release_year", 2000).gt("pages", 199).get();

        // -----------------------------

        assertTrue(responsePage.isSuccess());
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
