package com.syncano.library.android.tests;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.syncano.library.android.Config;
import com.syncano.library.Syncano;
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
public class FieldsFilterTest extends ApplicationTestCase<Application> {

    private static final String TAG = FieldsFilterTest.class.getSimpleName();

    private CodeBox codeBox;
    private Syncano syncano;

    public FieldsFilterTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        syncano = new Syncano(Config.API_KEY, Config.INSTANCE_NAME);

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
        FieldsFilter filter = new FieldsFilter(FieldsFilter.FilterType.INCLUDE_FIELDS, Arrays.asList(CodeBox.FIELD_ID, CodeBox.FIELD_NAME));
        requestGet.setFieldsFilter(filter);
        Response <CodeBox> responseGetCodeBox = requestGet.send();

        assertEquals(Response.HTTP_CODE_SUCCESS, responseGetCodeBox.getHttpResultCode());
        assertNotNull(responseGetCodeBox.getData());
        assertNotNull(responseGetCodeBox.getData().getName());
        assertNull(responseGetCodeBox.getData().getSource());
    }

    public void testGetManyFilter() {
        RequestGetList requestGetList = syncano.getCodeBoxes();
        FieldsFilter filter = new FieldsFilter(FieldsFilter.FilterType.INCLUDE_FIELDS, Arrays.asList(CodeBox.FIELD_ID, CodeBox.FIELD_NAME));
        requestGetList.setFieldsFilter(filter);
        Response <List<CodeBox>> responseGetCodeBoxes = requestGetList.send();

        assertEquals(Response.HTTP_CODE_SUCCESS, responseGetCodeBoxes.getHttpResultCode());
        assertNotNull(responseGetCodeBoxes.getData());
    }
}