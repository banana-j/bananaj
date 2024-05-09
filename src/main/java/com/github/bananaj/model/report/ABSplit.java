package com.github.bananaj.model.report;

import org.json.JSONObject;

import com.github.bananaj.utils.JSONObjectCheck;

/**
 * General stats about different groups of an A/B Split campaign. Does not include information about Mailchimp Pro's Multivariate Campaigns.
 *
 */
public class ABSplit {
	
	private SplitInfo a;
	private SplitInfo b;

	public ABSplit(JSONObject jsonObj) {
		JSONObjectCheck jObj = new JSONObjectCheck(jsonObj);
		JSONObject aSplit = jObj.getJSONObject("a");
		JSONObject bSplit = jObj.getJSONObject("b");
		if (aSplit != null) {
			a = new SplitInfo(aSplit);
		}
		if (bSplit != null) {
			b = new SplitInfo(bSplit);
		}
	}

	/**
	 * @return the a
	 */
	public SplitInfo getA() {
		return a;
	}

	/**
	 * @return the b
	 */
	public SplitInfo getB() {
		return b;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return
				"AB Split: " + System.lineSeparator() +
				"  a:" + System.lineSeparator() + getA().toString() + System.lineSeparator() +
				"  b:" + System.lineSeparator() + getB().toString();
	}

}
