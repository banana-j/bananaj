/**
 * @author alexanderweiss
 * @date 20.11.2015
 */
package com.github.alexanderwe.bananaj.model.campaign;

import org.json.JSONObject;

/**
 * Class representing a bounce for a campaign
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
	 * @return the hard_bounces
	 */
	public int getHardBounces() {
		return hardBounces;
	}

	/**
	 * @return the soft_bounces
	 */
	public int getSoftBounces() {
		return softBounces;
	}

	/**
	 * @return the syntax_error_bounces
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
				"Hard Bounces: " + hardBounces + System.lineSeparator() +
				"Soft Bounces: " + softBounces + System.lineSeparator() +
				"Syntax Errors: " + syntaxErrors;
	}

}
