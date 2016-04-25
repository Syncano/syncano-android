package com.syncano.library.documentation;

import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.api.BatchBuilder;
import com.syncano.library.api.Response;
import com.syncano.library.data.BatchAnswer;
import com.syncano.library.model.Author;
import com.syncano.library.model.Book;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Batch extends SyncanoApplicationTestCase {

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
    public void testBatch() {
        Book book1 = new Book();
        book1.save();
        Book book2 = new Book();
        book2.save();
        //
        // delete one object, update other
        BatchBuilder batch = new BatchBuilder(syncano);
        batch.add(syncano.updateObject(book1));
        batch.add(syncano.deleteObject(book2));
        Response<List<BatchAnswer>> response = batch.send();
        //

        assertTrue(response.isSuccess());

        //
        List<BatchAnswer> answers = response.getData();
        Book bookUdpated = answers.get(0).getDataAs(Book.class);
        //
        assertEquals(book1.title, bookUdpated.title);
        assertEquals(Response.HTTP_CODE_NO_CONTENT, answers.get(1).getHttpResultCode());
    }
}
