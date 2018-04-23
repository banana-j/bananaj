package com.github.alexanderwe.bananaj.model.list.interests;

import java.util.HashMap;
import java.util.Map;

public enum InterestCategoryType {

	CHECKBOXES("checkboxes"),
	DROPDOWN("dropdown"),
	RADIO("radio"),
	HIDDEN("hidden");
	
    private final String value;
    private final static Map<String, InterestCategoryType> CONSTANTS = new HashMap<String, InterestCategoryType>();

    static {
        for (InterestCategoryType c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private InterestCategoryType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
    
    public String value() {
        return value;
    }

    public static InterestCategoryType fromValue(String value) {
    	InterestCategoryType constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }
}
