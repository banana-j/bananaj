package com.github.alexanderwe.bananaj.model.filemanager;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONObject;

import com.github.alexanderwe.bananaj.connection.MailChimpConnection;
import com.github.alexanderwe.bananaj.model.MailchimpObject;
import com.github.alexanderwe.bananaj.utils.DateConverter;

/**
 * Class for representing a file manager folder.
 * Created by alexanderweiss on 22.01.16.
 */
public class FileManagerFolder extends MailchimpObject{


    private int folderId;
    private String name;
    private int file_count;
    private LocalDateTime createdAt;
    private String createdBy;
    private ArrayList<FileManagerFile> files;
    private JSONObject jsonData;
    private MailChimpConnection connection;



    public FileManagerFolder(int id, String name, int file_count, LocalDateTime createdAt, String createdBy, JSONObject jsonData, MailChimpConnection connection){
        super(String.valueOf(id),jsonData);	// set string representation of folder id
        this.folderId = id;	// set integer representation of folder id
        this.name = name;
        this.file_count = file_count;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.connection = connection;
    }

    public FileManagerFolder(MailChimpConnection connection, JSONObject jsonFileManagerFolder){
        super(String.valueOf(jsonFileManagerFolder.getInt("id")), jsonFileManagerFolder);	// set string representation of folder id
        this.folderId = jsonFileManagerFolder.getInt("id");	// set integer representation of folder id
        this.name = jsonFileManagerFolder.getString("name");
        this.file_count = jsonFileManagerFolder.getInt("file_count");
        this.createdAt = DateConverter.getInstance().createDateFromISO8601(jsonFileManagerFolder.getString("created_at"));
        this.createdBy = jsonFileManagerFolder.getString("created_by");
        this.connection = connection;
    }

    public void changeName(String name) throws Exception{
        JSONObject changedFolder  = new JSONObject();
        changedFolder.put("name", name);
        this.connection.do_Patch(new URL(this.getConnection().getFilemanagerfolderendpoint()+"/"+this.getId()), changedFolder.toString(), this.getConnection().getApikey());

    }

    public void deleteFolder() throws Exception {
    	getConnection().do_Delete(new URL(getConnection().getFilemanagerfolderendpoint()+"/"+getId()), getConnection().getApikey());
    }

    public String getName() {
        return name;
    }

    public int getFile_count() {
        return file_count;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public ArrayList<FileManagerFile> getFiles() {
    	if (files == null) {	// defer loading files list untill requested.
            try{
                setFiles();
            }catch(Exception e){
                e.printStackTrace();
            }
    	}
        return files;
    }

    private void setFiles() throws Exception {
		ArrayList<FileManagerFile> files = new ArrayList<FileManagerFile>();
    	if (file_count > 0) {

    		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
    		
    		int offset = 0;
    		int count = 100;
			List<FileManagerFile> filelist;

    		do {
				filelist = connection.getFileManager().getFileManagerFiles(count, offset);
				offset += count;
				for(FileManagerFile file : filelist) {
	    			if(file.getFolder_id() == folderId) {
	    				files.add(file);
	    			}
				}
    		} while (filelist.size() == 100 && files.size() < file_count);
    	}
    	this.files = files;
    }

    public FileManagerFile getFile(int id){
        for (FileManagerFile file:files){
            if(Integer.parseInt(file.getId()) == id){
                return file;
            }
        }
        return null;
    }

    public int getFolderId() {
    	return folderId;
    }

    public JSONObject getJsonData() {
        return jsonData;
    }

    public MailChimpConnection getConnection() {
        return connection;
    }

	@Override
    public String toString(){
        return "Folder-name: " + this.getName() + "Folder-Id: " + this.getId() + " File-count: " + this.getFile_count() + " Created at: " + this.getCreatedAt() +System.lineSeparator()+ " Files: "+ this.getFiles();
    }
}
