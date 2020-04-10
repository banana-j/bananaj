package com.github.bananaj.model.report;

import org.json.JSONObject;

/**
 * Updates on campaigns in the process of sending.
 *
 */
public class DeliveryStatus {
	
	private Boolean enabled;
	private Boolean canCancel;
	private String status;
	private Integer emailsSent;
	private Integer emailsCanceled;

	public DeliveryStatus(JSONObject jsonObj) {
		enabled = jsonObj.getBoolean("enabled");
		canCancel = jsonObj.has("can_cancel") ? jsonObj.getBoolean("can_cancel") : null;
		status = jsonObj.has("status") ? jsonObj.getString("status") : null;
		emailsSent = jsonObj.has("emails_sent") ? jsonObj.getInt("emails_sent") : null;
		emailsCanceled = jsonObj.has("emails_canceled") ? jsonObj.getInt("emails_canceled") : null;
	}

	/**
	 * @return Whether Campaign Delivery Status is enabled for this account and campaign.
	 */
	public Boolean getEnabled() {
		return enabled;
	}

	/**
	 * @return Whether a campaign send can be canceled.
	 */
	public Boolean getCanCancel() {
		return canCancel;
	}

	/**
	 * @return The current state of a campaign delivery.
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @return The total number of emails confirmed sent for this campaign so far.
	 */
	public Integer getEmailsSent() {
		return emailsSent;
	}

	/**
	 * @return The total number of emails canceled for this campaign.
	 */
	public Integer getEmailsCanceled() {
		return emailsCanceled;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return  "DeliveryStatus:"  + System.lineSeparator() +
				"    Enabled: " + getEnabled() + 
				(getEnabled() ? System.lineSeparator() + 
						"    Can Cancel: " + getCanCancel() + System.lineSeparator() +
						"    Status: " + getStatus() + System.lineSeparator() +
						"    Emails Sent: " + getEmailsSent() + System.lineSeparator() +
						"    Emails Canceled: " + getEmailsCanceled()
						: "");
	}

}
