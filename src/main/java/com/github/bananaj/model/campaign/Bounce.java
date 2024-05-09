/**
 * @author alexanderweiss
 * @date 20.11.2015
 */
package com.github.bananaj.model.campaign;

import org.json.JSONObject;

import com.github.bananaj.utils.JSONObjectCheck;

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

	private Integer hardBounces;
	private Integer softBounces;
	private Integer syntaxErrors;

	public Bounce(JSONObject bounce) {
		JSONObjectCheck jObj = new JSONObjectCheck(bounce);
		hardBounces = jObj.getInt("hard_bounces");
		softBounces = jObj.getInt("soft_bounces");
		syntaxErrors = jObj.getInt("syntax_errors");
	}

	/**
	 * @return The total number of hard bounced email addresses.
	 */
	public Integer getHardBounces() {
		return hardBounces;
	}

	/**
	 * @return The total number of soft bounced email addresses.
	 */
	public Integer getSoftBounces() {
		return softBounces;
	}

	/**
	 * @return The total number of addresses that were syntax-related bounces.
	 */
	public Integer getSyntaxErrors() {
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
