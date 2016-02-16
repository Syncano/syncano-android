package com.syncano.library.tests;

import com.syncano.library.Syncano;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.Response;
import com.syncano.library.api.ResponseGetList;
import com.syncano.library.api.Where;
import com.syncano.library.choice.Case;
import com.syncano.library.choice.FieldType;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.data.SyncanoTableView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ViewTable extends SyncanoApplicationTestCase {
    private static final String tableName = "great_warriors_view";
    private static final String tableDescription = "Sample virtual table for get warriors";

    @Before
    public void setUp() throws Exception {
        super.setUp();
        createClass(Inventory.class);
        createClass(Warrior.class);
        createSampleWarriors();
        //Remove view if exist and create new instance
        Response<SyncanoTableView> deleteResponse = syncano.deleteTableView(tableName).send();
        System.out.println("Delete response status: " + deleteResponse.isSuccess());
        SyncanoTableView syncanoTableView = new SyncanoTableView(tableName, Warrior.class);
        syncanoTableView.setDescription(tableDescription);
        syncanoTableView.setOrderBy(Warrior.FIELD_NUMBER);
        Where query = new Where().startsWith(Warrior.FIELD_NAME, "W", Case.INSENSITIVE);
        syncanoTableView.setQuery(query);
        syncanoTableView.addExpandField(Warrior.FIELD_INVENTORY);
        Response<SyncanoTableView> responseCreateTable =
                syncano.createTableView(syncanoTableView).send();
        assertTrue(responseCreateTable.isSuccess());
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
        Response deleteResponse = syncano.deleteTableView(tableName).send();
        assertTrue(deleteResponse.isSuccess());
        removeClass(Warrior.class);
        removeClass(Inventory.class);
    }

    @Test
    public void testSyncanoGetView() {
        // get from window
        ResponseGetList<Warrior> warriorsFromView = syncano.getViewObjects(Warrior.class, tableName).send();
        assertTrue(warriorsFromView.isSuccess());

        // get standard way
        ResponseGetList<Warrior> warriors = syncano.getObjects(Warrior.class).send();
        assertTrue(warriors.isSuccess());

        assertFalse(warriorsFromView.getData().isEmpty());
        Warrior warrior = warriorsFromView.getData().get(0);

        assertNotNull(warrior.inventory);
        assertNotNull(warrior.inventory.getId());

        warrior.fetch();
        assertNotNull(warrior.inventory.getId());
        assertNull(warrior.inventory.armor);
        warrior.inventory.fetch();
        assertNotNull(warrior.inventory.armor);
    }

    @Test
    public void testPleaseGetView() {
        ResponseGetList<Warrior> warriorListResponse =
                Syncano.please(Warrior.class).tableView(tableName).orderBy(Warrior.FIELD_NUMBER).getAll();
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
