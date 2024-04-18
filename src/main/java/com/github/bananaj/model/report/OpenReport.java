package com.github.bananaj.model.report;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.bananaj.connection.MailChimpConnection;
import com.github.bananaj.model.JSONParser;
import com.github.bananaj.model.ModelIterator;
import com.github.bananaj.utils.DateConverter;
import com.github.bananaj.utils.URLHelper;

/**
 * Detailed information about campaign emails that were opened by a list member.
 *
 */
public class OpenReport extends ModelIterator<OpenReportMember> {
	
	private String campaignId;
	private Integer totalOpens;
	private Integer totalItems;

	protected OpenReport(String query, MailChimpConnection connection) {
		super(OpenReportMember.class, query, connection);
	}

	public static OpenReport getOpenReport(MailChimpConnection connection, String campaignId, ZonedDateTime since) throws Exception {
		String baseUrl = URLHelper.join(connection.getReportsendpoint(), "/", campaignId, "/open-details",
				(since!=null ? "?since=" + URLEncoder.encode(DateConverter.toISO8601UTC(since), "UTF-8") : "") );
		return new OpenReport(baseUrl, connection);
	}

	@Override
	protected OpenReportMember buildRefObj(MailChimpConnection con, JSONObject objDetail) {
		return new OpenReportMember(objDetail);
	}

	@Override
	protected void parseEntities(JSONObject list) {
		super.parseEntities(list);
	}

	@Override
	protected void parseRoot(JSONObject list) {
		super.parseRoot(list);
		campaignId = list.getString("campaign_id");
		if (list.has("total_opens")) {
			totalOpens = list.getInt("total_opens");
		}
		if (list.has("total_items")) {
			totalItems = list.getInt("total_items");
		}
	}

	/**
	 * @return An iterator of members who opened a campaign email. Each members
	 *         object will contain information about the number of total opens by a
	 *         single member, as well as timestamps for each open event.
	 */
	public Iterable<OpenReportMember> getMembers() {
		return this;
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
	public Integer getTotalOpens() {
		return totalOpens;
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
		StringBuilder sb = new StringBuilder(100);
		sb.append("Campaign Open Report: " + System.lineSeparator());
		sb.append("    Campaign: " + getCampaignId() + System.lineSeparator());
		sb.append("    Total opens: " + getTotalOpens() + System.lineSeparator());
		sb.append("    Total items: " + getTotalItems() + System.lineSeparator());
		return sb.toString();
	}

}
