package com.github.bananaj.model.filemanager;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import org.json.JSONObject;

import com.github.bananaj.connection.MailChimpConnection;
import com.github.bananaj.model.JSONParser;
import com.github.bananaj.utils.DateConverter;

/**
 * Manage specific folders in the File Manager for your Mailchimp account. The
 * File Manager is a place to store images, documents, and other files you
 * include or link to in your campaigns, templates, or signup forms.
 */
public class FileManagerFolder implements JSONParser {
	private int id;
	private String name;
	private int fileCount;
	private ZonedDateTime createdAt;
	private String createdBy;
//	private ArrayList<FileManagerFile> files;
	private MailChimpConnection connection;

	public FileManagerFolder() {
		
	}
	
	public FileManagerFolder(MailChimpConnection connection, JSONObject jsonFileManagerFolder) {
		parse(connection, jsonFileManagerFolder);
	}

	public void parse(MailChimpConnection connection, JSONObject jsonFileManagerFolder) {
		id = jsonFileManagerFolder.getInt("id");
		name = jsonFileManagerFolder.getString("name");
		fileCount = jsonFileManagerFolder.getInt("file_count");
		createdAt = DateConverter.fromISO8601(jsonFileManagerFolder.getString("created_at"));
		createdBy = jsonFileManagerFolder.getString("created_by");
		this.connection = connection;
	}

	/**
	 * Rename folder
	 * 
	 * @param name the new folder name
	 * @throws Exception
	 */
	public void rename(String name) throws Exception {
		JSONObject jsonObj = getJsonRepresentation();
		String results = getConnection().do_Patch(
				new URL(getConnection().getFilemanagerfolderendpoint() + "/" + getId()), jsonObj.toString(),
				getConnection().getApikey());
		parse(connection, new JSONObject(results));
	}

	/**
	 * Remove a folder from File Manager
	 * 
	 * @throws Exception
	 */
	public void delete() throws Exception {
		getConnection().do_Delete(new URL(getConnection().getFilemanagerfolderendpoint() + "/" + getId()),
				getConnection().getApikey());
	}

	/**
	 * @return The unique id for the folder.
	 */
	public int getId() {
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
	public int getFileCount() {
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

//	/**
//	 * Gets a list of all files belonging to this folder. Mailchimp does not allow
//	 * querying for files that belong to a specific folder so the full list of files
//	 * is iterated on first read and cached internally.
//	 * 
//	 * @return List of all file manager files
//	 * @throws Exception
//	 */
//	public List<FileManagerFile> getFiles() throws Exception {
//		if (files == null) { // defer loading files list until requested.
//			cacheFoldersFiles();
//		}
//		return files;
//	}
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
	
//	/**
//	 * Mailchimp API does not allow querying for files by folder Id so we have to
//	 * request all files and filter them by the desired folder.
//	 * 
//	 * @throws Exception
//	 */
//	private void cacheFoldersFiles() throws Exception {
//		ArrayList<FileManagerFile> files = new ArrayList<FileManagerFile>();
//		if (fileCount > 0) {
//			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//			formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
//
//			int offset = 0;
//			int count = 1000;
//			List<FileManagerFile> filelist;
//
//			do {
//				filelist = connection.getFileManager().getFileManagerFiles(count, offset);
//				offset += count;
//				for (FileManagerFile file : filelist) {
//					if (file.getFolderId() == id) {
//						files.add(file);
//					}
//				}
//			} while (filelist.size() > 0 && files.size() < fileCount);
//		}
//		this.files = files;
//	}

//	/**
//	 * Searches the list of all files stored in the mailchimp account for the
//	 * specified file. Mailchimp does not allow querying for files that belong to a
//	 * specific folder so the full list of files is cached on first read.
//	 * 
//	 * @param id
//	 * @return The matching file by ID or null
//	 * @throws Exception
//	 */
//	public FileManagerFile getFile(int id) throws Exception {
//		if (files == null) { // defer loading files list until requested.
//			cacheFoldersFiles();
//		}
//		for (FileManagerFile file : files) {
//			if (file.getId() == id) {
//				return file;
//			}
//		}
//		return null;
//	}

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

		private void scanFiles() {
			int numfound = 0;
			while(it.hasNext() && numfound < 10) {
				FileManagerFile file = it.next();
				if (file.getFolderId() == id) {
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
