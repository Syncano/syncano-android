package com.syncano.library.documentation;

import com.syncano.library.Syncano;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.Response;
import com.syncano.library.api.ResponseGetList;
import com.syncano.library.api.Where;
import com.syncano.library.choice.Case;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.data.SyncanoTableView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ViewTable extends SyncanoApplicationTestCase {
    String tableName = "great_warriors_view";
    String tableDescription = "Sample virtual table for get warriors";

    @Before
    public void setUp() throws Exception {
        super.setUp();
        createClass(Warriors.class);
        createClass(Inventory.class);
        //Remove if exist and create new instance
        Response<SyncanoTableView> deleteResponse = syncano.deleteTableView(tableName).send();
        System.out.println("Delete response status: " + deleteResponse.isSuccess());
        SyncanoTableView syncanoTableView = new SyncanoTableView(tableName, Warriors.class);
        syncanoTableView.setDescription(tableDescription);
        syncanoTableView.setOrderBy(Warriors.FIELD_NUMBER);
        Where query = new Where().startsWith(Warriors.FIELD_NAME, "W", Case.INSENSITIVE);
        syncanoTableView.setQuery(query);
        syncanoTableView.addExpandField(Warriors.FIELD_INVENTORY);
        Response<SyncanoTableView> responseCreateTable = syncano.createTableView(syncanoTableView).send();
        assertTrue(responseCreateTable.isSuccess());
    }

    private void createSampleWarriors() {
        Warriors warriorSoccer = new Warriors();
        warriorSoccer.name = "";
        warriorSoccer.number = 10;
        warriorSoccer.inventory = new Inventory();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        Response deleteResponse = syncano.deleteTableView(tableName).send();
        assertTrue(deleteResponse.isSuccess());
        removeClass(Warriors.class);
        removeClass(Inventory.class);
    }


    @Test
    public void testSyncanoGetView() {
        ResponseGetList<Warriors> warriorListGetList = syncano.getViewObjects(Warriors.class, tableName).send();
        assertTrue(warriorListGetList.isSuccess());
        List<Warriors> data = warriorListGetList.getData();
        boolean dataIsEmpty = warriorListGetList.getData().isEmpty();
        assertFalse(dataIsEmpty);
        Warriors warriors = data.get(0);
        assertNotNull(warriors.inventory);
        assertNotNull(warriors.inventory.getId());
    }

    @Test
    public void testPleaseGetView() {
        ResponseGetList<Warriors> warriorListResponse = Syncano.please(Warriors.class).tableView(tableName).orderBy("text").getAll();
        assertTrue(warriorListResponse.isSuccess());
        assertFalse(warriorListResponse.getData().isEmpty());
    }

    @SyncanoClass(name = Warriors.WARRIORS_CLASS_NAME)
    private static class Warriors extends SyncanoObject {
        public static final String WARRIORS_CLASS_NAME = "inventory";
        public static final String FIELD_NAME = "name";
        public static final String FIELD_NUMBER = "number";
        public static final String FIELD_INVENTORY = Inventory.INVENTORY_CLASS_NAME;

        @SyncanoField(name = FIELD_NAME)
        public String name;
        @SyncanoField(name = FIELD_NUMBER)
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
