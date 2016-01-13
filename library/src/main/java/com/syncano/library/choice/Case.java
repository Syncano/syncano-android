package com.syncano.library.choice;

public enum Case {
    SENSITIVE(true),
    INSENSITIVE(false);

    private final boolean value;

    Case(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
}