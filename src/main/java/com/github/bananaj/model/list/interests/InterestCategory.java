package com.github.bananaj.model.list.interests;

import java.io.IOException;
import java.net.URL;

import org.json.JSONObject;

import com.github.bananaj.connection.MailChimpConnection;
import com.github.bananaj.connection.MailChimpQueryParameters;
import com.github.bananaj.model.JSONParser;
import com.github.bananaj.model.ModelIterator;
import com.github.bananaj.utils.JSONObjectCheck;

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
    public void parse(MailChimpConnection connection, JSONObject interestcategory) {
    	JSONObjectCheck jObj = new JSONObjectCheck(interestcategory);
    	this.connection = connection;
    	id = jObj.getString("id");
    	listId = jObj.getString("list_id");
    	title = jObj.getString("title");
    	displayOrder = jObj.getInt("display_order");
    	type = jObj.getEnum(InterestCategoryType.class, "type");
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
		JSONObjectCheck json = new JSONObjectCheck();

		json.put("title", getTitle());
		json.put("type", getType());
		json.put("display_order", getDisplayOrder());
		
		return json.getJsonObject();
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
	 * Get a list of this category's interests. Interest categories organize
	 * interests, which are used to group subscribers based on their preferences.
	 * These correspond to 'group titles' in the Mailchimp application.
	 * 
	 * @param queryParameters Optional query parameters to send to the MailChimp
	 *                        API.
	 * @see <a href="https://mailchimp.com/developer/marketing/api/interest-categories/list-interest-categories/" target="MailchimpAPIDoc">Lists/Audiences Interest Categories -- GET /lists/{list_id}/interest-categories</a>
	 * @throws IOException
	 * @throws Exception
	 */
	public Iterable<Interest> getInterests(MailChimpQueryParameters queryParameters) throws IOException, Exception {
		final String baseURL = connection.getListendpoint() + "/" + getListId() + "/interest-categories/"
				+ getId() + "/interests";
		return new ModelIterator<Interest>(Interest.class, baseURL, connection, queryParameters);
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
