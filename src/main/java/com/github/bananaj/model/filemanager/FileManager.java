package com.github.bananaj.model.filemanager;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.json.JSONObject;

import com.github.bananaj.connection.MailChimpConnection;
import com.github.bananaj.connection.MailChimpQueryParameters;
import com.github.bananaj.model.ModelIterator;
import com.github.bananaj.utils.FileInspector;
import com.github.bananaj.utils.URLHelper;

/**
 * Manage folders and files in the Mailchimp File Manager. The File Manager is a
 * place to store images, documents, and other files you include or link to in
 * your campaigns, templates, or signup forms.
 * 
 * @see <a href="https://mailchimp.com/developer/marketing/api/file-manager/" target="MailchimpAPIDoc">File Manager</a> 
 */

public class FileManager {

	private MailChimpConnection connection;

	public FileManager(MailChimpConnection connection) {
		this.connection = connection;
	}

	/**
	 * Get an iterator of all folders in the File Manager.
	 * @return folders iterator
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<FileManagerFolder> getFolders() throws IOException, Exception {
		return new ModelIterator<FileManagerFolder>(FileManagerFolder.class, getConnection().getFilemanagerfolderendpoint(), getConnection());
	}

	/**
	 * Get an iterator of all folders in the File Manager.
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/file-manager-folders/list-folders/" target="MailchimpAPIDoc">File Manager Folders -- GET /file-manager/folders</a>
	 * @return Iterator of all folders in the File Manager.
	 * @throws IOException
	 * @throws Exception
	 */
	public Iterable<FileManagerFolder> getFolders(final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		return new ModelIterator<FileManagerFolder>(FileManagerFolder.class, getConnection().getFilemanagerfolderendpoint(), getConnection(), queryParameters);
	}

	/**
	 * Get information about a specific folder in the File Manager.
	 * @param id The unique id for the File Manager folder.
	 * @return Information about a specific folder
	 * @throws IOException
	 * @throws Exception
	 */
	public FileManagerFolder getFolder(int id) throws IOException, Exception {
		JSONObject jsonFileManagerFolder = new JSONObject(getConnection().do_Get(URLHelper.url(getConnection().getFilemanagerfolderendpoint(),"/",Integer.toString(id)),connection.getApikey()));
		return new FileManagerFolder(getConnection(), jsonFileManagerFolder);
	}

	/**
	 * Create a new folder in the File Manager.
	 * @param name The name of the folder
	 * @throws IOException
	 * @throws Exception 
	 */
	public FileManagerFolder createFolder(String name) throws IOException, Exception {
		JSONObject newFolder  = new JSONObject();
		newFolder.put("name", name);
		JSONObject jsonFileManagerFolder = new JSONObject(getConnection().do_Post(new URL(getConnection().getFilemanagerfolderendpoint()), newFolder.toString(), getConnection().getApikey()));
		return new FileManagerFolder(getConnection(), jsonFileManagerFolder);
	}

	/**
	 * Get an iterator of available images and files stored in the File Manager for the account.
	 * @return File manager file iterator
	 * @throws IOException
	 * @throws Exception 
	 */
	public FileManagerFileIterator getFiles() throws IOException, Exception {
		return new FileManagerFileIterator(getConnection(),1000);
	}

	/**
	 * Get an iterator of available images and files stored in the File Manager for the account.
	 * @param queryParameters Optional query parameters to send to the MailChimp API. 
	 *   @see <a href="https://mailchimp.com/developer/marketing/api/file-manager-files/list-stored-files/" target="MailchimpAPIDoc">File Manager Files -- GET /file-manager/files</a>
	 * @return File manager file iterator
	 * @throws IOException
	 * @throws Exception
	 */
	public FileManagerFileIterator getFiles(final MailChimpQueryParameters queryParameters) throws IOException, Exception {
		return new FileManagerFileIterator(getConnection(), queryParameters);
	}
	
