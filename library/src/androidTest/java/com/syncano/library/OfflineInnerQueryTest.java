package com.syncano.library;

import android.support.test.runner.AndroidJUnit4;


import com.syncano.library.Model.Author;
import com.syncano.library.Model.Book;
import com.syncano.library.api.ResponseGetList;
import com.syncano.library.offline.OfflineHelper;
import com.syncano.library.offline.OfflineMode;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class OfflineInnerQueryTest extends SyncanoAndroidTestCase {

    @Before
    public void setUp() throws Exception {
        super.setUp();
        OfflineHelper.deleteDatabase(getContext(), Author.class);
        OfflineHelper.deleteDatabase(getContext(), Book.class);
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
        author.saveDownloadedDataToStorage(true).save();
        otherAuthor.saveDownloadedDataToStorage(true).save();
        book.saveDownloadedDataToStorage(true).save();
        otherBook.saveDownloadedDataToStorage(true).save();
        Book bookWithoutAuthor = createPlainBook();
        bookWithoutAuthor.saveDownloadedDataToStorage(true).save();
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
        author.name = "Gal Anonim";
        author.isMale = true;
        return author;
    }

    @Test
    public void testInnerQueries() {

        // equal to String
        ResponseGetList<Book> pleaseResponse = Syncano.please(Book.class).mode(OfflineMode.LOCAL).where().eq(Book.FIELD_AUTHOR, Author.FIELD_NAME, "Adam Mickiewicz").get();
        assertTrue(pleaseResponse.isSuccess());
        assertFalse(pleaseResponse.getData().isEmpty());

        // greater than string
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 1970);
        Date date = c.getTime();
        pleaseResponse = Syncano.please(Book.class).mode(OfflineMode.LOCAL).where().gt(Book.FIELD_AUTHOR, Author.FIELD_BIRTH_DATE, date).get();
        assertTrue(pleaseResponse.isSuccess());
        assertFalse(pleaseResponse.getData().isEmpty());

        // equal to boolean
        pleaseResponse = Syncano.please(Book.class).mode(OfflineMode.LOCAL).where().eq(Book.FIELD_AUTHOR, Author.FIELD_IS_MALE, false).get();
        assertTrue(pleaseResponse.isSuccess());
        assertTrue(pleaseResponse.getData().isEmpty());

        // is null
        pleaseResponse = Syncano.please(Book.class).mode(OfflineMode.LOCAL).where().isNull(Book.FIELD_AUTHOR).get();
        assertTrue(pleaseResponse.isSuccess());
        assertFalse(pleaseResponse.getData().isEmpty());
        assertNull(pleaseResponse.getData().get(0).author);

        // is not null
        pleaseResponse = Syncano.please(Book.class).mode(OfflineMode.LOCAL).where().isNotNull(Book.FIELD_AUTHOR).get();
        assertTrue(pleaseResponse.isSuccess());
        assertFalse(pleaseResponse.getData().isEmpty());
        assertNotNull(pleaseResponse.getData().get(0).author);

        // inner is null
        pleaseResponse = Syncano.please(Book.class).mode(OfflineMode.LOCAL).where().isNull(Book.FIELD_AUTHOR, Author.FIELD_BIRTH_DATE).get();
        assertTrue(pleaseResponse.isSuccess());
        assertFalse(pleaseResponse.getData().isEmpty());

        // inner not null
        pleaseResponse = Syncano.please(Book.class).mode(OfflineMode.LOCAL).where().isNotNull(Book.FIELD_AUTHOR, Author.FIELD_BIRTH_DATE).get();
        assertTrue(pleaseResponse.isSuccess());
        assertFalse(pleaseResponse.getData().isEmpty());

        // inner in array
        String greatPoetsNames[] = new String[]{"Adam Mickiewicz", "Juliusz Slowacki"};
        pleaseResponse = Syncano.please(Book.class).mode(OfflineMode.LOCAL).where().in(Book.FIELD_AUTHOR, Author.FIELD_NAME, greatPoetsNames).get();
        assertTrue(pleaseResponse.isSuccess());
        assertFalse(pleaseResponse.getData().isEmpty());

        // inner starts with
        pleaseResponse = Syncano.please(Book.class).mode(OfflineMode.LOCAL).where().startsWith(Book.FIELD_AUTHOR, Author.FIELD_NAME, "A").get();
        assertTrue(pleaseResponse.isSuccess());
        assertFalse(pleaseResponse.getData().isEmpty());
        assertNotNull(pleaseResponse.getData().get(0).author);

        // mixed
        pleaseResponse = Syncano.please(Book.class).mode(OfflineMode.LOCAL).where()
                .startsWith(Book.FIELD_AUTHOR, Author.FIELD_NAME, "A")
                .gte(Book.FIELD_PAGES, 200).get();
        assertTrue(pleaseResponse.isSuccess());
        assertFalse(pleaseResponse.getData().isEmpty());
    }

    @After
    public void tearDown() throws Exception {
        removeClass(Author.class);
        removeClass(Book.class);
        super.tearDown();
    }

}
