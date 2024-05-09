package com.github.bananaj.model.list.segment;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.github.bananaj.connection.Connection;
import com.github.bananaj.utils.JSONObjectCheck;

/**
 * The conditions of a segment. Static segments (tags) and fuzzy segments don’t have conditions.
 * 
 */
public class SegmentOptions {
	private final static Logger logger = Logger.getLogger(Connection.class);

	private MatchType match;
	private List<AbstractCondition> conditions;

	public SegmentOptions() {

	}

	public SegmentOptions (MatchType matchType, ArrayList<AbstractCondition> conditions) {
		setMatch(matchType);
		setConditions(conditions);
	}

	public SegmentOptions (JSONObject options) {
		JSONObjectCheck jObj = new JSONObjectCheck(options);
		match = jObj.getEnum(MatchType.class, "match");;

		if (jObj.has("conditions")) {
			conditions = new ArrayList<AbstractCondition>();
			JSONArray jsonConditions = jObj.getJSONArray("conditions");
			for (int i = 0; i<jsonConditions.length();i++) {
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
						conditions.add( new StringCondition.Builder()
								.conditionType(conditiontype)
								.field(jsonCondition.getString("field"))
								.operator(jsonCondition.getEnum(Operator.class, "op"))
								.extra(jsonCondition.getString("extra"))
								.value(jsonCondition.getString("value"))
								.build());
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

	public void addCondition(AbstractCondition condition){
		this.conditions.add(condition);
	}

	/**
	 * Match type
	 */
	public MatchType getMatch() {
		return match;
	}

	/**
	 * Match type
	 * @param matchType
	 */
	public void setMatch(MatchType matchType) {
		this.match = matchType;
	}

	/**
	 * A list of segment conditions
	 */
	public List<AbstractCondition> getConditions() {
		return conditions;
	}

	public void setConditions(ArrayList<AbstractCondition> conditions) {
		this.conditions = conditions;
	}

	/**
	 * Helper method to convert JSON for mailchimp PATCH/POST operations
	 */
	public JSONObject getJsonRepresentation() {
		JSONObject options = new JSONObject();
		options.put("match", this.getMatch().toString());

		JSONArray conditions = new JSONArray();

		for(AbstractCondition condition: this.getConditions()) {
			conditions.put(condition.getJsonRepresentation());
		}

		options.put("conditions", conditions);
		return options;
	}

	@Override
	public String toString() {
		return 
				"Options:" + System.lineSeparator() +
				"    Match type: " + getMatch().toString() +  System.lineSeparator() +
				(getConditions() != null ? "    " + getConditions().toString() : "");
	}

}
