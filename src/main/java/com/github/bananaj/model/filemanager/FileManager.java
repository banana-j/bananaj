package com.github.bananaj.model.filemanager;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.bananaj.connection.MailChimpConnection;
import com.github.bananaj.utils.FileInspector;
import com.github.bananaj.utils.ModelIterator;
import com.github.bananaj.utils.URLHelper;

/**
 * Manage folders and files in the Mailchimp File Manager. The File Manager is a
 * place to store images, documents, and other files you include or link to in
 * your campaigns, templates, or signup forms.
 * 
 * Created by alexanderweiss on 06.02.16.
 */

public class FileManager {

	private MailChimpConnection connection;

	public FileManager(MailChimpConnection connection) {
		this.connection = connection;
	}

	/**
	 * Get a iterator of all folders in the File Manager.
	 * @return folders iterator
	 * @throws Exception
	 */
	public Iterable<FileManagerFolder> getFolders() throws Exception {
		return new ModelIterator<FileManagerFolder>(FileManagerFolder.class, getConnection().getFilemanagerfolderendpoint(), getConnection());
	}

	/**
	 * Get a paginated list of folders in the File Manager.
	 * @param count Number of folders to return
	 * @param offset Zero based offset
	 * @throws Exception
	 */
	public List<FileManagerFolder> getFolders(int count, int offset) throws Exception {
		if (count < 1 || count > 1000) {
			throw new InvalidParameterException("Page size must be 1-1000");
		}
		
		List<FileManagerFolder> fileManagerFolders = new ArrayList<FileManagerFolder>();

		JSONObject jsonFileManagerFolders = new JSONObject(getConnection().do_Get(URLHelper.url(getConnection().getFilemanagerfolderendpoint(),
				"?offset=", Integer.toString(offset), "&count=", Integer.toString(count)), connection.getApikey()));
		JSONArray folderArray = jsonFileManagerFolders.getJSONArray("folders");
		//int total_items = jsonFileManagerFolders.getInt("total_items"); 	// The total number of items matching the query regardless of pagination
		for( int i = 0; i< folderArray.length();i++)
		{
			JSONObject folderDetail = folderArray.getJSONObject(i);
			FileManagerFolder folder = new FileManagerFolder(getConnection(), folderDetail);
			fileManagerFolders.add(folder);
		}
		return fileManagerFolders;
	}

	/**
	 * Get a specific file manager folder in mailchimp account account
	 * @throws Exception
	 */
	public FileManagerFolder getFolder(int id) throws Exception {
		JSONObject jsonFileManagerFolder = new JSONObject(getConnection().do_Get(URLHelper.url(getConnection().getFilemanagerfolderendpoint(),"/",Integer.toString(id)),connection.getApikey()));
		return new FileManagerFolder(getConnection(), jsonFileManagerFolder);
	}

	/**
	 * Create a new folder
	 * @param name The name of the folder
	 * @throws Exception
	 */
	public FileManagerFolder createFolder(String name) throws Exception {
		JSONObject newFolder  = new JSONObject();
		newFolder.put("name", name);
		JSONObject jsonFileManagerFolder = new JSONObject(getConnection().do_Post(new URL(getConnection().getFilemanagerfolderendpoint()), newFolder.toString(), getConnection().getApikey()));
		return new FileManagerFolder(getConnection(), jsonFileManagerFolder);
	}

	/**
	 * Get a iterator of available images and files stored in the File Manager for the account.
	 * @return file manager file iterator
	 * @throws Exception
	 */
	public Iterable<FileManagerFile> getFiles() throws Exception{
		return new ModelIterator<FileManagerFile>(FileManagerFile.class, getConnection().getFilesendpoint(), getConnection());
	}

	/**
	 * Get a paginated list of available images and files stored in the File Manager for the account.
	 * @param count Number of lists to return. Maximum value is 1000.
	 * @param offset Zero based offset
	 * @throws Exception
	 */
	public List<FileManagerFile> getFiles(int count, int offset) throws Exception{
		if (count < 1 || count > 1000) {
			throw new InvalidParameterException("Page size must be 1-1000");
		}

		List<FileManagerFile> files = new ArrayList<FileManagerFile>();

		// parse response
		JSONObject jsonFileManagerFiles = new JSONObject(getConnection().do_Get(URLHelper.url(getConnection().getFilesendpoint(),
				"?offset=", Integer.toString(offset), "&count=", Integer.toString(count)),getConnection().getApikey()));
		JSONArray filesArray = jsonFileManagerFiles.getJSONArray("files");
		//double total_file_size = jsonFileManagerFiles.getDouble("total_file_size"); 	// The total size of all File Manager files in bytes.
		//int total_items = jsonFileManagerFiles.getInt("total_items"); 	// The total number of items matching the query regardless of pagination
		for( int i = 0; i< filesArray.length();i++)
		{
			JSONObject fileDetail = filesArray.getJSONObject(i);
			FileManagerFile file = new FileManagerFile(getConnection(), fileDetail);
			files.add(file);
		}
		return files;
	}

	/**
	 * Get all files in your account
	 * @throws Exception
	 */
	public FileManagerFile getFile(int id) throws Exception {
		// parse response
		JSONObject jsonFileManagerFile = new JSONObject(getConnection().do_Get(URLHelper.url(getConnection().getFilesendpoint(),"/",Integer.toString(id)), getConnection().getApikey()));
		return new FileManagerFile(getConnection(), jsonFileManagerFile);
	}

	/**
	 * Upload a new file to the specified mailchimp folder
	 * @param folder_id The mailchimp folder id where the file will be placed
	 * @param filename The name of the mailchimp file
	 * @param file File to be uploaded
	 * @throws JSONException
	 * @throws MalformedURLException
	 * @throws Exception
	 */
	public FileManagerFile upload(int folder_id, String filename, File file) throws JSONException, MalformedURLException, Exception {
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
	 * Upload a new file to mailchimp with no folder
	 * @param filename The name of the mailchimp file
	 * @param file File to be uploaded
	 * @throws JSONException
	 * @throws MalformedURLException
	 * @throws Exception
	 */
	public FileManagerFile upload(String filename, File file) throws JSONException, MalformedURLException, Exception {
		String fExt = FileInspector.getInstance().getExtension(file);
		String fName = filename.endsWith(fExt) ? filename : filename+fExt;
		JSONObject upload_data  = new JSONObject();
		upload_data.put("name", fName);
		upload_data.put("file_data",FileInspector.getInstance().encodeFileToBase64Binary(file));
		JSONObject jsonFileManagerFile = new JSONObject(getConnection().do_Post(new URL(connection.getFilesendpoint()), upload_data.toString(),connection.getApikey()));
		return new FileManagerFile(getConnection(), jsonFileManagerFile);
	}

	/**
	 * Delete a file with specified fileID
	 * @param fileID
	 * @throws Exception
	 */
	public void deleteFile(String fileID) throws Exception{
		connection.do_Delete(URLHelper.url(connection.getFilesendpoint(),"/",fileID),connection.getApikey());
	}

	/**
	 * Delete a folder with specified folderID
	 * @param folderID
	 * @throws Exception
	 */
	public void deleteFolder(String folderID) throws Exception{
		getConnection().do_Delete(URLHelper.url(getConnection().getFilemanagerfolderendpoint(),"/",folderID), getConnection().getApikey());
	}

	public MailChimpConnection getConnection() {
		return connection;
	}

}
