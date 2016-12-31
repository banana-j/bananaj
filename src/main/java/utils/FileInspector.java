package utils;

import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
/**
 * Class for util method for files.
 * Created by alexanderweiss on 30.12.16.
 */
public class FileInspector {

    private static FileInspector instance = null;

    protected FileInspector () {

    }

    public static FileInspector getInstance(){
        if(instance == null){
            instance = new FileInspector();
        }
        return instance;
    }

    public String getExtension(File file){
        String extension = "";

        int i = file.getName().lastIndexOf('.');
        if (i >= 0) {
            extension = file.getName().substring(i+1);
        }

        return "."+extension;
    }

    /**
     * Encode a file to base 64 binary
     * @param file
     * @return
     */
    public String encodeFileToBase64Binary(File file){
        byte[] encodedBytes = null;
        try {
            encodedBytes = Base64.encodeBase64(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
        } catch (Exception e){
            e.printStackTrace();
        }

        return new String(encodedBytes);
    }

}
