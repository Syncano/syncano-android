package com.syncano.library.tests;

import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.Response;
import com.syncano.library.choice.Case;
import com.syncano.library.data.SyncanoClass;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.utils.SyncanoClassHelper;

import java.util.ArrayList;
import java.util.List;

public class QueryTest extends SyncanoApplicationTestCase {
    private static final String OBJECT_TITLE_NAME = "Example Title";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createClass(Article.class);

        Response<SyncanoClass> respClass = syncano.getSyncanoClass(SyncanoClassHelper.getSyncanoClassName(Article.class)).send();
        assertTrue(respClass.isSuccess());
        SyncanoClass clazz = respClass.getData();
        assertNotNull(clazz);
        assertEquals(SyncanoClassHelper.getSyncanoClassSchema(Article.class), clazz.getSchema());
    }

    public void testStringQuery() {
        List<Article> articleList = createTestsArticles();
        runTestWhereStartWith();
        runTestWhereEndWith();
        runTestWhereEqualTo();
        runTestWhereContains();
        runTestWhereConnectedQuery();
        removeTestsArticles(articleList);
    }

    private List<Article> createTestsArticles() {
        List<Article> articleList = new ArrayList<>();
        Article article1 = new Article();
        article1.titleName = OBJECT_TITLE_NAME;
        article1.releaseYear = 1993;
        Article article2 = new Article();
        article2.releaseYear = 1994;
        Article article3 = new Article();
        article3.releaseYear = 1995;
        Response<Article> responseCreateArticle1 = article1.save();
        Response<Article> responseCreateArticle2 = article2.save();
        Response<Article> responseCreateArticle3 = article3.save();
        assertTrue(responseCreateArticle1.isSuccess());
        assertTrue(responseCreateArticle2.isSuccess());
        assertTrue(responseCreateArticle3.isSuccess());
        articleList.add(responseCreateArticle1.getData());
        articleList.add(responseCreateArticle2.getData());
        articleList.add(responseCreateArticle3.getData());
        return articleList;
    }

    private void removeTestsArticles(List<Article> articleList) {
        for (Article article : articleList) {
            article.delete();
        }
    }

    private void runTestWhereStartWith() {
        Response<List<Article>> response;
        response = SyncanoObject.please(Article.class).where().startWith(Article.COLUMN_TITLE, "ExAmPle").get();
        assertTrue(response.isSuccess());
        assertTrue(response.getData().isEmpty());

        response = SyncanoObject.please(Article.class).where().startWith(Article.COLUMN_TITLE, "ExAmPle", Case.INSENSITIVE).get();
        assertTrue(response.isSuccess());
        assertFalse(response.getData().isEmpty());
    }

    private void runTestWhereConnectedQuery() {
        Response<List<Article>> response;
        response = SyncanoObject.please(Article.class).where().gt(Article.COLUMN_RELEASE_YEAR, 1993).lt(Article.COLUMN_RELEASE_YEAR, 1995).get();
        assertTrue(response.isSuccess());
        assertEquals(1, response.getData().size());
    }

    private void runTestWhereContains() {
        Response<List<Article>> response;
        response = SyncanoObject.please(Article.class).where().contains(Article.COLUMN_TITLE, "AmPle ti").get();
        assertTrue(response.isSuccess());
        assertTrue(response.getData().isEmpty());

        response = SyncanoObject.please(Article.class).where().contains(Article.COLUMN_TITLE, "AmPle ti", Case.INSENSITIVE).get();
        assertTrue(response.isSuccess());
        assertFalse(response.getData().isEmpty());
    }

    private void runTestWhereEqualTo() {
        Response<List<Article>> response;
        response = SyncanoObject.please(Article.class).where().eq(Article.COLUMN_TITLE, "example title").get();
        assertTrue(response.isSuccess());
        assertTrue(response.getData().isEmpty());
        response = SyncanoObject.please(Article.class).where().eq(Article.COLUMN_TITLE, "example title", Case.SENSITIVE).get();
        assertTrue(response.isSuccess());
        assertTrue(response.getData().isEmpty());
        response = SyncanoObject.please(Article.class).where().eq(Article.COLUMN_TITLE, "ExAmPle", Case.INSENSITIVE).get();
        assertTrue(response.isSuccess());
        assertTrue(response.getData().isEmpty());
        response = SyncanoObject.please(Article.class).where().eq(Article.COLUMN_TITLE, "example title", Case.INSENSITIVE).get();
        assertTrue(response.isSuccess());
        assertFalse(response.getData().isEmpty());
        response = SyncanoObject.please(Article.class).where().eq(Article.COLUMN_TITLE, "Example Title", Case.SENSITIVE).get();
        assertTrue(response.isSuccess());
        assertFalse(response.getData().isEmpty());
    }


    private void runTestWhereEndWith() {
        Response<List<Article>> response;
        response = SyncanoObject.please(Article.class).where().endsWith(Article.COLUMN_TITLE, "wrong_text_to_end_title", Case.INSENSITIVE).get();
        assertTrue(response.isSuccess());
        assertTrue(response.getData().isEmpty());
        response = SyncanoObject.please(Article.class).where().endsWith(Article.COLUMN_TITLE, "Example", Case.INSENSITIVE).get();
        assertTrue(response.isSuccess());
        assertTrue(response.getData().isEmpty());
        response = SyncanoObject.please(Article.class).where().endsWith(Article.COLUMN_TITLE, "Title").get();
        assertTrue(response.isSuccess());
        assertFalse(response.getData().isEmpty());
    }

    @com.syncano.library.annotation.SyncanoClass(name = Article.TABLE_NAME)
    public static class Article extends SyncanoObject {
        public final static String TABLE_NAME = "article";
        public final static String COLUMN_TITLE = "title";
        public final static String COLUMN_TEXT = "content";
        public final static String COLUMN_RELEASE_YEAR = "release_year";

        @SyncanoField(name = COLUMN_TITLE, filterIndex = true, orderIndex = true)
        public String titleName;
        @SyncanoField(name = COLUMN_TEXT)
        public String content;
        @SyncanoField(name = COLUMN_RELEASE_YEAR, filterIndex = true)
        public int releaseYear;

        public Article() {
        }

        public Article(int id) {
            setId(id);
        }
    }
}
