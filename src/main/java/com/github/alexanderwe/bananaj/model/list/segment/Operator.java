package com.github.alexanderwe.bananaj.model.list.segment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alexanderweiss on 27.12.16.
 */
public enum Operator {

	// Note: Mandrill ConditionType has unknown possible values for 'op'

	OPEN("open"), 
	CLICK("click"), 
	SENT("sent"), 
	NOOPEN("noopen"), 
	NOCLICK("noclick"), 
	NOSENT("nosent"),

	STARTED("started"), 
	COMPLETED("completed"), 
	NOT_STARTED("not_started"), 
	NOT_COMPLETED("not_completed"),

	MEMBER("member"), 
	NOTMEMBER("notmember"),

	GREATER("greater"), 
	LESS("less"), 

	IS("is"), 
	NOT("not"), 

	BLANK("blank"), 
	BLANK_NOT("blank_not"),

	CLIENT_IS("client_is"), 
	CLIENT_NOT("client_not"),

	SOURCE_IS("source_is"), 
	SOURCE_NOT("source_not"),

	INTERESTCONTAINS("interestcontains"), 
	INTERESTCONTAINSALL("interestcontainsall"), 
	INTERESTNOTCONTAINS("interestnotcontains"),

	CONTAINS("contains"), 
	NOTCONTAIN("notcontain"), 

	STARTS("starts"), 
	ENDS("ends"),

	GOAL_NOT("goal_not"), 
	GOAL_NOTCONTAIN("goal_notcontain"),

	FUZZY_IS("fuzzy_is"), 
	FUZZY_NOT("fuzzy_not"),

	STATIC_IS("static_is"), 
	STATIC_NOT("static_not"),

	IPGEOCOUNTRY("ipgeocountry"), 
	IPGEONOTCOUNTRY("ipgeonotcountry"), 
	IPGEOSTATE("ipgeostate"), 
	IPGEONOTSTATE("ipgeonotstate"),

	IPGEOIN("ipgeoin"), 
	IPGEONOTIN("ipgeonotin"),

	IPGEOINZIP("ipgeoinzip"),

	IPGEOUNKNOWN("ipgeounknown"),

	IPGEOISZIP("ipgeoiszip"), 
	IPGEONOTZIP("ipgeonotzip"),

	FOLLOW("follow"), 
	NOTFOLLOW("notfollow"),

	GEOIN("geoin"),

	DATE_WITHIN("date_within"),
	WITHIN("within"),
	NOTWITHIN("notwithin");

    private final String value;
    private final static Map<String, Operator> CONSTANTS = new HashMap<String, Operator>();

    static {
        for (Operator c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private Operator(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
    
    public String value() {
        return value;
    }

    public static Operator fromValue(String value) {
    	Operator constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }
}
