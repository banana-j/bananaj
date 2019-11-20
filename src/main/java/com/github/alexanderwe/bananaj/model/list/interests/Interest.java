package com.github.alexanderwe.bananaj.model.list.interests;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.json.JSONObject;

import com.github.alexanderwe.bananaj.connection.MailChimpConnection;
import com.github.alexanderwe.bananaj.exceptions.TransportException;

/**
 * Manage interests for a specific Mailchimp audience list. Assign subscribers
 * to interests to group them together. Interests are referred to as ‘group
 * names’ in the Mailchimp application.
 *
 */
public class Interest {
	private MailChimpConnection connection;
	private String categoryId;
    private String listId;
    private String id;
    private String name;
    private int subscriberCount;
    private Integer displayOrder;

    /**
	 * Construct class given a Mailchimp JSON object
     * 
     * @param jsonObj
     */
	public Interest(MailChimpConnection connection, JSONObject jsonObj) {
		parse(connection, jsonObj);
	}
	
    /**
     *  {@link Interest.Builder} model for local construction
     * @see Interest.Builder
     * @param b
     */
    public Interest(Builder b) {
		this.categoryId = b.categoryId;
		this.listId = b.listId;
		this.id = b.id;
		this.name = b.name;
		this.subscriberCount = b.subscriberCount;
		this.displayOrder = b.displayOrder;
		this.connection = b.connection;
    }
    
	public void parse(MailChimpConnection connection, JSONObject jsonObj) {
		categoryId = jsonObj.getString("category_id");
		listId = jsonObj.getString("list_id");
        id = jsonObj.getString("id");
        name = jsonObj.getString("name");
        subscriberCount = Integer.parseInt(jsonObj.getString("subscriber_count")); // Mailchimp should report an int, not a string
        displayOrder = jsonObj.getInt("display_order");
        this.connection = connection;
	}

	/**
     * @return The id for the interest category.
     */
	public String getCategoryId() {
		return categoryId;
	}

	/**
	 * @return The ID for the list that this interest belongs to.
	 */
	public String getListId() {
		return listId;
	}

	/**
	 * @return The ID for the interest.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return The name of the interest. This can be shown publicly on a subscription form.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name of the interest. This can be shown publicly on a
	 *             subscription form. You must call {@link #update()} for changes to
	 *             take effect.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return The number of subscribers associated with this interest.
	 */
	public int getSubscriberCount() {
		return subscriberCount;
	}

	/**
	 * @return The display order for interests.
	 */
	public Integer getDisplayOrder() {
		return displayOrder;
	}

	/**
	 * @param displayOrder The display order for interests. You must call
	 *                     {@link #update()} for changes to take effect.
	 */
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	/**
	 * Helper method to convert JSON for mailchimp PATCH/POST operations
	 */
	protected JSONObject getJsonRepresentation() {
		JSONObject json = new JSONObject();

		json.put("name", getName());
		if (getDisplayOrder() != null) {
			json.put("display_order", getDisplayOrder());
		}
		return json;
	}
	
	/**
	 * Remove this interest
	 * @throws URISyntaxException 
	 * @throws TransportException 
	 * @throws MalformedURLException 
	 */
	public void delete() throws MalformedURLException, TransportException, URISyntaxException {
		connection.do_Delete(new URL(connection.getListendpoint()+"/"+getListId()+"/interest-categories/"+getCategoryId()+"/interests/"+getId()), connection.getApikey());
	}
	
	/**
	 * Update this interest via a PATCH operation. Fields will be freshened
	 * from MailChimp.
	 * @throws URISyntaxException 
	 * @throws TransportException 
	 * @throws MalformedURLException 
	 */
	public void update() throws MalformedURLException, TransportException, URISyntaxException {
		JSONObject json = getJsonRepresentation();

		String results = connection.do_Patch(new URL(connection.getListendpoint()+"/"+getListId()+"/interest-categories/"+getCategoryId()+"/interests/"+getId()), json.toString(),connection.getApikey());
		parse(connection, new JSONObject(results));  // update this object with current data
	}
	
	@Override
    public String toString(){
        return  
				"Interest:" + System.lineSeparator() +
        		"    ID: " + this.getId() +  System.lineSeparator() +
                "    List ID: " + this.getListId() + System.lineSeparator() +
                "    Category Id: " + this.getCategoryId() + System.lineSeparator() +
                "    Name: " + this.getName() +  System.lineSeparator() +
                "    Subscriber Count: " + this.getSubscriberCount() + System.lineSeparator() +
                "    Display Order: " +  this.getDisplayOrder();
    }
	
	/**
	 * Builder for {@link Interest}
	 */
	public static class Builder {
		private MailChimpConnection connection;
		private String categoryId;
	    private String listId;
	    private String id;
	    private String name;
	    private int subscriberCount;
	    private Integer displayOrder;
		
        public Builder connection(MailChimpConnection connection) {
            this.connection = connection;
            return this;
        }
        
        public Builder categoryId(String s) {
            this.categoryId = s;
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

        public Builder name(String s) {
            this.name = s;
            return this;
        }
        
        public Builder subscriberCount(int s) {
            this.subscriberCount = s;
            return this;
        }
        
        public Builder displayOrder(int i) {
            this.displayOrder = new Integer(i);
            return this;
        }

        public Interest build() {
            return new Interest(this);
        }
	}
}
