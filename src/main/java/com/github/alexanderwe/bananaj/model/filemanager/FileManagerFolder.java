package model.filemanager;

import connection.MailChimpConnection;
import model.MailchimpObject;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.DateConverter;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.TimeZone;

/**
 * Class for representing a file manager folder.
 * Created by alexanderweiss on 22.01.16.
 */
public class FileManagerFolder extends MailchimpObject{


    private int id;
    private String name;
    private int file_count;
    private LocalDateTime createdAt;
    private String createdBy;
    private ArrayList<FileManagerFile> files;
    private JSONObject jsonData;
    private MailChimpConnection connection;



    public FileManagerFolder(int id, String name, int file_count, LocalDateTime createdAt, String createdBy, JSONObject jsonData, MailChimpConnection connection){
        super(String.valueOf(id),jsonData);
        this.name = name;
        this.file_count = file_count;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.connection = connection;
        try{
            setFiles();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void changeName(String name) throws Exception{
        JSONObject changedFolder  = new JSONObject();
        changedFolder.put("name", name);
        this.connection.do_Patch(new URL(this.getConnection().getFilemanagerfolderendpoint()+"/"+this.getId()), changedFolder.toString(), this.getConnection().getApikey());

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
        return files;
    }

    public void setFiles() throws Exception{
        ArrayList<FileManagerFile> files = new ArrayList<FileManagerFile>();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        // parse response
        JSONObject jsonFileManagerFiles = new JSONObject(getConnection().do_Get(new URL(connection.getFilesendpoint()),connection.getApikey()));
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

            if(file.getFolder_id() == Integer.parseInt(this.getId())) {
                files.add(file);
            }
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
