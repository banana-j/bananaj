/**
 * @author alexanderweiss
 * @date 12.12.2015
 */
package com.github.bananaj.model.report;

import org.json.JSONObject;

/**
 * Class for representing facebook likes
 * @author alexanderweiss
 *
 */
public class FacebookLikes {

	private int recipientLikes;
	private int uniqueLikes;
	private int facebookLikes;
	
	public FacebookLikes(JSONObject jsonObj) {
		recipientLikes = jsonObj.getInt("recipient_likes");
		uniqueLikes = jsonObj.getInt("unique_likes");
		facebookLikes = jsonObj.getInt("facebook_likes");
	}
	/**
	 * @return The number of recipients who liked the campaign on Facebook.
	 */
	public int getRecipientLikes() {
		return recipientLikes;
	}

	/**
	 * @return The number of unique likes.
	 */
	public int getUniqueLikes() {
		return uniqueLikes;
	}

	/**
	 * @return The number of Facebook likes for the campaign.
	 */
	public int getFacebookLikes() {
		return facebookLikes;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return
				"Facebook:" + System.lineSeparator() +
				"    Recipient Likes: " + getRecipientLikes() + System.lineSeparator() +
				"    Unique Likes: " +  getUniqueLikes() + System.lineSeparator() +
				"    Facebook Likes: " + getFacebookLikes();
	}

}
