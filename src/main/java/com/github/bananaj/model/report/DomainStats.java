package com.github.bananaj.model.report;

import org.json.JSONObject;

import com.github.bananaj.utils.JSONObjectCheck;

/**
 * Statistics for a domain from a campaign.
 *
 */
public class DomainStats {
	private String domain;
	private Integer emailsSent;
	private Integer bounces;
	private Integer opens;
	private Integer clicks;
	private Integer unsubs;
	private Integer delivered;
	private Double emailsPct;
	private Double bouncesPct;
	private Double opensPct;
	private Double clicksPct;
	private Double unsubsPct;

	public DomainStats(JSONObject domainstats) {
		JSONObjectCheck jObj = new JSONObjectCheck(domainstats);
		domain = jObj.getString("domain");
		emailsSent = jObj.getInt("emails_sent");
		bounces = jObj.getInt("bounces");
		opens = jObj.getInt("opens");
		clicks = jObj.getInt("clicks");
		unsubs = jObj.getInt("unsubs");
		delivered = jObj.getInt("delivered");
		emailsPct = jObj.getDouble("emails_pct");
		bouncesPct = jObj.getDouble("bounces_pct");
		opensPct = jObj.getDouble("opens_pct");
		clicksPct = jObj.getDouble("clicks_pct");
		unsubsPct = jObj.getDouble("unsubs_pct");
	}

	/**
	 * @return The name of the domain (gmail.com, hotmail.com, yahoo.com).
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * @return The number of emails sent to that specific domain.
	 */
	public Integer getEmailsSent() {
		return emailsSent;
	}

	/**
	 * @return The number of bounces at a domain.
	 */
	public Integer getBounces() {
		return bounces;
	}

	/**
	 * @return The number of opens for a domain.
	 */
	public Integer getOpens() {
		return opens;
	}

	/**
	 * @return The number of clicks for a domain.
	 */
	public Integer getClicks() {
		return clicks;
	}

	/**
	 * @return The total number of unsubscribes for a domain.
	 */
	public Integer getUnsubs() {
		return unsubs;
	}

	/**
	 * @return The number of successful deliveries for a domain.
	 */
	public Integer getDelivered() {
		return delivered;
	}

	/**
	 * @return The percentage of total emails that went to this domain.
	 */
	public Double getEmailsPct() {
		return emailsPct;
	}

	/**
	 * @return The percentage of total bounces from this domain.
	 */
	public Double getBouncesPct() {
		return bouncesPct;
	}

	/**
	 * @return The percentage of total opens from this domain.
	 */
	public Double getOpensPct() {
		return opensPct;
	}

	/**
	 * @return The percentage of total clicks from this domain.
	 */
	public Double getClicksPct() {
		return clicksPct;
	}

	/**
	 * @return The percentage of total unsubscribes from this domain.
	 */
	public Double getUnsubsPct() {
		return unsubsPct;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return
				"Domain: " + getDomain() + System.lineSeparator() +
				"    Total Sent: " + getEmailsSent() + System.lineSeparator() +
				"    Bounces: " + getBounces() + System.lineSeparator() +
				"    Opens: " + getOpens() + System.lineSeparator() +
				"    Clicks: " + getClicks() + System.lineSeparator() +
				"    Unsubscribes: " + getUnsubs() + System.lineSeparator() +
				"    Successful Deliveries: " + getDelivered() + System.lineSeparator() +
				"    Email %: " + getEmailsPct() + System.lineSeparator() +
				"    Bounces %: " + getBouncesPct() + System.lineSeparator() +
				"    Opens %: " + getOpensPct() + System.lineSeparator() +
				"    Clicks %: " + getClicksPct() + System.lineSeparator() +
				"    Unsubscribes %: " + getUnsubsPct();
	}

}
