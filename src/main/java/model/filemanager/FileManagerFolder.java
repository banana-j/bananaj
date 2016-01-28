package model.filemanager;

import model.MailchimpObject;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by alexanderweiss on 22.01.16.
 */
public class FileManagerFolder extends MailchimpObject{


    private int id;
    private String name;
    private int file_count;
    private Date createdAt;
    private String createdBy;
    private ArrayList<FileManagerFile> files;
    private JSONObject jsonData;



    public FileManagerFolder(int id, String name, int file_count, Date createdAt, String createdBy, JSONObject jsonData){
        super(String.valueOf(id),jsonData);
        setName(name);
        setFile_count(file_count);
        setCreatedAt(createdAt);
        setCreatedBy(createdBy);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFile_count() {
        return file_count;
    }

    public void setFile_count(int file_count) {
        this.file_count = file_count;
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

    public ArrayList<FileManagerFile> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<FileManagerFile> files) {
        this.files = files;
    }


    public JSONObject getJsonData() {
        return jsonData;
    }

    public void setJsonData(JSONObject jsonData) {
        this.jsonData = jsonData;
    }
}
