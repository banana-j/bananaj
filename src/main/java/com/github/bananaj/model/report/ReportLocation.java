/**
 * @author alexanderweiss
 * @date 19.11.2015
 */
package com.github.bananaj.model.report;

import org.json.JSONObject;

import com.github.bananaj.connection.MailChimpConnection;
import com.github.bananaj.model.JSONParser;
import com.github.bananaj.utils.JSONObjectCheck;

/**
 * Open locations for a specific campaign.
 * 
 *
 */
public class ReportLocation implements JSONParser {

    private String countryCode;
    private String region;
    private String regionName;
	private Integer opens;
	
	public ReportLocation() {

	}

	public ReportLocation(JSONObject jsonObj) {
		parse(null, jsonObj);
	}

	@Override
	public void parse(MailChimpConnection connection, JSONObject entity) {
		JSONObjectCheck jObj = new JSONObjectCheck(entity);
		countryCode = jObj.getString("country_code");
		region = jObj.getString("region");
		regionName = jObj.getString("region_name");
		opens = jObj.getInt("opens");
	}

	/**
	 * The ISO 3166 2 digit country code.
	 */
	public String getCountryCode() {
		return countryCode;
	}

	/**
	 * An internal code for the region representing the more specific location 
	 * area such as city or state. When this is blank, it indicates we know the 
	 * country, but not the region.
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * The name of the region, if we have one. For blank "region" values, this 
	 * will be "Rest of Country".
	 */
	public String getRegionName() {
		return regionName;
	}

	/**
	 * The number of unique campaign opens for a region.
	 */
	public Integer getOpens() {
		return opens;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(100);
		sb.append("Location Report: " + System.lineSeparator());
		sb.append("    Country Code: " + getCountryCode() + System.lineSeparator());
		sb.append("    Region: " + getRegion() + System.lineSeparator());
		sb.append("    Region Name: " + getRegionName() + System.lineSeparator());
		sb.append("    Opens: " + getOpens());
		return sb.toString();
	}

}
