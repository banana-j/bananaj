package com.github.bananaj.model;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.bananaj.connection.MailChimpConnection;
import com.github.bananaj.connection.MailChimpQueryParameters;

/**
 * Iterator class wrapper for MailChimp AIP. This class wraps MailChimp 
 * paginated APIs in a unified Iterator interface internally handling
 * all the pagination mechanics.
 * 
 * @param <T>
 */
public class ModelIterator<T extends JSONParser> implements Iterable<T> {
	
	protected MailChimpConnection connection;
	protected Queue<T> q = new LinkedList<T>();
	private Class<T> typeClasse;
	protected Integer totalItems;
	private long currentIndex = 0;
	private MailChimpQueryParameters queryParams;
	
	/**
	 * 
	 * @param typeClasse
	 * @param query
	 * @param connection
	 */
	public ModelIterator(Class<T> typeClasse, String query, MailChimpConnection connection) {
		this.typeClasse = typeClasse;
		this.connection = connection;
		queryParams = new MailChimpQueryParameters(query)
				.count(100)
				.offset(0);
		if (query != null) {
			readPagedEntities();
		}
	}

	/**
	 * Create iterator with a specified fetch size
	 * @param typeClasse
	 * @param query
	 * @param connection
	 * @param count Number of records to fetch per query. Maximum value is 1000.
	 */
	public ModelIterator(Class<T> typeClasse, String query, MailChimpConnection connection, int count) {
		this.typeClasse = typeClasse;
		this.connection = connection;
		queryParams = new MailChimpQueryParameters(query)
				.count(count)
				.offset(0);
		readPagedEntities();
	}

	/**
	 * Create iterator with a specified fetch size and starting offset.
	 * @param typeClasse
	 * @param query
	 * @param connection
	 * @param count Number of records to fetch per query. Maximum value is 1000.
	 * @param offset The number of records from a collection to skip.
	 * @deprecated
	 */
	public ModelIterator(Class<T> typeClasse, String query, MailChimpConnection connection, int count, int offset) {
		this.typeClasse = typeClasse;
		this.connection = connection;
		queryParams = new MailChimpQueryParameters(query)
				.count(count)
				.offset(offset * count);
		readPagedEntities();
	}

	public ModelIterator(Class<T> typeClasse, String query, MailChimpConnection connection, MailChimpQueryParameters params) {
		this.typeClasse = typeClasse;
		this.connection = connection;
		queryParams = params;
		if (queryParams == null) {
			queryParams = new MailChimpQueryParameters(query)
				.count(100)
				.offset(0);
		} else {
			Integer count = queryParams.getCount();
			Integer offset = queryParams.getOffset();
			if (count == null) {
				queryParams.count(100);	// MailChimp defaults to 10. Use larger value to reduce number of REST calls.    
			}
			
			if (offset == null) {
				queryParams.offset(0);
			}
		}
		queryParams.baseUrl(query);
		readPagedEntities();
	}
	
	private void readPagedEntities() {
		try {
			URL url = queryParams.getURL();
			queryParams.offset(queryParams.getOffset() + queryParams.getCount()); // step offset for next iteration
			final JSONObject list = new JSONObject(connection.do_Get(url,connection.getApikey()));
			parseEntities(list);
		} catch (IOException | JSONException | URISyntaxException e) {
			// Wrap checked exceptions in a RuntimeException.
			// Checked exceptions are warped in a RuntimeException to reduce the need for
			// boilerplate code inside of lambdas.
			throw new RuntimeException(e);  
		} 
	}
	
	/**
	 * Finds and extracts array elements for iteration and offers them to q.
	 * Override to handle special parsing requirements such as when base entity
	 * contains multiple array elements at the root level.
	 * 
	 * @param rootObj The base, or root, element returned by the MailChimp API.
	 */
	protected void parseEntities(final JSONObject rootObj) {
		parseRoot(rootObj);

		Iterator<String> keys = rootObj.keys();
		while(keys.hasNext()) {
			final String key = keys.next();
			if (key.equals("_links")) { continue; }
			final Object keyValue = rootObj.get(key);
			if (keyValue instanceof JSONArray) { // look for main entity array
				// TODO: TRACE -- found 'key' entity array of type T
				final JSONArray entArray = (JSONArray)keyValue;
				for (int i = 0 ; i < entArray.length(); i++)
				{
					final JSONObject objDetail = entArray.getJSONObject(i);
					q.offer(buildRefObj(connection, objDetail));
				}
				break;	// found entity array, no need to keep looking
			}
		}
	}

	/**
	 * Extracts total_items as a hint to the total number of items to be iterated.
	 * Override to allow superclass to extract additional root level entities.
	 * 
	 * @param arrayObj
	 */
	protected void parseRoot(JSONObject arrayObj) {
		if (arrayObj.has("total_items")) {
			totalItems = new Integer(arrayObj.getInt("total_items"));	// The total number of items matching the query regardless of pagination
		}
	}
	
	/**
	 * Constructs type T using reflection. Override to provide a concrete constructor.
	 * @param con
	 * @param objDetail
	 * @return Constructed object of type T
	 */
	protected T buildRefObj(MailChimpConnection con, JSONObject objDetail) {
		try {
			T ent;
			ent = typeClasse.newInstance();
			ent.parse(connection, objDetail);
			return ent;
		} catch (InstantiationException e) {
			throw new RuntimeException("Class " + typeClasse.getCanonicalName() + " missing default constructor", e);  
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e); 
		}
	}
	
	public Integer getTotalItems() {
		return totalItems;
	}

	@Override
	public Iterator<T> iterator() {
		Iterator<T> it = new Iterator<T>() {

			@Override
			public boolean hasNext() {
				if (q.peek() != null) {
					return true;
				}
				return false;
			}

			@Override
			public T next() {
				currentIndex++;
				T element = q.poll();
				
				if (element == null ) {
					throw new NoSuchElementException("the iteration has no more elements");
				}

				if (q.peek() == null && (totalItems == null || currentIndex < totalItems)) {
					// cache next page of entities when queue is empty
					try {
						readPagedEntities();
					} catch (Exception ex) {
						throw new NoSuchElementException(ex.getMessage());
					}
				}
				return element;
			}
			
		};
		return it;
	}
	
}
