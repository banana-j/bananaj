/**
 * @author alexanderweiss
 * @date 20.11.2015
 */
package com.github.bananaj.model.campaign;

import org.json.JSONObject;

/**
 * Class representing bounce counts for a campaign
 * 
 * Bounces occur when an email can't be delivered to an email address. When an
 * email bounces, it is classified as either a soft or a hard bounce.
 * 
 * Mailchimp recognizes two types of bounces, and we handle them differently.
 * Hard bounces happen when an email can't be delivered. This can be caused by
 * an invalid email address or an unexpected error during sending.
 * 
 * Soft bounces are recognized by the email server, but are returned to the
 * sender because the mailbox is either full or temporarily unavailable.
 * 
 * @author alexanderweiss
 *
 */
public class Bounce {

	private int hardBounces;
	private int softBounces;
	private int syntaxErrors;

	public Bounce(JSONObject jsonObj) {
		hardBounces = jsonObj.getInt("hard_bounces");
		softBounces = jsonObj.getInt("soft_bounces");
		syntaxErrors = jsonObj.getInt("syntax_errors");
	}

	/**
	 * @return The total number of hard bounced email addresses.
	 */
	public int getHardBounces() {
		return hardBounces;
	}

	/**
	 * @return The total number of soft bounced email addresses.
	 */
	public int getSoftBounces() {
		return softBounces;
	}

	/**
	 * @return The total number of addresses that were syntax-related bounces.
	 */
	public int getSyntaxErrors() {
		return syntaxErrors;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return
				"Bounces: " + System.lineSeparator() +
				"    Soft bounces: " + getSoftBounces() + System.lineSeparator() +
				"    Hard bounces: " +  getHardBounces() + System.lineSeparator() +
				"    Syntax error bounces: " + getSyntaxErrors();
	}

}
