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

import static org.junit.Assert.assertEquals;
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
        otherBook.pages = 100;
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
        author.name = "Adam Mickiewicz";
        author.isMale = true;
        Calendar cal = Calendar.getInstance();
        cal.set(1985, 4, 24);
        author.birthDate = cal.getTime();
        return author;
    }

    private Author createPlainAuthorWithoutDate() {
        Author author = new Author();
        author.name = "Al Anonim";
        author.isMale = true;
        return author;
    }

    @Test
    public void testInnerQueries() {
        // equal to string
        ResponseGetList<Book> pleaseResponse = Syncano.please(Book.class).where().eq(Book.FIELD_AUTHOR, Author.FIELD_NAME, "Adam Mickiewicz").get();
        assertTrue(pleaseResponse.isSuccess());
        assertEquals(1, pleaseResponse.getData().size());

        //greater than string
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 1970);
        Date date = c.getTime();
        pleaseResponse = Syncano.please(Book.class).where().gt(Book.FIELD_AUTHOR, Author.FIELD_BIRTH_DATE, date).get();
        assertTrue(pleaseResponse.isSuccess());
        assertEquals(1, pleaseResponse.getData().size());

        // equal to boolean
        pleaseResponse = Syncano.please(Book.class).where().eq(Book.FIELD_AUTHOR, Author.FIELD_IS_MALE, false).get();
        assertTrue(pleaseResponse.isSuccess());
        assertTrue(pleaseResponse.getData().isEmpty());

        // is null
        pleaseResponse = Syncano.please(Book.class).where().isNull(Book.FIELD_AUTHOR).get();
        assertTrue(pleaseResponse.isSuccess());
        assertEquals(1, pleaseResponse.getData().size());
        assertNull(pleaseResponse.getData().get(0).author);

        // is not null
        pleaseResponse = Syncano.please(Book.class).where().isNotNull(Book.FIELD_AUTHOR).get();
        assertTrue(pleaseResponse.isSuccess());
        assertEquals(2, pleaseResponse.getData().size());
        assertNotNull(pleaseResponse.getData().get(0).author);

        // inner is null
        pleaseResponse = Syncano.please(Book.class).where().isNull(Book.FIELD_AUTHOR, Author.FIELD_BIRTH_DATE).get();
        assertTrue(pleaseResponse.isSuccess());
        assertEquals(1, pleaseResponse.getData().size());

        // inner is not null
        pleaseResponse = Syncano.please(Book.class).where().isNotNull(Book.FIELD_AUTHOR, Author.FIELD_BIRTH_DATE).get();
        assertTrue(pleaseResponse.isSuccess());
        assertEquals(1, pleaseResponse.getData().size());

        // inner in array
        String greatPoetsNames[] = new String[]{"Adam Mickiewicz", "Juliusz Slowacki"};
        pleaseResponse = Syncano.please(Book.class).where().in(Book.FIELD_AUTHOR, Author.FIELD_NAME, greatPoetsNames).get();
        assertTrue(pleaseResponse.isSuccess());
        assertEquals(1, pleaseResponse.getData().size());

        // inner starts with
        pleaseResponse = Syncano.please(Book.class).where().startsWith(Book.FIELD_AUTHOR, Author.FIELD_NAME, "A").get();
        assertTrue(pleaseResponse.isSuccess());
        assertEquals(2, pleaseResponse.getData().size());
        assertNotNull(pleaseResponse.getData().get(0).author);

        //mixed
        pleaseResponse = Syncano.please(Book.class).where()
                .startsWith(Book.FIELD_AUTHOR, Author.FIELD_NAME, "A")
                .gte(Book.FIELD_PAGES, 200).get();
        assertTrue(pleaseResponse.isSuccess());
        assertEquals(1, pleaseResponse.getData().size());
    }

    @After
    public void tearDown() throws Exception {
        removeClass(Author.class);
        removeClass(Book.class);
        super.tearDown();
    }

}
