/**
 * @author alexanderweiss
 * @date 19.11.2015
 */
package com.github.bananaj.model.report;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.github.bananaj.connection.MailChimpConnection;
import com.github.bananaj.model.JSONParser;
import com.github.bananaj.utils.DateConverter;

/**
 * Open locations for a specific campaign.
 * 
 *
 */
public class ReportLocation implements JSONParser {

    private String countryCode;
    private String region;
    private String regionName;
	private int opens;
	
	public ReportLocation() {

	}

	public ReportLocation(JSONObject jsonObj) {
		parse(null, jsonObj);
	}

	@Override
	public void parse(MailChimpConnection connection, JSONObject entity) {
		countryCode = entity.getString("country_code");
		region = entity.getString("region");
		regionName = entity.getString("region_name");
		opens = entity.getInt("opens");
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
	public int getOpens() {
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
