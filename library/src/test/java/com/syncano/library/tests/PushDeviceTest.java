package com.syncano.library.tests;

import com.syncano.library.BuildConfig;
import com.syncano.library.Syncano;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.SyncanoBuilder;
import com.syncano.library.api.Response;
import com.syncano.library.api.ResponseGetList;
import com.syncano.library.data.PushDevice;
import com.syncano.library.data.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PushDeviceTest extends SyncanoApplicationTestCase {
    private PushDevice pushDevice;
    private Syncano userSyncano;
    private final static String USER_NAME = "user";
    private final static String USER_PASSWORD = "some_pass";
    private final String REGISTRATION_ID = "123456789";

    @Before
    public void setUp() throws Exception {
        super.setUp();
        deleteTestUser(USER_NAME);
        userSyncano = new SyncanoBuilder().apiKey(BuildConfig.API_KEY_USERS).instanceName(BuildConfig.INSTANCE_NAME)
                .customServerUrl(BuildConfig.STAGING_SERVER_URL).build();
        assertTrue(new User(USER_NAME, USER_PASSWORD).on(userSyncano).register().isSuccess());
        registerGCMPushDevice();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        removeGCMPushDevice();
    }

    public void registerGCMPushDevice() {
        pushDevice = new PushDevice();
        pushDevice.setDeviceId("321321");
        pushDevice.setLabel("label");
        pushDevice.setRegistrationId("12345");
        assertTrue(syncano.deletePushDevice(pushDevice).send().isSuccess());
        assertTrue(userSyncano.registerPushDevice(pushDevice).send().isSuccess());
    }

    @Test
    public void testGetListGCMDevices() {
        ResponseGetList<PushDevice> response = userSyncano.getPushDevices().send();
        assertTrue(response.isSuccess());
        assertFalse(response.getData().isEmpty());
    }

    @Test
    public void testGetGCMDevice() {
        Response<PushDevice> response = userSyncano.getPushDevice(pushDevice.getRegistrationId()).send();
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
    }

    public void removeGCMPushDevice() {
        Response<PushDevice> response = userSyncano.deletePushDevice(pushDevice).send();
        assertTrue(response.isSuccess());
    }

    @Test
    public void testUpdateGCMDevice() {
        Response<PushDevice> response = userSyncano.updatePushDevice(pushDevice).send();
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
    }
}
