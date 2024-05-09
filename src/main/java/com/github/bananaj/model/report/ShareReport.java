package com.github.bananaj.model.report;

import org.json.JSONObject;

/**
 * The url and password for a VIP report.
 *
 */
public class ShareReport {

	private String shareUrl;
	private String sharePassword;

	public ShareReport(JSONObject jsonObj) {
		shareUrl = jsonObj.optString("share_url", null);
		sharePassword = jsonObj.optString("share_password", null);
	}

	/**
	 * @return The URL for the VIP report.
	 */
	public String getShareUrl() {
		return shareUrl;
	}

	/**
	 * @return If password protected, the password for the VIP report.
	 */
	public String getSharePassword() {
		return sharePassword;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return
				"ShareReport: " + getShareUrl() + System.lineSeparator() +
				(getSharePassword()!=null ? "    Password: " + getSharePassword() + System.lineSeparator() : "");
	}

}
