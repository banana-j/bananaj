package com.github.bananaj.model.list.segment;

import org.json.JSONObject;

import com.github.bananaj.exceptions.ConditionException;

/**
 * Segment option condition condition_type uses an integer value
 */
public class IntegerCondition implements AbstractCondition {

	private ConditionType condition_type;
	private String field;
	private Operator operator;
	private Integer extra;
	private Integer value;

	/**
	 * Used when created a Condition locally with the Builder class
	 * @see Builder
	 * @param b
	 */

	public IntegerCondition(Builder b) throws ConditionException {
		if (b.condition_type == null) {
			throw new ConditionException("A condition need a condition_type.");
		}

		if (b.operator == null) {
			throw new ConditionException("A condition need an operator.");
		}

		if (b.field == null) {
			throw new ConditionException("A condition need a field to operate on.");
		}

		if (b.value == null) {
			throw new ConditionException("A condition need a value to compare on.");
		}

		this.condition_type = b.condition_type;
		this.operator = b.operator;
		this.field = b.field;
		this.extra = b.extra;
		this.value = b.value;
	}

	public ConditionType getConditionType() {
		return condition_type;
	}

	public String getField() {
		return field;
	}

	public Operator getOp() {
		return operator;
	}

	public Integer getExtra() {
		return extra;
	}

	public Integer getValue() {
		return value;
	}

	@Override
	public JSONObject getJsonRepresentation(){
		JSONObject condition = new JSONObject();
		condition.put("condition_type", getConditionType().toString());
		condition.put("op", getOp().toString());
		condition.put("field", getField());
		if (getExtra() != null) {
			condition.put("extra", getExtra());
		}
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
		private String field;
		private Operator operator;
		private Integer extra;
		private Integer value;

		public Builder conditionType(ConditionType condition_type) {
			this.condition_type = condition_type;
			return this;
		}

		public Builder field(String field) {
			this.field = field;
			return this;
		}

		public Builder operator(Operator op) {
			this.operator = op;
			return this;
		}

		public Builder extra(Integer extra) {
			this.extra = extra;
			return this;
		}

		public Builder value(Integer value) {
			this.value = value;
			return this;
		}

		public IntegerCondition build() throws ConditionException {
			return new IntegerCondition(this);
		}
	}

}
