package com.syncano.library.tests;

import com.syncano.library.Syncano;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.api.ResponseGetList;
import com.syncano.library.model.Author;
import com.syncano.library.model.Book;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class InnerQueryTest extends SyncanoApplicationTestCase {

    @Before
    public void setUp() throws Exception {
        super.setUp();
        createClass(Author.class);
        createClass(Book.class);
        createTestBooks();
    }

    private void createTestBooks() {
        Book book = createPlainBook();
        Book otherBook = createPlainBook();
        Author author = createPlainAuthor();
        Author otherAuthor = createPlainAuthorWithoutDate();
        book.author = author;
        otherBook.author = otherAuthor;
        author.save();
        otherAuthor.save();
        book.save();
        otherBook.save();
        Book bookWithoutAuthor = createPlainBook();
        bookWithoutAuthor.save();
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
        Calendar cal = Calendar.getInstance();
        cal.set(1985, 4, 24);
        author.birthDate = cal.getTime();
        return author;
    }

    private Author createPlainAuthorWithoutDate() {
        Author author = new Author();
        author.name = "Gal";
        author.surname = "Anonim";
        author.isMale = true;
        return author;
    }

    @Test
    public void testEqualToString() {
        ResponseGetList<Book> pleaseResponse = Syncano.please(Book.class).where().eq(Book.FIELD_AUTHOR, Author.FIELD_NAME, "Adam").eq(Book.FIELD_AUTHOR, Author.FIELD_SURNAME, "Mickiewicz").get();
        assertTrue(pleaseResponse.isSuccess());
        assertFalse(pleaseResponse.getData().isEmpty());
    }

    @Test
    public void testGreaterThanString() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 1970);
        Date date = c.getTime();
        ResponseGetList<Book> pleaseResponse = Syncano.please(Book.class).where().gt(Book.FIELD_AUTHOR, Author.FIELD_BIRTH_DATE, date).get();
        assertTrue(pleaseResponse.isSuccess());
        assertFalse(pleaseResponse.getData().isEmpty());
    }

    @Test
    public void testEqualToBoolean() {
        ResponseGetList<Book> pleaseResponse = Syncano.please(Book.class).where().eq(Book.FIELD_AUTHOR, Author.FIELD_IS_MALE, false).get();
        assertTrue(pleaseResponse.isSuccess());
        assertTrue(pleaseResponse.getData().isEmpty());
    }

    @Test
    public void testIsNull() {
        ResponseGetList<Book> pleaseResponse = Syncano.please(Book.class).where().isNull(Book.FIELD_AUTHOR).get();
        assertTrue(pleaseResponse.isSuccess());
        assertFalse(pleaseResponse.getData().isEmpty());
        assertNull(pleaseResponse.getData().get(0).author);
    }

    @Test
    public void testIsNotNull() {
        ResponseGetList<Book> pleaseResponse = Syncano.please(Book.class).where().isNotNull(Book.FIELD_AUTHOR).get();
        assertTrue(pleaseResponse.isSuccess());
        assertFalse(pleaseResponse.getData().isEmpty());
        assertNotNull(pleaseResponse.getData().get(0).author);
    }

    @Test
    public void testInnerIsNull() {
        ResponseGetList<Book> pleaseResponse = Syncano.please(Book.class).where().isNull(Book.FIELD_AUTHOR, Author.FIELD_BIRTH_DATE).get();
        assertTrue(pleaseResponse.isSuccess());
        assertFalse(pleaseResponse.getData().isEmpty());
    }

    @Test
    public void testInnerIsNotNull() {
        ResponseGetList<Book> pleaseResponse = Syncano.please(Book.class).where().isNotNull(Book.FIELD_AUTHOR, Author.FIELD_BIRTH_DATE).get();
        assertTrue(pleaseResponse.isSuccess());
        assertFalse(pleaseResponse.getData().isEmpty());
    }

    @Test
    public void testInnerInArray() {
        String greatPoetsSurnames[] = new String[]{"Mickiewicz", "Slowacki", "Tuwim"};
        ResponseGetList<Book> pleaseResponse = Syncano.please(Book.class).where().in(Book.FIELD_AUTHOR, Author.FIELD_SURNAME, greatPoetsSurnames).get();
        assertTrue(pleaseResponse.isSuccess());
        assertFalse(pleaseResponse.getData().isEmpty());
    }

    @Test
    public void testInnerStartsWith() {
        ResponseGetList<Book> pleaseResponse = Syncano.please(Book.class).where().startsWith(Book.FIELD_AUTHOR, Author.FIELD_NAME, "A").get();
        assertTrue(pleaseResponse.isSuccess());
        assertFalse(pleaseResponse.getData().isEmpty());
        assertNotNull(pleaseResponse.getData().get(0).author);
    }

    @After
    public void tearDown() throws Exception {
        removeClass(Author.class);
        removeClass(Book.class);
        super.tearDown();
    }

}
