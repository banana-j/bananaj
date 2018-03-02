package com.github.alexanderwe.bananaj.model.list.segment;

import org.json.JSONObject;

import com.github.alexanderwe.bananaj.exceptions.ConditionException;

/**
 * Segment option condition condition_type uses an integer value
 */
public class IntegerCondition implements AbstractCondition {

    private String field;
    private Operator operator;
    private Integer extra;
    private Integer value;

    /**
     * Used when created a Condition locally with the Builder class
     * @see Builder
     * @param b
     */

    public IntegerCondition(Builder b) throws ConditionException{
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

        this.extra = b.extra;
        
        if (b.value == null) {
            throw new ConditionException("A condition need a value to compare on.");
        } else {
            this.value = b.value;
        }
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
        condition.put("op", getOp().value());
        condition.put("field", getField());
        condition.put("value", getValue());

        return condition;
    }

    @Override
    public String toString(){
        return "Field: " + getField() + System.lineSeparator() +
                "Operator: " + getOp().value() +  System.lineSeparator() +
                getExtra() != null ? "Extra: " + getExtra() + System.lineSeparator() : "" +
                "Value: " + getValue() + System.lineSeparator();
    }

    public static class Builder {
        private String field;
        private Operator operator;
        private Integer extra;
        private Integer value;

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

        public IntegerCondition build() {
            try {
                return new IntegerCondition(this);
            } catch (ConditionException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
