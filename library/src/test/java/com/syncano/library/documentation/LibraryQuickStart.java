package com.syncano.library.documentation;

import android.content.Context;
import android.util.Log;

import com.syncano.library.Syncano;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.SyncanoBuilder;
import com.syncano.library.SyncanoDashboard;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.Response;
import com.syncano.library.api.ResponseGetList;
import com.syncano.library.callbacks.SyncanoCallback;
import com.syncano.library.callbacks.SyncanoListCallback;
import com.syncano.library.choice.FilterType;
import com.syncano.library.choice.RuntimeName;
import com.syncano.library.choice.SocialAuthBackend;
import com.syncano.library.choice.TraceStatus;
import com.syncano.library.data.AbstractUser;
import com.syncano.library.data.DataEndpoint;
import com.syncano.library.data.Profile;
import com.syncano.library.data.Script;
import com.syncano.library.data.ScriptEndpoint;
import com.syncano.library.data.SyncanoFile;
import com.syncano.library.data.Trace;
import com.syncano.library.data.User;
import com.syncano.library.model.Author;
import com.syncano.library.model.Book;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LibraryQuickStart extends SyncanoApplicationTestCase {
    private static final String TAG = LibraryQuickStart.class.getSimpleName();

    private Syncano syncano;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        this.syncano = super.syncano;
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void initiateSyncano() {
        Context context = null;
        //
        Syncano syncano = new SyncanoBuilder().apiKey("YOUR API KEY").instanceName("YOUR INSTANCE")
                .androidContext(context).build();
        //
    }

    @Test
    public void singleton() {
        Context context = null;
        //
        new SyncanoBuilder().apiKey("YOUR API KEY").instanceName("YOUR INSTANCE")
                .androidContext(context).setAsGlobalInstance(true).build();
        //
    }

    @Test
    public void getSync() throws InterruptedException {
        createClass(Author.class);
        createClass(Book.class);
        //
        ResponseGetList<Book> response;
        // on global Syncano instance
        response = Syncano.please(Book.class).get();
        // on specific Syncano instance
        response = Syncano.please(Book.class).on(syncano).get();

        List<Book> books = response.getData();
        //
    }

    @Test
    public void getAsync() throws InterruptedException {
        createClass(Author.class);
        createClass(Book.class);
        //
        SyncanoListCallback<Book> callback = new SyncanoListCallback<Book>() {
            @Override
            public void success(ResponseGetList<Book> response, List<Book> result) {
                // Request succeed.
            }

            @Override
            public void failure(ResponseGetList<Book> response) {
                // Something went wrong. Check error codes in response object.
            }
        };

        // on global Syncano instance
        Syncano.please(Book.class).get(callback);
        // on specific Syncano instance
        Syncano.please(Book.class).on(syncano).get(callback);
        //
    }

    @Test
    public void getSingle() throws InterruptedException {
        createClass(Author.class);
        createClass(Book.class);
        int id = 5;
        //
        //sync
        Book book = Syncano.please(Book.class).get(id).getData();
        // async
        Syncano.please(Book.class).get(id, new SyncanoCallback<Book>() {
            @Override
            public void success(Response<Book> response, Book result) {
            }

            @Override
            public void failure(Response<Book> response) {
            }
        });
        //
    }

    @Test
    public void createUpdateObject() throws InterruptedException {
        createClass(Author.class);
        createClass(Book.class);
        //
        Book book = new Book();
        book.title = "John's life";
        Response<Book> response = book.save();
        // you can check if any response succeeded calling isSuccess()
        if (response.isSuccess()) {
        }
        //

        //
        // id has to be set in an object
        book.title = "New Title";
        book.save();
        //
    }

    @Test
    public void references() throws InterruptedException {
        createClass(Author.class);
        createClass(Book.class);
        //
        // proper way
        Book book = new Book();
        Author author = new Author();
        book.author = author;
        // author created on Syncano first and has id set
        Response<Author> authorSave = author.save();
        Response<Book> bookSave = book.save();

        // wrong way
        book = new Book();
        author = new Author();
        book.author = author;
        // book will be saved but without author
        // or will return an error if author has id set, but doesn't exist on server
        bookSave = book.save();
        //
    }

    @Test
    public void clearField() throws InterruptedException {
        createClass(Author.class);
        createClass(Book.class);
        //
        Book book = new Book();
        book.title = "John's life";
        book.pages = 1500;
        book.save();

        book.clearField(Book.FIELD_TITLE);
        book.save();
        // title field should be empty now
        //
    }

    @Test
    public void where() throws InterruptedException {
        createClass(Author.class);
        createClass(Book.class);
        //
        ResponseGetList<Book> response = Syncano.please(Book.class).orderBy(Book.FIELD_TITLE)
                .where().gte(Book.FIELD_ID, 10).lte(Book.FIELD_ID, 15).get();

        //You can also query by your reference field
        response = Syncano.please(Book.class).where()
                .eq(Book.FIELD_AUTHOR, Author.FIELD_NAME, "Adam").get();
        //
    }

    @Test
    public void paging() throws InterruptedException {
        createClass(Author.class);
        createClass(Book.class);
        //
        // Automatic paging. It downloads all objects from server in a loop. It will cause problems, when you have many objects.
        ResponseGetList<Book> resp = Syncano.please(Book.class).getAll(true).get();

        // Manual paging with specified limit of elements per one single page.
        resp = Syncano.please(Book.class).limit(10).get();
        while (resp.hasNextPage()) {
            resp = resp.getNextPage();
        }
        //
    }

    @Test
    public void filterFields() throws InterruptedException {
        createClass(Author.class);
        createClass(Book.class);
        //
        ResponseGetList<Book> response = Syncano.please(Book.class)
                .selectFields(FilterType.INCLUDE_FIELDS, Book.FIELD_ID, Book.FIELD_TITLE).get();
        //
    }

    @Test
    public void dataEndpoints() throws InterruptedException {
        createClass(Author.class);
        createClass(Book.class);
        DataEndpoint de = new DataEndpoint("book_and_author", Book.class);
        de.addExpandField(Book.FIELD_AUTHOR);
        assertTrue(((SyncanoDashboard) syncano).createDataEndpoint(de).send().isSuccess());
        //
        ResponseGetList<Book> response = Syncano.please(Book.class).dataEndpoint("book_and_author").get();
        //

        //
        response = Syncano.please(Book.class).dataEndpoint("book_and_author").orderBy(Book.FIELD_TITLE).get();
        //
    }

    @Test
    public void deleteObject() throws InterruptedException {
        createClass(Author.class);
        createClass(Book.class);
        Book book = new Book("Some book", "Great book");
        book.save();
        int bookId = book.getId();
        //
        Response<Book> response = syncano.deleteObject(Book.class, bookId).send();
        // or other way, when you have object with an id set
        response = book.delete();
        //
    }

    @Test
    public void estimateCount() throws InterruptedException {
        createClass(Author.class);
        createClass(Book.class);
        //
        // If you want to get estimated count with your data
        ResponseGetList<Book> response = Syncano.please(Book.class).estimateCount().get();
        Integer count = response.getEstimatedCount();
        // If you want only estimated count of objects
        Response<Integer> responseCount = Syncano.please(Book.class).getCountEstimation();
        count = responseCount.getData();
        //
    }

    @Test
    public void runScript() throws InterruptedException {
        Script s = new Script("some_label", "", RuntimeName.NODEJS);
        SyncanoDashboard dash = (SyncanoDashboard) syncano;
        Response<Script> resp = dash.createScript(s).send();
        int scriptId = resp.getData().getId();

        //
        Response<Trace> response = new Script(scriptId).run();
        Trace trace = response.getData();
        // running script is asynchronous, you can check current execution status calling fetch()
        trace.fetch();
        if (trace.getStatus() == TraceStatus.SUCCESS) {
        }
        //

        dash.deleteScript(scriptId).send();
    }

    @Test
    public void runScriptEndpoint() throws InterruptedException {
        //
        ScriptEndpoint se = new ScriptEndpoint("endpoint_name");
        Response<Trace> response = se.run();
        // with script endpoint you get result immediately, don't have to call fetch() on Trace
        String output = se.getOutput();
        //
    }

    @Test
    public void runScriptEndpointCustom() throws InterruptedException {
        //
        ScriptEndpoint se = new ScriptEndpoint("endpoint_name");
        Response<MyResponse> response = se.runCustomResponse(MyResponse.class);
        MyResponse output = se.getCustomResponse();

        // if you want to parse result by yourself
        Response<String> stringResponse = se.runCustomResponse();
        String stringOutput = se.getCustomResponse();
        //
    }

    public class MyResponse {
        @SyncanoField(name = "some_param")
        String someParam;
    }

    @Test
    public void responseCodesErrors() throws InterruptedException {
        createClass(Author.class);
        createClass(Book.class);
        //
        ResponseGetList<Book> response = Syncano.please(Book.class).get();
        if (response.isSuccess()) {
            // Success
        } else {
            Log.d(TAG, "Result Code: " + response.getHttpResultCode());
            Log.d(TAG, "Reason Phrase: " + response.getHttpReasonPhrase());
        }

        response.getResultCode(); // Library error codes
        response.getError(); // Library error message

        response.getHttpResultCode(); // Http result code
        response.getHttpReasonPhrase(); // Http error message
        //
    }

    @Test
    public void registerUser() throws InterruptedException {
        //
        User newUser = new User("username", "password");
        Response<User> response = newUser.register();
        //
    }

    @Test
    public void loginUser() throws InterruptedException {
        //
        User user = new User("username", "password");
        Response<User> response = user.login();
        //
    }

    @Test
    public void loginSocialUser() throws InterruptedException {
        //
        //SocialAuthBackend.FACEBOOK
        //SocialAuthBackend.GOOGLE_OAUTH2
        //SocialAuthBackend.LINKEDIN
        //SocialAuthBackend.TWITTER
        User user = new User(SocialAuthBackend.FACEBOOK, "freaky_auth_token");
        Response<User> response = user.loginSocialUser();
        //
    }

    @Test
    public void customUserAndProfile() throws InterruptedException {
        //
        MyUser user = new MyUser("username", "password");
        Response<MyUser> responseLogin = user.login();
        if (responseLogin.isSuccess()) {
            MyUserProfile profile = user.getProfile();
            profile.avatar = new SyncanoFile(new File(getAssetsDir(), "blue.png"));
            Response<MyUserProfile> responseSaveAvatar = profile.save();
        }
        //
    }

    public static class MyUser extends AbstractUser<MyUserProfile> {
        public MyUser(String login, String pass) {
            super(login, pass);
        }

        public MyUser() {
        }
    }

    public static class MyUserProfile extends Profile {
        public MyUserProfile() {
        }

        @SyncanoField(name = "avatar")
        public SyncanoFile avatar;
    }
}
