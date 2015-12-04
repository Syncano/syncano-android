package com.syncano.library.tests;

import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.BatchBuilder;
import com.syncano.library.api.Response;
import com.syncano.library.data.BatchAnswer;
import com.syncano.library.data.SyncanoObject;

import java.util.List;

public class BatchTest extends SyncanoApplicationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createClass(Book.class);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        removeClass(Book.class);
    }

    public void testBatch() {
        BatchBuilder bb = new BatchBuilder(syncano);
        bb.add(syncano.createObject(new Book("John", "John's life")));
        bb.add(syncano.createObject(new Book("Anne Ann", "Live well without head")));
        Response<List<BatchAnswer>> r = bb.send();
        List<BatchAnswer> answers = r.getData();
        assertNotNull(answers);
        Book book1 = answers.get(0).getDataAs(Book.class);
        assertNotNull(book1);
    }

    public void testBatchPretty() {
        //TODO use shared syncano instance
        BatchBuilder bb = new BatchBuilder(syncano);
        bb.add(RequestBuilder.createObject(new Book("John", "John's life")));
        bb.add(RequestBuilder.createObject(new Book("Anne Ann", "Live well without head")));
        Response<List<BatchAnswer>> r = bb.send();
        List<BatchAnswer> answers = r.getData();
        assertNotNull(answers);
        Book book1 = answers.get(0).getDataAs(Book.class);
        assertNotNull(book1);
    }

    @SyncanoClass(name = "Book")
    public static class Book extends SyncanoObject {

        public Book(String author, String title) {
            this.author = author;
            this.title = title;
        }

        @SyncanoField(name = "author")
        String author;
        @SyncanoField(name = "title")
        String title;
    }
}
