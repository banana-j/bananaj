package com.github.bananaj.model.batch;

import java.util.HashMap;
import java.util.Map.Entry;

import org.json.JSONObject;

import com.github.bananaj.utils.JSONObjectCheck;

public class BatchOperation {

	private String method;
	private String path;
	private HashMap<String,String> params;
	private String body;
	private String operationId;
	
	public BatchOperation() {

	}

	@SuppressWarnings("unchecked")
	private BatchOperation(Builder b) {
		this.method = b.method;
		this.path = b.path;
		if (b.params != null) {
			this.params = (HashMap<String, String>) b.params.clone();
		}
		this.body = b.body;
		this.operationId = b.operationId;
	}

	public String getMethod() {
		return method;
	}

	public BatchOperation setMethod(String method) {
		this.method = method;
		return this;
	}

	public BatchOperation setMethod(OperationMethod method) {
		this.method = method.toString();
		return this;
	}
	
	public String getPath() {
		return path;
	}

	public BatchOperation setPath(String path) {
		this.path = path;
		return this;
	}

	public HashMap<String, String> getParams() {
		return params;
	}

	public BatchOperation setParam(String key, String value) {
		if (params == null) {
			params = new HashMap<String, String>();
		}
		params.put(key, value);
		return this;
	}

	public String getBody() {
		return body;
	}

	public BatchOperation setBody(String body) {
		this.body = body;
		return this;
	}

	public String getOperationId() {
		return operationId;
	}

	public BatchOperation setOperationId(String operationId) {
		this.operationId = operationId;
		return this;
	}

	/**
	 * Helper method to convert JSON for mailchimp PATCH/POST operations
	 */
	public JSONObject getJsonRepresentation() {
		JSONObjectCheck jsonSettings = new JSONObjectCheck();

		jsonSettings.put("method", getMethod());
		jsonSettings.put("path", getPath());
		jsonSettings.put("params", getParams());
		jsonSettings.put("body", getBody());
		jsonSettings.put("operation_id", getOperationId());

		return jsonSettings.getJsonObject();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (getOperationId() != null) {
			sb.append(getOperationId()); 
			sb.append(" ");
		}
		sb.append(getMethod());
		sb.append(" ");
		sb.append(getPath());
		sb.append(" ");
		if (getParams() != null) {
			for (final Entry<String, String> e : getParams().entrySet())  {
				sb.append(e.getKey());
				sb.append("=");
				sb.append(e.getValue());
				sb.append(" ");
			}
		}
		//sb.append(System.lineSeparator() + "  Body: " + getBody());
		sb.append(System.lineSeparator());
		return sb.toString();
	}

	/**
	 * BatchOperation builder pattern. 
	 */
	public static class Builder {
		private String method;
		private String path;
		private HashMap<String,String> params;
		private String body;
		private String operationId;
		
		@SuppressWarnings("unchecked")
		public Builder(BatchOperation batch) {
			this.method = batch.method;
			this.path = batch.path;
			this.params = (HashMap<String, String>) batch.params.clone();
			this.body = batch.body;
			this.operationId = batch.operationId;
		}

		public Builder() {
			
		}

		public BatchOperation build() {
			return new BatchOperation(this);
		}

		public Builder method(String method) {
			this.method = method;
			return this;
		}

		public Builder method(OperationMethod method) {
			this.method = method.toString();
			return this;
		}
		
		public Builder path(String path) {
			this.path = path;
			return this;
		}

		public Builder addParam(String key, String value) {
			if (params == null) {
				params = new HashMap<String, String>();
			}
			params.put(key, value);
			return this;
		}

		public Builder body(String body) {
			this.body = body;
			return this;
		}

		public Builder operationId(String operationId) {
			this.operationId = operationId;
			return this;
		}
	}
	
}
