package com.github.bananaj.model.list;

import java.time.ZonedDateTime;

import org.json.JSONObject;

import com.github.bananaj.utils.DateConverter;
import com.github.bananaj.utils.JSONObjectCheck;

public class ListStats {

	private Integer memberCount;		// The number of active members in the list.
	private Integer totalContacts;		// An approximate count of all contacts in any state.
	private Integer unsubscribeCount;	// The number of members who have unsubscribed from the list.
	private Integer cleanedCount;		// The number of members cleaned from the list.
	private Integer memberCountSinceSend;	// The number of active members in the list since the last campaign was sent.
	private Integer unsubscribeCountSinceSend;	// The number of members who have unsubscribed since the last campaign was sent.
	private Integer cleanedCountSinceSend;		// The number of members cleaned from the list since the last campaign was sent.
	private Integer campaignCount;		// The number of campaigns in any status that use this list.
	private ZonedDateTime campaignLastSent;	// The date and time the last campaign was sent to this list. This is updated when a campaign is sent to 10 or more recipients.
	private Integer mergeFieldCount;	// The number of merge vars for this list (not EMAIL, which is required).
	private Double avgSubscritionRate;	// The average number of subscriptions per month for the list (not returned if we haven’t calculated it yet).
	private Double avgUnsubscribeRate;	// The average number of unsubscriptions per month for the list (not returned if we haven’t calculated it yet).
	private Double targetSubscriptionRate;	// The target number of subscriptions per month for the list to keep it growing (not returned if we haven’t calculated it yet).
	private Double openRate;		// The average open rate (a percentage represented as a number between 0 and 100) per campaign for the list (not returned if we haven’t calculated it yet).
	private Double clickRate;		// The average click rate (a percentage represented as a number between 0 and 100) per campaign for the list (not returned if we haven’t calculated it yet).
	private ZonedDateTime lastSubcribedDate;	// The date and time of the last time someone subscribed to this list.
	private ZonedDateTime lastUnsubscrivedDate;	// The date and time of the last time someone unsubscribed from this list.
	
	public ListStats() {

	}

	public ListStats(JSONObject jsonObj) {
		if (jsonObj != null) {
			JSONObjectCheck jObj = new JSONObjectCheck(jsonObj);
			memberCount = jObj.getInt("member_count");
			totalContacts = jObj.getInt("total_contacts");
			unsubscribeCount = jObj.getInt("unsubscribe_count");
			cleanedCount = jObj.getInt("cleaned_count");
			memberCountSinceSend = jObj.getInt("member_count_since_send");
			unsubscribeCountSinceSend = jObj.getInt("unsubscribe_count_since_send");
			cleanedCountSinceSend = jObj.getInt("cleaned_count_since_send");
			campaignCount = jObj.getInt("campaign_count");
			campaignLastSent = jObj.getISO8601Date("campaign_last_sent");
			mergeFieldCount = jObj.getInt("merge_field_count");
			avgSubscritionRate = jObj.getDouble("avg_sub_rate");
			avgUnsubscribeRate = jObj.getDouble("avg_unsub_rate");
			targetSubscriptionRate = jObj.getDouble("target_sub_rate");
			openRate = jObj.getDouble("open_rate");
			clickRate = jObj.getDouble("click_rate");
			lastSubcribedDate = jObj.getISO8601Date("last_sub_date");
			lastUnsubscrivedDate = jObj.getISO8601Date("last_unsub_date");
		}
	}
	
	/**
	 * The total number of members (sum of subscribed, unsubscribed, and cleaned)
	 */
	public Integer getTotalMemberCount() {
		return memberCount + unsubscribeCount + cleanedCount;
	}
	
	/**
	 * The number of active members in the list.
	 */
	public Integer getMemberCount() {
		return memberCount;
	}

	/**
	 * An approximate count of all contacts in any state. Must specify include_total_contacts in query request.
	 */
	public Integer getTotalContacts() {
		return totalContacts;
	}

	public void setMemberCount(int memberCount) {
		this.memberCount = memberCount;
	}

	/**
	 * The number of members who have unsubscribed from the list.
	 */
	public Integer getUnsubscribeCount() {
		return unsubscribeCount;
	}

	public void setUnsubscribeCount(int unsubscribeCount) {
		this.unsubscribeCount = unsubscribeCount;
	}

	/**
	 * The number of members cleaned from the list.
	 */
	public Integer getCleanedCount() {
		return cleanedCount;
	}

	public void setCleanedCount(int cleanedCount) {
		this.cleanedCount = cleanedCount;
	}

	/**
	 * The number of active members in the list since the last campaign was sent.
	 */
	public Integer getMemberCountSinceSend() {
		return memberCountSinceSend;
	}

	/**
	 * The number of members who have unsubscribed since the last campaign was sent.
	 */
	public Integer getUnsubscribeCountSinceSend() {
		return unsubscribeCountSinceSend;
	}

	/**
	 * The number of members cleaned from the list since the last campaign was sent.
	 */
	public Integer getCleanedCountSinceSend() {
		return cleanedCountSinceSend;
	}

	/**
	 * The number of campaigns in any status that use this list.
	 */
	public Integer getCampaignCount() {
		return campaignCount;
	}

	/**
	 * The date and time the last campaign was sent to this list. This is updated when a campaign is sent to 10 or more recipients.
	 */
	public ZonedDateTime getCampaignLastSent() {
		return campaignLastSent;
	}

	/**
	 * The number of merge vars for this list (not EMAIL, which is required).
	 */
	public Integer getMergeFieldCount() {
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
	public ZonedDateTime getLastSubcribedDate() {
		return lastSubcribedDate;
	}

	/**
	 * The date and time of the last time someone unsubscribed from this list
	 */
	public ZonedDateTime getLastUnsubscrivedDate() {
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
				(getCampaignLastSent() != null ? "    Campaign Last Sent: " + DateConverter.toLocalString(getCampaignLastSent()) + System.lineSeparator() : "") +
				"    Merge Fields: " + getMergeFieldCount() + System.lineSeparator() +
				(getAvgSubscritionRate() != null ? "    Avg Subscription Rate: " + getAvgSubscritionRate() + System.lineSeparator() : "") +
				(getAvgUnsubscribeRate() != null ? "    Avg Unsubscribe Rate: " + getAvgUnsubscribeRate() + System.lineSeparator() : "") +
				(getTargetSubscriptionRate() != null ? "    Target Subscription Rate: " + getTargetSubscriptionRate() + System.lineSeparator() : "") +
				(getOpenRate() != null ? "    Open Rate: " + getOpenRate() + System.lineSeparator() : "") +
				(getClickRate() != null ? "    Click Rate: " + getClickRate() + System.lineSeparator() : "") +
				(getLastSubcribedDate() != null ? "    Last Subscribed: " + DateConverter.toLocalString(getLastSubcribedDate()) + System.lineSeparator() : "") +
				(getLastUnsubscrivedDate() != null ? "    Last Unscribe: " + DateConverter.toLocalString(getLastUnsubscrivedDate()) : "");
	}

}
