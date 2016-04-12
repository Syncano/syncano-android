package com.syncano.library.data;

import com.syncano.library.choice.RuntimeName;

/**
 * @deprecated use Script instead
 */
@Deprecated
public class CodeBox extends Script {

    @Deprecated
    public CodeBox() {
        super();
    }

    @Deprecated
    public CodeBox(int id) {
        super(id);
    }

    @Deprecated
    public CodeBox(String label, String source, RuntimeName runtimeName) {
        super(label, source, runtimeName);
    }
}
