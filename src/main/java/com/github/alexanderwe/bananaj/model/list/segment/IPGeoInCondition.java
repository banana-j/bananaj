package com.github.alexanderwe.bananaj.model.list.segment;

import org.json.JSONObject;

import com.github.alexanderwe.bananaj.exceptions.ConditionException;

/**
 * Segment option condition condition_type for "IPGeoIn"
 */
public class IPGeoInCondition implements AbstractCondition {

	private ConditionType condition_type;
	private Operator op;
	private String field;
	private String lng;
	private String lat;
	private Integer value;
	private String addr;

	/**
	 * Used when created a Condition locally with the Builder class
	 * @see Builder
	 * @param b
	 */

	public IPGeoInCondition(Builder b) throws ConditionException {
		if (b.condition_type == null) {
			throw new ConditionException("A condition need a condition_type.");
		}

		if (b.op == null) {
			throw new ConditionException("A condition need an operator.");
		}

		if (b.field == null) {
			throw new ConditionException("A condition need a field to operate on.");
		}

		if (b.value == null) {
			throw new ConditionException("A condition need a value to compare on.");
		}

		this.condition_type = b.condition_type;
		this.op = b.op;
		this.field = b.field;
		this.value = b.value;
		this.lng = b.lng;
		this.lat = b.lat;
		this.addr = b.addr;
	}

	public ConditionType getConditionType() {
		return condition_type;
	}

	public String getField() {
		return field;
	}

	public Operator getOp() {
		return op;
	}

	public String getLng() {
		return lng;
	}

	public Integer getValue() {
		return value;
	}

	public String getLat() {
		return lat;
	}

	public String getAddr() {
		return addr;
	}

	@Override
	public JSONObject getJsonRepresentation(){
		JSONObject condition = new JSONObject();
		condition.put("condition_type", getConditionType());
		condition.put("op", getOp().value());
		condition.put("field", getField());
		condition.put("value", getValue());

		return condition;
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
		private Operator op;
		private String field;
		private String lng;
		private String lat;
		private Integer value;
		private String addr;

		public Builder conditionType(ConditionType condition_type) {
			this.condition_type = condition_type;
			return this;
		}

		public Builder field(String field) {
			this.field = field;
			return this;
		}

		public Builder operator(Operator op) {
			this.op = op;
			return this;
		}

		public Builder lng(String lng) {
			this.lng = lng;
			return this;
		}

		public Builder lat(String lat) {
			this.lat = lat;
			return this;
		}

		public Builder value(Integer value) {
			this.value = value;
			return this;
		}

		public Builder addr(String addr) {
			this.addr = addr;
			return this;
		}

		public IPGeoInCondition build() throws ConditionException {
			return new IPGeoInCondition(this);
		}
	}

}
