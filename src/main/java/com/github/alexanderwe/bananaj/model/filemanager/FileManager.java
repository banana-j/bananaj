package com.github.alexanderwe.bananaj.model.filemanager;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.alexanderwe.bananaj.connection.MailChimpConnection;
import com.github.alexanderwe.bananaj.utils.FileInspector;

/**
 * Class for uploading and deleting files
 * Created by alexanderweiss on 06.02.16.
 */

public class FileManager {

    private MailChimpConnection connection;

    public FileManager(MailChimpConnection connection) {
       this.connection = connection;
    }

    /**
     * Get file manager folders in mailchimp account account
     * @return List containing the first 100 folders
     * @throws Exception
     */
    public List<FileManagerFolder> getFileManagerFolders() throws Exception {
    	return getFileManagerFolders(100,0);
    }

    /**
     * Get file manager folders in mailchimp account account with pagination
	 * @param count Number of folders to return
	 * @param offset Zero based offset
     * @return
     * @throws Exception
     */
    public List<FileManagerFolder> getFileManagerFolders(int count, int offset) throws Exception {
        List<FileManagerFolder> fileManagerFolders = new ArrayList<FileManagerFolder>();

        JSONObject jsonFileManagerFolders = new JSONObject(getConnection().do_Get(new URL(getConnection().getFilemanagerfolderendpoint() 
        		+ "?offset=" + offset + "&count=" + count),connection.getApikey()));
        JSONArray folderArray = jsonFileManagerFolders.getJSONArray("folders");
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
     * @return
     * @throws Exception
     */
    public FileManagerFolder getFileManagerFolder(int id) throws Exception {
        JSONObject jsonFileManagerFolder = new JSONObject(getConnection().do_Get(new URL(getConnection().getFilemanagerfolderendpoint()+"/"+id),connection.getApikey()));
        return new FileManagerFolder(getConnection(), jsonFileManagerFolder);
    }

    /**
     * Create a new folder
     * @param name The name of the folder
     * @return
     * @throws Exception
     */
    public FileManagerFolder createFileManagerFolder(String name) throws Exception {
        JSONObject newFolder  = new JSONObject();
        newFolder.put("name", name);
        JSONObject jsonFileManagerFolder = new JSONObject(this.connection.do_Post(new URL(this.getConnection().getFilemanagerfolderendpoint()), newFolder.toString(), this.getConnection().getApikey()));
        return new FileManagerFolder(getConnection(), jsonFileManagerFolder);
    }
    
    /**
     * Get files in your account
     * @return List containing the first 100 files
     * @throws Exception
     */
    public List<FileManagerFile> getFileManagerFiles() throws Exception{
        return getFileManagerFiles(100,0);
    }

    /**
     * Get files in your account with pagination
	 * @param count Number of lists to return
	 * @param offset Zero based offset
     * @return
     * @throws Exception
     */
    public List<FileManagerFile> getFileManagerFiles(int count, int offset) throws Exception{
        List<FileManagerFile> files = new ArrayList<FileManagerFile>();


        // parse response
        JSONObject jsonFileManagerFiles = new JSONObject(getConnection().do_Get(new URL(getConnection().getFilesendpoint() 
        		+ "?offset=" + offset + "&count=" + count),getConnection().getApikey()));
        JSONArray filesArray = jsonFileManagerFiles.getJSONArray("files");
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
     * @return
     * @throws Exception
     */
    public FileManagerFile getFileManagerFile(int id) throws Exception {
        // parse response
        JSONObject jsonFileManagerFile = new JSONObject(getConnection().do_Get(new URL(this.getConnection().getFilesendpoint()+"/"+id),getConnection().getApikey()));
        return new FileManagerFile(getConnection(), jsonFileManagerFile);
    }

    /**
     * Upload a new file with folder id
     * @param folder_id The id of the folder
     * @param filename The name of the file
     * @param file
     * @return
     * @throws JSONException
     * @throws MalformedURLException
     * @throws Exception
     */
    public FileManagerFile upload(int folder_id, String filename, File file) throws JSONException, MalformedURLException, Exception{
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
     * Upload a new file without folder id
     * @param filename The name of the file
     * @param file
     * @return
     * @throws JSONException
     * @throws MalformedURLException
     * @throws Exception
     */
    public FileManagerFile upload(String filename, File file) throws JSONException, MalformedURLException, Exception{
    	String fExt = FileInspector.getInstance().getExtension(file);
    	String fName = filename.endsWith(fExt) ? filename : filename+fExt;
        JSONObject upload_data  = new JSONObject();
        upload_data.put("name", fName);
        upload_data.put("file_data",FileInspector.getInstance().encodeFileToBase64Binary(file));
        JSONObject jsonFileManagerFile = new JSONObject(getConnection().do_Post(new URL(connection.getFilesendpoint()), upload_data.toString(),connection.getApikey()));
        return new FileManagerFile(getConnection(), jsonFileManagerFile);
    }

    /**
     * Delete a file with specific fileID
     * @param fileID
     * @throws Exception
     */
    public void deleteFile(String fileID) throws Exception{
        connection.do_Delete(new URL(connection.getFilesendpoint()+"/"+fileID),connection.getApikey());
    }

    public MailChimpConnection getConnection() {
        return connection;
    }

}
