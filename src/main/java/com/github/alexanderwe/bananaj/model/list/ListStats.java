package com.github.alexanderwe.bananaj.model.list;

import java.time.LocalDateTime;

import org.json.JSONObject;

import com.github.alexanderwe.bananaj.utils.DateConverter;

public class ListStats {

	private int memberCount;		// The number of active members in the list.
	private int unsubscribeCount;	// The number of members who have unsubscribed from the list.
	private int cleanedCount;		// The number of members cleaned from the list.
	private int memberCountSinceSend;	// The number of active members in the list since the last campaign was sent.
	private int unsubscribeCountSinceSend;	// The number of members who have unsubscribed since the last campaign was sent.
	private int cleanedCountSinceSend;		// The number of members cleaned from the list since the last campaign was sent.
	private int campaignCount;		// The number of campaigns in any status that use this list.
	private LocalDateTime campaignLastSent;	// The date and time the last campaign was sent to this list. This is updated when a campaign is sent to 10 or more recipients.
	private int mergeFieldCount;	// The number of merge vars for this list (not EMAIL, which is required).
	private Double avgSubscritionRate;	// The average number of subscriptions per month for the list (not returned if we haven’t calculated it yet).
	private Double avgUnsubscribeRate;	// The average number of unsubscriptions per month for the list (not returned if we haven’t calculated it yet).
	private Double targetSubscriptionRate;	// The target number of subscriptions per month for the list to keep it growing (not returned if we haven’t calculated it yet).
	private Double openRate;		// The average open rate (a percentage represented as a number between 0 and 100) per campaign for the list (not returned if we haven’t calculated it yet).
	private Double clickRate;		// The average click rate (a percentage represented as a number between 0 and 100) per campaign for the list (not returned if we haven’t calculated it yet).
	private LocalDateTime lastSubcribedDate;	// The date and time of the last time someone subscribed to this list.
	private LocalDateTime lastUnsubscrivedDate;	// The date and time of the last time someone unsubscribed from this list.
	
	public ListStats() {

	}

	public ListStats(JSONObject jsonObj) {
		if (jsonObj != null) {
			memberCount = jsonObj.getInt("member_count");
			unsubscribeCount = jsonObj.getInt("unsubscribe_count");
			cleanedCount = jsonObj.getInt("cleaned_count");
			memberCountSinceSend = jsonObj.getInt("member_count_since_send");
			unsubscribeCountSinceSend = jsonObj.getInt("unsubscribe_count_since_send");
			cleanedCountSinceSend = jsonObj.getInt("cleaned_count_since_send");
			campaignCount = jsonObj.getInt("campaign_count");
			campaignLastSent = getOptionalDate(jsonObj,"campaign_last_sent");
			mergeFieldCount = jsonObj.getInt("merge_field_count");
			avgSubscritionRate = jsonObj.has("avg_sub_rate") ? jsonObj.getDouble("avg_sub_rate") : null;
			avgUnsubscribeRate = jsonObj.has("avg_unsub_rate") ? jsonObj.getDouble("avg_unsub_rate") : null;
			targetSubscriptionRate = jsonObj.has("target_sub_rate") ? jsonObj.getDouble("target_sub_rate") : null;
			openRate = jsonObj.has("open_rate") ? jsonObj.getDouble("open_rate") : null;
			clickRate = jsonObj.has("click_rate") ? jsonObj.getDouble("click_rate") : null;
			lastSubcribedDate = getOptionalDate(jsonObj,"last_sub_date");
			lastUnsubscrivedDate = getOptionalDate(jsonObj,"last_unsub_date");
		}
	}
	
	private LocalDateTime getOptionalDate(JSONObject stats, String key) {
		if(stats.has(key)) {
			String value = stats.getString(key);
			if (value.length() > 0) {
				return DateConverter.createDateFromISO8601(value);
			}
		}
		return null;
	}

	/**
	 * The total number of members (sum of subscribed, unsubscribed, and cleaned)
	 */
	public int getTotalMemberCount() {
		return memberCount + unsubscribeCount + cleanedCount;
	}
	
	/**
	 * The number of active members in the list.
	 */
	public int getMemberCount() {
		return memberCount;
	}

	public void setMemberCount(int memberCount) {
		this.memberCount = memberCount;
	}

	/**
	 * The number of members who have unsubscribed from the list.
	 */
	public int getUnsubscribeCount() {
		return unsubscribeCount;
	}

	public void setUnsubscribeCount(int unsubscribeCount) {
		this.unsubscribeCount = unsubscribeCount;
	}

