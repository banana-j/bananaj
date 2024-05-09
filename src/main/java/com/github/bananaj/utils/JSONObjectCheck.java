package com.github.bananaj.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.ZonedDateTime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * JSONObject Wrapper with checked getters to return null when JSONObject does not have key. 
 *
 */
public class JSONObjectCheck {

	JSONObject jsonObj;
	
	public JSONObjectCheck() {
		this.jsonObj = new JSONObject();
	}
	
	public JSONObjectCheck(JSONObject jsonObj) {
		this.jsonObj = jsonObj;
	}

	public boolean has(String key) {
		return jsonObj.has(key);
	}

	public JSONObject getJsonObject() {
		return jsonObj;
	}
	
	public ZonedDateTime getISO8601Date(String key) {
		if (!jsonObj.has(key)) return null;
		return DateConverter.fromISO8601(jsonObj.getString(key));
	}
	
	public Object get(String key) throws JSONException {
		if (!jsonObj.has(key)) return null;
		return jsonObj.get(key);
	}

	public <E extends Enum<E>> E getEnum(Class<E> clazz, String key) throws JSONException {
		if (!jsonObj.has(key)) return null;
		return Enum.valueOf(clazz, jsonObj.getString(key).toUpperCase());
	}

	public Boolean getBoolean(String key) throws JSONException {
		if (!jsonObj.has(key)) return null;
		return jsonObj.getBoolean(key);
	}

	public BigInteger getBigInteger(String key) throws JSONException {
		if (!jsonObj.has(key)) return null;
		return jsonObj.getBigInteger(key);
	}

	public BigDecimal getBigDecimal(String key) throws JSONException {
		if (!jsonObj.has(key)) return null;
		return jsonObj.getBigDecimal(key);
	}

	public Double getDouble(String key) throws JSONException {
		if (!jsonObj.has(key)) return null;
		return jsonObj.getDouble(key);
	}

	public Float getFloat(String key) throws JSONException {
		if (!jsonObj.has(key)) return null;
		return jsonObj.getFloat(key);
	}

	public Number getNumber(String key) throws JSONException {
		if (!jsonObj.has(key)) return null;
		return jsonObj.getNumber(key);
	}

	public Integer getInt(String key) throws JSONException {
		if (!jsonObj.has(key)) return null;
		return jsonObj.getInt(key);
	}

	public JSONArray getJSONArray(String key) throws JSONException {
		if (!jsonObj.has(key)) return null;
		return jsonObj.getJSONArray(key);
	}

	public JSONObject getJSONObject(String key) throws JSONException {
		if (!jsonObj.has(key)) return null;
		return jsonObj.getJSONObject(key);
	}

	public Long getLong(String key) throws JSONException {
		if (!jsonObj.has(key)) return null;
		return jsonObj.getLong(key);
	}

	public String getString(String key) throws JSONException {
		return jsonObj.optString(key,null);
	}

	public JSONObjectCheck put(String key, String value) {
		jsonObj.put(key, value);
		return this;
	}

	public JSONObjectCheck put(String key, Boolean value) {
		if (value != null) {
			jsonObj.put(key, value.booleanValue());
		} else {
			jsonObj.remove(key);
		}
		return this;
	}
	
	public JSONObjectCheck put(String key, Double value) {
		if (value != null) {
			jsonObj.put(key, value.doubleValue());
		} else {
			jsonObj.remove(key);
		}
		return this;
	}

	public JSONObjectCheck put(String key, Integer value) {
		if (value != null) {
			jsonObj.put(key, value.intValue());
		} else {
			jsonObj.remove(key);
		}
		return this;
	}
	public JSONObjectCheck put(String key, Float value) {
		if (value != null) {
			jsonObj.put(key, value.floatValue());
		} else {
			jsonObj.remove(key);
		}
		return this;
	}

	public JSONObjectCheck put(String key, Object value) {
		if (value != null) {
			jsonObj.put(key, value);
		} else {
			jsonObj.remove(key);
		}
		return this;
	}

	public JSONObjectCheck put(String key, Enum value) {
		if (value != null) {
			jsonObj.put(key, value.toString());
		} else {
			jsonObj.remove(key);
		}
		return this;
	}

}
