package com.github.alexanderwe.bananaj.model.filemanager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;

import org.json.JSONObject;

import com.github.alexanderwe.bananaj.connection.MailChimpConnection;
import com.github.alexanderwe.bananaj.utils.DateConverter;

/**
 * Class representing one specific file manager file.
 * Created by alexanderweiss on 22.01.16.
 * TODO change methods are not working
 */
public class FileManagerFile {

	private int id;
	private int folderId;
	private FileType type;
	private String name;
	private String fullSizeUrl;
	private String thumbnailUrl;
	private int size;
	private LocalDateTime createdAt;
	private String createdBy;
	private int width;
	private int height;
	private MailChimpConnection connection;

	private static final int BUFFER_SIZE = 4096;

	public FileManagerFile(MailChimpConnection connection, JSONObject jsonObj) {
		parse(connection, jsonObj);
	}

	public FileManagerFile (Builder b){
		name = b.name;
		id = b.folderId;
	}

	public void parse(MailChimpConnection connection, JSONObject jsonObj) {
		id = jsonObj.getInt("id");
		folderId = jsonObj.getInt("folder_id");
		type = FileType.valueOf(jsonObj.getString("type").toUpperCase());
		name = jsonObj.getString("name");
		fullSizeUrl = jsonObj.getString("full_size_url");
		thumbnailUrl = jsonObj.getString("thumbnail_url");
		size = jsonObj.getInt("size");
		createdAt = DateConverter.createDateFromISO8601(jsonObj.getString("created_at"));
		createdBy = jsonObj.getString("created_by");
		this.connection = connection;

		if(jsonObj.getString("type").equals("image")) {
			width = jsonObj.getInt("width");
			height = jsonObj.getInt("height");
		}
	}

	public void update() throws Exception {
		JSONObject jsonObj = getJsonRepresentation();
		String results = getConnection().do_Patch(new URL(getConnection().getFilesendpoint()+"/"+getId()), jsonObj.toString(), getConnection().getApikey());
		parse(connection, new JSONObject(results));
	}
	
	public void delete() throws Exception {
		getConnection().do_Delete(new URL(getConnection().getFilesendpoint()+"/"+getId()), getConnection().getApikey());
	}

	//http://www.codejava.net/java-se/networking/use-httpurlconnection-to-download-file-from-an-http-url
	public void downloadFile(String saveDir)
			throws IOException {
		URL url = new URL(getFullSizeUrl());
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		int responseCode = httpConn.getResponseCode();

		// always check HTTP response code first
		if (responseCode == HttpURLConnection.HTTP_OK) {
			String fileName = "";
			String disposition = httpConn.getHeaderField("Content-Disposition");
			String contentType = httpConn.getContentType();
			int contentLength = httpConn.getContentLength();

			if (disposition != null) {
				// extracts file name from header field
				int index = disposition.indexOf("filename=");
				if (index > 0) {
					fileName = disposition.substring(index + 10,
							disposition.length() - 1);
				}
			} else {
				// extracts file name from URL
				fileName = getFullSizeUrl().substring(getFullSizeUrl().lastIndexOf("/") + 1,
						getFullSizeUrl().length());
			}

			System.out.println("Content-Type = " + contentType);
			System.out.println("Content-Disposition = " + disposition);
			System.out.println("Content-Length = " + contentLength);
			System.out.println("fileName = " + fileName);

			// opens input stream from the HTTP com.github.alexanderwe.bananaj.connection
			InputStream inputStream = httpConn.getInputStream();
			String saveFilePath = saveDir + File.separator + fileName;

			// opens an output stream to save into file
			FileOutputStream outputStream = new FileOutputStream(saveFilePath);

			int bytesRead;
			byte[] buffer = new byte[BUFFER_SIZE];
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}

			outputStream.close();
			inputStream.close();

			System.out.println("File downloaded");
		} else {
			System.out.println("No file to download. Server replied HTTP code: " + responseCode);
		}
		httpConn.disconnect();
	}

	/**
	 * @return The unique id of the file.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return The id of the folder.
	 */
	public int getFolderId() {
		return folderId;
	}

	/**
	 * Move file to a different folder. Must call {@link #update()} for change to take effect.
	 * @param folderId the folderId for the file
	 */
	public void setFolderId(int folderId) {
		this.folderId = folderId;
	}

	/**
	 * @return The type of file in the File Manager.
	 */
	public FileType getType() {
		return type;
	}

	/**
	 * @return The name of the file.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Rename the file. . Must call {@link #update()} for change to take effect.
	 * @param name the new file name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return The url of the full-size file.
	 */
	public String getFullSizeUrl() {
		return fullSizeUrl;
	}

	/**
	 * @return The url of the thumbnail preview.
	 */
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	/**
	 * @return The size of the file in bytes.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @return The date and time a file was added to the File Manager
	 */
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	/**
	 * @return The username of the profile that uploaded the file.
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @return The width of the image.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return The height of an image.
	 */
	public int getHeight() {
		return height;
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
		jsonObj.put("folder_id", getFolderId());

		return jsonObj;
	}
	
	@Override
	public String toString(){
		return 
				"ID: " + getId() +
				" Name: " + getName() + 
				" Type: " + getType().toString() + 
				(getType() == FileType.IMAGE ?  
					" Width: " + getWidth()+"px " + 
					" Height: "+ getHeight()+"px" : "" ) +
				" Folder-Id: " + getId();
	}


	public static class Builder {
		private String name;
		private int folderId;

		public FileManagerFile.Builder name(String name) {
			this.name = name;
			return this;
		}

		public FileManagerFile.Builder folder(int folderId) {
			this.folderId = folderId;
			return this;
		}

		public FileManagerFile build() {
			return new FileManagerFile(this);
		}
	}

}
