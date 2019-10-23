package com.github.alexanderwe.bananaj.model.automation;

public enum DelayAction {

	PREVIOUS_CAMPAIGN_SENT("previous_campaign_sent"),
	PREVIOUS_CAMPAIGN_OPENED("previous_campaign_opened"),
	PREVIOUS_CAMPAIGN_NOT_OPENED("previous_campaign_not_opened"),
	PREVIOUS_CAMPAIGN_CLICKED_ANY("previous_campaign_clicked_any"),
	PREVIOUS_CAMPAIGN_NOT_CLICKED_ANY("previous_campaign_not_clicked_any"),
	PREVIOUS_CAMPAIGN_SPECIFIC_CLICKED("previous_campaign_specific_clicked"),
	ECOMM_ABANDONED_BROWSE("ecomm_abandoned_browse"),
	ECOMM_BOUGHT_ANY("ecomm_bought_any"),
	ECOMM_BOUGHT_PRODUCT("ecomm_bought_product"),
	ECOMM_BOUGHT_CATEGORY("ecomm_bought_category"),
	ECOMM_NOT_BOUGHT_ANY("ecomm_not_bought_any"),
	ECOMM_ABANDONED_CART("ecomm_abandoned_cart"),
	CAMPAIGN_SENT("campaign_sent"),
	OPENED_EMAIL("opened_email"),
	NOT_OPENED_EMAIL("not_opened_email"),
	CLICKED_EMAIL("clicked_email"),
	NOT_CLICKED_EMAIL("not_clicked_email"),
	CAMPAIGN_SPECIFIC_CLICKED("campaign_specific_clicked"),
	MANUAL("manual"),
	SIGNUP("signup"),
	MERGE_CHANGED("merge_changed"),
	GROUP_ADD("group_add"),
	GROUP_REMOVE("group_remove"),
	MANDRILL_SENT("mandrill_sent"),
	MANDRILL_OPENED("mandrill_opened"),
	MANDRILL_CLICKED("mandrill_clicked"),
	MANDRILL_ANY("mandrill_any"),
	API("api"),
	GOAL("goal"),
	ANNUAL("annual"),
	BIRTHDAY("birthday"),
	DATE("date"),
	DATE_ADDED("date_added"),
	TAG_ADD("tag_add");	

	private String stringRepresentation;

	DelayAction(String stringRepresentation ) {
		setStringRepresentation(stringRepresentation);
	}

	@Override
	public String toString() {
		return stringRepresentation;
	}

	/**
	 * @param Set the stringRepresentation for the enum constant.
	 */
	private void setStringRepresentation(String stringRepresentation) {
		this.stringRepresentation = stringRepresentation;
	}

}
