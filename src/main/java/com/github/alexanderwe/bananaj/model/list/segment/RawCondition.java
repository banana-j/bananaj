package com.github.alexanderwe.bananaj.model.list.segment;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.github.alexanderwe.bananaj.connection.Connection;

public class RawCondition implements AbstractCondition {
	private final static Logger logger = Logger.getLogger(Connection.class);

	private ConditionType condition_type;
	private JSONObject jsonObj;

	public RawCondition(Builder b) {
		condition_type = b.condition_type;
		jsonObj = b.jsonObj;
	}

	@Override
	public ConditionType getConditionType() {
		return condition_type;
	}

	@Override
	public JSONObject getJsonRepresentation() {
		return jsonObj;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "    " + getJsonRepresentation().toString();
	}

	public static class Builder {
		private ConditionType condition_type;
		private JSONObject jsonObj;
		
        public Builder conditionType(ConditionType condition_type) {
            this.condition_type = condition_type;
            return this;
        }

        public Builder json(JSONObject jsonObj) {
            this.jsonObj = jsonObj;
            try {
            	if (condition_type == null) {
            		condition_type = ConditionType.valueOf(jsonObj.getString("condition_type").toUpperCase());
            	}
            } catch (Exception e) {
            	// log warning for unknown condition_type
            	logger.error("Unknown condition type : " + (jsonObj.has("condition_type") ? jsonObj.getString("condition_type") : "<undefined>"));
            }
            return this;
        }
        
        public RawCondition build() {
        	return new RawCondition(this);
        }
	}
}
