package com.github.bananaj.model.filemanager;

import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import org.json.JSONObject;

import com.github.bananaj.connection.MailChimpConnection;
import com.github.bananaj.connection.MailChimpQueryParameters;
import com.github.bananaj.model.JSONParser;
import com.github.bananaj.utils.DateConverter;
import com.github.bananaj.utils.JSONObjectCheck;

/**
 * Manage specific folders in the File Manager for your Mailchimp account. The
 * File Manager is a place to store images, documents, and other files you
 * include or link to in your campaigns, templates, or signup forms.
 */
public class FileManagerFolder implements JSONParser {
	private Integer id;
	private String name;
	private Integer fileCount;
	private ZonedDateTime createdAt;
	private String createdBy;
	private MailChimpConnection connection;

	public FileManagerFolder() {
		
	}
	
	public FileManagerFolder(MailChimpConnection connection, JSONObject jsonFileManagerFolder) {
		parse(connection, jsonFileManagerFolder);
	}

	public void parse(MailChimpConnection connection, JSONObject jsonFileManagerFolder) {
		JSONObjectCheck jObj = new JSONObjectCheck(jsonFileManagerFolder);
		this.connection = connection;
		id = jObj.getInt("id");
		name = jObj.getString("name");
		fileCount = jObj.getInt("file_count");
		createdAt = jObj.getISO8601Date("created_at");
		createdBy = jObj.getString("created_by");
	}

	/**
	 * Rename folder
	 * 
	 * @param name the new folder name
	 * @throws IOException
	 * @throws Exception 
	 */
	public void rename(String name) throws IOException, Exception {
		JSONObject jsonObj = getJsonRepresentation();
		String results = getConnection().do_Patch(
				new URL(getConnection().getFilemanagerfolderendpoint() + "/" + getId()), jsonObj.toString(),
				getConnection().getApikey());
		parse(connection, new JSONObject(results));
	}

	/**
	 * Remove a folder from File Manager
	 * 
	 * @throws IOException
	 * @throws Exception 
	 */
	public void delete() throws IOException, Exception {
		getConnection().do_Delete(new URL(getConnection().getFilemanagerfolderendpoint() + "/" + getId()),
				getConnection().getApikey());
	}

	/**
	 * @return The unique id for the folder.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @return The name of the folder.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return The number of files in the folder.
	 */
	public Integer getFileCount() {
		return fileCount;
	}

	/**
	 * @return The date and time a file was added to the File Manager
	 */
	public ZonedDateTime getCreatedAt() {
		return createdAt;
	}

	/**
	 * @return The username of the profile that created the folder.
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * Gets a list iterator of all files belonging to this folder. Mailchimp does
	 * not allow querying for files that belong to a specific folder so the full
	 * list of files is scanned and filtered.
	 * 
	 * @return List of all file manager files
	 * @throws Exception
	 */
	public Iterable<FileManagerFile> getFiles() throws Exception {
		// TODO: Contact Mailchimp about shortcoming and look for undocumented filter on folder_id
		return new FolderFileIterator();
	}
	
	/**
	 * Gets an iterator of all files belonging to this folder. Mailchimp does
	 * not allow querying for files that belong to a specific folder so the full
	 * list of files is scanned and filtered.
	 * 
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/file-manager-files/list-stored-files/" target="MailchimpAPIDoc">File Manager Files -- GET /file-manager/files</a>
	 * @return Iterator of all files belonging to this folder
	 * @throws Exception
	 */
	public Iterable<FileManagerFile> getFiles(final MailChimpQueryParameters queryParameters) throws Exception {
		// TODO: Contact Mailchimp about shortcoming and look for undocumented filter on folder_id
		return new FolderFileIterator(queryParameters);
	}
	
	public MailChimpConnection getConnection() {
		return connection;
	}

	/**
	 * Helper method to convert JSON for mailchimp PATCH/POST operations
	 */
	protected JSONObject getJsonRepresentation() throws Exception {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("name", getName());
		return jsonObj;
	}

	@Override
	public String toString() {
		return "Folder: " + getName() +  System.lineSeparator() +
        		"    ID: " + getId() +  System.lineSeparator() +
        		"    File count: " + getFileCount() + System.lineSeparator() +
				"    Created at: " + DateConverter.toLocalString(getCreatedAt()) + System.lineSeparator() +
				"    Created by: " + getCreatedBy();
	}
	
	/**
	 * Mailchimp API does not allow querying for files by folder Id so we have to
	 * request all files and filter them by the desired folder.
	 * 
	 */
	private class FolderFileIterator implements Iterable<FileManagerFile> {
		Iterator<FileManagerFile> it;
		Queue<FileManagerFile> q = new LinkedList<FileManagerFile>();
		private int totfound = 0;

		
		public FolderFileIterator() throws Exception {
			it = connection.getFileManager().getFiles().iterator();
			scanFiles();
		}

		public FolderFileIterator(final MailChimpQueryParameters queryParameters) throws Exception {
			it = connection.getFileManager().getFiles(queryParameters).iterator();
			scanFiles();
		}

		private void scanFiles() {
			int numfound = 0;
			while(it.hasNext() && numfound < 10) {
				FileManagerFile file = it.next();
				if (id.equals(file.getFolderId())) {
					q.offer(file);
					numfound++;
					totfound++;
					if (totfound == fileCount) { break; }
				}
			}
		}

		@Override
		public Iterator<FileManagerFile> iterator() {
			Iterator<FileManagerFile> it = new Iterator<FileManagerFile>() {

				@Override
				public boolean hasNext() {
					return q.peek() != null;
				}

				@Override
				public FileManagerFile next() {
					FileManagerFile file = q.poll();
					if (q.peek() == null && totfound < fileCount) {
						scanFiles();	// queue is empty so scan for more files
					}
					if (file == null ) {
						throw new NoSuchElementException("the iteration has no more elements");
					}
					return file;
				}
				
			};
			return it;
		}

	}
}
