package com.github.bananaj.model.list.interests;

import java.io.IOException;
import java.net.URL;

import org.json.JSONObject;

import com.github.bananaj.connection.MailChimpConnection;
import com.github.bananaj.model.JSONParser;
import com.github.bananaj.model.ModelIterator;

/**
 * Manage interest categories for a specific list. Interest categories organize
 * interests, which are used to group subscribers based on their preferences.
 * These correspond to 'group titles' in the Mailchimp application.
 * 
 */
public class InterestCategory implements JSONParser {
	private MailChimpConnection connection;
	private String id;
    private String listId;
    private String title;
    private Integer displayOrder;
    private InterestCategoryType type;

	public InterestCategory() {

	}

	public InterestCategory(MailChimpConnection connection, JSONObject jsonObj) {
		parse(connection, jsonObj);
	}

    /**
     * Used when created a Segment locally with the Builder class
     * @see Builder
     * @param b
     */
    public InterestCategory(Builder b) {
		this.id = b.id;
		this.listId = b.listId;
		this.title = b.title;
		this.displayOrder = b.displayOrder;
		this.type = b.type;
		this.connection = b.connection;
    }
    
    /**
	 * Parse a JSON representation of interest category into this.
	 * @param connection 
	 * @param jsonObj
     */
	public void parse(MailChimpConnection connection, JSONObject jsonObj) {
		id = jsonObj.getString("id");
		listId = jsonObj.getString("list_id");
		title = jsonObj.getString("title");
		displayOrder = jsonObj.getInt("display_order");
		type = InterestCategoryType.valueOf(jsonObj.getString("type").toUpperCase());
		this.connection = connection;
	}
	
	/**
	 * @return The id for the interest category.
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @return The unique list id for the category.
	 */
    public String getListId() {
		return listId;
	}

	/**
	 * @return The text description of this category. This field appears on signup forms and is often phrased as a question.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title The text description of this category. This field appears on
	 *              signup forms and is often phrased as a question. You must call
	 *              {@link #update()} for changes to take effect.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return The order that the categories are displayed in the list. Lower numbers display first.
	 */
	public Integer getDisplayOrder() {
		return displayOrder;
	}

	/**
	 * @param displayOrder The order that the categories are displayed in the list.
	 *                     Lower numbers display first. You must call
	 *                     {@link #update()} for changes to take effect.
	 */
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	/**
	 * @return Determines how this category’s interests appear on signup forms.
	 */
	public InterestCategoryType getType() {
		return type;
	}

	/**
	 * @param type Determines how this category’s interests appear on signup forms.
	 *             You must call {@link #update()} for changes to take effect.
	 */
	public void setType(InterestCategoryType type) {
		this.type = type;
	}

	/**
	 * Helper method to convert JSON for mailchimp PATCH/POST operations
	 */
	public JSONObject getJsonRepresentation() {
		JSONObject json = new JSONObject();

		json.put("title", getTitle());
		json.put("type", getType().toString());
		if (getDisplayOrder() != null) {
			json.put("display_order", getDisplayOrder());
		}
		return json;
	}
	
	/**
	 * Remove this interest category
	 * @throws IOException
	 * @throws Exception 
	 */
	public void delete() throws IOException, Exception {
		connection.do_Delete(new URL(connection.getListendpoint()+"/"+getListId()+"/interest-categories/"+getId()), connection.getApikey());
	}
	
	/**
	 * Update this interest category via a PATCH operation. Fields will be freshened
	 * from MailChimp.
	 * @throws IOException
	 * @throws Exception 
	 */
	public void update() throws IOException, Exception {
		JSONObject json = getJsonRepresentation();

		String results = connection.do_Patch(new URL(connection.getListendpoint()+"/"+getListId()+"/interest-categories/"+getId()), json.toString(), connection.getApikey());
		parse(connection, new JSONObject(results));  // update this object with current data
	}
	
	/**
	 * Get a list of this category's interests. Interests are referred to as ‘group names’ in
	 * the MailChimp application.
	 * 
	 * @param pageSize Number of records to fetch per query. Maximum value is 1000.
	 * @param pageNumber First page number to fetch starting from 0.
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<Interest> getInterests(int pageSize, int pageNumber) throws IOException, Exception {
		final String baseURL = connection.getListendpoint() + "/" + getListId() + "/interest-categories/"
				+ getId() + "/interests";
		return new ModelIterator<Interest>(Interest.class, baseURL, connection, pageSize, pageNumber);
	}

	/**
	 * Get a list of this category's interests. Interests are referred to
	 * as ‘group names’ in the MailChimp application.
	 * 
	 * @return Interests iterator
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<Interest> getInterests() throws IOException, Exception {
		final String baseURL = connection.getListendpoint() + "/" + getListId() + "/interest-categories/"
				+ getId() + "/interests";
		return new ModelIterator<Interest>(Interest.class, baseURL, connection);
	}

	/**
	 * Add an interest to this category.
	 * @param interest
	 * @throws IOException
	 * @throws Exception 
	 */
	public Interest addInterest(Interest interest) throws IOException, Exception {
		JSONObject json = interest.getJsonRepresentation();
		String results = connection.do_Post(new URL(connection.getListendpoint()+"/"+getListId()+"/interest-categories/"+getId()+"/interests"), json.toString(), connection.getApikey());
		return new Interest(connection, new JSONObject(results));
	}
	
	/**
	 * Remove an interest from this category.
	 * @param interestId
	 * @throws IOException
	 * @throws Exception 
	 */
	public void deleteInrest(String interestId) throws IOException, Exception {
		connection.do_Delete(new URL(connection.getListendpoint()+"/"+getListId()+"/interest-categories/"+getId()+"/interests/"+interestId), connection.getApikey());
	}
	
	@Override
    public String toString(){
        return  
				"Interest Category:" + System.lineSeparator() +
        		"    ID: " + getId() +  System.lineSeparator() +
                "    Title: " + getTitle() +  System.lineSeparator() +
                "    Type: " + getType() + System.lineSeparator() +
                "    List ID: " + getListId() + System.lineSeparator() +
                "    Display Order: " +  getDisplayOrder();
    }
	
	public static class Builder {
		private MailChimpConnection connection;
	    private String id;
	    private String listId;
	    private String title;
	    private Integer displayOrder;
	    private InterestCategoryType type;
		
		public Builder connection(MailChimpConnection connection) {
			this.connection = connection;
			return this;
		}

        public Builder listId(String s) {
            this.listId = s;
            return this;
        }

        public Builder id(String s) {
            this.id = s;
            return this;
        }

        public Builder title(String s) {
            this.title = s;
            return this;
        }
        
        public Builder displayOrder(int i) {
            this.displayOrder = new Integer(i);
            return this;
        }
        
        public Builder type(InterestCategoryType type) {
            this.type = type;
            return this;
        }

        public Builder type(String type) {
            this.type = InterestCategoryType.valueOf(type.toUpperCase());
            return this;
        }
        
        public InterestCategory build() {
            return new InterestCategory(this);
        }
	}
}
