package com.github.alexanderwe.bananaj.model.campaign;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.alexanderwe.bananaj.exceptions.ConditionException;
import com.github.alexanderwe.bananaj.model.list.segment.AbstractCondition;
import com.github.alexanderwe.bananaj.model.list.segment.ConditionType;
import com.github.alexanderwe.bananaj.model.list.segment.DoubleCondition;
import com.github.alexanderwe.bananaj.model.list.segment.IPGeoInCondition;
import com.github.alexanderwe.bananaj.model.list.segment.IntegerCondition;
import com.github.alexanderwe.bananaj.model.list.segment.MatchType;
import com.github.alexanderwe.bananaj.model.list.segment.OpCondition;
import com.github.alexanderwe.bananaj.model.list.segment.Operator;
import com.github.alexanderwe.bananaj.model.list.segment.RawCondition;
import com.github.alexanderwe.bananaj.model.list.segment.StringArrayCondition;
import com.github.alexanderwe.bananaj.model.list.segment.StringCondition;

/**
 * A class representing all campaign segmentation options. This object should
 * contain a savedSegmentId to use an existing segment, or you can create a new
 * segment by including both match and conditions options.
 */
public class CampaignSegmentOpts {

	private Integer savedSegmentId;
	private Integer prebuiltSegmentId;
	private MatchType match;
	private List<AbstractCondition> conditions;

	/**
	 * Used when created a Condition locally with the Builder class
	 * @see Builder
	 * @param b
	 */

	public CampaignSegmentOpts(Builder b) throws ConditionException{
		this.savedSegmentId = b.savedSegmentId;
		this.prebuiltSegmentId = b.prebuiltSegmentId;
		this.match = b.match;
		this.conditions = b.conditions;
	}

	public CampaignSegmentOpts(JSONObject jsonObj) {
		savedSegmentId = jsonObj.has("saved_segment_id") ? jsonObj.getInt("saved_segment_id") : null;
		prebuiltSegmentId = jsonObj.has("prebuilt_segment_id") ? jsonObj.getInt("prebuilt_segment_id") : null;
		match = jsonObj.has("match") ? MatchType.valueOf(jsonObj.getString("match").toUpperCase()) : null;

		if (jsonObj.has("conditions"))
		{
			conditions = new ArrayList<AbstractCondition>();
			JSONArray jsonConditions = jsonObj.getJSONArray("conditions");
			for (int i = 0; i<jsonConditions.length();i++){
				JSONObject jsonCondition = jsonConditions.getJSONObject(i);

				try {
					ConditionType conditiontype = ConditionType.fromValue(jsonCondition.getString("condition_type"));
					switch(conditiontype) {
					case AIM:
					case AUTOMATION:
					case CONVERSATION:
					case EMAIL_CLIENT:
					case LANGUAGE:
					case SIGNUP_SOURCE:
					case SURVEY_MONKEY:
					case ECOMM_CATEGORY:
					case ECOMM_STORE:
					case GOAL_ACTIVITY:
					case IP_GEO_COUNTRY_STATE:
					case SOCIAL_AGE:
					case SOCIAL_GENDER:
					case SOCIAL_NETWORK_MEMBER:
					case SOCIAL_NETWORK_FOLLOW:
					case ADDRESS_MERGE:
					case BIRTHDAY_MERGE:
					case DATE_MERGE:
					case TEXT_MERGE:
					case SELECT_MERGE:
					case EMAIL_ADDRESS:
						conditions.add( new StringCondition.Builder()
								.conditionType(conditiontype)
								.field(jsonCondition.getString("field"))
								.operator(Operator.fromValue(jsonCondition.getString("op")))
								.value(jsonCondition.getString("value"))
								.build());
						break;

					case ECOMM_SPENT:
					case IP_GEO_ZIP:
						conditions.add( new IntegerCondition.Builder()
								.conditionType(conditiontype)
								.field(jsonCondition.getString("field"))
								.operator(Operator.fromValue(jsonCondition.getString("op")))
								.value(jsonCondition.getInt("value"))
								.build());
						break;

					case CAMPAIGN_POLL:
					case MEMBER_RATING:
					case ECOMM_NUMBER:
					case FUZZY_SEGMENT:
					case STATIC_SEGMENT:
					case SOCIAL_INFLUENCE:
						conditions.add( new DoubleCondition.Builder()
								.conditionType(conditiontype)
								.field(jsonCondition.getString("field"))
								.operator(Operator.fromValue(jsonCondition.getString("op")))
								.value(jsonCondition.getDouble("value"))
								.build());
						break;

					case DATE:
					case GOAL_TIMESTAMP:
					case ZIP_MERGE:
						conditions.add( new StringCondition.Builder()
								.conditionType(conditiontype)
								.field(jsonCondition.getString("field"))
								.operator(Operator.fromValue(jsonCondition.getString("op")))
								.extra(jsonCondition.getString("extra"))
								.value(jsonCondition.getString("value"))
								.build());
						break;


					case MANDRILL:
					case VIP:
					case ECOMM_PURCHASED:
					case IP_GEO_UNKNOWN:
						conditions.add( new OpCondition.Builder()
								.conditionType(conditiontype)
								.field(jsonCondition.getString("field"))
								.operator(Operator.fromValue(jsonCondition.getString("op")))
								.build());
						break;


					case INTERESTS:
						List<String> values = null;
						if (jsonCondition.has("value")) {
							JSONArray jsonArray = jsonCondition.getJSONArray("value");
							values = new ArrayList<String>(jsonArray.length());
							for (int j=0; j<jsonArray.length(); j++) {
								values.add( jsonArray.getString(j) );
							}
						}
						conditions.add( new StringArrayCondition.Builder()
								.conditionType(conditiontype)
								.field(jsonCondition.getString("field"))
								.operator(Operator.fromValue(jsonCondition.getString("op")))
								.value(values)
								.build());
						break;

					case IP_GEO_IN_ZIP:
						conditions.add( new IntegerCondition.Builder()
								.conditionType(conditiontype)
								.field(jsonCondition.getString("field"))
								.operator(Operator.fromValue(jsonCondition.getString("op")))
								.extra(jsonCondition.getInt("extra"))
								.value(jsonCondition.getInt("value"))
								.build());
						break;

					case IP_GEO_IN:
						conditions.add( new IPGeoInCondition.Builder()
								.conditionType(conditiontype)
								.field(jsonCondition.getString("field"))
								.operator(Operator.fromValue(jsonCondition.getString("op")))
								.lng(jsonCondition.getString("lng"))
								.lat(jsonCondition.getString("lat"))
								.value(jsonCondition.getInt("value"))
								.addr(jsonCondition.getString("addr"))
								.build());
						break;
					}
				}
				catch (Exception e) {
	            	// TODO: log warning for unknown or invalid condition
					// Use raw condition for unknowns.
					conditions.add( new RawCondition.Builder()
							.json(jsonCondition)
							.build());
				}
			}
		} else {
			conditions = null;
		}
	}

