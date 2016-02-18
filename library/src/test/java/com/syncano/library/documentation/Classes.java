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
        author.name = "Adam";
        author.surname = "Mickiewicz";
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
    public void testRemoveReferenceObject() {
        Book book = createPlainBook();
        Author author = createPlainAuthor();
        book.author = author;
        Response authorSave = author.save();
        assertTrue(authorSave.isSuccess());
        Response bookSaveResponse = book.save();
        assertTrue(bookSaveResponse.isSuccess());
        book.author = null;
        Response bookRemoveReferenceResponse = book.save();
        assertTrue(bookRemoveReferenceResponse.isSuccess());
        Response bookFetchResponse = book.fetch();
        assertTrue(bookFetchResponse.isSuccess());
        assertNull(book.author);
    }

    @Test
    public void testChangeReferenceObject() {
        Book book = createPlainBook();
        Author originalAuthor = createPlainAuthor();
        book.author = originalAuthor;
        Response authorSave = originalAuthor.save();
        assertTrue(authorSave.isSuccess());
        Response bookSaveResponse = book.save();
        assertTrue(bookSaveResponse.isSuccess());
        changeAuthorToGhost(book.getId());
        Response bookFetchResponse = book.fetch();
        assertTrue(bookFetchResponse.isSuccess());
        assertNotNull(book.author);
        assertTrue(book.author.isDirty());
        assertNull(book.author.name);
        assertNull(book.author.surname);
    }

    private void changeAuthorToGhost(Integer bookId) {
        Author ghostAuthor = createPlainAuthor();
        ghostAuthor.surname = "Ghost";
        Response ghostAuthorSave = ghostAuthor.save();
        assertTrue(ghostAuthorSave.isSuccess());
        Response<Book> requestGetOneBook = syncano.getObject(Book.class, bookId).send();
        assertTrue(requestGetOneBook.isSuccess());
        Book book = requestGetOneBook.getData();
        assertNotNull(book);
        book.author = ghostAuthor;
        Response response = syncano.updateObject(book, false).send();
        assertTrue(response.isSuccess());
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
        otherAuthor.name = "Juliusz";
        otherAuthor.surname = "Slowacki";
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
        otherAuthor.name = "Juliusz";
        otherAuthor.surname = "Slowacki";
        //change book author without save
        book.author = otherAuthor;
        Response<Book> fetchResponse = book.fetch();
        assertTrue(fetchResponse.isSuccess());
        //Should be dirty object
        assertTrue(book.author.isDirty());
        //Reference should be point to new object
        assertTrue(book.author != otherAuthor);
    }
}
