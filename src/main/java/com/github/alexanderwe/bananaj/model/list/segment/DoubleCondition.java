package com.github.alexanderwe.bananaj.model.list.segment;

import org.json.JSONObject;

import com.github.alexanderwe.bananaj.exceptions.ConditionException;
import com.github.alexanderwe.bananaj.model.list.segment.StringCondition.Builder;

/**
 * Segment option condition condition_type uses a Number value
 */
public class DoubleCondition implements AbstractCondition {

	private ConditionType condition_type;
    private String field;
    private Operator operator;
    private Double value;

    /**
     * Used when created a Condition locally with the Builder class
     * @see Builder
     * @param b
     */

    public DoubleCondition(Builder b) throws ConditionException {
        if (b.condition_type == null) {
            throw new ConditionException("A condition need a condition_type.");
        } else {
            this.condition_type = b.condition_type;
        }

        if (b.operator == null) {
            throw new ConditionException("A condition need an operator.");
        } else {
            this.operator = b.operator;
        }

        if (b.field == null) {
            throw new ConditionException("A condition need a field to operate on.");
        } else {
            this.field = b.field;
        }

        if (b.value == null) {
            throw new ConditionException("A condition need a value to compare on.");
        } else {
            this.value = b.value;
        }
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

    public Double getValue() {
        return value;
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

    @Override
    public String toString(){
        return "ConditionType: " + getConditionType() + System.lineSeparator() +
        		"Field: " + getField() + System.lineSeparator() +
                "Operator: " + getOp().value() +  System.lineSeparator() +
                "Value: " + getValue() + System.lineSeparator();
    }

    public static class Builder {
    	private ConditionType condition_type;
        private String field;
        private Operator operator;
        private Double value;

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

        public Builder value(Double value) {
            this.value = value;
            return this;
        }

        public DoubleCondition build() {
            try {
                return new DoubleCondition(this);
            } catch (ConditionException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
