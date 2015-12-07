package com.syncano.library.tests;

import com.syncano.library.Syncano;
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
        // create objects
        Syncano s = Syncano.getInstance();
        BatchBuilder bb = new BatchBuilder(s);
        Book book1New = new Book("John", "John's life");
        bb.add(s.createObject(book1New));
        Book book2New = new Book("Anne Ann", "Live well without head");
        bb.add(s.createObject(book2New));
        Response<List<BatchAnswer>> r = bb.send();
        assertEquals(Response.HTTP_CODE_SUCCESS, r.getHttpResultCode());

        List<BatchAnswer> answers = r.getData();
        assertNotNull(answers);
        Book book1 = answers.get(0).getDataAs(Book.class);
        assertNotNull(book1);
        assertEquals(book1New.title, book1.title);
        Book book2 = answers.get(1).getDataAs(Book.class);
        assertNotNull(book2);
        assertEquals(book2New.title, book2.title);

        // delete one object, update other
        book1.title += "abc";
        BatchBuilder bb2 = new BatchBuilder(s);
        bb2.add(s.updateObject(book1));
        bb2.add(s.deleteObject(book2));
        Response<List<BatchAnswer>> r2 = bb2.send();
        assertEquals(Response.HTTP_CODE_SUCCESS, r2.getHttpResultCode());

        List<BatchAnswer> answers2 = r2.getData();
        assertNotNull(answers2);
        Book bookUdpated = answers2.get(0).getDataAs(Book.class);
        assertEquals(book1.title, bookUdpated.title);
        assertEquals(Response.HTTP_CODE_NO_CONTENT, answers2.get(1).getHttpResultCode());
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
