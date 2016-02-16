package com.syncano.library.documentation;

import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.Response;
import com.syncano.library.choice.FieldType;
import com.syncano.library.data.SyncanoObject;

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
        //Should be dirty object
        assertTrue(book.author.isDirty());
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

    @SyncanoClass(name = Book.BOOK_CLASS)
    private class Book extends SyncanoObject {
        public static final String BOOK_CLASS = "Book";

        public static final String FIELD_TITLE = "book_title";
        public static final String FIELD_PAGES = "total_pages";
        public static final String FIELD_NON_FICTION = "non-fiction";
        public static final String FIELD_AUTHOR = "author";

        @SyncanoField(name = FIELD_TITLE)
        public String title;
        @SyncanoField(name = FIELD_PAGES)
        public Integer pages;
        @SyncanoField(name = FIELD_NON_FICTION)
        public Boolean nonFiction;
        @SyncanoField(name = FIELD_AUTHOR, type = FieldType.REFERENCE, target = Author.AUTHOR_CLASS)
        public Author author;
    }

    @SyncanoClass(name = Author.AUTHOR_CLASS)
    private class Author extends SyncanoObject {
        public static final String AUTHOR_CLASS = "Author";

        public static final String FIELD_NAME = "name";
        public static final String FIELD_SURNAME = "surname";
        public static final String FIELD_IS_MALE = "is_male";

        @SyncanoField(name = FIELD_NAME)
        public String name;
        @SyncanoField(name = FIELD_SURNAME)
        public String surname;
        @SyncanoField(name = FIELD_IS_MALE)
        public Boolean isMale;

    }

    // -----------------------------
}
