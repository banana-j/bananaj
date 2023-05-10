package com.github.bananaj.model.list.mergefield;

import java.io.IOException;
import java.net.URL;

import org.json.JSONObject;

import com.github.bananaj.connection.MailChimpConnection;
import com.github.bananaj.model.JSONParser;
import com.github.bananaj.model.list.interests.Interest;

/**
 * Manage merge fields (formerly merge vars) for a specific list.
 * 
 * Created by Alexander on 09.08.2016.
 */
public class MergeField implements JSONParser {

	private MailChimpConnection connection;
	private Integer id;
	private String tag;
	private String name;
	private MergeFieldType type;
	private Boolean required;
	private String defaultValue;
	private Boolean isPublic;
	private Integer displayOrder;
	private MergeFieldOptions options;
	private String helpText;
	private String listId;

	public MergeField() {
		
	}
	
	public MergeField(MailChimpConnection connection, JSONObject jsonObj) {
		parse(connection, jsonObj);
	}

	public MergeField(Builder b) {
		connection = b.connection;
		tag = b.tag;
		name = b.name;
		type = b.type;
		required = b.required;
		defaultValue = b.defaultValue;
		isPublic = b.isPublic;
		displayOrder = b.displayOrder;
		options = b.options;
		helpText = b.helpText;
	}

	public void parse(MailChimpConnection connection, JSONObject jsonObj) {
		this.connection = connection;
		id = jsonObj.getInt("merge_id");
		tag = jsonObj.getString("tag");
		name = jsonObj.getString("name");
		type = MergeFieldType.valueOf(jsonObj.getString("type").toUpperCase());
		required = jsonObj.getBoolean("required");
		defaultValue = jsonObj.getString("default_value");
		isPublic = jsonObj.getBoolean("public");
		displayOrder = jsonObj.getInt("display_order");
		listId = jsonObj.getString("list_id");
		helpText = jsonObj.getString("help_text");

		if (jsonObj.has("options")) {
			JSONObject optionsObj = jsonObj.getJSONObject("options");
			options = new MergeFieldOptions(optionsObj);
		}
	}

	/**
	 * @return An unchanging id for the merge field.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @return The tag used in Mailchimp campaigns and for the /members endpoint.
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * @param tag The tag used in Mailchimp campaigns and for the /members endpoint.
	 *            You must call {@link #update()} for changes to take effect.
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * @return The name of the merge field.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name of the merge field. You must call {@link #update()} for
	 *             changes to take effect.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return The type for the merge field.
	 */
	public MergeFieldType getType() {
		return type;
	}

	/**
	 * @return The boolean value if the merge field is required.
	 */
	public Boolean getRequired() {
		return required;
	}

	/**
	 * @param required The boolean value if the merge field is required. You must
	 *                 call {@link #update()} for changes to take effect.
	 */
	public void setRequired(Boolean required) {
		this.required = required;
	}

	/**
	 * @return The default value for the merge field if null.
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @param defaultValue The default value for the merge field if null. You must
	 *                     call {@link #update()} for changes to take effect.
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * @return Whether the merge field is displayed on the signup form.
	 */
	public Boolean getIsPublic() {
		return isPublic;
	}

