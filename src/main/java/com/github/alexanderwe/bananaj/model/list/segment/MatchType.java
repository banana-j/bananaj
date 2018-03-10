package com.github.alexanderwe.bananaj.model.list.segment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alexanderweiss on 27.12.16.
 */
public enum MatchType {

    ANY("any"),
    ALL("all");

    private final String value;
    private final static Map<String, MatchType> CONSTANTS = new HashMap<String, MatchType>();

    static {
        for (MatchType c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private MatchType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
    
    public String value() {
        return value;
    }

    public static MatchType fromValue(String value) {
    	MatchType constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }
}
