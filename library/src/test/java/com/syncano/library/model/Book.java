package com.syncano.library.model;

import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.data.SyncanoObject;

@SyncanoClass(name = Book.BOOK_CLASS)
public class Book extends SyncanoObject {
    public static final String BOOK_CLASS = "Book";

    public static final String FIELD_TITLE = "book_title";
    public static final String FIELD_SUBTITLE = "subtitle";
    public static final String FIELD_PAGES = "total_pages";
    public static final String FIELD_AUTHOR = "author";

    @SyncanoField(name = FIELD_TITLE, orderIndex = true)
    public String title;
    @SyncanoField(name = FIELD_SUBTITLE, filterIndex = true, orderIndex = true)
    public String subtitle;
    @SyncanoField(name = FIELD_PAGES, orderIndex = true)
    public Integer pages;
    @SyncanoField(name = FIELD_AUTHOR, filterIndex = true)
    public Author author;

    public Book(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    public Book(String title, Author author) {
        this.title = title;
        this.author = author;
    }

    public Book() {
    }
}