	/**
	 * @param isPublic Whether the merge field is displayed on the signup form. You
	 *                 must call {@link #update()} for changes to take effect.
	 */
	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}

	/**
	 * @return A string that identifies this merge field collections' list.
	 */
	public String getListId() {
		return listId;
	}

	/**
	 * @return Extra options for some merge field types.
	 */
	public MergeFieldOptions getOptions() {
		return options;
	}

	/**
	 * @param options Extra options for some merge field types. You must call
	 *                {@link #update()} for changes to take effect.
	 */
	public void setOptions(MergeFieldOptions options) {
		this.options = options;
	}

	/**
	 * @return The order that the merge field displays on the list signup form.
	 */
	public Integer getDisplayOrder() {
		return displayOrder;
	}

	/**
	 * @param displayOrder The order that the merge field displays on the list
	 *                     signup form. You must call {@link #update()} for changes
	 *                     to take effect.
	 */
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	/**
	 * @return Extra text to help the subscriber fill out the form.
	 */
	public String getHelpText() {
		return helpText;
	}

	/**
	 * @param helpText Extra text to help the subscriber fill out the form. You must call
	 *              {@link #update()} for changes to take effect.
	 */
	public void setHelpText(String helpText) {
		this.helpText = helpText;
	}

	/**
	 * Helper method to convert JSON for Mailchimp PATCH/POST operations
	 */
	public JSONObject getJsonRepresentation() {
		JSONObject json = new JSONObject();

		if (tag != null) {
			json.put("tag", tag);
		}
		json.put("name", name);
		if (type != null) {
			json.put("type", type.toString());
		}
		if (required != null) {
			json.put("required", required);
		}
		if (defaultValue != null) {
			json.put("default_value", defaultValue);
		}
		if (isPublic != null) {
			json.put("public", isPublic);
		}
		if (displayOrder != null) {
			json.put("display_order", displayOrder);
		}
		if (options != null) {
			json.put("options", options.getJsonRepresentation());
		}
		if (helpText != null) {
			json.put("help_text", helpText);
		}

		return json;
	}

	/**
	 * Remove this merge field
	 * @throws IOException
	 * @throws Exception
	 */
	public void deleteMergeField() throws IOException, Exception {
		connection.do_Delete(new URL(connection.getListendpoint()+"/"+getListId()+"/merge-fields/"+getId()),connection.getApikey());
	}
	
	/**
	 * Update this merge field via a PATCH operation. Fields will be freshened
	 * from MailChimp.
	 * @throws IOException
	 * @throws Exception 
	 */
	public void update() throws IOException, Exception {
		JSONObject json = getJsonRepresentation();

		String results = connection.do_Patch(new URL(connection.getListendpoint()+"/"+getListId()+"/merge-fields/"+getId()), json.toString(), connection.getApikey());
		parse(connection, new JSONObject(results));  // update this object with current data
	}
	
	@Override
	public String toString(){
		return "Merge Field Id: " + getId() + " " + getName() + System.lineSeparator() +
				"    Tag: " + (tag!=null ? tag : "") + System.lineSeparator() +
				"    Type: " + (type!=null ? type.toString() : "") + System.lineSeparator() +
				"    Required: " + (required!=null ? required : "") + System.lineSeparator() +
				"    Default value: " + (defaultValue!=null ? defaultValue : "") + System.lineSeparator() +
				"    Public: " + (isPublic!=null ? isPublic : "") + System.lineSeparator() +
				"    Display order: " + (displayOrder!=null ? displayOrder : "") +
				(options != null ?  options.toString() : "");
	}

	/**
	 * Builder for {@link Interest}
	 */
	public static class Builder {
		private MailChimpConnection connection;
		private String tag;
		private String name;
		private MergeFieldType type;
		private Boolean required;
		private String defaultValue;
		private Boolean isPublic;
		private Integer displayOrder;
		private MergeFieldOptions options;
		private String helpText;

		/**
		 * @param connection the connection to set
		 */
		public Builder connection(MailChimpConnection connection) {
			this.connection = connection;
			return this;
		}
		/**
		 * @param tag The tag used in Mailchimp campaigns and for the /members endpoint.
		 */
		public Builder tag(String tag) {
			this.tag = tag;
			return this;
		}
		/**
		 * @param name The name of the merge field.
		 */
		public Builder name(String name) {
			this.name = name;
			return this;
		}
		/**
		 * @param type The type for the merge field.
		 */
		public Builder type(MergeFieldType type) {
			this.type = type;
			return this;
		}
		/**
		 * @param required The boolean value if the merge field is required.
		 */
		public Builder required(Boolean required) {
			this.required =required;
			return this;
		}
		/**
		 * @param defaultValue The default value for the merge field if null.
		 */
		public Builder defaultValue(String defaultValue) {
			this.defaultValue = defaultValue;
			return this;
		}
		/**
		 * @param isPublic Whether the merge field is displayed on the signup form.
		 */
		public Builder isPublic(Boolean isPublic) {
			this.isPublic = isPublic;
			return this;
		}
		/**
		 * @param displayOrder The order that the merge field displays on the list signup form.
		 */
		public Builder displayOrder(Integer displayOrder) {
			this.displayOrder = displayOrder;
			return this;
		}
		/**
		 * @param options Extra options for some merge field types.
		 */
		public Builder options(MergeFieldOptions options) {
			this.options = options;
			return this;
		}
		/**
		 * @param helpText Extra text to help the subscriber fill out the form.
		 */
		public Builder helpText(String helpText) {
			this.helpText = helpText;
			return this;
		}

		public MergeField build() {
			return new MergeField(this);
		}
	}
}
