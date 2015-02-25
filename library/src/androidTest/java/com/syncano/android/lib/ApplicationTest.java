package com.syncano.android.lib;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.syncano.android.lib.annotation.SyncanoClass;
import com.syncano.android.lib.api.objects.SyncanoObject;
import com.syncano.android.lib.data.DataObject;
import com.syncano.android.lib.data.Page;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    Syncano syncano;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Syncano.init("c39742252034618f71c5d7e9ff556fe21464d0ee");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testAccount() {


        TestClass obj = new TestClass();
        /*SyncanoObjects.create(obj);*/

        /*Response<Page<TestClass>> response = SyncanoObject.get("instance", TestClass.class);


        /*List<TestClass> list = response.getData();
        List<TestClass> list2 =  response.getNext();
        Log.d("dawid", "Item id: " + response.getData().getObjects().get(0).getId());


        //SyncanoObjects.get("", TestClass.class, 5);


        r.success();
        r.getError();
        r.getResultCode();
        r.getHttpResultCode();
        r.getHttpError();




        GetCallback<TestClass> callback = new GetCallback<TestClass>() {
            public void done(List<Testclass> list) {
                // zapis do bazy
                // odswiez liste

            }

            public void error(SyncanoError errorMessage) {

            }
        };



        Syncan.getObjects(params, new GetCallback<TestClass>() {
            public void done(List<Testclass> list) {}
            public void error(SyncanoError errorMessage) {}
        });





        SyncanoObject.getNextPage(int id, params, TestClass.class, Callback)
        SyncanoObject.getPrevPage(int id, params, TestClass.class, Callback)


        SyncanoObject.getNextPage(0, params, TestClass.class, Callback)
    }

    onListEnd() {
        if(page.hasNext()) {
            SyncanoObject.get(page.getNext(), callback);
        }*/
    }

    @SyncanoClass(name = "TestClass")
    class TestClass extends DataObject
    {

    }
}