	public CampaignSegmentOpts() {

	}

	/**
	 * @return The id for an existing saved segment.
	 */
	public Integer getSavedSegmentId() {
		return savedSegmentId;
	}

	/**
	 * @return The prebuilt segment id, if a prebuilt segment has been designated for this campaign.
	 */
	public Integer getPrebuiltSegmentId() {
		return prebuiltSegmentId;
	}

	/**
	 * @return Segment match type.
	 */
	public MatchType getMatch() {
		return match;
	}

	/**
	 * @return Segment match conditions.
	 */
	public List<AbstractCondition> getConditions() {
		return conditions;
	}

	/**
	 * Helper method to convert JSON for mailchimp PATCH/POST operations
	 */
	public JSONObject getJsonRepresentation(){
		JSONObject segmentOpts = new JSONObject();

		if (getSavedSegmentId() != null) {
			segmentOpts.put("saved_segment_id", getSavedSegmentId());
		}

		if (getPrebuiltSegmentId() != null) {
			segmentOpts.put("prebuilt_segment_id", getPrebuiltSegmentId());
		}

		if (getMatch() != null) {
			segmentOpts.put("match", getMatch().getStringRepresentation());
		}

		if (getConditions() != null) {
			JSONArray jsonConditions = new JSONArray();
			for (AbstractCondition c : getConditions()) {
				jsonConditions.put(c.getJsonRepresentation());
			}
			segmentOpts.put("conditions", jsonConditions);
		}

		return segmentOpts;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(
				"    Segment opts:" + System.lineSeparator() +
				"        Saved Segment ID: " + getSavedSegmentId() + System.lineSeparator() +
				"        Match: " + getMatch().toString());
		if (conditions != null && conditions.size() > 0) {
			sb.append(System.lineSeparator());
			sb.append("        Conditions: ");
			int n = 0;
			for (AbstractCondition condition : conditions) {
				sb.append(System.lineSeparator());
				sb.append("          " + ++n + ": ");
				sb.append(condition.toString());
			}
		}
		return sb.toString();
	}

	public static class Builder {
		private Integer savedSegmentId;
		private Integer prebuiltSegmentId;
		private MatchType match;
		private List<AbstractCondition> conditions;

		public Builder savedSegmentId(Integer savedSegmentId) {
			this.savedSegmentId = savedSegmentId;
			return this;
		}

		public Builder prebuiltSegmentId(Integer prebuiltSegmentId) {
			this.prebuiltSegmentId = prebuiltSegmentId;
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
