package model.filemanager;

import connection.MailChimpConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utils.DateConverter;
import utils.FileInspector;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for uploading and deleting files
 * Created by alexanderweiss on 06.02.16.
 */

public class FileManager {

    private MailChimpConnection connection;

    public FileManager(MailChimpConnection connection){
       this.connection = connection;
    }

    /**
     * Get all file manager folders in mailchimp account account
     * @return
     * @throws Exception
     */
    public List<FileManagerFolder> getFileManagerFolders() throws Exception{
        List<FileManagerFolder> fileManagerFolders = new ArrayList<FileManagerFolder>();

        JSONObject jsonFileManagerFolders = new JSONObject(getConnection().do_Get(new URL(getConnection().getFilemanagerfolderendpoint()),connection.getApikey()));
        JSONArray folderArray = jsonFileManagerFolders.getJSONArray("folders");
        for( int i = 0; i< folderArray.length();i++)
        {
            JSONObject folderDetail = folderArray.getJSONObject(i);
            FileManagerFolder folder = new FileManagerFolder(folderDetail.getInt("id"),folderDetail.getString("name"),folderDetail.getInt("file_count"),DateConverter.getInstance().createDateFromISO8601(folderDetail.getString("created_at")), folderDetail.getString("created_by"),folderDetail,this.getConnection());
            fileManagerFolders.add(folder);
        }
        return fileManagerFolders;
    }

    /**
     * Get a specific file manager folder in mailchimp account account
     * @return
     * @throws Exception
     */
    public FileManagerFolder getFileManagerFolder(int id) throws Exception{
        JSONObject jsonFileManagerFolder = new JSONObject(getConnection().do_Get(new URL(getConnection().getFilemanagerfolderendpoint()+"/"+id),connection.getApikey()));
        return new FileManagerFolder(jsonFileManagerFolder.getInt("id"),jsonFileManagerFolder.getString("name"),jsonFileManagerFolder.getInt("file_count"),DateConverter.getInstance().createDateFromISO8601(jsonFileManagerFolder.getString("created_at")), jsonFileManagerFolder.getString("created_by"),jsonFileManagerFolder,this.getConnection());
    }

    /**
     * Get all files in your account
     * @return
     * @throws Exception
     */
    public List<FileManagerFile> getFileManagerFiles() throws Exception{
        List<FileManagerFile> files = new ArrayList<FileManagerFile>();


        // parse response
        JSONObject jsonFileManagerFiles = new JSONObject(getConnection().do_Get(new URL(this.getConnection().getFilesendpoint()),connection.getApikey()));
        JSONArray filesArray = jsonFileManagerFiles.getJSONArray("files");
        for( int i = 0; i< filesArray.length();i++)
        {
            FileManagerFile file;
            JSONObject fileDetail = filesArray.getJSONObject(i);
            if(fileDetail.getString("type").equals("image")){
                file = new FileManagerFile(fileDetail.getInt("id"),fileDetail.getInt("folder_id"),fileDetail.getString("type"),fileDetail.getString("name"),fileDetail.getString("full_size_url"),fileDetail.getInt("size"), DateConverter.getInstance().createDateFromISO8601(fileDetail.getString("created_at")),fileDetail.getString("created_by"), fileDetail.getInt("width"), fileDetail.getInt("height"), this.getConnection(), fileDetail);
            }else{
                file = new FileManagerFile(fileDetail.getInt("id"),fileDetail.getInt("folder_id"),fileDetail.getString("type"),fileDetail.getString("name"),fileDetail.getString("full_size_url"),fileDetail.getInt("size"),DateConverter.getInstance().createDateFromISO8601(fileDetail.getString("created_at")),fileDetail.getString("created_by"), this.getConnection(),fileDetail);

            }

            files.add(file);
        }
        return files;
    }

    /**
     * Get all files in your account
     * @return
     * @throws Exception
     */
    public FileManagerFile getFileManagerFile(int id) throws Exception{
        // parse response
        JSONObject jsonFileManagerFile = new JSONObject(getConnection().do_Get(new URL(this.getConnection().getFilesendpoint()+"/"+id),connection.getApikey()));

        if(jsonFileManagerFile.getString("type").equals("image")){
            return new FileManagerFile(jsonFileManagerFile.getInt("id"),jsonFileManagerFile.getInt("folder_id"),jsonFileManagerFile.getString("type"),jsonFileManagerFile.getString("name"),jsonFileManagerFile.getString("full_size_url"),jsonFileManagerFile.getInt("size"),DateConverter.getInstance().createDateFromISO8601(jsonFileManagerFile.getString("created_at")),jsonFileManagerFile.getString("created_by"), jsonFileManagerFile.getInt("width"), jsonFileManagerFile.getInt("height"), this.getConnection(),jsonFileManagerFile);
        }else{
            return new FileManagerFile(jsonFileManagerFile.getInt("id"),jsonFileManagerFile.getInt("folder_id"),jsonFileManagerFile.getString("type"),jsonFileManagerFile.getString("name"),jsonFileManagerFile.getString("full_size_url"),jsonFileManagerFile.getInt("size"),DateConverter.getInstance().createDateFromISO8601(jsonFileManagerFile.getString("created_at")),jsonFileManagerFile.getString("created_by"), this.getConnection(), jsonFileManagerFile);
        }
    }

    /**
     * Upload a file with folder id
     * @param folder_id
     * @param filename
     * @param file
     * @throws JSONException
     * @throws MalformedURLException
     * @throws Exception
     */
    public void upload(int folder_id,String filename, File file) throws JSONException, MalformedURLException, Exception{
        JSONObject upload_data  = new JSONObject();
        upload_data.put("folder_id", folder_id);
        upload_data.put("name", filename+FileInspector.getInstance().getExtension(file));
        upload_data.put("file_data",FileInspector.getInstance().encodeFileToBase64Binary(file));
        getConnection().do_Post(new URL(connection.getFilesendpoint()), upload_data.toString(),connection.getApikey());
    }

    /**
     * Upload a file without folder id
     * @param filename
     * @param file
     * @throws JSONException
     * @throws MalformedURLException
     * @throws Exception
     */
    public void upload(String filename, File file) throws JSONException, MalformedURLException, Exception{
        JSONObject upload_data  = new JSONObject();
        upload_data.put("name", filename+ FileInspector.getInstance().getExtension(file));
        upload_data.put("file_data",FileInspector.getInstance().encodeFileToBase64Binary(file));
        getConnection().do_Post(new URL(connection.getFilesendpoint()), upload_data.toString(),connection.getApikey());
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
