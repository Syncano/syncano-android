package com.syncano.library.tests;

import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.Response;
import com.syncano.library.choice.Case;
import com.syncano.library.choice.FieldType;
import com.syncano.library.data.SyncanoObject;

import java.util.List;

public class QueryTest extends SyncanoApplicationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createClass(Article.class);
        createTestsArticles();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        removeClass(Article.class);
    }

    public void testStringQueries() {
        runTestsOnColumn(Article.COLUMN_TITLE);
    }

    private void runTestsOnColumn(String column) {
        runTestWhereStartWith(column);
        runTestWhereEndWith(column);
        runTestWhereEqualTo(column);
        runTestWhereContains(column);
        runTestWhereConnectedQuery(column);
    }

    private void createTestsArticles() {
        Article article1 = new Article("Example Title");
        Article article2 = new Article("ExAmPlE TiTlE");
        Article article3 = new Article("other Title");
        Response<Article> responseCreateArticle1 = article1.save();
        Response<Article> responseCreateArticle2 = article2.save();
        Response<Article> responseCreateArticle3 = article3.save();
        assertTrue(responseCreateArticle1.isSuccess());
        assertTrue(responseCreateArticle2.isSuccess());
        assertTrue(responseCreateArticle3.isSuccess());
    }

    private void runTestWhereStartWith(String column) {
        Response<List<Article>> response;
        response = SyncanoObject.please(Article.class).where().startsWith(column, "ExAmPlE").get();
        assertTrue(response.isSuccess());
        assertEquals(1, response.getData().size());

        response = SyncanoObject.please(Article.class).where().startsWith(column, "EXAMPLE", Case.INSENSITIVE).get();
        assertTrue(response.isSuccess());
        assertEquals(2, response.getData().size());

        response = SyncanoObject.please(Article.class).where().startsWith(column, "nothing", Case.INSENSITIVE).get();
        assertTrue(response.isSuccess());
        assertEquals(0, response.getData().size());
    }

    private void runTestWhereConnectedQuery(String column) {
        Response<List<Article>> response;
        response = SyncanoObject.please(Article.class).where().gt(Article.COLUMN_RELEASE_YEAR, 1993).lt(Article.COLUMN_RELEASE_YEAR, 1995)
                .contains(column, "PlE Ti").get();
        assertTrue(response.isSuccess());
        assertEquals(0, response.getData().size());

        response = SyncanoObject.please(Article.class).where().gt(Article.COLUMN_RELEASE_YEAR, 1993).lt(Article.COLUMN_RELEASE_YEAR, 2000)
                .contains(column, "PlE Ti", Case.SENSITIVE).get();
        assertTrue(response.isSuccess());
        assertEquals(1, response.getData().size());
    }

    private void runTestWhereContains(String column) {
        Response<List<Article>> response;
        response = SyncanoObject.please(Article.class).where().contains(column, "ample Ti").get();
        assertTrue(response.isSuccess());
        assertEquals(1, response.getData().size());

        response = SyncanoObject.please(Article.class).where().contains(column, "ample Ti", Case.INSENSITIVE).get();
        assertTrue(response.isSuccess());
        assertEquals(2, response.getData().size());
    }

    private void runTestWhereEqualTo(String column) {
        Response<List<Article>> response;
        response = SyncanoObject.please(Article.class).where().eq(column, "example title").get();
        assertTrue(response.isSuccess());
        assertEquals(0, response.getData().size());
        response = SyncanoObject.please(Article.class).where().eq(column, "example title", Case.SENSITIVE).get();
        assertTrue(response.isSuccess());
        assertEquals(0, response.getData().size());
        response = SyncanoObject.please(Article.class).where().eq(column, "example title", Case.INSENSITIVE).get();
        assertTrue(response.isSuccess());
        assertEquals(2, response.getData().size());
        response = SyncanoObject.please(Article.class).where().eq(column, "example", Case.INSENSITIVE).get();
        assertTrue(response.isSuccess());
        assertEquals(0, response.getData().size());
        response = SyncanoObject.please(Article.class).where().eq(column, "Example Title", Case.SENSITIVE).get();
        assertTrue(response.isSuccess());
        assertEquals(1, response.getData().size());
    }


    private void runTestWhereEndWith(String column) {
        Response<List<Article>> response;
        response = SyncanoObject.please(Article.class).where().endsWith(column, "wrong_text_to_end_title", Case.INSENSITIVE).get();
        assertTrue(response.isSuccess());
        assertEquals(0, response.getData().size());
        response = SyncanoObject.please(Article.class).where().endsWith(column, "Example", Case.INSENSITIVE).get();
        assertTrue(response.isSuccess());
        assertEquals(0, response.getData().size());
        response = SyncanoObject.please(Article.class).where().endsWith(column, "Title").get();
        assertTrue(response.isSuccess());
        assertEquals(2, response.getData().size());
        response = SyncanoObject.please(Article.class).where().endsWith(column, "Title", Case.INSENSITIVE).get();
        assertTrue(response.isSuccess());
        assertEquals(3, response.getData().size());
    }

    @SyncanoClass(name = Article.TABLE_NAME)
    public static class Article extends SyncanoObject {
        public final static String TABLE_NAME = "article";
        public final static String COLUMN_TITLE = "title";
        public final static String COLUMN_TEXT = "content";
        public final static String COLUMN_RELEASE_YEAR = "release_year";

        @SyncanoField(name = COLUMN_TITLE, filterIndex = true, orderIndex = true)
        public String titleName;
        @SyncanoField(name = COLUMN_TEXT, type = FieldType.TEXT)
        public String content;
        @SyncanoField(name = COLUMN_RELEASE_YEAR, filterIndex = true)
        public int releaseYear = 1996;

        public Article() {
        }

        public Article(String title) {
            titleName = title;
            content = title;
        }
    }
}
