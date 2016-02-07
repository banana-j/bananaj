package model.filemanager;

import connection.MailchimpConnection;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by alexanderweiss on 06.02.16.
 */

public class FileManager {

    private MailchimpConnection connection;

    public FileManager(MailchimpConnection connection){
        setConnection(connection);
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
