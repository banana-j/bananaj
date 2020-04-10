package com.github.bananaj.model.report;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Detailed information about campaign emails that were opened by a list member.
 *
 */
public class OpenReport {
	
	private List<OpenReportMember> members;
	private String campaignId;
	private int totalOpens;
	private int totalItems;

	public OpenReport(JSONObject jsonObj) {
		campaignId = jsonObj.getString("campaign_id");
		totalOpens = jsonObj.getInt("total_opens");
		totalItems = jsonObj.getInt("total_items");
		final JSONArray openArray = jsonObj.getJSONArray("members");
		members = new ArrayList<OpenReportMember>(openArray.length());
		for(int i=0; i<openArray.length(); i++) {
			members.add(new OpenReportMember(openArray.getJSONObject(i)));
		}
	}

	/**
	 * @return An array of objects, each representing a list member who opened a
	 *         campaign email. Each members object will contain information about
	 *         the number of total opens by a single member, as well as timestamps
	 *         for each open event.
	 */
	public List<OpenReportMember> getMembers() {
		return members;
	}

	/**
	 * @return The campaign id.
	 */
	public String getCampaignId() {
		return campaignId;
	}

	/**
	 * @return The total number of opens matching the query regardless of pagination.
	 */
	public int getTotalOpens() {
		return totalOpens;
	}

	/**
	 * @return The total number of items matching the query regardless of pagination.
	 */
	public int getTotalItems() {
		return totalItems;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "    Total opens: " + getTotalOpens();
	}

}
