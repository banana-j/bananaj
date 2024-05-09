/**
 * @author alexanderweiss
 * @date 20.11.2015
 */
package com.github.bananaj.model.report;

import org.json.JSONObject;

import com.github.bananaj.utils.JSONObjectCheck;

/**
 * Class for representing forward counts in a campaign
 * 
 * @author alexanderweiss
 *
 */
public class Forward {

	private Integer count;
	private Integer opens;
	
	public Forward(JSONObject jsonObj) {
		JSONObjectCheck jObj = new JSONObjectCheck(jsonObj);
		count = jObj.getInt("forwards_count");
		opens = jObj.getInt("forwards_opens");
	}

	/**
	 * @return How many times the campaign has been forwarded.
	 */
	public Integer getCount() {
		return count;
	}

	/**
	 * @return How many times the forwarded campaign has been opened.
	 */
	public Integer getOpens() {
		return opens;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return
				"Forwards:" + System.lineSeparator() +
				"    Forward count: " + getCount() + System.lineSeparator() +
				"    Forward open: " + getOpens();
	}
	
	
}
