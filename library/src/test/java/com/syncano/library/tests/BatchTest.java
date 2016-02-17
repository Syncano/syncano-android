package com.syncano.library.tests;

import com.syncano.library.Syncano;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.api.BatchBuilder;
import com.syncano.library.api.Response;
import com.syncano.library.data.BatchAnswer;
import com.syncano.library.model.Book;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BatchTest extends SyncanoApplicationTestCase {

    @Before
    public void setUp() throws Exception {
        super.setUp();
        createClass(Book.class);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        removeClass(Book.class);
    }

    @Test
    public void testBatch() {
        // create objects
        Syncano s = Syncano.getInstance();
        BatchBuilder bb = new BatchBuilder(s);
        Book book1New = new Book("John", "John's life");
        bb.add(s.createObject(book1New));
        Book book2New = new Book("Anne Ann", "Live well without head");
        bb.add(s.createObject(book2New));
        Response<List<BatchAnswer>> r = bb.send();
        assertTrue(r.isSuccess());

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
        assertTrue(r2.isSuccess());

        List<BatchAnswer> answers2 = r2.getData();
        assertNotNull(answers2);
        Book bookUdpated = answers2.get(0).getDataAs(Book.class);
        assertEquals(book1.title, bookUdpated.title);
        assertEquals(Response.HTTP_CODE_NO_CONTENT, answers2.get(1).getHttpResultCode());
    }
}