	/**
	 * The number of members cleaned from the list.
	 */
	public int getCleanedCount() {
		return cleanedCount;
	}

	public void setCleanedCount(int cleanedCount) {
		this.cleanedCount = cleanedCount;
	}

	/**
	 * The number of active members in the list since the last campaign was sent.
	 */
	public int getMemberCountSinceSend() {
		return memberCountSinceSend;
	}

	/**
	 * The number of members who have unsubscribed since the last campaign was sent.
	 */
	public int getUnsubscribeCountSinceSend() {
		return unsubscribeCountSinceSend;
	}

	/**
	 * The number of members cleaned from the list since the last campaign was sent.
	 */
	public int getCleanedCountSinceSend() {
		return cleanedCountSinceSend;
	}

	/**
	 * The number of campaigns in any status that use this list.
	 */
	public int getCampaignCount() {
		return campaignCount;
	}

	/**
	 * The date and time the last campaign was sent to this list. This is updated when a campaign is sent to 10 or more recipients.
	 */
	public LocalDateTime getCampaignLastSent() {
		return campaignLastSent;
	}

	/**
	 * The number of merge vars for this list (not EMAIL, which is required).
	 */
	public int getMergeFieldCount() {
		return mergeFieldCount;
	}

	/**
	 * The average number of subscriptions per month for the list (not returned if we haven’t calculated it yet).
	 */
	public Double getAvgSubscritionRate() {
		return avgSubscritionRate;
	}

	/**
	 * The average number of unsubscriptions per month for the list (not returned if we haven’t calculated it yet).
	 */
	public Double getAvgUnsubscribeRate() {
		return avgUnsubscribeRate;
	}

	/**
	 * The target number of subscriptions per month for the list to keep it growing (not returned if we haven’t calculated it yet).
	 */
	public Double getTargetSubscriptionRate() {
		return targetSubscriptionRate;
	}

	/**
	 * The average open rate (a percentage represented as a number between 0 and 100) per campaign for the list (not returned if we haven’t calculated it yet).
	 */
	public Double getOpenRate() {
		return openRate;
	}

	/**
	 * The average click rate (a percentage represented as a number between 0 and 100) per campaign for the list (not returned if we haven’t calculated it yet).
	 */
	public Double getClickRate() {
		return clickRate;
	}

	/**
	 * The date and time of the last time someone subscribed to this list
	 */
	public LocalDateTime getLastSubcribedDate() {
		return lastSubcribedDate;
	}

	/**
	 * The date and time of the last time someone unsubscribed from this list
	 */
	public LocalDateTime getLastUnsubscrivedDate() {
		return lastUnsubscrivedDate;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return 
				"List Stats:" + System.lineSeparator() +
				"    Members: " + getMemberCount() + System.lineSeparator() +
				"    Unsubscribed: " + getUnsubscribeCount() + System.lineSeparator() +
				"    Cleaned: " + getCleanedCount() + System.lineSeparator() +
				"    Members Since Send: " + memberCountSinceSend + System.lineSeparator() +
				"    Unsubscribed Since Send: " + getUnsubscribeCountSinceSend() + System.lineSeparator() +
				"    Cleaned Since Send: " + getCleanedCountSinceSend() + System.lineSeparator() +
				"    Campaigns: " + getCampaignCount() + System.lineSeparator() +
				(getCampaignLastSent() != null ? "    Campaign Last Sent: " + getCampaignLastSent() + System.lineSeparator() : "") +
				"    Merge Fields: " + getMergeFieldCount() + System.lineSeparator() +
				(getAvgSubscritionRate() != null ? "    Avg Subscription Rate: " + getAvgSubscritionRate() + System.lineSeparator() : "") +
				(getAvgUnsubscribeRate() != null ? "    Avg Unsubscribe Rate: " + getAvgUnsubscribeRate() + System.lineSeparator() : "") +
				(getTargetSubscriptionRate() != null ? "    Target Subscription Rate: " + getTargetSubscriptionRate() + System.lineSeparator() : "") +
				(getOpenRate() != null ? "    Open Rate: " + getOpenRate() + System.lineSeparator() : "") +
				(getClickRate() != null ? "    Click Rate: " + getClickRate() + System.lineSeparator() : "") +
				(getLastSubcribedDate() != null ? "    Last Subscribed: " + getLastSubcribedDate() + System.lineSeparator() : "") +
				(getLastUnsubscrivedDate() != null ? "    Last Unscribe: " + getLastUnsubscrivedDate() : "");
	}

}
