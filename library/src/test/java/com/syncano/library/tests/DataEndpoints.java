package com.syncano.library.tests;

import com.syncano.library.Syncano;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.Response;
import com.syncano.library.api.ResponseGetList;
import com.syncano.library.api.Where;
import com.syncano.library.choice.Case;
import com.syncano.library.data.DataEndpoint;
import com.syncano.library.data.SyncanoObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DataEndpoints extends SyncanoApplicationTestCase {
    private static final String endpointName = "great_warriors_endpoint";
    private static final String endpointDescription = "Sample data ebdpoint for get warriors";

    @Before
    public void setUp() throws Exception {
        super.setUp();
        createClass(Inventory.class);
        createClass(Warrior.class);
        createSampleWarriors();
        //Remove endpoint if exist and create new instance
        Response<DataEndpoint> deleteResponse = syncano.deleteDataEndpoint(endpointName).send();
        System.out.println("Delete response status: " + deleteResponse.isSuccess());
        DataEndpoint dataEndpoint = new DataEndpoint(endpointName, Warrior.class);
        dataEndpoint.setDescription(endpointDescription);
        dataEndpoint.setOrderBy(Warrior.FIELD_NUMBER);
        Where query = new Where().startsWith(Warrior.FIELD_NAME, "W", Case.INSENSITIVE);
        dataEndpoint.setQuery(query);
        dataEndpoint.addExpandField(Warrior.FIELD_INVENTORY);
        Response<DataEndpoint> responseCreate = syncano.createDataEndpoint(dataEndpoint).send();
        assertTrue(responseCreate.isSuccess());
    }

    private void createSampleWarriors() {
        Warrior warriorSoccer = new Warrior();
        warriorSoccer.name = "Warrior man";
        warriorSoccer.number = 10;
        Inventory inventory = new Inventory();
        inventory.armor = "Leather armor";
        Response responseCreateInventory = inventory.save();
        assertTrue(responseCreateInventory.isSuccess());
        warriorSoccer.inventory = inventory;
        Response<Inventory> responseCreateWarrior = warriorSoccer.save();
        assertTrue(responseCreateWarrior.isSuccess());
        warriorSoccer.fetch();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        Response deleteResponse = syncano.deleteDataEndpoint(endpointName).send();
        assertTrue(deleteResponse.isSuccess());
        removeClass(Warrior.class);
        removeClass(Inventory.class);
    }

    @Test
    public void testSyncanoGetFromDataEndpoint() {
        // get from window
        ResponseGetList<Warrior> warriorsFromEndpoint = syncano.getObjectsDataEndpoint(Warrior.class, endpointName).send();
        assertTrue(warriorsFromEndpoint.isSuccess());

        // get standard way
        ResponseGetList<Warrior> warriors = syncano.getObjects(Warrior.class).send();
        assertTrue(warriors.isSuccess());

        assertFalse(warriorsFromEndpoint.getData().isEmpty());
        Warrior warrior = warriorsFromEndpoint.getData().get(0);

        assertNotNull(warrior.inventory);
        assertNotNull(warrior.inventory.getId());
        warrior.fetch();
        assertNotNull(warrior.inventory.getId());
        warrior.inventory.fetch();
        assertNotNull(warrior.inventory.armor);
    }

    @Test
    public void testPleaseGetFromDataEndpoint() {
        ResponseGetList<Warrior> warriorListResponse =
                Syncano.please(Warrior.class).dataEndpoint(endpointName).orderBy(Warrior.FIELD_NUMBER).getAll();
        assertTrue(warriorListResponse.isSuccess());
        assertFalse(warriorListResponse.getData().isEmpty());
    }

    @SyncanoClass(name = Warrior.WARRIORS_CLASS_NAME)
    private static class Warrior
            extends SyncanoObject {
        public static final String WARRIORS_CLASS_NAME = "warriors";
        public static final String FIELD_NAME = "name";
        public static final String FIELD_NUMBER = "number";
        public static final String FIELD_INVENTORY = Inventory.INVENTORY_CLASS_NAME;

        @SyncanoField(name = FIELD_NAME, filterIndex = true)
        public String name;
        @SyncanoField(name = FIELD_NUMBER, orderIndex = true)
        public int number;
        @SyncanoField(name = FIELD_INVENTORY)
        public Inventory inventory;
    }

    @SyncanoClass(name = Inventory.INVENTORY_CLASS_NAME)
    private static class Inventory extends SyncanoObject {
        public static final String INVENTORY_CLASS_NAME = "inventory";
        public static final String FIELD_ARMOR = "armor";
        public static final String FIELD_WEAPON = "weapon";
        @SyncanoField(name = FIELD_ARMOR)
        public String armor;
        @SyncanoField(name = FIELD_WEAPON)
        public String weapon;
    }
}
