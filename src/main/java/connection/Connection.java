package connection;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Created by Alexander on 10.08.2016.
 */
public class Connection {


    public String do_Get(URL url, String authorization) throws Exception{
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("Authorization",authorization);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode+"\n");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
    public String do_Post(URL url, String post_string, String authorization) throws Exception{

        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestProperty("Authorization", authorization);
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        // Indicate that we want to write to the HTTP request body
        con.setDoOutput(true);
        con.setRequestMethod("POST");

        // Writing the post data to the HTTP request body
        BufferedWriter httpRequestBodyWriter = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
        httpRequestBodyWriter.write(post_string);
        httpRequestBodyWriter.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url + System.lineSeparator() + "Send data: " + post_string);
        System.out.println("Response Code : " + responseCode+"\n");

        // Reading from the HTTP response body
        Scanner httpResponseScanner = new Scanner(con.getInputStream());
        String inputLine;
        StringBuilder response = new StringBuilder();
        try{
            while ((inputLine = httpResponseScanner.nextLine()) != null) {
                response.append(inputLine);
            }
        }catch (NoSuchElementException nsee){
            System.out.println("Line not found error");
        }

        httpResponseScanner.close();
        return response.toString();
    }

    public String do_Post(URL url, String authorization) throws Exception{
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

        // optional default is GET
        con.setRequestMethod("POST");

        //add request header
        con.setRequestProperty("Authorization",authorization);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Response Code : " + responseCode+"\n");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    public String do_Delete(URL url, String authorization) throws Exception{
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod("DELETE");
        con.setRequestProperty("Authorization", authorization);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'DELETE' request to URL : " + url);
        System.out.println("Response Code : " + responseCode+"\n");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null)
        {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}
