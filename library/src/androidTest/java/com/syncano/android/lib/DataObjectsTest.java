package com.syncano.android.lib;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.syncano.android.lib.annotation.SyncanoClass;
import com.syncano.android.lib.annotation.SyncanoField;
import com.syncano.android.lib.api.Page;
import com.syncano.android.lib.api.SyncanoException;
import com.syncano.android.lib.data.SyncanoObject;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class DataObjectsTest extends ApplicationTestCase<Application> {

    private static final String TAG = DataObjectsTest.class.getSimpleName();

    private final static int TIMEOUT_MILLIS = 10 * 1000;

    private Syncano syncano;
    private CountDownLatch lock;

    public DataObjectsTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        syncano = new Syncano(Config.API_KEY, Config.INSTANCE_NAME);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testDataObjects() throws InterruptedException {

        /*String userName = "Andrzej Kartofel";
        String password = "Potato";
        String newUserName = "Andrzej Ziemniak";

        final UserClass userClass = new UserClass();
        userClass.userName = userName;
        userClass.password = password;

        final WeakReference<UserClass> resultRef = new WeakReference<>();

        // ----------------- Create -----------------
        lock = new CountDownLatch(1);
        syncano.createObject(userClass, new GetCallback<UserClass>() {

            @Override
            public void success(UserClass object) {
                assertNotNull(object);
                resultRef.value = object;
                lock.countDown();
            }

            @Override
            public void failure(SyncanoException error) {
                fail("Failed to create object.");
            }
        });
        lock.await(TIMEOUT_MILLIS, TimeUnit.MICROSECONDS);

        // ----------------- Get One -----------------
        lock = new CountDownLatch(1);
        syncano.getObject(UserClass.class, resultRef.value.getId(), null, new GetCallback<UserClass>() {
            @Override
            public void success(UserClass object) {
                assertNotNull(object);
                assertEquals(resultRef.value.userName, object.userName);
                assertEquals(resultRef.value.userName, object.userName);
                lock.countDown();
            }

            @Override
            public void failure(SyncanoException error) {
                fail("Failed to get object.");
            }
        });
        lock.await(TIMEOUT_MILLIS, TimeUnit.MICROSECONDS);

        // ----------------- Update -----------------
        resultRef.value.userName = newUserName;
        lock = new CountDownLatch(1);
        syncano.updateObject(resultRef.value, new GetCallback<UserClass>() {
            @Override
            public void success(UserClass object) {
                assertNotNull(object);
                assertEquals(resultRef.value.userName, object.userName);
                assertEquals(resultRef.value.userName, object.userName);
                lock.countDown();
            }

            @Override
            public void failure(SyncanoException error) {
                fail("Failed to update object.");
            }
        });
        lock.await(TIMEOUT_MILLIS, TimeUnit.MICROSECONDS);

        // ----------------- Get Page -----------------
        lock = new CountDownLatch(1);
        syncano.getObjectsPage(UserClass.class, null, new GetCallback<Page<UserClass>>() {
            @Override
            public void success(Page<UserClass> page) {
                assertNotNull(page);
                assertNotNull(page.getObjects());
                assertTrue("List should contain at least one item.", page.getObjects().size() > 0);
                lock.countDown();
            }

            @Override
            public void failure(SyncanoException error) {
                fail("Failed to get list");
            }
        });
        lock.await(TIMEOUT_MILLIS, TimeUnit.MICROSECONDS);

        // ----------------- Delete -----------------
        lock = new CountDownLatch(1);
        syncano.deleteObject(UserClass.class, resultRef.value.getId(), new DeleteCallback() {
            @Override
            public void success() {
                lock.countDown();
            }

            @Override
            public void failure(SyncanoException error) {
                fail("Failed to delete object. " + error.getHttpResultCode());
            }
        });
        lock.await(TIMEOUT_MILLIS, TimeUnit.MICROSECONDS);

        // ----------------- Get One -----------------
        lock = new CountDownLatch(1);
        syncano.getObject(UserClass.class, resultRef.value.getId(), null, new GetCallback<UserClass>() {
            @Override
            public void success(UserClass object) {
                assertNull("Failed to delete.", object);
            }

            @Override
            public void failure(SyncanoException error) {
                lock.countDown();
            }
        });
        lock.await(TIMEOUT_MILLIS, TimeUnit.MICROSECONDS);*/
    }

    @SyncanoClass(name = "User")
    class UserClass extends SyncanoObject
    {
        @SyncanoField(name = "user_name")
        public String userName;

        @SyncanoField(name = "password")
        public String password;
    }
}