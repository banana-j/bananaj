/**
 * @author alexanderweiss
 * @date 12.12.2015
 */
package model.report;


/**
 * Class for representing facebook likes
 * @author alexanderweiss
 *
 */
public class FacebookLikes {

	private int recipient_likes;
	private int unique_likes;
	private int facebook_likes;
	
	public FacebookLikes(int recipient_likes, int unique_likes, int facebook_likes  ) {
		setRecipient_likes(recipient_likes);
		setUnique_likes(unique_likes);
		setFacebook_likes(facebook_likes);
	}

	/**
	 * @return the recipient_likes
	 */
	public int getRecipient_likes() {
		return recipient_likes;
	}

	/**
	 * @param recipient_likes the recipient_likes to set
	 */
	public void setRecipient_likes(int recipient_likes) {
		this.recipient_likes = recipient_likes;
	}

	/**
	 * @return the unique_likes
	 */
	public int getUnique_likes() {
		return unique_likes;
	}

	/**
	 * @param unique_likes the unique_likes to set
	 */
	public void setUnique_likes(int unique_likes) {
		this.unique_likes = unique_likes;
	}

	/**
	 * @return the facebook_likes
	 */
	public int getFacebook_likes() {
		return facebook_likes;
	}

	/**
	 * @param facebook_likes the facebook_likes to set
	 */
	public void setFacebook_likes(int facebook_likes) {
		this.facebook_likes = facebook_likes;
	}

}
