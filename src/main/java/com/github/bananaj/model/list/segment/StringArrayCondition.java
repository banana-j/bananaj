package com.github.bananaj.model.list.segment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.bananaj.exceptions.ConditionException;
import com.github.bananaj.utils.JSONObjectCheck;

/**
 * Segment option condition condition_type uses a String array value
 */
public class StringArrayCondition implements AbstractCondition {

	private ConditionType condition_type;
	private String field;
	private Operator op;
	private List<String> value = new ArrayList<String>();

	/**
	 * Used when created a Condition locally with the Builder class
	 * @see Builder
	 * @param b
	 */

	public StringArrayCondition(Builder b) throws ConditionException {
		if (b.condition_type == null) {
			throw new ConditionException("A condition need a condition_type.");
		}

		if (b.operator == null) {
			throw new ConditionException("A condition need an operator.");
		}

		if (b.field == null) {
			throw new ConditionException("A condition need a field to operate on.");
		}

		this.condition_type = b.condition_type;
		this.op = b.operator;
		this.field = b.field;
		this.value = b.value;
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

	public List<String> getValue() {
		return value;
	}

	@Override
	public JSONObject getJsonRepresentation(){
		JSONObjectCheck condition = new JSONObjectCheck();
		JSONArray valuearray = new JSONArray();

		Iterator<String> v = value.iterator();
		while (v.hasNext()) {
			valuearray.put(v.next());
		}

		condition.put("condition_type", getConditionType());
		condition.put("op", getOp());
		condition.put("field", getField());
		condition.put("value", valuearray);

		return condition.getJsonObject();
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
		private List<String> value = new ArrayList<String>();

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

		public Builder value(List<String> value) {
			this.value = value;
			return this;
		}

		public StringArrayCondition build() throws ConditionException {
			return new StringArrayCondition(this);
		}
	}

}
