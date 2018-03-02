package com.github.alexanderwe.bananaj.model.list.segment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alexanderweiss on 04.02.16.
 */
public enum ConditionType {

    AIM("Aim"),
    AUTOMATION("Automation"),
    CAMPAIGN_POLL("CampaignPoll"),
    CONVERSATION("Conversation"),
    DATE("Date"),
    EMAIL_CLIENT("EmailClient"),
    LANGUAGE("Language"),
    MANDRILL("Mandrill"),
    MEMBER_RATING("MemberRating"),
    SIGNUP_SOURCE("SignupSource"),
    SURVEY_MONKEY("SurveyMonkey"),
    VIP("VIP"),
    INTERESTS("Interests"),
    ECOMM_CATEGORY("EcommCategory"),
    ECOMM_NUMBER("EcommNumber"),
    ECOMM_PURCHASED("EcommPurchased"),
    ECOMM_SPENT("EcommSpent"),
    ECOMM_STORE("EcommStore"),
    GOAL_ACTIVITY("GoalActivity"),
    GOAL_TIMESTAMP("GoalTimestamp"),
    FUZZY_SEGMENT("FuzzySegment"),
    STATIC_SEGMENT("StaticSegment"),
    IP_GEO_COUNTRY_STATE("IPGeoCountryState"),
    IP_GEO_IN("IPGeoIn"),
    IP_GEO_IN_ZIP("IPGeoInZip"),
    IP_GEO_UNKNOWN("IPGeoUnknown"),
    IP_GEO_ZIP("IPGeoZip"),
    SOCIAL_AGE("SocialAge"),
    SOCIAL_GENDER("SocialGender"),
    SOCIAL_INFLUENCE("SocialInfluence"),
    SOCIAL_NETWORK_MEMBER("SocialNetworkMember"),
    SOCIAL_NETWORK_FOLLOW("SocialNetworkFollow"),
    ADDRESS_MERGE("AddressMerge"),
    ZIP_MERGE("ZipMerge"),
    BIRTHDAY_MERGE("BirthdayMerge"),
    DATE_MERGE("DateMerge"),
    TEXT_MERGE("TextMerge"),
    SELECT_MERGE("SelectMerge"),
    EMAIL_ADDRESS("EmailAddress");

    private final String value;
    private final static Map<String, ConditionType> CONSTANTS = new HashMap<String, ConditionType>();

    static {
        for (ConditionType c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private ConditionType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
    
    public String value() {
        return value;
    }

    public static ConditionType fromValue(String value) {
        ConditionType constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }
}
