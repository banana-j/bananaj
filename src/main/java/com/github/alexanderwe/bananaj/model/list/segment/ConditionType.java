package com.github.alexanderwe.bananaj.model.list.segment;

/**
 * Created by alexanderweiss on 04.02.16.
 */
public enum ConditionType {

	ADDRESSMERGE("AddressMerge"),
	AIM("Aim"),
	AUTOMATION("Automation"),
	BIRTHDAYMERGE("BirthdayMerge"),
	CAMPAIGNPOLL("CampaignPoll"),
	CONVERSATION("Conversation"),
	DATE("Date"),
	DATEMERGE("DateMerge"),
	ECOMMCATEGORY("EcommCategory"),
	ECOMMNUMBER("EcommNumber"),
	ECOMMPURCHASED("EcommPurchased"),
	ECOMMSPENT("EcommSpent"),
	ECOMMSTORE("EcommStore"),
	EMAILADDRESS("EmailAddress"),
	EMAILCLIENT("EmailClient"),
	FUZZYSEGMENT("FuzzySegment"),
	GOALACTIVITY("GoalActivity"),
	GOALTIMESTAMP("GoalTimestamp"),
	INTERESTS("Interests"),
	IPGEOCOUNTRYSTATE("IPGeoCountryState"),
	IPGEOIN("IPGeoIn"),
	IPGEOINZIP("IPGeoInZip"),
	IPGEOUNKNOWN("IPGeoUnknown"),
	IPGEOZIP("IPGeoZip"),
	LANGUAGE("Language"),
	MANDRILL("Mandrill"),
	MEMBERRATING("MemberRating"),
	NEWSUBSCRIBERS("NewSubscribers"),
	PREDICTEDAGE("PredictedAge"),
	PREDICTEDGENDER("PredictedGender"),
	SELECTMERGE("SelectMerge"),
	SIGNUPSOURCE("SignupSource"),
	SOCIALAGE("SocialAge"),
	SOCIALGENDER("SocialGender"),
	SOCIALINFLUENCE("SocialInfluence"),
	SOCIALNETWORKFOLLOW("SocialNetworkFollow"),
	SOCIALNETWORKMEMBER("SocialNetworkMember"),
	STATICSEGMENT("StaticSegment"),
	SURVEYMONKEY("SurveyMonkey"),
	TEXTMERGE("TextMerge"),
	VIP("VIP"),
	ZIPMERGE("ZipMerge");

	private String stringRepresentation;

	ConditionType(String stringRepresentation ) {
		setStringRepresentation(stringRepresentation);
	}

	@Override
	public String toString() {
		return stringRepresentation;
	}

	/**
	 * @param stringRepresentation Set the stringRepresentation for the enum constant.
	 */
	private void setStringRepresentation(String stringRepresentation) {
		this.stringRepresentation = stringRepresentation;
	}

}
