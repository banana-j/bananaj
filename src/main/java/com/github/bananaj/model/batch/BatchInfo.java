package com.github.bananaj.model.batch;

import java.io.IOException;
import java.time.ZonedDateTime;

import org.json.JSONObject;

import com.github.bananaj.connection.MailChimpConnection;
import com.github.bananaj.connection.MailChimpQueryParameters;
import com.github.bananaj.model.JSONParser;
import com.github.bananaj.utils.JSONObjectCheck;
import com.github.bananaj.utils.URLHelper;

// TODO: add methods to download response body
/**
 * Object representing the status of a mailchimp batch operation
 *
 */
public class BatchInfo implements JSONParser {
	private MailChimpConnection connection;
	private String id;
	private BatchStatus status;
	private Integer totalOperations;
	private Integer finishedOperations;
	private Integer erroredOperations;
	private ZonedDateTime submittedAt;
	private ZonedDateTime completedAt;
	private String responseBodyUrl;

	public BatchInfo() {

	}

	public BatchInfo(MailChimpConnection connection, JSONObject batchStatus) {
		parse(connection, batchStatus);
	}

	@Override
	public void parse(MailChimpConnection connection, JSONObject batchStatus) {
		JSONObjectCheck jObj = new JSONObjectCheck(batchStatus);	
		this.connection = connection;
		id = jObj.getString("id");
		status = jObj.getEnum(BatchStatus.class, "status");
		totalOperations = jObj.getInt("total_operations");
		finishedOperations = jObj.getInt("finished_operations");
		erroredOperations = jObj.getInt("errored_operations");
		submittedAt = jObj.getISO8601Date("submitted_at");
		completedAt = jObj.getISO8601Date("completed_at");
		responseBodyUrl = jObj.getString("response_body_url");
	}

	/**
	 * @return the MailChimp com.github.bananaj.connection
	 */
	public MailChimpConnection getConnection() {
		return connection;
	}

	/**
	 * 
	 * @return Unique id of the batch call
	 */
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @return Status for the whole call
	 */
	public BatchStatus getStatus() {
		return status;
	}

	/**
	 * 
	 * @return Number of operations in the batch
	 */
	public Integer getTotalOperations() {
		return totalOperations;
	}

	/**
	 * 
	 * @return Number of finished operations
	 */
	public Integer getFinishedOperations() {
		return finishedOperations;
	}

	/**
	 * 
	 * @return Number of errored operations
	 */
	public Integer getErroredOperations() {
		return erroredOperations;
	}

	/**
	 * 
	 * @return Datetime the call was made
	 */
	public ZonedDateTime getSubmittedAt() {
		return submittedAt;
	}

	/**
	 * 
	 * @return Datetime when all the operations completed
	 */
	public ZonedDateTime getCompletedAt() {
		return completedAt;
	}

	/**
	 * 
	 * @return URL to use to retrieve results
	 */
	public String getResponseBodyUrl() {
		return responseBodyUrl;
	}
	
	/**
	 * Refresh batch status
     * @param queryParameters Optional query parameters to send to the MailChimp API. 
     *   @see <a href="https://mailchimp.com/developer/marketing/api/batch-operations/get-batch-operation-status/" target="MailchimpAPIDoc">Batch Operations -- /batches/{batch_id}</a>
	 * @return this
	 * @throws IOException
	 * @throws Exception
	 */
	public BatchInfo update(final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		MailChimpQueryParameters query = queryParameters != null ? (MailChimpQueryParameters) queryParameters.clone() : new MailChimpQueryParameters();
		query.baseUrl(URLHelper.join(connection.getBatchendpoint(),"/",getId()));
		String results = connection.do_Get(query.getURL(), connection.getApikey());
		parse(connection, new JSONObject(results));
		return this;
	}

	/**
	 * Stops a batch request from running. Since only one batch request is run at a
	 * time, this can be used to cancel a long running request. The results of any
	 * completed operations will not be available after this call.
	 * 
	 * @throws IOException
	 * @throws Exception
	 */
	public void delete(String batch_id) throws IOException, Exception {
		connection.deleteBatch(getId());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Batch Info: " + getId() + System.lineSeparator());
		sb.append("  Status: " + getStatus() + System.lineSeparator());
		sb.append("  TotalOperations: " + getTotalOperations() + System.lineSeparator());
		sb.append("  Finished Operations: " + getFinishedOperations() + System.lineSeparator());
		sb.append("  Errored Operations: " + getErroredOperations() + System.lineSeparator());
		sb.append("  Submitted At: " + getSubmittedAt() + System.lineSeparator());
		if (getCompletedAt() != null) {sb.append("  Completed At: " + getCompletedAt() + System.lineSeparator());}
		sb.append("  Response Body Url: " + getResponseBodyUrl() + System.lineSeparator());
		return sb.toString();
	}
	
	
}
