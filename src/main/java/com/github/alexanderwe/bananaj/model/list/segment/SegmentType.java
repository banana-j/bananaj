package com.github.alexanderwe.bananaj.model.list.segment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alexanderweiss on 04.02.16.
 */
public enum SegmentType {

    SAVED("saved"),
    STATIC("static"),
    FUZZY("fuzzy");

    private final String value;
    private final static Map<String, SegmentType> CONSTANTS = new HashMap<String, SegmentType>();

    static {
        for (SegmentType c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private SegmentType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
    
    public String value() {
        return value;
    }

    public static SegmentType fromValue(String value) {
    	SegmentType constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }
}
