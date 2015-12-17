package com.syncano.library.tests;

import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.FieldsFilter;
import com.syncano.library.api.RequestGet;
import com.syncano.library.api.RequestGetList;
import com.syncano.library.api.Response;
import com.syncano.library.choice.FieldType;
import com.syncano.library.choice.RuntimeName;
import com.syncano.library.data.CodeBox;
import com.syncano.library.data.SyncanoObject;

import java.util.Arrays;
import java.util.List;

public class FieldsFilterTest extends SyncanoApplicationTestCase {

    private CodeBox codeBox;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createClass(ExampleSyncanoObject.class);
        createCodeBox();
        createTestsObject();
    }

    private void createTestsObject() {
        ExampleSyncanoObject exampleSyncanoObject = new ExampleSyncanoObject();
        Response<ExampleSyncanoObject> responseCreateSyncanoObject = exampleSyncanoObject.save();
        assertTrue(responseCreateSyncanoObject.isSuccess());
    }

    private void createCodeBox() {
        String codeBoxName = "CodeBox Test";
        RuntimeName runtime = RuntimeName.NODEJS;
        String source = "var msg = 'this is message from our Codebox'; console.log(msg);";
        final CodeBox newCodeBox = new CodeBox(codeBoxName, source, runtime);
        Response<CodeBox> responseCodeBoxCreate = syncano.createCodeBox(newCodeBox).send();
        assertTrue(responseCodeBoxCreate.isSuccess());
        assertNotNull(responseCodeBoxCreate.getData());
        codeBox = responseCodeBoxCreate.getData();
    }

    public void testGetObjectListFilterExclude() {
        FieldsFilter filter = new FieldsFilter(FieldsFilter.FilterType.INCLUDE_FIELDS, ExampleSyncanoObject.COLUMN_IMPORTANT_NUMBER);


    }

    @Override
    protected void tearDown() throws Exception {
        removeClass(ExampleSyncanoObject.class);
        deleteCodeBox();
    }

    private void deleteCodeBox() {
        Response<CodeBox> responseCodeBoxDelete = syncano.deleteCodeBox(codeBox.getId()).send();
        assertTrue(responseCodeBoxDelete.isSuccess());
    }

    public void testGetOneFilter() {
        RequestGet<CodeBox> requestGet = syncano.getCodeBox(codeBox.getId());
        FieldsFilter filter = new FieldsFilter(FieldsFilter.FilterType.INCLUDE_FIELDS, Arrays.asList(CodeBox.FIELD_ID, CodeBox.FIELD_LABEL));
        requestGet.setFieldsFilter(filter);
        Response<CodeBox> responseGetCodeBox = requestGet.send();

        assertTrue(responseGetCodeBox.isSuccess());
        assertNotNull(responseGetCodeBox.getData());
        assertNotNull(responseGetCodeBox.getData().getLabel());
        assertNull(responseGetCodeBox.getData().getSource());
    }

    public void testGetManyFilter() {
        RequestGetList<CodeBox> requestGetList = syncano.getCodeBoxes();
        FieldsFilter filter = new FieldsFilter(FieldsFilter.FilterType.INCLUDE_FIELDS, Arrays.asList(CodeBox.FIELD_ID, CodeBox.FIELD_LABEL));
        requestGetList.setFieldsFilter(filter);
        Response<List<CodeBox>> responseGetCodeBoxes = requestGetList.send();

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