package com.syncano.android.lib.utils;

import java.io.Serializable;
import java.util.Date;

public class NanosDate extends Date implements Serializable {
    private int nanos = 0;

    public NanosDate(long millis) {
        super(millis);
    }

    public int getNanos() {
        return nanos;
    }

    public void setNanos(int nanos) {
        this.nanos = nanos;
    }

    @Override
    public boolean before(Date date) {
        if (super.equals(date)) {
            if (date instanceof NanosDate) {
                return nanos < ((NanosDate) date).nanos;
            }
        }
        return super.before(date);
    }

    @Override
    public boolean after(Date date) {
        if (super.equals(date)) {
            if (date instanceof NanosDate) {
                return nanos > ((NanosDate) date).nanos;
            }
        }
        return super.after(date);
    }
}
