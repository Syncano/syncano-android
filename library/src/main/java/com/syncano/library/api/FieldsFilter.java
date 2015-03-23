package com.syncano.library.api;


import com.syncano.library.Constants;

import java.util.List;

public class FieldsFilter {

    public enum FilterType {

        INCLUDE_FIELDS,
        EXCLUDE_FIELDS
    }

    private FilterType filterType;
    private List<String> fieldNames;


    public FieldsFilter(FilterType filterType, List<String> fieldNames) {
        this.filterType = filterType;
        this.fieldNames = fieldNames;
    }

    public FilterType getFilterType() {
        return filterType;
    }

    public String getFilterTypeString() {
        if (FilterType.EXCLUDE_FIELDS.equals(filterType)) {
            return Constants.URL_PARAM_EXCLUDED_FIELDS;
        } else {
            return Constants.URL_PARAM_FIELDS;
        }
    }

    public List<String> getFieldNames() {
        return fieldNames;
    }
}
