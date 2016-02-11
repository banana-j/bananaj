package model.filemanager;

import connection.MailchimpConnection;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by alexanderweiss on 06.02.16.
 */

public class FileManager {

    private MailchimpConnection connection;

    public FileManager(MailchimpConnection connection){
        setConnection(connection);
    }


    /**
     * Get all file manager folders in mailchimp account account
     * @return
     * @throws Exception
     */
    public ArrayList<FileManagerFolder> getFileManagerFolders() throws Exception{
        ArrayList<FileManagerFolder> fileManagerFolders = new ArrayList<FileManagerFolder>();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        JSONObject jsonFileManagerFolders = new JSONObject(getConnection().do_Get(new URL(getConnection().getFILEMANAGERFOLDERENDPOINT())));
        JSONArray folderArray = jsonFileManagerFolders.getJSONArray("folders");
        for( int i = 0; i< folderArray.length();i++)
        {
            JSONObject folderDetail = folderArray.getJSONObject(i);
            FileManagerFolder folder = new FileManagerFolder(folderDetail.getInt("id"),folderDetail.getString("name"),folderDetail.getInt("file_count"),folderDetail.getString("created_at"), folderDetail.getString("created_by"),folderDetail,this.getConnection());
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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        JSONObject jsonFileManagerFolder = new JSONObject(getConnection().do_Get(new URL(getConnection().getFILEMANAGERFOLDERENDPOINT()+"/"+id)));
        return new FileManagerFolder(jsonFileManagerFolder.getInt("id"),jsonFileManagerFolder.getString("name"),jsonFileManagerFolder.getInt("file_count"),jsonFileManagerFolder.getString("created_at"), jsonFileManagerFolder.getString("created_by"),jsonFileManagerFolder,this.getConnection());
    }


    /**
     * Get all files in your account
     * @return
     * @throws Exception
     */
    public ArrayList<FileManagerFile> getFileManagerFiles() throws Exception{
        ArrayList<FileManagerFile> files = new ArrayList<FileManagerFile>();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        // parse response
        JSONObject jsonFileManagerFiles = new JSONObject(getConnection().do_Get(new URL(this.getConnection().getFILESENDPOINT())));
        JSONArray filesArray = jsonFileManagerFiles.getJSONArray("files");
        for( int i = 0; i< filesArray.length();i++)
        {
            FileManagerFile file = null;
            JSONObject fileDetail = filesArray.getJSONObject(i);
            if(fileDetail.getString("type").equals("image")){
                file = new FileManagerFile(fileDetail.getInt("id"),fileDetail.getInt("folder_id"),fileDetail.getString("type"),fileDetail.getString("name"),fileDetail.getString("full_size_url"),fileDetail.getInt("size"),formatter.parse(fileDetail.getString("created_at")),fileDetail.getString("created_by"), fileDetail.getInt("width"), fileDetail.getInt("height"), fileDetail);
            }else{
                file = new FileManagerFile(fileDetail.getInt("id"),fileDetail.getInt("folder_id"),fileDetail.getString("type"),fileDetail.getString("name"),fileDetail.getString("full_size_url"),fileDetail.getInt("size"),formatter.parse(fileDetail.getString("created_at")),fileDetail.getString("created_by"), fileDetail);

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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        // parse response
        JSONObject jsonFileManagerFile = new JSONObject(getConnection().do_Get(new URL(this.getConnection().getFILESENDPOINT()+"/"+id)));

        if(jsonFileManagerFile.getString("type").equals("image")){
            return new FileManagerFile(jsonFileManagerFile.getInt("id"),jsonFileManagerFile.getInt("folder_id"),jsonFileManagerFile.getString("type"),jsonFileManagerFile.getString("name"),jsonFileManagerFile.getString("full_size_url"),jsonFileManagerFile.getInt("size"),formatter.parse(jsonFileManagerFile.getString("created_at")),jsonFileManagerFile.getString("created_by"), jsonFileManagerFile.getInt("width"), jsonFileManagerFile.getInt("height"), jsonFileManagerFile);
        }else{
            return new FileManagerFile(jsonFileManagerFile.getInt("id"),jsonFileManagerFile.getInt("folder_id"),jsonFileManagerFile.getString("type"),jsonFileManagerFile.getString("name"),jsonFileManagerFile.getString("full_size_url"),jsonFileManagerFile.getInt("size"),formatter.parse(jsonFileManagerFile.getString("created_at")),jsonFileManagerFile.getString("created_by"), jsonFileManagerFile);
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
        upload_data.put("name", filename+getExtension(file));
        upload_data.put("file_data",encodeFileToBase64Binary(file));
        getConnection().do_Post(new URL(connection.getFILESENDPOINT()), upload_data.toString());
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
        upload_data.put("name", filename+getExtension(file));
        upload_data.put("file_data",encodeFileToBase64Binary(file));
        getConnection().do_Post(new URL(connection.getFILESENDPOINT()), upload_data.toString());
    }



    public void deleteFile(String fileID) throws Exception{
        connection.do_Delete(new URL(connection.getFILESENDPOINT()+"/"+fileID));
    }

    private String encodeFileToBase64Binary(File file){
        byte[] encodedBytes = null;
        try {
            encodedBytes = Base64.encodeBase64(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
        } catch (Exception e){
            e.printStackTrace();
        }

        return new String(encodedBytes);
    }



    private String getExtension(File file){
        String extension = "";

        int i = file.getName().lastIndexOf('.');
        if (i >= 0) {
            extension = file.getName().substring(i+1);
        }

        return "."+extension;
    }

    public MailchimpConnection getConnection() {
        return connection;
    }

    public void setConnection(MailchimpConnection connection) {
        this.connection = connection;
    }
}
