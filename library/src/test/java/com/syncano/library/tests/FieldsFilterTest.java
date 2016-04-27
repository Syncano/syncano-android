package com.syncano.library.tests;

import com.syncano.library.Syncano;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.FieldsFilter;
import com.syncano.library.api.RequestGet;
import com.syncano.library.api.RequestGetList;
import com.syncano.library.api.Response;
import com.syncano.library.choice.FieldType;
import com.syncano.library.choice.FilterType;
import com.syncano.library.choice.RuntimeName;
import com.syncano.library.data.Script;
import com.syncano.library.data.SyncanoObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FieldsFilterTest extends SyncanoApplicationTestCase {

    private Script script;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        createClass(ExampleSyncanoObject.class);
        createScript();
        createTestsObject();
    }

    @After
    public void tearDown() throws Exception {
        removeClass(ExampleSyncanoObject.class);
        deleteScript();
        super.tearDown();
    }

    private void createTestsObject() {
        ExampleSyncanoObject exampleSyncanoObject = new ExampleSyncanoObject();
        exampleSyncanoObject.importantNumber = 131;
        exampleSyncanoObject.longTextSample = "Example very long text";
        exampleSyncanoObject.title = "Short title";

        Response<ExampleSyncanoObject> responseCreateSyncanoObject = exampleSyncanoObject.save();
        assertTrue(responseCreateSyncanoObject.isSuccess());
    }

    private void createScript() {
        RuntimeName runtime = RuntimeName.NODEJS;
        String source = "var msg = 'this is message from our Script'; console.log(msg);";
        Script newScript = new Script("Test name", source, runtime);
        Response<Script> responseCreate = syncano.createScript(newScript).send();
        assertTrue(responseCreate.isSuccess());
        assertNotNull(responseCreate.getData());
        script = responseCreate.getData();
    }

    @Test
    public void testGetObjectListFilterIncludePlease() {
        Response<List<ExampleSyncanoObject>> syncanoResponse1 = Syncano.please(ExampleSyncanoObject.class)
                .selectFields(FilterType.INCLUDE_FIELDS, ExampleSyncanoObject.COLUMN_IMPORTANT_NUMBER).get();
        assertTrue(syncanoResponse1.isSuccess());
        ExampleSyncanoObject exampleSyncanoObject = syncanoResponse1.getData().get(0);
        assertNotNull(exampleSyncanoObject.importantNumber);
        assertNull(exampleSyncanoObject.getId());
        assertNull(exampleSyncanoObject.title);
        assertNull(exampleSyncanoObject.longTextSample);
    }

    @Test
    public void testGetObjectListFilterExcludePlease() {
        Response<List<ExampleSyncanoObject>> syncanoResponse = Syncano.please(ExampleSyncanoObject.class)
                .selectFields(FilterType.EXCLUDE_FIELDS, ExampleSyncanoObject.COLUMN_LONG_TEXT).get();
        assertTrue(syncanoResponse.isSuccess());
        ExampleSyncanoObject exampleSyncanoObject = syncanoResponse.getData().get(0);
        assertNull(exampleSyncanoObject.longTextSample);
        assertNotNull(exampleSyncanoObject.getId());
        assertNotNull(exampleSyncanoObject.title);
        assertNotNull(exampleSyncanoObject.importantNumber);
    }

    @Test
    public void testGetObjectListFilterExcludeSyncano() {
        Response<List<ExampleSyncanoObject>> syncanoResponse = syncano.getObjects(ExampleSyncanoObject.class)
                .selectFields(FilterType.INCLUDE_FIELDS, ExampleSyncanoObject.FIELD_ID).send();
        assertTrue(syncanoResponse.isSuccess());
        ExampleSyncanoObject exampleSyncanoObject = syncanoResponse.getData().get(0);
        assertNotNull(exampleSyncanoObject.getId());
        Response<ExampleSyncanoObject> syncanoResponse2 = syncano.getObject(exampleSyncanoObject)
                .selectFields(FilterType.EXCLUDE_FIELDS, ExampleSyncanoObject.COLUMN_LONG_TEXT).send();
        exampleSyncanoObject = syncanoResponse2.getData();
        assertNotNull(exampleSyncanoObject.getId());
        assertNotNull(exampleSyncanoObject.importantNumber);
        assertNotNull(exampleSyncanoObject.title);
        assertNull(exampleSyncanoObject.longTextSample);
    }


    private void deleteScript() {
        Response<Script> responseCodeBoxDelete = syncano.deleteScript(script.getId()).send();
        assertTrue(responseCodeBoxDelete.isSuccess());
    }

    @Test
    public void testGetOneFilter() {
        RequestGet<Script> requestGet = syncano.getScript(script.getId());
        FieldsFilter filter = new FieldsFilter(FilterType.INCLUDE_FIELDS, Arrays.asList(Script.FIELD_ID, Script.FIELD_LABEL));
        requestGet.setFieldsFilter(filter);
        Response<Script> responseGetCodeBox = requestGet.send();

        assertTrue(responseGetCodeBox.isSuccess());
        assertNotNull(responseGetCodeBox.getData());
        assertNotNull(responseGetCodeBox.getData().getLabel());
        assertNull(responseGetCodeBox.getData().getSource());
    }

    @Test
    public void testGetManyFilter() {
        RequestGetList<Script> requestGetList = syncano.getScripts();
        FieldsFilter filter = new FieldsFilter(FilterType.INCLUDE_FIELDS, Arrays.asList(Script.FIELD_ID, Script.FIELD_LABEL));
        requestGetList.setFieldsFilter(filter);
        Response<List<Script>> responseGetCodeBoxes = requestGetList.send();

        assertTrue(responseGetCodeBoxes.isSuccess());
        assertNotNull(responseGetCodeBoxes.getData());
    }

    @SyncanoClass(name = ExampleSyncanoObject.TABLE_NAME)
    private static class ExampleSyncanoObject extends SyncanoObject {
        public final static String TABLE_NAME = "example_syncano_object";
        public final static String COLUMN_LONG_TEXT = "long_text";
        public final static String COLUMN_TITLE = "title";
        public final static String COLUMN_IMPORTANT_NUMBER = "important";

        @SyncanoField(name = COLUMN_LONG_TEXT, type = FieldType.TEXT)
        public String longTextSample;
        @SyncanoField(name = COLUMN_TITLE, type = FieldType.STRING)
        public String title;
        @SyncanoField(name = COLUMN_IMPORTANT_NUMBER, type = FieldType.STRING)
        public Integer importantNumber;
    }
}