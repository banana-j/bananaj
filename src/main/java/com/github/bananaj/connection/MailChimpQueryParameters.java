package com.github.bananaj.connection;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.github.bananaj.utils.DateConverter;

/**
 * 
 * Utility class to aid in the building of MailChimp API URLs. Allows common 
 * parameters as well as optional API specific parameters to be defined for
 * individual queries.
 *
 * Pagination can be performed by specifying count and offset.
 *  
 */
public class MailChimpQueryParameters {

	// General Query Parameters
	protected String baseUrl;
	protected String includeFields;
	protected String excludeFields;
	protected HashMap<String,String> queryParams; 
	// TODO: collection of include fields
	// TODO: collection of exclude fields
	
	public MailChimpQueryParameters() {
		// TODO Auto-generated constructor stub
	}

	public MailChimpQueryParameters(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public MailChimpQueryParameters baseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
		return this;
	}

	/**
	 * A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation.
	 * @param fields
	 * @return this
	 */
	public MailChimpQueryParameters includeFields(String fields) {
		this.includeFields = fields;
		return this;
	}

	/**
	 * A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation.
	 * @param fields
	 * @return this
	 */
	public MailChimpQueryParameters excludeFields(String fields) {
		this.excludeFields = fields;
		return this;
	}

	/**
	 * The number of records to return. Typical default value is 10. Maximum value is 1000
	 * @param count
	 * @return this
	 */
	public MailChimpQueryParameters count(Integer count) {
		if (count <= 0 || count > 1000) {
			throw new NumberFormatException("Out of range (1-1000)");
		}
		param("count",count.toString());
		return this;
	}

	public Integer getCount() {
		if (queryParams == null || !queryParams.containsKey("count")) {
			return null;
		}
		return Integer.parseInt(queryParams.get("count"));
	}

	/**
	 * Used for pagination, this it the number of records from a collection to skip. Default value is 0.
	 * @param offset
	 * @return  this
	 */
	public MailChimpQueryParameters offset(Integer offset) {
		if (offset < 0) {
			throw new NumberFormatException("<0");
		}
		param("offset",offset.toString());
		return this;
	}
	
	public Integer getOffset() {
		if (queryParams == null || !queryParams.containsKey("offset")) {
			return null;
		}
		return Integer.parseInt(queryParams.get("offset"));
	}
	
	/**
	 * Add an API specific query parameter.
	 * @param key
	 * @param value
	 * @return this
	 */
	public MailChimpQueryParameters param(String key, String value) {
		if (queryParams == null) {
			queryParams = new LinkedHashMap<String,String>();
		}
		if (value == null) {
			queryParams.remove(key);
		} else {
			queryParams.put(key, value);
		}
		return this;
	}

	/**
	 * Add an API specific query parameter.
	 * @param key
	 * @param value
	 * @return this
	 */
	public MailChimpQueryParameters param(String key, ZonedDateTime value) {
		if (queryParams == null) {
			queryParams = new LinkedHashMap<String,String>();
		}
		if (value == null) {
			queryParams.remove(key);
		} else {
			queryParams.put(key, DateConverter.toISO8601UTC(value));
		}
		return this;
	}
	
	public String getParam(String key) {
		if (queryParams == null) {
			return null;
		}
		return queryParams.get(key);
	}
	
	public URL getURL() throws IOException {
		StringBuilder sb = new StringBuilder(baseUrl);
		boolean first = !baseUrl.contains("?");
		
		if (queryParams != null && queryParams.size() > 0) {
			for (String key : queryParams.keySet()) {
				String value = queryParams.get(key);
				if(first) {
					sb.append("?");
					first = false;
				} else {
					sb.append("&");
				}
				sb.append(encodeValue(key));
				sb.append("=");
				sb.append(encodeValue(value));
			}
		}

		if (includeFields != null && includeFields.length() > 0) {
			if(first) {
				sb.append("?");
				first = false;
			} else {
				sb.append("&");
			}
			sb.append("fields");
			sb.append("=");
			sb.append(encodeValue(includeFields));
		}

		if (excludeFields != null && excludeFields.length() > 0) {
			if(first) {
				sb.append("?");
				first = false;
			} else {
				sb.append("&");
			}
			sb.append("exclude_fields");
			sb.append("=");
			sb.append(encodeValue(excludeFields));
		}

		return new URL(sb.toString());
	}

	private String encodeValue(String value) throws UnsupportedEncodingException {
	    return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
	}
}
