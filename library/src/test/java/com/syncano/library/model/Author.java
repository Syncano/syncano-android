package com.syncano.library.model;

import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.data.SyncanoObject;

import java.util.Date;

@SyncanoClass(name = Author.AUTHOR_CLASS)
public class Author extends SyncanoObject {
    public static final String AUTHOR_CLASS = "Author";

    public static final String FIELD_NAME = "name";
    public static final String FIELD_IS_MALE = "is_male";
    public static final String FIELD_BIRTH_DATE = "birth_date";

    @SyncanoField(name = FIELD_NAME, filterIndex = true)
    public String name;
    @SyncanoField(name = FIELD_IS_MALE, filterIndex = true)
    public Boolean isMale;
    @SyncanoField(name = FIELD_BIRTH_DATE, filterIndex = true)
    public Date birthDate;

    public Author() {
    }

    public Author(String name) {
        this.name = name;
    }
}