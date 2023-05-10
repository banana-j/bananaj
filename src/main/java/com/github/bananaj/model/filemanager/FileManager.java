package com.github.bananaj.model.filemanager;

import java.io.File;
import java.io.IOException;
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
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<FileManagerFolder> getFolders() throws IOException, Exception {
		return new ModelIterator<FileManagerFolder>(FileManagerFolder.class, getConnection().getFilemanagerfolderendpoint(), getConnection());
	}

	/**
	 * Get a paginated list of folders in the File Manager.
	 * @param pageSize Number of records to fetch per query. Maximum value is 1000.
	 * @param pageNumber First page number to fetch starting from 0.
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<FileManagerFolder> getFolders(int pageSize, int pageNumber) throws IOException, Exception {
		return new ModelIterator<FileManagerFolder>(FileManagerFolder.class, getConnection().getFilemanagerfolderendpoint(), getConnection(), pageSize, pageNumber);
	}

	/**
	 * Get a specific file manager folder in mailchimp account account
	 * @throws IOException
	 * @throws Exception 
	 */
	public FileManagerFolder getFolder(int id) throws IOException, Exception {
		JSONObject jsonFileManagerFolder = new JSONObject(getConnection().do_Get(URLHelper.url(getConnection().getFilemanagerfolderendpoint(),"/",Integer.toString(id)),connection.getApikey()));
		return new FileManagerFolder(getConnection(), jsonFileManagerFolder);
	}

	/**
	 * Create a new folder
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
	 * Get a iterator of available images and files stored in the File Manager for the account.
	 * @return file manager file iterator
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<FileManagerFile> getFiles() throws IOException, Exception {
		return new ModelIterator<FileManagerFile>(FileManagerFile.class, getConnection().getFilesendpoint(), getConnection());
	}

	/**
	 * Get a paginated list of available images and files stored in the File Manager for the account.
	 * @param pageSize Number of records to fetch per query. Maximum value is 1000.
	 * @param pageNumber First page number to fetch starting from 0.
	 * @throws IOException
	 * @throws Exception 
	 */
	public Iterable<FileManagerFile> getFiles(int pageSize, int pageNumber) throws IOException, Exception {
		return new ModelIterator<FileManagerFile>(FileManagerFile.class, getConnection().getFilesendpoint(), getConnection(), pageSize, pageNumber);
	}

	/**
	 * Get all files in your account
	 * @throws IOException
	 * @throws Exception 
	 */
	public FileManagerFile getFile(int id) throws IOException, Exception {
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
	 * Upload a new file to mailchimp with no folder
	 * @param filename The name of the mailchimp file
	 * @param file File to be uploaded
	 * @throws JSONException
	 * @throws MalformedURLException
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
	 * Delete a file with specified fileID
	 * @param fileID
	 * @throws IOException
	 * @throws Exception 
	 */
	public void deleteFile(String fileID) throws IOException, Exception {
		connection.do_Delete(URLHelper.url(connection.getFilesendpoint(),"/",fileID),connection.getApikey());
	}

	/**
	 * Delete a folder with specified folderID
	 * @param folderID
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
