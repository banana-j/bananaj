package com.github.alexanderwe.bananaj.connection;

import com.github.alexanderwe.bananaj.exceptions.TransportException;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Created by Alexander on 10.08.2016.
 */
public class Connection {


    public String do_Get(URL url, String authorization) throws TransportException, URISyntaxException {
        CloseableHttpClient httpclient;

        HttpGet httpget = new HttpGet(url.toURI());
        httpget.addHeader("Authorization", authorization);
        httpclient = HttpClients.createDefault();
        try (CloseableHttpResponse response = httpclient.execute(httpget)) {


            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode < 200 || responseCode > 299) {
                throw buildTransportError("GET", url.toExternalForm(), response);
            }

            return createResponseFromEntity(response.getEntity());
        } catch (Exception e) {
            throw new TransportException("GET " + url.toExternalForm() + " failed", e);
        }
    }

    public String do_Post(URL url, String post_string, String authorization) throws TransportException, URISyntaxException {
        CloseableHttpClient httpclient;


        HttpPost httppost = new HttpPost(url.toURI());
        httppost.addHeader("Content-Type", "application/json; charset=UTF-8");
        httppost.addHeader("Authorization", authorization);
        httppost.setEntity(EntityBuilder.create().setBinary(post_string.getBytes(StandardCharsets.UTF_8)).build());

        httpclient = HttpClients.createDefault();
        try (CloseableHttpResponse response = httpclient.execute(httppost)) {

            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode < 200 || responseCode > 299) {
                throw buildTransportError("POST", url.toExternalForm(), response);
            }


            return createResponseFromEntity(response.getEntity());
        } catch (Exception e) {
            throw new TransportException("POST " + post_string.length() + " bytes to " + url.toExternalForm() + " failed", e);
        }
    }

    public String do_Patch(URL url, String patch_string, String authorization) throws TransportException, URISyntaxException {
        CloseableHttpClient httpclient;


        HttpPatch httppatch = new HttpPatch(url.toURI());
        httppatch.addHeader("Content-Type", "application/json; charset=UTF-8");
        httppatch.addHeader("Authorization", authorization);
        httppatch.setEntity(EntityBuilder.create().setBinary(patch_string.getBytes(StandardCharsets.UTF_8)).build());

        httpclient = HttpClients.createDefault();
        try (CloseableHttpResponse response = httpclient.execute(httppatch)) {

            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode < 200 || responseCode > 299) {
                throw buildTransportError("PATCH", url.toExternalForm(), response);
            }

            return createResponseFromEntity(response.getEntity());
        } catch (Exception e) {
            throw new TransportException("PATCH " + patch_string.length() + " bytes to " + url.toExternalForm() + " failed", e);
        }
    }


    public String do_Put(URL url, String put_string, String authorization) throws TransportException, URISyntaxException {
        CloseableHttpClient httpclient;

        HttpPut httpput = new HttpPut(url.toURI());
        httpput.addHeader("Content-Type", "application/json; charset=UTF-8");
        httpput.addHeader("Authorization", authorization);
        httpput.setEntity(EntityBuilder.create().setBinary(put_string.getBytes(StandardCharsets.UTF_8)).build());

        httpclient = HttpClients.createDefault();
        try (CloseableHttpResponse response = httpclient.execute(httpput)) {


            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode < 200 || responseCode > 299) {
                throw buildTransportError("DELETE", url.toExternalForm(), response);
            }

            return createResponseFromEntity(response.getEntity());
        } catch (Exception e) {
            throw new TransportException("PUT " + put_string.length() + " bytes to " + url.toExternalForm() + " failed", e);
        }
    }

    public String do_Post(URL url, String authorization) throws TransportException, URISyntaxException {
        CloseableHttpClient httpclient = null;

        HttpPost httppost = new HttpPost(url.toURI());
        httppost.addHeader("Content-Type", "application/json; charset=UTF-8");
        httppost.addHeader("Authorization", authorization);
        httpclient = HttpClients.createDefault();

        try (CloseableHttpResponse response = httpclient.execute(httppost)) {

            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode < 200 || responseCode > 299) {
                throw buildTransportError("POST", url.toExternalForm(), response);
            }

            return createResponseFromEntity(response.getEntity());
        } catch (Exception e) {
            throw new TransportException("POST " + url.toExternalForm() + " failed", e);
        }
    }

    public String do_Delete(URL url, String authorization) throws TransportException, URISyntaxException {
        CloseableHttpClient httpclient;

        HttpDelete httpdelete = new HttpDelete(url.toURI());
        httpdelete.addHeader("Content-Type", "application/json; charset=UTF-8");
        httpdelete.addHeader("Authorization", authorization);

        httpclient = HttpClients.createDefault();
        try (CloseableHttpResponse response = httpclient.execute(httpdelete)) {

            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode < 200 || responseCode > 299) {
                throw buildTransportError("DELETE", url.toExternalForm(), response);
            }

            return createResponseFromEntity(response.getEntity());
        } catch (Exception e) {
            throw new TransportException("DELETE " + url.toExternalForm() + " failed", e);
        }
    }


    private String createResponseFromEntity(HttpEntity entity) throws IOException {
        InputStream entityStream;
        if (entity != null) {
            long length = entity.getContentLength();
            entityStream = entity.getContent();
            StringBuilder strbuilder = new StringBuilder(length > 16 && length < Integer.MAX_VALUE ? (int) length : 200);
            try (Reader reader = new BufferedReader(new InputStreamReader
                    (entityStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
                int c = 0;
                while ((c = reader.read()) != -1) {
                    strbuilder.append((char) c);
                }
            }
            return strbuilder.toString();
        }
        return null;
    }

    private TransportException buildTransportError(String verb, String url, CloseableHttpResponse response) {
        int responseCode = response.getStatusLine().getStatusCode();
        JSONObject errObj;
        try {
            errObj = new JSONObject(createResponseFromEntity(response.getEntity()));
            String errType = getErrorObjString(errObj, "type");
            String errTitle = getErrorObjString(errObj, "title");
            String errDetail = getErrorObjString(errObj, "detail");
            String errInstance = getErrorObjString(errObj, "instance");
            return new TransportException("Status: " + Integer.toString(responseCode) + " " + verb + ": " + url + " Reason: " + response.getStatusLine().getReasonPhrase()
                    + " - " + errTitle + " Details: " + errDetail + " Instance: " + errInstance + " Type: " + errType);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return new TransportException("Status: " + Integer.toString(responseCode) + " " + verb + ": " + url + " Reason: " + response.getStatusLine().getReasonPhrase());
    }


    private String getErrorObjString(JSONObject errObj, String key) {
        if (errObj.has(key)) {
            return errObj.getString(key);
        }
        return "";
    }
}
	
