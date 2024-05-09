package com.github.bananaj.model.list.mergefield;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.bananaj.model.list.interests.Interest;
import com.github.bananaj.utils.JSONObjectCheck;

/**
 *  Extra options for some merge field types.
 *  
 */
public class MergeFieldOptions {

	private Integer defaultCountry;
	private String phoneFormat;
	private String dateFormat;
	private List<String> choices;
	private Integer size;

	/**
	 * Construct class given a Mailchimp JSON object
	 * 
	 * @param jsonObj
	 */
	public MergeFieldOptions(JSONObject options) {
		JSONObjectCheck jObj = new JSONObjectCheck(options);
		defaultCountry = jObj.getInt("default_country");
		phoneFormat = jObj.getString("phone_format");
		dateFormat = jObj.getString("date_format");
		size = jObj.getInt("size");
		if (jObj.has("choices")) {
			JSONArray choicesObj = jObj.getJSONArray("choices");
			choices = new ArrayList<String>(choicesObj.length());
			for (int i = 0; i < choicesObj.length(); i++) {
				choices.add((String )choicesObj.get(i));
			}
		}
	}

	/**
	 * 
	 * @param default_country In an address field, the default country code if none supplied.
	 * @param phone_format In a phone field, the phone number type: US or International.
	 * @param date_format In a date or birthday field, the format of the date.
	 * @param choices In a radio or dropdown non-group field, the available options for members to pick from.
	 * @param size In a text field, the default length of the text field.
	 */
	public MergeFieldOptions(int default_country, String phone_format, String date_format, List<String> choices, int size){
		this.defaultCountry = default_country;
		this.phoneFormat = phone_format;
		this.dateFormat = date_format;
		this.choices = choices;
		this.size = size;
	}

	public MergeFieldOptions(Builder b) {
		defaultCountry = b.defaultCountry;
		phoneFormat = b.phoneFormat;
		dateFormat = b.dateFormat;
		choices = b.choices;
		size = b.size;
	}

	/**
	 * @return In an address field, the default country code if none supplied.
	 */
	public int getDefaultCountry() {
		return defaultCountry;
	}

	/**
	 * @return In a phone field, the phone number type: US or International.
	 */
	public String getPhoneFormat() {
		return phoneFormat;
	}

	/**
	 * @return In a date or birthday field, the format of the date.
	 */
	public String getDateFormat() {
		return dateFormat;
	}

	/**
	 * @return In a radio or dropdown non-group field, the available options for members to pick from.
	 */
	public List<String> getChoices() {
		return choices;
	}

	/**
	 * @return In a text field, the default length of the text field.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Helper method to convert JSON for mailchimp PATCH/POST operations
	 */
	public JSONObject getJsonRepresentation() {
		JSONObjectCheck json = new JSONObjectCheck();

		json.put("default_country", defaultCountry);
		json.put("phone_format", phoneFormat);
		json.put("date_format", dateFormat);
		if (choices != null && choices.size() > 0) {
			JSONArray choiceArry = new JSONArray();
			for(String choice: choices) {
				choiceArry.put(choice);
			}
			json.put("choices", choiceArry);
		}
		json.put("size", size);

		return json.getJsonObject();
	}
	
	@Override
	public String toString() {
		return (defaultCountry != null ? System.lineSeparator() + "    Default country: " + defaultCountry : "") +
				(phoneFormat != null ? System.lineSeparator() + "    Phone format: " + phoneFormat : "") +
				(dateFormat != null ? System.lineSeparator() + "    Date format: " + dateFormat : "") +
				(choices != null ? System.lineSeparator() + "    Choices: " + choices : "") +
				(size != null ? System.lineSeparator() + "    Size: " + size : "");
	}

	/**
	 * Builder for {@link Interest}
	 */
	public static class Builder {
		private Integer defaultCountry;
		private String phoneFormat;
		private String dateFormat;
		private List<String> choices;
		private Integer size;
		/**
		 * @param defaultCountry In an address field, the default country code if none supplied.
		 */
		public Builder defaultCountry(Integer defaultCountry) {
			this.defaultCountry = defaultCountry;
			return this;
		}
		/**
		 * @param phoneFormat In a phone field, the phone number type: US or International.
		 */
		public Builder phoneFormat(String phoneFormat) {
			this.phoneFormat = phoneFormat;
			return this;
		}
		/**
		 * @param dateFormat In a date or birthday field, the format of the date.
		 */
		public Builder dateFormat(String dateFormat) {
			this.dateFormat = dateFormat;
			return this;
		}
		/**
		 * @param choices In a radio or dropdown non-group field, the available options for members to pick from.
		 */
		public Builder choices(List<String> choices) {
			this.choices = choices;
			return this;
		}
		/**
		 * @param size In a text field, the default length of the text field.
		 */
		public Builder size(Integer size) {
			this.size = size;
			return this;
		}

		public MergeFieldOptions build() {
			return new MergeFieldOptions(this);
		}
	}
}
