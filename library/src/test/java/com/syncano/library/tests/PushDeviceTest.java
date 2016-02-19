package com.syncano.library.tests;

import com.google.gson.JsonObject;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.api.Response;
import com.syncano.library.api.ResponseGetList;
import com.syncano.library.data.PushDevice;
import com.syncano.library.data.PushMessage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PushDeviceTest extends SyncanoApplicationTestCase {
    private PushDevice pushDevice;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        registerGCMPushDevice();
    }

    public void registerGCMPushDevice() {
        pushDevice = new PushDevice();
        pushDevice.deviceId = "321321";
        pushDevice.label = "label";
        pushDevice.registrationId = "12345";
        syncano.pushGCMRegister(pushDevice).send();
    }

    @Test
    public void testGetListGCMDevices() {
        ResponseGetList<PushDevice> response = syncano.pushGetGCMDevices().send();
        assertTrue(response.isSuccess());
        assertFalse(response.getData().isEmpty());
    }

    @Test
    public void testGetGCMDevice() {
        Response<PushDevice> response = syncano.pushGetGCMDevice(pushDevice.registrationId).send();
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
    }

    public void removeGCMPushDevice() {
        Response<PushDevice> response = syncano.pushRemoveGCMDevice(pushDevice).send();
        assertTrue(response.isSuccess());
    }

    @Test
    public void testUpdateGCMDevice() {
        Response<PushDevice> response = syncano.pushUpdateGCMDevice(pushDevice).send();
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
    }

    @Test
    public void testGetGCMMessages() {
        ResponseGetList<PushMessage> response = syncano.pushGetGCMMessages().send();
        assertTrue(response.isSuccess());
        assertFalse(response.getData().isEmpty());
    }

    @Test
    public void testAddGCMMessage() {
        PushMessage pushMessage = new PushMessage();
        pushMessage.content = new JsonObject();
        pushMessage.content.addProperty("message", "Sample message");
        Response<PushMessage> response = syncano.pushAddGCMMessage(pushMessage).send();
        //TODO Backed return Invalid JSON schema
        //  assertTrue(response.isSuccess());
    }


    @Override
    @After
    public void tearDown() throws Exception {
        super.tearDown();
        removeGCMPushDevice();
    }

}
