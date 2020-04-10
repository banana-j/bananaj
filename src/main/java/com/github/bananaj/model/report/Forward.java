/**
 * @author alexanderweiss
 * @date 20.11.2015
 */
package com.github.bananaj.model.report;

import org.json.JSONObject;

/**
 * Class for representing forward counts in a campaign
 * 
 * @author alexanderweiss
 *
 */
public class Forward {

	private int count;
	private int opens;
	
	public Forward(JSONObject jsonObj) {
		count = jsonObj.getInt("forwards_count");
		opens = jsonObj.getInt("forwards_opens");
	}

	/**
	 * @return How many times the campaign has been forwarded.
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @return How many times the forwarded campaign has been opened.
	 */
	public int getOpens() {
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
