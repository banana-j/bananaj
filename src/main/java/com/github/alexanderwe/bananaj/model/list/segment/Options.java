package com.github.alexanderwe.bananaj.model.list.segment;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.github.alexanderwe.bananaj.connection.Connection;

/**
 * The conditions of a segment. Static segments (tags) and fuzzy segments don’t have conditions.
 * 
 * Created by alexanderweiss on 04.02.16.
 */
public class Options {
	private final static Logger logger = Logger.getLogger(Connection.class);

	private MatchType match;
	private List<AbstractCondition> conditions;

	public Options() {

	}

	public Options (MatchType matchType, ArrayList<AbstractCondition> conditions) {
		setMatch(matchType);
		setConditions(conditions);
	}

	public Options (JSONObject jsonObj) {
		match = MatchType.valueOf(jsonObj.getString("match").toUpperCase());

		if (jsonObj.has("conditions")) {
			conditions = new ArrayList<AbstractCondition>();
			JSONArray jsonConditions = jsonObj.getJSONArray("conditions");
			for (int i = 0; i<jsonConditions.length();i++) {
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
	            	// log warning for unknown or invalid condition
					logger.warn("Unknown or invalid condition : " + e.getMessage(), e);
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
		options.put("match", this.getMatch().getStringRepresentation());

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
				"    Match type: " + getMatch().getStringRepresentation() +  System.lineSeparator() +
				(getConditions() != null ? "    " + getConditions().toString() : "");
	}

}
