package com.syncano.library.utils;

import java.io.Serializable;
import java.util.Date;

public class NanosDate extends Date implements Serializable {
    private int nanos = 0;

    public NanosDate(long millis, int nanos) {
        super(millis);
        this.nanos = nanos;
    }

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
    public boolean equals(Object object) {
        if (!(object instanceof Date)) {
            return false;
        }
        Date other = (Date) object;
        if (other.getTime() != this.getTime()) {
            return false;
        }
        if (object instanceof NanosDate) {
            NanosDate otherND = (NanosDate) object;
            if (nanos == otherND.nanos) {
                return true;
            } else {
                return false;
            }
        }
        if (this.nanos == 0) {
            return true;
        }
        return false;
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
