package com.github.bananaj.model.campaign;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * The send checklist for a campaign
 *
 */
public class CampaignSendChecklist {

	private boolean ready;
	private List<CampaignSendCheck> items;

	public CampaignSendChecklist(JSONObject jsonObj) {
		ready = jsonObj.getBoolean("is_ready");
		items = new ArrayList<CampaignSendCheck>();
		JSONArray itemList = jsonObj.getJSONArray("items");
		for (int i=0; i<itemList.length(); i++) {
			CampaignSendCheck check = new CampaignSendCheck(itemList.getJSONObject(i));
			items.add(check);
		}
	}

	public CampaignSendChecklist() {

	}

	/**
	 * @return Whether the campaign is ready to send
	 */
	public boolean isReady() {
		return ready;
	}

	/**
	 * @return A list of feedback items to review before sending your campaign
	 */
	public List<CampaignSendCheck> getItems() {
		return items;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Ready: " + isReady() + System.lineSeparator());
		for (CampaignSendCheck check : items) {
			sb.append(check.toString() + System.lineSeparator());
		}
		return sb.toString();
	}
	
}
