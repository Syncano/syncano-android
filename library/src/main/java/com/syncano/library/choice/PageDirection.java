package com.syncano.library.choice;

public enum PageDirection {

    NEXT("1"),
    PREVIOUS("0");

    private String param;

    PageDirection(String param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return param;
    }
}
