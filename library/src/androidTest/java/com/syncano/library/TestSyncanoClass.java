package com.syncano.library;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.data.SyncanoObject;

@SyncanoClass(name = "testsyncanoclass")
public class TestSyncanoClass extends SyncanoObject {

    @SyncanoField(name = "value_one")
    public String valueOne;

    @SyncanoField(name = "value_two")
    public String valueTwo;

    public TestSyncanoClass() {
    }

    public TestSyncanoClass(String valueOne, String valueTwo) {
        this.valueOne = valueOne;
        this.valueTwo = valueTwo;
    }

    public static JsonArray getSchema() {
        /*
        [
            {"type": "string","name": "value_one"},
            {"type": "string","name": "value_two"}
        ]
         */

        JsonObject fieldOne = new JsonObject();
        fieldOne.addProperty("type", "string");
        fieldOne.addProperty("name", "value_one");

        JsonObject fieldTwo = new JsonObject();
        fieldTwo.addProperty("type", "string");
        fieldTwo.addProperty("name", "value_two");

        JsonArray array = new JsonArray();
        array.add(fieldOne);
        array.add(fieldTwo);

        return array;
    }
}
