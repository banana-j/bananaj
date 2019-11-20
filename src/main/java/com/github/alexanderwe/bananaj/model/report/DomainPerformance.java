package com.github.alexanderwe.bananaj.model.report;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Statistics for the top-performing domains from a campaign.
 *
 */
public class DomainPerformance {
	private List<DomainStats> domains;
	private int totalSent;
	private String campaignId;
	private int totalItems;

	public DomainPerformance(JSONObject jsonObj) {
		totalSent = jsonObj.getInt("total_sent");
		campaignId = jsonObj.getString("campaign_id");
		totalItems = jsonObj.getInt("total_items");
		JSONArray domainsArray = jsonObj.getJSONArray("domains");
    	domains = new ArrayList<DomainStats>(domainsArray.length());
    	for( int i = 0; i< domainsArray.length(); i++)
    	{
    		JSONObject domainDetail = domainsArray.getJSONObject(i);
    		DomainStats report = new DomainStats(domainDetail);
    		domains.add(report);
    	}
	}

	/**
	 * @return The top 5 email domains based on total delivered emails.
	 */
	public List<DomainStats> getDomains() {
		return domains;
	}

	/**
	 * @return The total number of emails sent for the campaign.
	 */
	public int getTotalSent() {
		return totalSent;
	}

	/**
	 * @return The unique id for the campaign.
	 */
	public String getCampaignId() {
		return campaignId;
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
		StringBuilder sb = new StringBuilder(300);
		sb.append("Domain Performance: " + getCampaignId() + System.lineSeparator());
		sb.append("    Total Sent: " + getTotalSent() + System.lineSeparator());
		sb.append("    Item Count: " + getTotalItems());
		for(DomainStats st : getDomains()) {
			sb.append(System.lineSeparator());
			sb.append(st.toString());
		}
		return sb.toString();
	}

}
