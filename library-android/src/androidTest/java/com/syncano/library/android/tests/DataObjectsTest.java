package com.syncano.library.android.tests;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.syncano.library.android.Config;
import com.syncano.library.android.SyncanoAndroid;
import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.RequestGetList;
import com.syncano.library.api.Response;
import com.syncano.library.api.Where;
import com.syncano.library.data.SyncanoObject;

import java.util.List;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class DataObjectsTest extends ApplicationTestCase<Application> {

    private static final String TAG = DataObjectsTest.class.getSimpleName();

    private SyncanoAndroid syncano;

    public DataObjectsTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        syncano = new SyncanoAndroid(Config.API_KEY, Config.INSTANCE_NAME);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testDataObjects() throws InterruptedException {

        String userName = "Andrzej Kartofel";
        String password = "Potato";
        String newUserName = "Andrzej Ziemniak";

        final UserClass newUserObject = new UserClass();
        newUserObject.userName = userName;
        newUserObject.password = password;

        final UserClass userObject;

        // ----------------- Create -----------------
        Response <UserClass> responseCreateObject = syncano.createObject(newUserObject).send();

        assertEquals(Response.HTTP_CODE_CREATED, responseCreateObject.getHttpResultCode());
        assertNotNull(responseCreateObject.getData());
        userObject = responseCreateObject.getData();

        // ----------------- Get One -----------------
        Response <UserClass> responseGetUser = syncano.getObject(UserClass.class, userObject.getId()).send();

        assertEquals(Response.HTTP_CODE_SUCCESS, responseGetUser.getHttpResultCode());
        assertNotNull(responseGetUser.getData());
        assertEquals(userObject.userName, responseGetUser.getData().userName);
        assertEquals(userObject.userName, responseGetUser.getData().userName);

        // ----------------- Update -----------------
        userObject.userName = newUserName;
        Response <UserClass> responseUpdateUser = syncano.updateObject(userObject).send();

        assertEquals(Response.HTTP_CODE_SUCCESS, responseUpdateUser.getHttpResultCode());
        assertNotNull(responseUpdateUser.getData());
        assertEquals(userObject.userName, responseUpdateUser.getData().userName);
        assertEquals(userObject.userName, responseUpdateUser.getData().userName);

        // ----------------- Get List -----------------
        Response <List<UserClass>> responseGetCodeBoxes = syncano.getObjects(UserClass.class).send();

        assertNotNull(responseGetCodeBoxes.getData());
        assertTrue("List should contain at least one item.", responseGetCodeBoxes.getData().size() > 0);

        // ----------------- Delete -----------------
        Response <UserClass> responseDeleteObject = syncano.deleteObject(UserClass.class, userObject.getId()).send();

        assertEquals(Response.HTTP_CODE_NO_CONTENT, responseDeleteObject.getHttpResultCode());
    }

    public void testWhereFilter() {
        UserClass userOne = (UserClass) syncano.createObject(new UserClass("User", "One")).send().getData();
        UserClass userTwo = (UserClass) syncano.createObject(new UserClass("User", "Two")).send().getData();

        Where where = new Where();
        where.eq(UserClass.FIELD_ID, userOne.getId());

        RequestGetList<UserClass> requestGetList = syncano.getObjects(UserClass.class);
        requestGetList.setWhereFilter(where);
        Response<List<UserClass>> response= requestGetList.send();

        assertEquals(response.getHttpReasonPhrase(), Response.HTTP_CODE_SUCCESS, response.getHttpResultCode());
        assertNotNull(response.getData());
        assertEquals(1, response.getData().size()); // Should be only one item with given id.


        syncano.deleteObject(UserClass.class, userOne.getId());
        syncano.deleteObject(UserClass.class, userTwo.getId());
    }

    public void testOrderBy() {
        UserClass userOne = (UserClass) syncano.createObject(new UserClass("User", "One")).send().getData();
        UserClass userTwo = (UserClass) syncano.createObject(new UserClass("User", "Two")).send().getData();

        RequestGetList<UserClass> requestGetList = syncano.getObjects(UserClass.class);
        requestGetList.setOrderBy(UserClass.FIELD_ID, true);
        Response<List<UserClass>> response= requestGetList.send();

        assertEquals(response.getHttpReasonPhrase(), Response.HTTP_CODE_SUCCESS, response.getHttpResultCode());
        assertNotNull(response.getData());
        assertTrue(response.getData().size() > 1);
        // If order is descending, first id on list should be bigger.
        assertTrue(response.getData().get(0).getId() > response.getData().get(1).getId());

        syncano.deleteObject(UserClass.class, userOne.getId());
        syncano.deleteObject(UserClass.class, userTwo.getId());
    }

    public void testPageSize() {
        UserClass userOne = (UserClass) syncano.createObject(new UserClass("User", "One")).send().getData();
        UserClass userTwo = (UserClass) syncano.createObject(new UserClass("User", "Two")).send().getData();

        int limitItems = 1;

        RequestGetList<UserClass> requestGetList = syncano.getObjects(UserClass.class);
        requestGetList.setLimit(limitItems);
        Response<List<UserClass>> response= requestGetList.send();

        assertEquals(response.getHttpReasonPhrase(), Response.HTTP_CODE_SUCCESS, response.getHttpResultCode());
        assertNotNull(response.getData());
        assertEquals(limitItems, response.getData().size());

        syncano.deleteObject(UserClass.class, userOne.getId());
        syncano.deleteObject(UserClass.class, userTwo.getId());
    }

    @SyncanoClass(name = "User")
    class UserClass extends SyncanoObject
    {
        @SyncanoField(name = "user_name")
        public String userName;

        @SyncanoField(name = "password")
        public String password;

        UserClass() {
        }

        UserClass(String userName, String password) {
            this.userName = userName;
            this.password = password;
        }
    }
}