package com.github.alexanderwe.bananaj.model.campaign;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.alexanderwe.bananaj.exceptions.ConditionException;
import com.github.alexanderwe.bananaj.model.list.segment.AbstractCondition;
import com.github.alexanderwe.bananaj.model.list.segment.MatchType;

/**
 * 
 */
public class CampaignSegmentOpts {

    private Integer saved_segment_id;
    private MatchType match;
    private List<AbstractCondition> conditions;

    /**
     * Used when created a Condition locally with the Builder class
     * @see Builder
     * @param b
     */

    public CampaignSegmentOpts(Builder b) throws ConditionException{
    	this.saved_segment_id = b.saved_segment_id;
    	this.match = b.match;
    	this.conditions = b.conditions;
    }

    public CampaignSegmentOpts(JSONObject jsonObj) {
    	if (jsonObj.has("saved_segment_id")) {
    		this.saved_segment_id = jsonObj.getInt("saved_segment_id");
    	}
    	this.match = MatchType.valueOf(jsonObj.getString("match").toUpperCase());
    	this.conditions = new ArrayList<AbstractCondition>();
// TODO:
//    	if (jsonObj.has("conditions"))
//    	{
//    		JSONArray conditions = jsonObj.getJSONArray("conditions");
//    		for(int i=0; i<conditions.length(); i++) {
//    			AbstractCondition condition = new AbstractCondition(conditions.get(i));
//        		this.conditions.add(condition);
//    		}
//    	}
    }
    
    public CampaignSegmentOpts() {
    	
    }
    
    public Integer getSavedSegmentId() {
        return saved_segment_id;
    }

	public MatchType getMatch() {
		return match;
	}

	public List<AbstractCondition> getConditions() {
        return conditions;
    }

	/**
	 * Helper method to convert JSON for mailchimp PATCH/POST operations
	 * @return
	 */
    public JSONObject getJsonRepresentation(){
        JSONObject segmentOpts = new JSONObject();

        if (getSavedSegmentId() != null) {
        	segmentOpts.put("saved_segment_id", getSavedSegmentId());
        }

        if (getMatch() != null) {
        	segmentOpts.put("match", getMatch().value());
        }
        
        if (getConditions() != null) {
        	JSONArray jsonConditions = new JSONArray();
        	Iterator<AbstractCondition> i = getConditions().iterator();
        	while (i.hasNext()) {
        		jsonConditions.put(i.next());
        	}
        	segmentOpts.put("conditions", jsonConditions);
        }
        
        return segmentOpts;
    }

    @Override
    public String toString() {
        return "Saved Segment ID: " + getSavedSegmentId() + System.lineSeparator() +
        		"Match: " + getMatch().toString()  + System.lineSeparator() + 
                "Conditions: " + getConditions().toString() +  System.lineSeparator();
    }

    public static class Builder {
        private Integer saved_segment_id;
        private MatchType match;
        private List<AbstractCondition> conditions;

        public Builder savedSegmentId(Integer saved_segment_id) {
            this.saved_segment_id = saved_segment_id;
            return this;
        }

        public Builder match(MatchType match) {
            this.match = match;
            return this;
        }

        public Builder conditions( List<AbstractCondition> conditions) {
            this.conditions = conditions;
            return this;
        }

        public CampaignSegmentOpts build() {
            try {
                return new CampaignSegmentOpts(this);
            } catch (ConditionException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
