//package com.syncano.library.tests;
//
//import com.syncano.library.Syncano;
//import com.syncano.library.SyncanoApplicationTestCase;
//import com.syncano.library.annotation.SyncanoClass;
//import com.syncano.library.annotation.SyncanoField;
//import com.syncano.library.data.SyncanoObject;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//public class CollectionTest extends SyncanoApplicationTestCase {
//
//    @Before
//    public void setUp() throws Exception {
//        super.setUp();
//        createClass(Book.class);
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        super.tearDown();
//        removeClass(Book.class);
//    }
//
//    private Collection<SyncanoObject> createPlainCollectionSyncanoObject() {
//        List<SyncanoObject> bookColecction = new ArrayList<>();
//        for (int i = 0; i < 30; i++) {
//            Book book1New = new Book("John " + i, "John's life");
//            bookColecction.add(book1New);
//        }
//        return bookColecction;
//    }
//
//    @Test
//    public void testBatch() {
//        // create objects
//        Syncano s = Syncano.getInstance();
//        Collection<SyncanoObject> collection = createPlainCollectionSyncanoObject();
//        s.createObjects(collection);
//    }
//
//    @SyncanoClass(name = "Book")
//    private static class Book extends SyncanoObject {
//
//        @SyncanoField(name = "author")
//        String author;
//        @SyncanoField(name = "title")
//        String title;
//
//        public Book(String author, String title) {
//            this.author = author;
//            this.title = title;
//        }
//    }
//}