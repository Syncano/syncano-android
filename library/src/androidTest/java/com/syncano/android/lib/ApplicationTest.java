package com.syncano.android.lib;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.syncano.android.lib.annotation.SyncanoClass;
import com.syncano.android.lib.annotation.SyncanoField;
import com.syncano.android.lib.api.Page;
import com.syncano.android.lib.api.SyncanoException;
import com.syncano.android.lib.callbacks.GetCallback;
import com.syncano.android.lib.callbacks.GetOneCallback;
import com.syncano.android.lib.data.SyncanoObject;
import com.syncano.android.lib.utils.GsonHelper;

import java.util.List;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    private static final String TAG = ApplicationTest.class.getSimpleName();

    public ApplicationTest() {
        super(Application.class);
    }

    Syncano syncano;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        syncano = new Syncano("c39742252034618f71c5d7e9ff556fe21464d0ee", "test_seba");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testAccount() {


        UserClass userClass = new UserClass();
        userClass.userName = "Marek";
        userClass.password = "secret password";

        //Log.d(TAG, "json: " + GsonHelper.createGson().toJson(userClass));

        /*syncano.createOrUpdateObject(userClass, new GetOneCallback<UserClass>() {

            @Override
            public void success(UserClass object) {
                Log.d("ApplicationTest", "Success: " + object);
                //Log.d("ApplicationTest", "User: " + object.imie + " " + object.wiek);
            }

            @Override
            public void failure(SyncanoException error) {
                Log.d("ApplicationTest", "Failure: " + error);
                Log.d("ApplicationTest", "HttpError: " + error.getHttpError());
            }
        });*/

        syncano.getObjects(UserClass.class, null, new GetCallback<UserClass>() {
            @Override
            public void success(Page<UserClass> page) {
                Log.d(TAG, "Success: " + page);
                for (UserClass user : page.getObjects())
                {
                    Log.d(TAG, "User: " + new Gson().toJson(user));
                    Log.d(TAG, "    getCreatedAt: " + user.getCreatedAt());
                    Log.d(TAG, "    getUpdatedAt: " + user.getUpdatedAt());
                }
            }

            @Override
            public void failure(SyncanoException error) {
                Log.d(TAG, "SyncanoError: " + error);
                Log.d(TAG, "HttpError: " + error.getHttpError());
            }
        });
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