package model.filemanager;

import model.MailchimpObject;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * Class for representing one specific file manager file.
 * Created by alexanderweiss on 22.01.16.
 */
public class FileManagerFile extends MailchimpObject {

    private int folder_id;
    private String type;
    private String name;
    private String full_size_url;
    private int size;
    private Date createdAt;
    private String createdBy;
    private int width;
    private int height;
    private String file_data;

    private static final int BUFFER_SIZE = 4096;


    public FileManagerFile(int id, int folder_id, String type, String name, String full_size_url, int size, Date createdAt, String createdBy, int width, int height,  JSONObject jsonData) {
        super(String.valueOf(id),jsonData);
        setFolder_id(folder_id);
        setType(type);
        setName(name);
        setFull_size_url(full_size_url);
        setSize(size);
        setCreatedAt(createdAt);
        setCreatedBy(createdBy);
        setWidth(width);
        setHeight(height);
        setFile_data(file_data);
    }

    public FileManagerFile(int id, int folder_id, String type, String name, String full_size_url, int size, Date createdAt, String createdBy, JSONObject jsonData) {
        super(String.valueOf(id), jsonData);
        setFolder_id(folder_id);
        setType(type);
        setName(name);
        setFull_size_url(full_size_url);
        setSize(size);
        setCreatedAt(createdAt);
        setCreatedBy(createdBy);
        setFile_data(file_data);
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

            // opens input stream from the HTTP connection
            InputStream inputStream = httpConn.getInputStream();
            String saveFilePath = saveDir + File.separator + fileName;

            // opens an output stream to save into file
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);

            int bytesRead = -1;
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

    public void setFolder_id(int folder_id) {
        this.folder_id = folder_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFull_size_url() {
        return full_size_url;
    }

    public void setFull_size_url(String full_size_url) {
        this.full_size_url = full_size_url;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getFile_data() {
        return file_data;
    }

    public void setFile_data(String file_data) {
        this.file_data = file_data;
    }
    
    @Override
    public String toString(){
        return "ID: " + this.getId() +" Name: " + this.getName() + " Type: " + this.getType() + " Width: " + this.getWidth()+"px "  + " Height: "+ this.getHeight()+"px" +" "+this.getType() + " Folder-Id: " + this.getFolder_id();
    }
}
