package model.filemanager;

import model.MailchimpObject;
import org.json.JSONObject;

import java.util.Date;

/**
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


    public FileManagerFile(int id, int folder_id, String type, String name, String full_size_url, int size, Date createdAt, String createdBy, int width, int height, JSONObject jsonData) {
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
        return "ID: " + this.getId() +" Name: " + this.getName() + " Type: " +" Width: " + this.getWidth()+"px "  + " Height: "+ this.getHeight()+"px" +" "+this.getType() + " Folder-Id: " + this.getFolder_id();
    }
}
