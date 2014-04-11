
package pl.mindpower.hydra.test;

import android.test.InstrumentationTestCase;

import pl.mindpower.hydra.Hydra;
import pl.mindpower.hydra.modules.Response;
import pl.mindpower.hydra.modules.data.ParamsDataGet;
import pl.mindpower.hydra.modules.data.ResponseDataGet;

public class HydraTest extends InstrumentationTestCase {

    public final static String TAG = "HydraTest";
    private Hydra hydra;
    private static final String PROJECT_ID = "464";
    private static final String COLLECTION_ID = "304";

    @Override
    protected void setUp() throws Exception {
        hydra = new Hydra(getInstrumentation().getContext(), "sxdev", "sxdev");
    }

    public void testDataGet() {
        ParamsDataGet params = new ParamsDataGet(PROJECT_ID, COLLECTION_ID, null);

        ResponseDataGet response = hydra.dataGet(params);
        assertTrue(response.getResultCode() == Response.CODE_SUCCESS);
    }
}
