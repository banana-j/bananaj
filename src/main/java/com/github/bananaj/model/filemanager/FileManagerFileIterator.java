package com.github.bananaj.model.filemanager;

import org.json.JSONObject;

import com.github.bananaj.connection.MailChimpConnection;
import com.github.bananaj.connection.MailChimpQueryParameters;
import com.github.bananaj.model.ModelIterator;

/**
 * 
 * ModelIterator&lt;FileManagerFile&gt; implementation that includes getter for 
 * total size of all File Manager files
 *
 */
public class FileManagerFileIterator extends ModelIterator<FileManagerFile> {
	Long totalFileSize;

	public FileManagerFileIterator(MailChimpConnection connection, int count, int offset) {
		super(FileManagerFile.class, connection.getFilesendpoint(), connection, count, offset);
	}

	public FileManagerFileIterator(MailChimpConnection connection, int count) {
		super(FileManagerFile.class, connection.getFilesendpoint(), connection, count);
	}

	public FileManagerFileIterator(MailChimpConnection connection, MailChimpQueryParameters params) {
		super(FileManagerFile.class, connection.getFilesendpoint(), connection, params);
	}

	public FileManagerFileIterator(MailChimpConnection connection) {
		super(FileManagerFile.class, connection.getFilesendpoint(), connection);
	}

	@Override
	protected void parseRoot(JSONObject arrayObj) {
		super.parseRoot(arrayObj);
		if (arrayObj.has("total_file_size")) {
			totalFileSize = new Long(arrayObj.getLong("total_file_size"));	// The total number of items matching the query regardless of pagination
		}
	}

	/**
	 * @return The total size of all File Manager files in bytes.
	 */
	public Long getTotalFileSize() {
		return totalFileSize;
	}

}
