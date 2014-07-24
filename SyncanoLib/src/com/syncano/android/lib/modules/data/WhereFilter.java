package com.syncano.android.lib.modules.data;

public enum WhereFilter {

    DATA1_EQ ("data1__eq"),
    DATA2_EQ ("data2__eq"),
    DATA3_EQ ("data3__eq"),
    DATA1_NEQ ("data1__neq"),
    DATA2_NEQ ("data2__neq"),
    DATA3_NEQ ("data3__neq"),
    DATA1_LTE ("data1__lte"),
    DATA2_LTE ("data2__lte"),
    DATA3_LTE ("data3__lte"),
    DATA1_LT ("data1__lt"),
    DATA2_LT ("data2__lt"),
    DATA3_LT ("data3__lt"),
    DATA1_GTE ("data1__gte"),
    DATA2_GTE ("data2__gte"),
    DATA3_GTE ("data3__gte"),
    DATA1_GT ("data1__gt"),
    DATA2_GT ("data2__gt"),
    DATA3_GT ("data3__gt");

    private String where;

    private WhereFilter(String s) {
        where = s;
    }

    @Override
    public String toString(){
        return where;
    }
}
