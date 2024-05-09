package com.github.bananaj.model.report;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.bananaj.utils.JSONObjectCheck;

/**
 * Statistics for the top-performing domains from a campaign.
 *
 */
public class DomainPerformance {
	private List<DomainStats> domains;
	private Integer totalSent;
	private String campaignId;
	private Integer totalItems;

	public DomainPerformance(JSONObject domainperformance) {
		JSONObjectCheck jObj = new JSONObjectCheck(domainperformance);
		totalSent = jObj.getInt("total_sent");
		campaignId = jObj.getString("campaign_id");
		totalItems = jObj.getInt("total_items");
		JSONArray domainsArray = jObj.getJSONArray("domains");
    	domains = new ArrayList<DomainStats>(domainsArray != null ? domainsArray.length() : 0);
    	if (domainsArray != null) {
    		for( int i = 0; i< domainsArray.length(); i++)
    		{
    			JSONObject domainDetail = domainsArray.getJSONObject(i);
    			DomainStats report = new DomainStats(domainDetail);
    			domains.add(report);
    		}
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
	public Integer getTotalSent() {
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
	public Integer getTotalItems() {
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
