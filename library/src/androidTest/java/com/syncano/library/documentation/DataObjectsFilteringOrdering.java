package com.syncano.library.documentation;

import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.Response;
import com.syncano.library.data.SyncanoFile;
import com.syncano.library.data.SyncanoObject;

import java.util.List;

public class DataObjectsFilteringOrdering extends SyncanoApplicationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createClass(Book.class);
    }

    public void testFilteringOrdering() {

        // ---------- Filtering Data Objects

        Response<List<Book>> responseList = SyncanoObject.please(Book.class)
                .where().gt("release_year", 1995).get();

        // -----------------------------

        assertEquals(Response.HTTP_CODE_SUCCESS, responseList.getHttpResultCode());
        assertNotNull(responseList.getData());

        // ---------- Ordering Data Objects

        Response<List<Book>> responseOrdered = SyncanoObject.please(Book.class)
                .sortBy("release_year").get();

        // -----------------------------

        assertEquals(Response.HTTP_CODE_SUCCESS, responseOrdered.getHttpResultCode());
        assertNotNull(responseOrdered.getData());
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
    }
}
