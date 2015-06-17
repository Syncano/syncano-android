package com.syncano.library.tests;

import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.api.FieldsFilter;
import com.syncano.library.api.RequestGet;
import com.syncano.library.api.RequestGetList;
import com.syncano.library.api.Response;
import com.syncano.library.choice.RuntimeName;
import com.syncano.library.data.CodeBox;

import java.util.Arrays;
import java.util.List;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class FieldsFilterTest extends SyncanoApplicationTestCase {

    private static final String TAG = FieldsFilterTest.class.getSimpleName();

    private CodeBox codeBox;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        String codeBoxName = "CodeBox Test";
        RuntimeName runtime = RuntimeName.NODEJS;
        String source = "var msg = 'this is message from our Codebox'; console.log(msg);";

        final CodeBox newCodeBox = new CodeBox(codeBoxName, source, runtime);

        // ----------------- Create CodeBox -----------------
        Response<CodeBox> responseCodeBoxCreate = syncano.createCodeBox(newCodeBox).send();

        assertEquals(Response.HTTP_CODE_CREATED, responseCodeBoxCreate.getHttpResultCode());
        assertNotNull(responseCodeBoxCreate.getData());
        codeBox = responseCodeBoxCreate.getData();
    }

    @Override
    protected void tearDown() throws Exception {
        // ----------------- Delete CodeBox -----------------
        Response<CodeBox> responseCodeBoxDelete = syncano.deleteCodeBox(codeBox.getId()).send();
        assertTrue(responseCodeBoxDelete.isOk());
    }

    public void testGetOneFilter() {
        RequestGet requestGet = syncano.getCodeBox(codeBox.getId());
        FieldsFilter filter = new FieldsFilter(FieldsFilter.FilterType.INCLUDE_FIELDS, Arrays.asList(CodeBox.FIELD_ID, CodeBox.FIELD_LABEL));
        requestGet.setFieldsFilter(filter);
        Response <CodeBox> responseGetCodeBox = requestGet.send();

        assertEquals(Response.HTTP_CODE_SUCCESS, responseGetCodeBox.getHttpResultCode());
        assertNotNull(responseGetCodeBox.getData());
        assertNotNull(responseGetCodeBox.getData().getLabel());
        assertNull(responseGetCodeBox.getData().getSource());
    }

    public void testGetManyFilter() {
        RequestGetList requestGetList = syncano.getCodeBoxes();
        FieldsFilter filter = new FieldsFilter(FieldsFilter.FilterType.INCLUDE_FIELDS, Arrays.asList(CodeBox.FIELD_ID, CodeBox.FIELD_LABEL));
        requestGetList.setFieldsFilter(filter);
        Response <List<CodeBox>> responseGetCodeBoxes = requestGetList.send();

        assertEquals(Response.HTTP_CODE_SUCCESS, responseGetCodeBoxes.getHttpResultCode());
        assertNotNull(responseGetCodeBoxes.getData());
    }
}