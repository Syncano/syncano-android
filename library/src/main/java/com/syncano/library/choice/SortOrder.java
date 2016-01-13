package com.syncano.library.choice;

/**
 * SortOrder specifies how rows of data are sorted
 * <li>{@link #ASCENDING}</li>
 * <li>{@link #DESCENDING}</li>
 */
public enum SortOrder {
    //Rows are sorted in ascending order.
    ASCENDING(),
    // Rows are sorted in descending order.
    DESCENDING();

    SortOrder() {
    }
}