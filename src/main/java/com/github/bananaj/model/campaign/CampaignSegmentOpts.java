package com.github.bananaj.model.campaign;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.github.bananaj.connection.Connection;
import com.github.bananaj.model.list.segment.AbstractCondition;
import com.github.bananaj.model.list.segment.ConditionType;
import com.github.bananaj.model.list.segment.DoubleCondition;
import com.github.bananaj.model.list.segment.IPGeoInCondition;
import com.github.bananaj.model.list.segment.IntegerCondition;
import com.github.bananaj.model.list.segment.MatchType;
import com.github.bananaj.model.list.segment.OpCondition;
import com.github.bananaj.model.list.segment.Operator;
import com.github.bananaj.model.list.segment.RawCondition;
import com.github.bananaj.model.list.segment.StringArrayCondition;
import com.github.bananaj.model.list.segment.StringCondition;
import com.github.bananaj.utils.JSONObjectCheck;

/**
 * A class representing all campaign segmentation options. This object should
 * contain a savedSegmentId to use an existing segment, or you can create a new
 * segment by including both match and conditions options.
 */
public class CampaignSegmentOpts {
	private final static Logger logger = Logger.getLogger(Connection.class);

	private Integer savedSegmentId;
	private Integer prebuiltSegmentId;
	private MatchType match;
	private List<AbstractCondition> conditions;

	/**
	 * Used when created a Condition locally with the Builder class
	 * @see Builder
	 * @param b
	 */

	public CampaignSegmentOpts(Builder b) {
		this.savedSegmentId = b.savedSegmentId;
		this.prebuiltSegmentId = b.prebuiltSegmentId;
		this.match = b.match;
		this.conditions = b.conditions;
	}

	public CampaignSegmentOpts(JSONObject options) {
		JSONObjectCheck jObj = new JSONObjectCheck(options);
		savedSegmentId = jObj.getInt("saved_segment_id");
		prebuiltSegmentId = jObj.getInt("prebuilt_segment_id");
		match = jObj.getEnum(MatchType.class, "match");

		if (jObj.has("conditions"))
		{
			conditions = new ArrayList<AbstractCondition>();
			JSONArray jsonConditions = jObj.getJSONArray("conditions");
			for (int i = 0; i<jsonConditions.length();i++){
				JSONObjectCheck jsonCondition = new JSONObjectCheck(jsonConditions.getJSONObject(i));

				try {
					ConditionType conditiontype = jsonCondition.getEnum(ConditionType.class, "condition_type");
					switch(conditiontype) {
					case AIM:
					case AUTOMATION:
					case CONVERSATION:
					case EMAILCLIENT:
					case LANGUAGE:
					case SIGNUPSOURCE:
					case SURVEYMONKEY:
					case ECOMMCATEGORY:
					case ECOMMSTORE:
					case GOALACTIVITY:
					case IPGEOCOUNTRYSTATE:
					case SOCIALAGE:
					case SOCIALGENDER:
					case SOCIALNETWORKMEMBER:
					case SOCIALNETWORKFOLLOW:
					case ADDRESSMERGE:
					case BIRTHDAYMERGE:
					case DATEMERGE:
					case TEXTMERGE:
					case SELECTMERGE:
					case EMAILADDRESS:
					case NEWSUBSCRIBERS:
					case PREDICTEDAGE:
					case PREDICTEDGENDER:
						conditions.add( new StringCondition.Builder()
								.conditionType(conditiontype)
								.field(jsonCondition.getString("field"))
								.operator(jsonCondition.getEnum(Operator.class, "op"))
								.value(jsonCondition.getString("value"))
								.build());
						break;

					case ECOMMSPENT:
					case IPGEOZIP:
						conditions.add( new IntegerCondition.Builder()
								.conditionType(conditiontype)
								.field(jsonCondition.getString("field"))
								.operator(jsonCondition.getEnum(Operator.class, "op"))
								.value(jsonCondition.getInt("value"))
								.build());
						break;

					case CAMPAIGNPOLL:
					case MEMBERRATING:
					case ECOMMNUMBER:
					case FUZZYSEGMENT:
					case STATICSEGMENT:
					case SOCIALINFLUENCE:
						conditions.add( new DoubleCondition.Builder()
								.conditionType(conditiontype)
								.field(jsonCondition.getString("field"))
								.operator(jsonCondition.getEnum(Operator.class, "op"))
								.value(jsonCondition.getDouble("value"))
								.build());
						break;

					case DATE:
					case GOALTIMESTAMP:
					case ZIPMERGE:
						StringCondition.Builder b = new StringCondition.Builder()
								.conditionType(conditiontype)
								.field(jsonCondition.getString("field"))
								.operator(jsonCondition.getEnum(Operator.class, "op"))
								.extra(jsonCondition.getString("extra"))
								.value(jsonCondition.getString("value"));
						conditions.add(b.build());
						break;


					case MANDRILL:
					case VIP:
					case ECOMMPURCHASED:
					case IPGEOUNKNOWN:
						conditions.add( new OpCondition.Builder()
								.conditionType(conditiontype)
								.field(jsonCondition.getString("field"))
								.operator(jsonCondition.getEnum(Operator.class, "op"))
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
								.operator(jsonCondition.getEnum(Operator.class, "op"))
								.value(values)
								.build());
						break;

					case IPGEOINZIP:
						conditions.add( new IntegerCondition.Builder()
								.conditionType(conditiontype)
								.field(jsonCondition.getString("field"))
								.operator(jsonCondition.getEnum(Operator.class, "op"))
								.extra(jsonCondition.getInt("extra"))
								.value(jsonCondition.getInt("value"))
								.build());
						break;

					case IPGEOIN:
						conditions.add( new IPGeoInCondition.Builder()
								.conditionType(conditiontype)
								.field(jsonCondition.getString("field"))
								.operator(jsonCondition.getEnum(Operator.class, "op"))
								.lng(jsonCondition.getString("lng"))
								.lat(jsonCondition.getString("lat"))
								.value(jsonCondition.getInt("value"))
								.addr(jsonCondition.getString("addr"))
								.build());
						break;
					}
				}
				catch (Exception e) {
	            	// log warning for unknown or invalid condition
					logger.warn("Unknown or invalid condition : " + e.getMessage(), e);
					// Use raw condition for unknowns.
					conditions.add( new RawCondition.Builder()
							.json(jsonCondition.getJsonObject())
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
	protected JSONObject getJsonRepresentation(){
		JSONObject segmentOpts = new JSONObject();

		if (getSavedSegmentId() != null) {
			segmentOpts.put("saved_segment_id", getSavedSegmentId());
		}

		if (getPrebuiltSegmentId() != null) {
			segmentOpts.put("prebuilt_segment_id", getPrebuiltSegmentId());
		}

		if (getMatch() != null) {
			segmentOpts.put("match", getMatch().toString());
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
				"        Saved Segment ID: " + getSavedSegmentId() + System.lineSeparator());
		sb.append(getMatch() != null ? "        Match: " + getMatch().toString() : "");
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
			return new CampaignSegmentOpts(this);
		}
	}

}
