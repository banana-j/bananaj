package com.github.alexanderwe.bananaj.model.campaign;

import org.json.JSONObject;

import com.github.alexanderwe.bananaj.exceptions.ConditionException;

/**
 * 
 */
public class CampaignRecipients {

    private String list_id;
    private CampaignSegmentOpts segment_opts;

    /**
     * Used when created a Condition locally with the Builder class
     * @see Builder
     * @param b
     */

    public CampaignRecipients(Builder b) throws ConditionException{
    	this.segment_opts = b.segment_opts;
    	this.list_id = b.list_id;
    }

    public String getListId() {
        return list_id;
    }

    public CampaignSegmentOpts getSegmentOpts() {
        return segment_opts;
    }

    public JSONObject getJsonRepresentation(){
        JSONObject recipents = new JSONObject();
        
        recipents.put("list_id", getListId());
        if (getSegmentOpts() != null) {
        	recipents.put("segment_opts", getSegmentOpts().getJsonRepresentation());
        }

        return recipents;
    }

    @Override
    public String toString() {
        return  "List ID: " + getListId() + System.lineSeparator() +
                "Segment opts: " + getSegmentOpts().toString();
    }

    public static class Builder {
        private String list_id;
        private CampaignSegmentOpts segment_opts;

        public Builder listId(String list_id) {
            this.list_id = list_id;
            return this;
        }

        public Builder segmentOpts(CampaignSegmentOpts op) {
            this.segment_opts = op;
            return this;
        }

        public CampaignRecipients build() {
            try {
                return new CampaignRecipients(this);
            } catch (ConditionException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
