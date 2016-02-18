package com.syncano.library.documentation;

import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.api.Response;
import com.syncano.library.model.Author;
import com.syncano.library.model.Book;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


public class Classes extends SyncanoApplicationTestCase {

    @Before
    public void setUp() throws Exception {
        super.setUp();
        createClass(Author.class);
        createClass(Book.class);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        removeClass(Book.class);
        removeClass(Author.class);
    }

    private Book createPlainBook() {
        Book book = new Book();
        book.title = "Sample title";
        book.pages = 231;
        return book;
    }

    private Author createPlainAuthor() {
        Author author = new Author();
        author.name = "Adam Mickiewicz";
        author.isMale = true;
        return author;
    }

    @Test
    public void testReferenceObject() {
        Book book = createPlainBook();
        Author author = createPlainAuthor();
        book.author = author;
        Response authorSave = author.save();
        assertTrue(authorSave.isSuccess());
        Response bookSaveResponse = book.save();
        assertTrue(bookSaveResponse.isSuccess());
        assertNotNull(book.author);
        assertNotNull(book.author.name);
    }

    @Test
    public void testCreateReferenceWithPlainObject() {
        Book book = createPlainBook();
        book.author = createPlainAuthor();
        Response bookSaveResponse = book.save();
        assertTrue(bookSaveResponse.isSuccess());
        //Author is replaced by reference got from backend
        assertNull(book.author);
    }

    @Test
    public void testChangeReferenceToSyncanoObject() {
        Book book = createPlainBook();
        Author author = createPlainAuthor();
        book.author = author;
        Response authorSave = author.save();
        assertTrue(authorSave.isSuccess());
        Response bookSaveResponse = book.save();
        assertTrue(bookSaveResponse.isSuccess());
        assertNotNull(book.author);
        Author otherAuthor = new Author();
        otherAuthor.name = "Juliusz Slowacki";
        otherAuthor.save();
        book.author = otherAuthor;
        book.fetch();
    }

    @Test
    public void testChangeReferenceToPlainObject() {
        Book book = createPlainBook();
        Author author = createPlainAuthor();
        book.author = author;
        Response authorSave = author.save();
        assertTrue(authorSave.isSuccess());
        Response bookSaveResponse = book.save();
        assertTrue(bookSaveResponse.isSuccess());
        assertNotNull(book.author);
        Author otherAuthor = new Author();
        otherAuthor.name = "Juliusz Slowacki";
        //change book author without save
        book.author = otherAuthor;
        book.fetch();
        //Should be dirty object
        assertTrue(book.author.isDirty());
    }
}
