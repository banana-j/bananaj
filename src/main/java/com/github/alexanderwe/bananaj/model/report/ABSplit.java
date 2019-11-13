package com.github.alexanderwe.bananaj.model.report;

import org.json.JSONObject;

/**
 * General stats about different groups of an A/B Split campaign. Does not include information about Mailchimp Pro's Multivariate Campaigns.
 *
 */
public class ABSplit {
	
	private SplitInfo a;
	private SplitInfo b;

	public ABSplit(JSONObject jsonObj) {
		JSONObject aSplit = jsonObj.getJSONObject("a");
		JSONObject bSplit = jsonObj.getJSONObject("b");
		a = new SplitInfo(aSplit);
		b = new SplitInfo(bSplit);
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
