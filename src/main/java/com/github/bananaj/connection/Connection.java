package com.github.bananaj.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Low level HTTP/HTTPS protocol handler
 */
public class Connection {

	final static Logger logger = Logger.getLogger(Connection.class);
	
    public String do_Get(URL url, String authorization) throws IOException, URISyntaxException {
    	log("GET", url, null);
        CloseableHttpClient httpclient;

        HttpGet httpget = new HttpGet(url.toURI());
        httpget.addHeader("Authorization", authorization);
        httpclient = HttpClients.createDefault();
        
        /*// TODO: Add option to ignore certificate issues for testing (i.e. like curl -k) 
        try {
        	// ignore certificate issues
        	httpclient = HttpClients
        			.custom()
        			.setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
        			.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
        			.build();
        } catch (Exception e) {
        	logger.error("GET " + url.toString() + " : " + e.getMessage(), e);
        	System.exit(1);
        	httpclient = null;
        }
        */
        
        try (CloseableHttpResponse response = httpclient.execute(httpget)) {

            int responseCode = response.getStatusLine().getStatusCode();
            logger.debug(response.getStatusLine().getReasonPhrase().toString());
            if (responseCode < 200 || responseCode > 299) {
                throw buildTransportError("GET", url.toExternalForm(), response);
            }

            return createResponseFromEntity(response.getEntity());
        } catch (IOException e) {
        	logger.error("GET " + url.toString() + " : " + e.getMessage(), e);
        	throw e;
        } catch (Exception e) {
        	logger.error("GET " + url.toString() + " : " + e.getMessage(), e);
            throw new IOException("GET " + url.toExternalForm() + " failed", e);
        }
    }

    public String do_Post(URL url, String post_string, String authorization) throws IOException, URISyntaxException {
    	log("POST", url, post_string);
        CloseableHttpClient httpclient;

        HttpPost httppost = new HttpPost(url.toURI());
        httppost.addHeader("Content-Type", "application/json; charset=UTF-8");
        httppost.addHeader("Authorization", authorization);
        httppost.setEntity(EntityBuilder.create().setBinary(post_string.getBytes(StandardCharsets.UTF_8)).build());

        httpclient = HttpClients.createDefault();
        try (CloseableHttpResponse response = httpclient.execute(httppost)) {

            int responseCode = response.getStatusLine().getStatusCode();
            logger.debug(response.getStatusLine().getReasonPhrase());
            if (responseCode < 200 || responseCode > 299) {
                throw buildTransportError("POST", url.toExternalForm(), response);
            }


            return createResponseFromEntity(response.getEntity());
        } catch (IOException e) {
        	logger.error("POST " + url.toString() + " : " + e.getMessage(), e);
        	throw e;
        } catch (Exception e) {
        	logger.error("POST " + url.toString() + " : " + e.getMessage(), e);
            throw new IOException("POST " + post_string.length() + " bytes to " + url.toExternalForm() + " failed", e);
        }
    }

    public String do_Patch(URL url, String patch_string, String authorization) throws IOException, URISyntaxException {
    	log("PATCH", url, patch_string);
        CloseableHttpClient httpclient;

        HttpPatch httppatch = new HttpPatch(url.toURI());
        httppatch.addHeader("Content-Type", "application/json; charset=UTF-8");
        httppatch.addHeader("Authorization", authorization);
        httppatch.setEntity(EntityBuilder.create().setBinary(patch_string.getBytes(StandardCharsets.UTF_8)).build());

        httpclient = HttpClients.createDefault();
        try (CloseableHttpResponse response = httpclient.execute(httppatch)) {

            int responseCode = response.getStatusLine().getStatusCode();
            logger.debug(response.getStatusLine().getReasonPhrase());
            if (responseCode < 200 || responseCode > 299) {
                throw buildTransportError("PATCH", url.toExternalForm(), response);
            }

            return createResponseFromEntity(response.getEntity());
        } catch (IOException e) {
        	logger.error("PATCH " + url.toString() + " : " + e.getMessage(), e);
        	throw e;
        } catch (Exception e) {
        	logger.error("PATCH " + url.toString() + " : " + e.getMessage(), e);
            throw new IOException("PATCH " + patch_string.length() + " bytes to " + url.toExternalForm() + " failed", e);
        }
    }


    public String do_Put(URL url, String put_string, String authorization) throws IOException, URISyntaxException {
    	log("PUT", url, put_string);
        CloseableHttpClient httpclient;

        HttpPut httpput = new HttpPut(url.toURI());
        httpput.addHeader("Content-Type", "application/json; charset=UTF-8");
        httpput.addHeader("Authorization", authorization);
        httpput.setEntity(EntityBuilder.create().setBinary(put_string.getBytes(StandardCharsets.UTF_8)).build());

        httpclient = HttpClients.createDefault();
        try (CloseableHttpResponse response = httpclient.execute(httpput)) {


            int responseCode = response.getStatusLine().getStatusCode();
            logger.debug(response.getStatusLine().getReasonPhrase());
            if (responseCode < 200 || responseCode > 299) {
                throw buildTransportError("PUT", url.toExternalForm(), response);
            }

            return createResponseFromEntity(response.getEntity());
        } catch (IOException e) {
        	logger.error("PUT " + url.toString() + " : " + e.getMessage(), e);
        	throw e;
        } catch (Exception e) {
        	logger.error("PUT " + url.toString() + " : " + e.getMessage(), e);
            throw new IOException("PUT " + put_string.length() + " bytes to " + url.toExternalForm() + " failed", e);
        }
    }

