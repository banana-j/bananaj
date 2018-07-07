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
import com.github.alexanderwe.bananaj.model.MailchimpObject;
import com.github.alexanderwe.bananaj.utils.DateConverter;
import com.github.alexanderwe.bananaj.utils.FileInspector;

/**
 * Class for representing one specific file manager file.
 * Created by alexanderweiss on 22.01.16.
 * TODO change methods are not working
 */
public class FileManagerFile extends MailchimpObject {

    private int folder_id;
    private String type;
    private String name;
    private String full_size_url;
    private int size;
    private LocalDateTime createdAt;
    private String createdBy;
    private int width;
    private int height;
    private String file_data;
    private MailChimpConnection connection;
    private boolean isImage;

    private static final int BUFFER_SIZE = 4096;


    /**
     * File is an image
     * @param id
     * @param folder_id
     * @param type
     * @param name
     * @param full_size_url
     * @param size
     * @param createdAt
     * @param createdBy
     * @param width
     * @param height
     * @param jsonData
     */
    public FileManagerFile(int id, int folder_id, String type, String name, String full_size_url, int size, LocalDateTime createdAt, String createdBy, int width, int height, MailChimpConnection connection,  JSONObject jsonData) {
        super(String.valueOf(id),jsonData);
        this.folder_id = folder_id;
        this.type = type;
        this.name = name;
        this.full_size_url = full_size_url;
        this.size = size;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.width = width;
        this.height = height;
        this.connection = connection;
        this.isImage = true;
    }

    /**
     * File is not an image
     * @param id
     * @param folder_id
     * @param type
     * @param name
     * @param full_size_url
     * @param size
     * @param createdAt
     * @param createdBy
     * @param jsonData
     */
    public FileManagerFile(int id, int folder_id, String type, String name, String full_size_url, int size, LocalDateTime createdAt, String createdBy, MailChimpConnection connection, JSONObject jsonData) {
        super(String.valueOf(id),jsonData);
        this.folder_id = folder_id;
        this.type = type;
        this.name = name;
        this.full_size_url = full_size_url;
        this.size = size;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.connection = connection;
        this.isImage = false;
    }

    public FileManagerFile(MailChimpConnection connection, JSONObject jsonFileManagerFile) {
        super(String.valueOf(jsonFileManagerFile.getInt("id")), jsonFileManagerFile);
        this.folder_id = jsonFileManagerFile.getInt("id");
        this.folder_id = jsonFileManagerFile.getInt("folder_id");
        this.type = jsonFileManagerFile.getString("type");
        this.name = jsonFileManagerFile.getString("name");
        this.full_size_url = jsonFileManagerFile.getString("full_size_url");
        this.size = jsonFileManagerFile.getInt("size");
        this.createdAt = DateConverter.getInstance().createDateFromISO8601(jsonFileManagerFile.getString("created_at"));
        this.createdBy = jsonFileManagerFile.getString("created_by");
        this.connection = connection;
        this.isImage = false;
        
        if(jsonFileManagerFile.getString("type").equals("image")) {
            this.isImage = true;
            this.width = jsonFileManagerFile.getInt("width");
            this.height = jsonFileManagerFile.getInt("height");
        } else {
            this.isImage = false;
        }
    }

    public FileManagerFile (Builder b){
        this.name = b.name;
        this.file_data = b.file_data;
        this.folder_id = b.folderId;
    }

    /**
     * Change the name of the file
     * @param name
     * @throws Exception
     */
    public void changeName(String name) throws Exception{
        JSONObject changedFileName = new JSONObject();
        changedFileName.put("name", name);
        changedFileName.put("folder_id", this.getFolder_id());
        this.connection.do_Patch(new URL(this.getConnection().getFilesendpoint()+"/"+this.getId()), changedFileName.toString(), this.getConnection().getApikey());
        this.name = name;
    }

    /**
     * Change the folder of this file
     * @param folderID  Setting folderID to "0" will remove a file from its current folder.
     * @throws Exception
     */
    public void changeFolder(int folderID) throws Exception {
        JSONObject changedFileName = new JSONObject();
        changedFileName.put("name", name);
        changedFileName.put("folder_id", folderID);
        this.connection.do_Patch(new URL(this.getConnection().getFilesendpoint()+"/"+this.getId()), changedFileName.toString(), this.getConnection().getApikey());
        this.folder_id = folderID;
    }

    public void deleteFile() throws Exception {
    	getConnection().do_Delete(new URL(getConnection().getFilesendpoint()+"/"+getId()), getConnection().getApikey());
    }

    //http://www.codejava.net/java-se/networking/use-httpurlconnection-to-download-file-from-an-http-url
    public void downloadFile(String saveDir)
            throws IOException {
        URL url = new URL(this.getFull_size_url());
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
                fileName = this.getFull_size_url().substring(this.getFull_size_url().lastIndexOf("/") + 1,
                        this.getFull_size_url().length());
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

    public int getFolder_id() {
        return folder_id;
    }


    public String getType() {
        return type;
    }


    public String getName() {
        return name;
    }


    public String getFull_size_url() {
        return full_size_url;
    }


    public int getSize() {
        return size;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public String getCreatedBy() {
        return createdBy;
    }


    public int getWidth() {
        return width;
    }


    public int getHeight() {
        return height;
    }


    public String getFile_data() {
        return file_data;
    }

    public MailChimpConnection getConnection() {
        return connection;
    }


    public boolean isImage(){
        return this.isImage;
    }

    
    @Override
    public String toString(){
        return "ID: " + this.getId() +" Name: " + this.getName() + " Type: " + this.getType() + " Width: " + this.getWidth()+"px "  + " Height: "+ this.getHeight()+"px" +" "+this.getType() + " Folder-Id: " + this.getFolder_id();
    }


    public static class Builder {
        private String name;
        private String file_data;
        private int folderId;
        private JSONObject jsonRepresentation = new JSONObject();

        public FileManagerFile.Builder name(String name) {
            this.name = name;
            return this;
        }

        public FileManagerFile.Builder folder(int folderId) {
            this.folderId = folderId;
            return this;
        }

        public FileManagerFile.Builder fileData(String file_data) {
            this.file_data = file_data;
            return this;
        }

        /**
         * Extract the data of the file
         * @param file
         * @return
         */
        public FileManagerFile.Builder fileData(File file) {
            this.file_data = FileInspector.getInstance().encodeFileToBase64Binary(file);
            return this;
        }

        public FileManagerFile build() {
            return new FileManagerFile(this);
        }
    }

}
