package com.syncano.library.documentation;

import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.Response;
import com.syncano.library.choice.FieldType;
import com.syncano.library.data.SyncanoObject;


public class Classes extends SyncanoApplicationTestCase {

    // ---------- Creating a Class
    public void testCreateClass() {
        Response<com.syncano.library.data.SyncanoClass> response = syncano.createSyncanoClass(Book.class).send();
    }

    @SyncanoClass(name = "Book")
    class Book extends SyncanoObject {

        public static final String FIELD_TITLE = "book_title";
        public static final String FIELD_PAGES = "total_pages";
        public static final String FIELD_NON_FINCTION = "non-fiction";
        public static final String FIELD_AUTHOR = "author";
        public static final String AUTHOR_CLASS = "Author";

        @SyncanoField(name = FIELD_TITLE)
        public String title;
        @SyncanoField(name = FIELD_PAGES)
        public Integer pages;
        @SyncanoField(name = FIELD_NON_FINCTION)
        public Boolean nonFiction;
        @SyncanoField(name = FIELD_AUTHOR, type = FieldType.REFERENCE, target = AUTHOR_CLASS)
        public Integer author;
    }
    // -----------------------------
}
