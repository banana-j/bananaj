/**
 * @author alexanderweiss
 * @date 12.12.2015
 */
package com.github.bananaj.model.report;

import org.json.JSONObject;

import com.github.bananaj.utils.JSONObjectCheck;

/**
 * Class for representing facebook likes
 * @author alexanderweiss
 *
 */
public class FacebookLikes {

	private Integer recipientLikes;
	private Integer uniqueLikes;
	private Integer facebookLikes;
	
	public FacebookLikes(JSONObject jsonObj) {
		JSONObjectCheck jObj = new JSONObjectCheck(jsonObj);
		recipientLikes = jObj.getInt("recipient_likes");
		uniqueLikes = jObj.getInt("unique_likes");
		facebookLikes = jObj.getInt("facebook_likes");
	}
	/**
	 * @return The number of recipients who liked the campaign on Facebook.
	 */
	public Integer getRecipientLikes() {
		return recipientLikes;
	}

	/**
	 * @return The number of unique likes.
	 */
	public Integer getUniqueLikes() {
		return uniqueLikes;
	}

	/**
	 * @return The number of Facebook likes for the campaign.
	 */
	public Integer getFacebookLikes() {
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
