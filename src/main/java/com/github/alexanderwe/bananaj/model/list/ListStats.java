package com.github.alexanderwe.bananaj.model.list;

import java.time.LocalDateTime;

import org.json.JSONObject;

import com.github.alexanderwe.bananaj.utils.DateConverter;

public class ListStats {

	int memberCount;		// The number of active members in the list.
	int unsubscribeCount;	// The number of members who have unsubscribed from the list.
	int cleanedCount;		// The number of members cleaned from the list.
	private int memberCountSinceSend;	// The number of active members in the list since the last campaign was sent.
	private int unsubscribeCountSinceSend;	// The number of members who have unsubscribed since the last campaign was sent.
	private int cleanedCountSinceSend;		// The number of members cleaned from the list since the last campaign was sent.
	private int campaignCount;		// The number of campaigns in any status that use this list.
	private LocalDateTime campaignLastSent;	// The date and time the last campaign was sent to this list. This is updated when a campaign is sent to 10 or more recipients.
	private int mergeFieldCount;	// The number of merge vars for this list (not EMAIL, which is required).
	private Double avgSubscritionsRate;	// The average number of subscriptions per month for the list (not returned if we haven’t calculated it yet).
	private Double avgUnsibscriptionsRate;	// The average number of unsubscriptions per month for the list (not returned if we haven’t calculated it yet).
	private Double targetSubscriptionsRate;	// The target number of subscriptions per month for the list to keep it growing (not returned if we haven’t calculated it yet).
	private Double openRate;		// The average open rate (a percentage represented as a number between 0 and 100) per campaign for the list (not returned if we haven’t calculated it yet).
	private Double clickRate;		// The average click rate (a percentage represented as a number between 0 and 100) per campaign for the list (not returned if we haven’t calculated it yet).
	private LocalDateTime lastSubcribedDate;	// The date and time of the last time someone subscribed to this list.
	private LocalDateTime lastUnsubscrivedDate;	// The date and time of the last time someone unsubscribed from this list.
	
	public ListStats() {

	}

	public ListStats(JSONObject stats) {
		if (stats != null) {
			this.memberCount = stats.getInt("member_count");
			this.unsubscribeCount = stats.getInt("unsubscribe_count");
			this.cleanedCount = stats.getInt("cleaned_count");
			this.memberCountSinceSend = stats.getInt("member_count_since_send");
			this.unsubscribeCountSinceSend = stats.getInt("unsubscribe_count_since_send");
			this.cleanedCountSinceSend = stats.getInt("cleaned_count_since_send");
			this.campaignCount = stats.getInt("campaign_count");
			this.campaignLastSent = getOptionalDate(stats,"campaign_last_sent");
			this.mergeFieldCount = stats.getInt("merge_field_count");
			if(stats.has("avg_sub_rate")) {
				this.avgSubscritionsRate = stats.getDouble("avg_sub_rate");
			}
			if(stats.has("avg_unsub_rate")) {
				this.avgUnsibscriptionsRate = stats.getDouble("avg_unsub_rate");
			}
			if(stats.has("target_sub_rate")) {
				this.targetSubscriptionsRate = stats.getDouble("target_sub_rate");
			}
			if(stats.has("open_rate")) {
				this.openRate = stats.getDouble("open_rate");
			}
			if(stats.has("click_rate")) {
				this.clickRate = stats.getDouble("click_rate");
			}
			this.lastSubcribedDate = getOptionalDate(stats,"last_sub_date");
			this.lastUnsubscrivedDate = getOptionalDate(stats,"last_unsub_date");
		}
	}
	
	private LocalDateTime getOptionalDate(JSONObject stats, String key) {
		if(stats.has(key)) {
			String value = stats.getString(key);
			if (value.length() > 0) {
				return DateConverter.getInstance().createDateFromISO8601(value);
			}
		}
		return null;
	}

	/**
	 * The total number of members (sum of subscribed, unsubscribed, and cleaned)
	 * @return
	 */
	public int getTotalMemberCount() {
		return memberCount + unsubscribeCount + cleanedCount;
	}
	
	/**
	 * The number of active members in the list.
	 * @return
	 */
	public int getMemberCount() {
		return memberCount;
	}

	public void setMemberCount(int member_count) {
		this.memberCount = member_count;
	}

	/**
	 * The number of members who have unsubscribed from the list.
	 * @return
	 */
	public int getUnsubscribeCount() {
		return unsubscribeCount;
	}

	public void setUnsubscribeCount(int unsubscribe_count) {
		this.unsubscribeCount = unsubscribe_count;
	}

	/**
	 * The number of members cleaned from the list.
	 * @return
	 */
	public int getCleanedCount() {
		return cleanedCount;
	}

	public void setCleanedCount(int cleaned_count) {
		this.cleanedCount = cleaned_count;
	}

	/**
	 * The number of active members in the list since the last campaign was sent.
	 * @return
	 */
	public int getMemberCountSinceSend() {
		return memberCountSinceSend;
	}

	/**
	 * The number of members who have unsubscribed since the last campaign was sent.
	 * @return
	 */
	public int getUnsubscribeCountSinceSend() {
		return unsubscribeCountSinceSend;
	}

	/**
	 * The number of members cleaned from the list since the last campaign was sent.
	 * @return
	 */
	public int getCleanedCountSinceSend() {
		return cleanedCountSinceSend;
	}

	/**
	 * The number of campaigns in any status that use this list.
	 * @return
	 */
	public int getCampaignCount() {
		return campaignCount;
	}

	/**
	 * The date and time the last campaign was sent to this list. This is updated when a campaign is sent to 10 or more recipients.
	 * @return
	 */
	public LocalDateTime getCampaignLastSent() {
		return campaignLastSent;
	}

	/**
	 * The number of merge vars for this list (not EMAIL, which is required).
	 * @return
	 */
	public int getMergeFieldCount() {
		return mergeFieldCount;
	}

	/**
	 * The average number of subscriptions per month for the list (not returned if we haven’t calculated it yet).
	 * @return
	 */
	public Double getAvgSubscritionsRate() {
		return avgSubscritionsRate;
	}

	/**
	 * The average number of unsubscriptions per month for the list (not returned if we haven’t calculated it yet).
	 * @return
	 */
	public Double getAvgUnsibscriptionsRate() {
		return avgUnsibscriptionsRate;
	}

	/**
	 * The target number of subscriptions per month for the list to keep it growing (not returned if we haven’t calculated it yet).
	 * @return
	 */
	public Double getTargetSubscriptionsRate() {
		return targetSubscriptionsRate;
	}

	/**
	 * The average open rate (a percentage represented as a number between 0 and 100) per campaign for the list (not returned if we haven’t calculated it yet).
	 * @return
	 */
	public Double getOpenRate() {
		return openRate;
	}

	/**
	 * The average click rate (a percentage represented as a number between 0 and 100) per campaign for the list (not returned if we haven’t calculated it yet).
	 * @return
	 */
	public Double getClickRate() {
		return clickRate;
	}

	/**
	 * The date and time of the last time someone subscribed to this list
	 * @return
	 */
	public LocalDateTime getLastSubcribedDate() {
		return lastSubcribedDate;
	}

	/**
	 * The date and time of the last time someone unsubscribed from this list
	 * @return
	 */
	public LocalDateTime getLastUnsubscrivedDate() {
		return lastUnsubscrivedDate;
	}

	
}