	/**
	 * Get the total size of all files in the File Manager.
	 * @return The total size of all File Manager files in bytes.
	 */
	public Long getTotalFileSize() {
		MailChimpQueryParameters p = new MailChimpQueryParameters()
				.count(1)
				.offset(0)
				.excludeFields("files,total_items,_links");
		FileManagerFileIterator fi = new FileManagerFileIterator(getConnection(), p);
		return fi.getTotalFileSize();
	}
	
	/**
	 * Get the total number of files in the File Manager.
	 * @return The total number of files.
	 */
	public Integer getTotalFilesCount() {
		MailChimpQueryParameters p = new MailChimpQueryParameters()
				.count(1)
				.offset(0)
				.excludeFields("files,total_file_size,_links,");
		FileManagerFileIterator fi = new FileManagerFileIterator(getConnection(), p);
		return fi.getTotalItems();
	}
	
	/** 
	 * Get information about a specific file in the File Manager.
	 * @param id The unique id for the File Manager file.
	 * @return Information about a specific file in the File Manager
	 * @throws IOException
	 * @throws Exception
	 */
	public FileManagerFile getFile(int id) throws IOException, Exception {
		// parse response
		JSONObject jsonFileManagerFile = new JSONObject(getConnection().do_Get(URLHelper.url(getConnection().getFilesendpoint(),"/",Integer.toString(id)), getConnection().getApikey()));
		return new FileManagerFile(getConnection(), jsonFileManagerFile);
	}

	/**
	 * Upload a new image or file to the File Manager.
	 * @param folder_id The id of the folder.
	 * @param filename The name of the file.
	 * @param file The file to be uploaded
	 * @return File information for the newly uploaded file
	 * @throws IOException
	 * @throws Exception
	 */
	public FileManagerFile upload(int folder_id, String filename, File file) throws IOException, Exception {
		String fExt = FileInspector.getInstance().getExtension(file);
		String fName = filename.endsWith(fExt) ? filename : filename+fExt;
		JSONObject upload_data  = new JSONObject();
		upload_data.put("folder_id", folder_id);
		upload_data.put("name", fName);
		upload_data.put("file_data",FileInspector.getInstance().encodeFileToBase64Binary(file));
		JSONObject jsonFileManagerFile = new JSONObject(getConnection().do_Post(new URL(connection.getFilesendpoint()), upload_data.toString(),connection.getApikey()));
		return new FileManagerFile(getConnection(), jsonFileManagerFile);
	}

	/**
	 * Upload a new image or file to the File Manager.
	 * @param filename The name of the file.
	 * @param file The file to be uploaded
	 * @return File information for the newly uploaded file
	 * @throws IOException
	 * @throws Exception
	 */
	public FileManagerFile upload(String filename, File file) throws IOException, Exception {
		String fExt = FileInspector.getInstance().getExtension(file);
		String fName = filename.endsWith(fExt) ? filename : filename+fExt;
		JSONObject upload_data  = new JSONObject();
		upload_data.put("name", fName);
		upload_data.put("file_data",FileInspector.getInstance().encodeFileToBase64Binary(file));
		JSONObject jsonFileManagerFile = new JSONObject(getConnection().do_Post(new URL(connection.getFilesendpoint()), upload_data.toString(),connection.getApikey()));
		return new FileManagerFile(getConnection(), jsonFileManagerFile);
	}

	/**
	 * Remove a specific file from the File Manager.
	 * @param fileID The unique id for the File Manager file.
	 * @throws IOException
	 * @throws Exception 
	 */
	public void deleteFile(String fileID) throws IOException, Exception {
		connection.do_Delete(URLHelper.url(connection.getFilesendpoint(),"/",fileID),connection.getApikey());
	}

	/**
	 * Delete a specific folder in the File Manager.
	 * @param folderID The unique id for the File Manager folder.
	 * @throws IOException
	 * @throws Exception 
	 */
	public void deleteFolder(String folderID) throws IOException, Exception {
		getConnection().do_Delete(URLHelper.url(getConnection().getFilemanagerfolderendpoint(),"/",folderID), getConnection().getApikey());
	}

	public MailChimpConnection getConnection() {
		return connection;
	}

}
