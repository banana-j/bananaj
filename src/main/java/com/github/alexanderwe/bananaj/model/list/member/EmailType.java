package com.github.alexanderwe.bananaj.model.list.member;

import java.util.HashMap;
import java.util.Map;

public enum EmailType {

	HTML("html"),
	TEXT("text");

    private final String value;
    private final static Map<String, EmailType> CONSTANTS = new HashMap<String, EmailType>();

    static {
        for (EmailType c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private EmailType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
    
    public String value() {
        return value;
    }

    public static EmailType fromValue(String value) {
    	EmailType constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }
}
