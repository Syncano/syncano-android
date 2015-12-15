package com.syncano.library.tests;

import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.CounterBuilder;
import com.syncano.library.api.Response;
import com.syncano.library.choice.FieldType;
import com.syncano.library.data.SyncanoObject;

import java.util.List;

public class IncreaseDecreaseTest extends SyncanoApplicationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createClass(KernelOS.class);
        createTestsKernel();
    }

    private void createTestsKernel() {
        KernelOS kernelOS = new KernelOS();
        Response<KernelOS> responseCreateKernelOS = kernelOS.save();
        assertTrue(responseCreateKernelOS.isSuccess());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        removeClass(KernelOS.class);
    }

    public void testIncreaseValue() {
        int expectedKernelVersion = 0;
        int expectedKernelRevision = 0;

        Response<List<KernelOS>> kernelOSResponse = syncano.getObjects(KernelOS.class).send();
        KernelOS kernelOS = kernelOSResponse.getData().get(0);
        Response<KernelOS> response = kernelOS.changeFieldsCounters(new CounterBuilder()
                .incrementField(KernelOS.COLUMN_KERNEL_VERSION, 2));
        expectedKernelVersion += 2;
        assertTrue(response.isSuccess());
        kernelOS = response.getData();
        assertEquals(kernelOS.kernelVersion, expectedKernelVersion);
        assertEquals(kernelOS.kernelRevision, expectedKernelRevision);

        Response<KernelOS> response2 = kernelOS.changeFieldsCounters(
                new CounterBuilder()
                        .incrementField(KernelOS.COLUMN_KERNEL_VERSION, 2)
                        .decrementField(KernelOS.COLUMN_KERNEL_REVISION, 1)
        );
        expectedKernelVersion += 2;
        expectedKernelRevision--;
        assertTrue(response2.isSuccess());
        kernelOS = response2.getData();
        assertEquals(kernelOS.kernelVersion, expectedKernelVersion);
        assertEquals(kernelOS.kernelRevision, expectedKernelRevision);
    }


    @SyncanoClass(name = KernelOS.TABLE_NAME)
    public static class KernelOS extends SyncanoObject {
        public final static String TABLE_NAME = "kernel";
        public final static String COLUMN_KERNEL_NAME = "kernel_name";
        public final static String COLUMN_KERNEL_VERSION = "kernel_version";
        public final static String COLUMN_KERNEL_REVISION = "kernel_revision";
        @SyncanoField(name = COLUMN_KERNEL_NAME, filterIndex = true, type = FieldType.STRING)
        public String kernelName;
        @SyncanoField(name = COLUMN_KERNEL_VERSION)
        public int kernelVersion;
        @SyncanoField(name = COLUMN_KERNEL_REVISION)
        public int kernelRevision;
    }
}
