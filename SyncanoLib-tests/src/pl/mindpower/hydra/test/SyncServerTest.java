
package pl.mindpower.hydra.test;

import android.test.InstrumentationTestCase;
import android.util.Log;

import pl.mindpower.hydra.modules.Response;
import pl.mindpower.hydra.modules.data.ParamsDataGet;
import pl.mindpower.hydra.objects.Data;
import pl.mindpower.hydra.syncserver.DataChanges;
import pl.mindpower.hydra.syncserver.SyncServerConnection;
import pl.mindpower.hydra.syncserver.SyncServerConnection.SubscriptionListener;
import pl.mindpower.hydra.syncserver.SyncServerConnection.SyncServerCallback;
import pl.mindpower.hydra.syncserver.SyncServerConnection.SyncServerListener;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SyncServerTest extends InstrumentationTestCase {

    public final static String TAG = "SyncServerTest";
    private SyncServerConnection conn;
    private CountDownLatch lock;
    private final static int WAITING_MILLIS = 120000;
    private static final String PROJECT_ID = "464";
    private static final String COLLECTION_ID = "304";
    private boolean wasResponse;

    @Override
    protected void setUp() throws Exception {
        lock = new CountDownLatch(1);
        conn = new SyncServerConnection(getInstrumentation().getContext(), "sxdev", "sxdev", new SyncServerListener() {
            @Override
            public void message(String object, String message) {
                Log.d(TAG, "message " + object + " " + message);
            }

            @Override
            public void error(String why) {
                Log.d(TAG, "error: " + why);
            }

            @Override
            public void disconnected() {
                Log.d(TAG, "disconnected");
                lock.countDown();
            }

            @Override
            public void connected() {
                Log.d(TAG, "connected");
                lock.countDown();
            }
        }, new SubscriptionListener() {
            @Override
            public void deleted(int[] ids) {
                Log.d(TAG, "deleted " + ids);
            }

            @Override
            public void changed(ArrayList<DataChanges> changes) {
                Log.d(TAG, "changed");
            }

            @Override
            public void added(Data data) {
                Log.d(TAG, "added");
            }
        });

        conn.start();
        lock.await(WAITING_MILLIS, TimeUnit.MILLISECONDS);
        assertTrue(conn.isConnected());
    }

    @Override
    protected void tearDown() throws Exception {
        conn.stop();
    }

    public void testDataGet() throws Exception {
        ParamsDataGet params = new ParamsDataGet(PROJECT_ID, COLLECTION_ID, null);

        lock = new CountDownLatch(1);
        wasResponse = false;
        conn.call(params, new SyncServerCallback() {
            @Override
            public void result(Response response) {
                wasResponse = true;
                assertTrue(response.getResultCode() == Response.CODE_SUCCESS);
                lock.countDown();
            }
        });
        lock.await(WAITING_MILLIS, TimeUnit.MILLISECONDS);
        assertTrue(wasResponse);
    }
}
