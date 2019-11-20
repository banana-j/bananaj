package com.github.alexanderwe.bananaj.model.report;

import org.json.JSONObject;

/**
 * Statistics for a domain from a campaign.
 *
 */
public class DomainStats {
	private String domain;
	private int emailsSent;
	private int bounces;
	private int opens;
	private int clicks;
	private int unsubs;
	private int delivered;
	private double emailsPct;
	private double bouncesPct;
	private double opensPct;
	private double clicksPct;
	private double unsubsPct;

	public DomainStats(JSONObject jsonObj) {
		domain = jsonObj.getString("domain");
		emailsSent = jsonObj.getInt("emails_sent");
		bounces = jsonObj.getInt("bounces");
		opens = jsonObj.getInt("opens");
		clicks = jsonObj.getInt("clicks");
		unsubs = jsonObj.getInt("unsubs");
		delivered = jsonObj.getInt("delivered");
		emailsPct = jsonObj.getDouble("emails_pct");
		bouncesPct = jsonObj.getDouble("bounces_pct");
		opensPct = jsonObj.getDouble("opens_pct");
		clicksPct = jsonObj.getDouble("clicks_pct");
		unsubsPct = jsonObj.getDouble("unsubs_pct");
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
	public int getEmailsSent() {
		return emailsSent;
	}

	/**
	 * @return The number of bounces at a domain.
	 */
	public int getBounces() {
		return bounces;
	}

	/**
	 * @return The number of opens for a domain.
	 */
	public int getOpens() {
		return opens;
	}

	/**
	 * @return The number of clicks for a domain.
	 */
	public int getClicks() {
		return clicks;
	}

	/**
	 * @return The total number of unsubscribes for a domain.
	 */
	public int getUnsubs() {
		return unsubs;
	}

	/**
	 * @return The number of successful deliveries for a domain.
	 */
	public int getDelivered() {
		return delivered;
	}

	/**
	 * @return The percentage of total emails that went to this domain.
	 */
	public double getEmailsPct() {
		return emailsPct;
	}

	/**
	 * @return The percentage of total bounces from this domain.
	 */
	public double getBouncesPct() {
		return bouncesPct;
	}

	/**
	 * @return The percentage of total opens from this domain.
	 */
	public double getOpensPct() {
		return opensPct;
	}

	/**
	 * @return The percentage of total clicks from this domain.
	 */
	public double getClicksPct() {
		return clicksPct;
	}

	/**
	 * @return The percentage of total unsubscribes from this domain.
	 */
	public double getUnsubsPct() {
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