    public String do_Post(URL url, String authorization) throws IOException, URISyntaxException {
    	log("POST", url, null);
        CloseableHttpClient httpclient = null;

        HttpPost httppost = new HttpPost(url.toURI());
        httppost.addHeader("Content-Type", "application/json; charset=UTF-8");
        httppost.addHeader("Authorization", authorization);
        httpclient = HttpClients.createDefault();

        try (CloseableHttpResponse response = httpclient.execute(httppost)) {

            int responseCode = response.getStatusLine().getStatusCode();
            logger.debug(response.getStatusLine().getReasonPhrase());
            if (responseCode < 200 || responseCode > 299) {
                throw buildTransportError("POST", url.toExternalForm(), response);
            }

            return createResponseFromEntity(response.getEntity());
        } catch (IOException e) {
        	logger.error("POST " + url.toString() + " : " + e.getMessage(), e);
        	throw e;
        } catch (Exception e) {
        	logger.error("POST " + url.toString() + " : " + e.getMessage(), e);
            throw new IOException("POST " + url.toExternalForm() + " failed", e);
        }
    }

    public String do_Delete(URL url, String authorization) throws IOException, URISyntaxException {
    	log("DELETE", url, null);
        CloseableHttpClient httpclient;

        HttpDelete httpdelete = new HttpDelete(url.toURI());
        httpdelete.addHeader("Content-Type", "application/json; charset=UTF-8");
        httpdelete.addHeader("Authorization", authorization);

        httpclient = HttpClients.createDefault();
        try (CloseableHttpResponse response = httpclient.execute(httpdelete)) {

            int responseCode = response.getStatusLine().getStatusCode();
            logger.debug(response.getStatusLine().getReasonPhrase());
            if (responseCode < 200 || responseCode > 299) {
                throw buildTransportError("DELETE", url.toExternalForm(), response);
            }

            return createResponseFromEntity(response.getEntity());
        } catch (IOException e) {
        	logger.error("DELETE " + url.toString() + " : " + e.getMessage(), e);
        	throw e;
        } catch (Exception e) {
        	logger.error("DELETE " + url.toString() + " : " + e.getMessage(), e);
            throw new IOException("DELETE " + url.toExternalForm() + " failed", e);
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
                int c;
                while ((c = reader.read()) != -1) {
                    strbuilder.append((char) c);
                }
            }
            logger.trace(strbuilder.toString());
            return strbuilder.toString();
        }
        return null;
    }

    private IOException buildTransportError(String verb, String url, CloseableHttpResponse response) {
        int responseCode = response.getStatusLine().getStatusCode();
        JSONObject errObj;
        try {
            errObj = new JSONObject(createResponseFromEntity(response.getEntity()));
            String errType = getErrorObjString(errObj, "type");
            String errTitle = getErrorObjString(errObj, "title");
            String errDetail = getErrorObjString(errObj, "detail");
            String errInstance = getErrorObjString(errObj, "instance");
            String errors = "";
            if (errObj.has("errors")) {
            	JSONArray errArray = errObj.getJSONArray("errors");
            	for(int i=0; i< errArray.length(); i++) {
            		JSONObject errorDetail = errArray.getJSONObject(i);
            		String field =  getErrorObjString(errorDetail, "field");
            		String message =  getErrorObjString(errorDetail, "message");
            		if (field != null && message != null) {
            			errors +=  System.lineSeparator() + "field: " + field + " message: " + message;
            		}
            	}
            }
            return new IOException("Status: " + Integer.toString(responseCode) + " " + verb + ": " + url + " Reason: " + response.getStatusLine().getReasonPhrase()
                    + " - " + errTitle + " Details: " + errDetail + " Instance: " + errInstance + " Type: " + errType + errors);
        } catch (IOException | JSONException e) {
        	logger.error(e.getMessage(), e);
        }
        return new IOException("Status: " + Integer.toString(responseCode) + " " + verb + ": " + url + " Reason: " + response.getStatusLine().getReasonPhrase());
    }


    private String getErrorObjString(JSONObject errObj, String key) {
        if (errObj.has(key)) {
            return errObj.getString(key);
        }
        return "";
    }
    
    private void log(String verb, URL url, String payload) {
    	if (payload != null && logger.isTraceEnabled()) {
    		logger.trace(verb + " " + url.toString() + " : " + System.lineSeparator() + payload);
    	} else if (logger.isDebugEnabled()) {
    		logger.debug(verb + " " + url.toString());
    	}
    }
}
	
