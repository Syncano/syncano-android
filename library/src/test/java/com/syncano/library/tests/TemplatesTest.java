package com.syncano.library.tests;

import com.google.gson.JsonObject;
import com.syncano.library.Syncano;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.api.RequestGetList;
import com.syncano.library.api.Response;
import com.syncano.library.api.ResponseGetList;
import com.syncano.library.data.Template;
import com.syncano.library.model.Author;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TemplatesTest extends SyncanoApplicationTestCase {

    private final static String NAME1 = "temp1";
    private final static String NAME2 = "temp2";
    private final static String KEY = "ordinary_key";
    private final static String VALUE1 = "nothing really special";
    private final static String VALUE2 = "something special";

    @Before
    public void setUp() throws Exception {
        super.setUp();
        // delete old templates

        ResponseGetList<Template> resp = syncano.getTemplates().send();
        assertTrue(resp.isSuccess());
        List<Template> temps = resp.getData();
        for (Template t : temps) {
            assertTrue(syncano.deleteTemplate(t.getName()).send().isSuccess());
        }
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testGetObjectsWithTemplate() throws InterruptedException {
        // make objects
        createClass(Author.class);
        String name1 = "John Smith";
        String name2 = "Adam Adam";
        assertTrue(new Author(name1).save().isSuccess());
        assertTrue(new Author(name2).save().isSuccess());

        // make template
        Template t1 = makeTemplate(NAME1);
        t1.setContent("{% set objects = response.objects %}" +
                "{% if objects %}" +
                "{% for object in objects %}" +
                "{{ object['name'] }}," +
                "{% endfor %}" +
                "{% endif %}");
        Response<Template> respCreate = syncano.createTemplate(t1).send();
        assertTrue(respCreate.isSuccess());

        String expectedResult = name1 + "," + name2 + ",";
        // request objects with syncano plain call
        RequestGetList<Author> getReq = Syncano.please(Author.class).prepareGetRequest();
        Response<String> resp = syncano.getObjectsWithTemplate(getReq, NAME1).send();
        assertTrue(resp.isSuccess());
        assertEquals(expectedResult, resp.getData());

        // request objects with please helper
        resp = Syncano.please(Author.class).getWithTemplate(NAME1);
        assertTrue(resp.isSuccess());
        assertEquals(expectedResult, resp.getData());

        // request objects with where
        resp = Syncano.please(Author.class).where().eq(Author.FIELD_NAME, name1).getWithTemplate(NAME1);
        assertTrue(resp.isSuccess());
        assertEquals(name1 + ",", resp.getData());

        // TODO add test with data endpoint
    }

    @Test
    public void testManagingTemplates() {
        // create new template
        Template t1 = makeTemplate(NAME1);
        Response<Template> respCreate = syncano.createTemplate(makeTemplate(NAME1)).send();
        assertTrue(respCreate.isSuccess());
        assertEquals(t1.getName(), respCreate.getData().getName());
        assertEquals(t1.getContent(), respCreate.getData().getContent());
        assertEquals(t1.getContentType(), respCreate.getData().getContentType());
        assertEquals(t1.getContext(), respCreate.getData().getContext());

        // get it as single
        Response<Template> respOne = syncano.getTemplate(t1.getName()).send();
        assertNotNull(respOne.getData());
        assertTrue(respOne.isSuccess());
        assertEquals(t1.getName(), respOne.getData().getName());
        assertEquals(t1.getContent(), respOne.getData().getContent());
        assertEquals(t1.getContentType(), respOne.getData().getContentType());
        assertEquals(t1.getContext(), respOne.getData().getContext());

        // get it as list
        ResponseGetList<Template> resp = syncano.getTemplates().send();
        assertNotNull(resp.getData());
        assertTrue(resp.isSuccess());
        assertEquals(1, resp.getData().size());
        assertEquals(t1.getName(), resp.getData().get(0).getName());
        assertEquals(t1.getContent(), resp.getData().get(0).getContent());
        assertEquals(t1.getContentType(), resp.getData().get(0).getContentType());
        assertEquals(t1.getContext(), resp.getData().get(0).getContext());

        // render
        Response<String> respRender = syncano.renderTemplate(t1.getName()).send();
        assertTrue(respRender.isSuccess());
        assertEquals(VALUE1, respRender.getData());

        // update template
        Template t2 = makeTemplate(NAME1);
        JsonObject jo = new JsonObject();
        jo.addProperty(KEY, VALUE2);
        t2.setContext(jo);
        Response<Template> respUpdate = syncano.updateTemplate(t2).send();
        assertTrue(respUpdate.isSuccess());
        assertEquals(NAME1, respUpdate.getData().getName());
        assertEquals(t1.getContent(), respUpdate.getData().getContent());
        assertEquals(t1.getContentType(), respUpdate.getData().getContentType());
        assertEquals(jo, respUpdate.getData().getContext());

        // render after update
        respRender = syncano.renderTemplate(t2.getName()).send();
        assertTrue(respRender.isSuccess());
        assertEquals(VALUE2, respRender.getData());

        // rename
        Response<Template> respRename = syncano.renameTemplate(NAME1, NAME2).send();
        assertTrue(respRename.isSuccess());
        assertEquals(NAME2, respRename.getData().getName());
        assertEquals(t1.getContent(), respRename.getData().getContent());
        assertEquals(t1.getContentType(), respRename.getData().getContentType());
        assertEquals(jo, respRename.getData().getContext());

        // delete
        Response<Void> respDelete = syncano.deleteTemplate(NAME2).send();
        assertTrue(respDelete.isSuccess());
        assertNull(respDelete.getData());

        // check if no templates left
        resp = syncano.getTemplates().send();
        assertTrue(resp.isSuccess());
        assertEquals(0, resp.getData().size());
    }

    private Template makeTemplate(String name) {
        Template t = new Template(name);
        t.setContent("{{ " + KEY + " }}");
        JsonObject jo = new JsonObject();
        jo.addProperty(KEY, VALUE1);
        t.setContext(jo);
        t.setContentType("text/plain");
        return t;
    }
}
