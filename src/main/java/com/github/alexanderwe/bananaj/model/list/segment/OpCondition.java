package com.github.alexanderwe.bananaj.model.list.segment;

import org.json.JSONObject;

import com.github.alexanderwe.bananaj.exceptions.ConditionException;

/**
 * Segment option condition condition_type That only use operator and field
 */
public class OpCondition implements AbstractCondition {

    private String field;
    private Operator operator;

    /**
     * Used when created a Condition locally with the Builder class
     * @see Builder
     * @param b
     */

    public OpCondition(Builder b) throws ConditionException{
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
    }

    public String getField() {
        return field;
    }

    public Operator getOp() {
        return operator;
    }

    @Override
    public JSONObject getJsonRepresentation(){
        JSONObject condition = new JSONObject();
        condition.put("op", getOp().value());
        condition.put("field", getField());

        return condition;
    }

    @Override
    public String toString(){
        return "Field: " + getField() + System.lineSeparator() +
                "Operator: " + getOp().value() +  System.lineSeparator();
    }

    public static class Builder {
        private String field;
        private Operator operator;

        public Builder field(String field) {
            this.field = field;
            return this;
        }

        public Builder operator(Operator op) {
            this.operator = op;
            return this;
        }

        public OpCondition build() {
            try {
                return new OpCondition(this);
            } catch (ConditionException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
