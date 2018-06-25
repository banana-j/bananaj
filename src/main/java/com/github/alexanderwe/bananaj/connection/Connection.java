package com.github.alexanderwe.bananaj.connection;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
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

import com.github.alexanderwe.bananaj.exceptions.TransportException;

/**
 * Created by Alexander on 10.08.2016.
 */
public class Connection {


	public String do_Get(URL url, String authorization) throws TransportException {
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		InputStream entityStream = null;
		
		try {
			httpclient = HttpClients.createDefault();
			HttpGet httpget = new HttpGet(url.toURI());
			httpget.addHeader("Authorization", authorization);
			response = httpclient.execute(httpget);

			int responseCode = response.getStatusLine().getStatusCode();
			//System.out.println("\nSending 'GET' request to URL : " + url);
			//System.out.println("Response Code : " + responseCode + "\n");
			if (responseCode >= 400) {
				throw new TransportException("Error: " + responseCode + " GET " + url.toExternalForm() + " " + response.getStatusLine().getReasonPhrase());
			}

			HttpEntity entity = response.getEntity();
			long length = entity.getContentLength();
			entityStream = entity.getContent();
			StringBuilder strbuilder = new StringBuilder(length > 16  && length < Integer.MAX_VALUE ? (int)length : 200);
			try (Reader reader = new BufferedReader(new InputStreamReader
					(entityStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
				int c = 0;
				while ((c = reader.read()) != -1) {
					strbuilder.append((char) c);
				}
			}
			return strbuilder.toString();
		} catch (Exception e) {
			throw new TransportException("GET " + url.toExternalForm() + " failed", e);
		} finally {
			if (entityStream != null) {try {entityStream.close();} catch (Exception e) { }}
			if (response != null) {try {response.close();} catch (Exception e) { }}
		}
	}

	public String do_Post(URL url, String post_string, String authorization) throws TransportException {
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		InputStream entityStream = null;
		
		try {
			httpclient = HttpClients.createDefault();
			HttpPost httppost = new HttpPost(url.toURI());
			httppost.addHeader("Authorization", authorization);
			httppost.addHeader("Content-Type", "application/json; charset=UTF-8");
			httppost.setEntity(EntityBuilder.create().setText(post_string).build());
			response = httpclient.execute(httppost);

			int responseCode = response.getStatusLine().getStatusCode();
			//System.out.println("\nSending 'POST' request to URL : " + url + System.lineSeparator() + "Send data: " + (post_string.length() > 500 ? post_string.substring(0, 500)+"..." : post_string));
			//System.out.println("Response Code : " + responseCode + "\n");
			if (responseCode >= 400) {
				throw new TransportException("Error: " + responseCode + " POST " + url.toExternalForm() + " " + response.getStatusLine().getReasonPhrase());
			}

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				long length = entity.getContentLength();
				entityStream = entity.getContent();
				StringBuilder strbuilder = new StringBuilder(length > 16  && length < Integer.MAX_VALUE ? (int)length : 200);
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
		} catch (Exception e) {
			throw new TransportException("POST " + post_string.length() + " bytes to " + url.toExternalForm() + " failed", e);
		} finally {
			if (entityStream != null) {try {entityStream.close();} catch (Exception e) { }}
			if (response != null) {try {response.close();} catch (Exception e) { }}
		}
	}

	public String do_Patch(URL url, String patch_string, String authorization) throws TransportException {
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		InputStream entityStream = null;
		
		try {
			httpclient = HttpClients.createDefault();
			HttpPatch httppatch = new HttpPatch(url.toURI());
			httppatch.addHeader("Authorization", authorization);
			httppatch.addHeader("Content-Type", "application/json; charset=UTF-8");
			httppatch.setEntity(EntityBuilder.create().setText(patch_string).build());
			response = httpclient.execute(httppatch);

			int responseCode = response.getStatusLine().getStatusCode();
			//System.out.println("\nSending 'PATCH' request to URL : " + url + System.lineSeparator() + "Send data: " + (patch_string.length() > 500 ? patch_string.substring(0, 500)+"..." : patch_string));
			//System.out.println("Response Code : " + responseCode + "\n");
			if (responseCode >= 400) {
				throw new TransportException("Error: " + responseCode + " PATCH " + url.toExternalForm() + " " + response.getStatusLine().getReasonPhrase());
			}

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				long length = entity.getContentLength();
				entityStream = entity.getContent();
				StringBuilder strbuilder = new StringBuilder(length > 16  && length < Integer.MAX_VALUE ? (int)length : 200);
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
		} catch (Exception e) {
			throw new TransportException("PATCH " + patch_string.length() + " bytes to " + url.toExternalForm() + " failed", e);
		} finally {
			if (entityStream != null) {try {entityStream.close();} catch (Exception e) { }}
			if (response != null) {try {response.close();} catch (Exception e) { }}
		}
	}

	public String do_Put(URL url, String put_string, String authorization) throws TransportException {
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		InputStream entityStream = null;
		
		try {
			httpclient = HttpClients.createDefault();
			HttpPut httpput = new HttpPut(url.toURI());
			httpput.addHeader("Authorization", authorization);
			httpput.addHeader("Content-Type", "application/json; charset=UTF-8");
			httpput.setEntity(EntityBuilder.create().setText(put_string).build());
			response = httpclient.execute(httpput);

			int responseCode = response.getStatusLine().getStatusCode();
			//System.out.println("\nSending 'PUT' request to URL : " + url + System.lineSeparator() + "Send data: " + (put_string.length() > 500 ? put_string.substring(0, 500)+"..." : put_string));
			//System.out.println("Response Code : " + responseCode + "\n");
			if (responseCode >= 400) {
				throw new TransportException("Error: " + responseCode + " PUT " + url.toExternalForm() + " " + response.getStatusLine().getReasonPhrase());
			}

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				long length = entity.getContentLength();
				entityStream = entity.getContent();
				StringBuilder strbuilder = new StringBuilder(length > 16  && length < Integer.MAX_VALUE ? (int)length : 200);
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
		} catch (Exception e) {
			throw new TransportException("PUT " + put_string.length() + " bytes to " + url.toExternalForm() + " failed", e);
		} finally {
			if (entityStream != null) {try {entityStream.close();} catch (Exception e) { }}
			if (response != null) {try {response.close();} catch (Exception e) { }}
		}
	}

	public String do_Post(URL url, String authorization) throws TransportException {
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		InputStream entityStream = null;
		
		try {
			httpclient = HttpClients.createDefault();
			HttpPost httppost = new HttpPost(url.toURI());
			httppost.addHeader("Authorization", authorization);
			httppost.addHeader("Content-Type", "application/json; charset=UTF-8");
			response = httpclient.execute(httppost);

			int responseCode = response.getStatusLine().getStatusCode();
			//System.out.println("\nSending 'POST' request to URL : " + url);
			//System.out.println("Response Code : " + responseCode + "\n");
			if (responseCode >= 400) {
				throw new TransportException("Error: " + responseCode + " POST " + url.toExternalForm() + " " + response.getStatusLine().getReasonPhrase());
			}

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				long length = entity.getContentLength();
				entityStream = entity.getContent();
				StringBuilder strbuilder = new StringBuilder(length > 16  && length < Integer.MAX_VALUE ? (int)length : 200);
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
		} catch (Exception e) {
			throw new TransportException("POST " + url.toExternalForm() + " failed", e);
		} finally {
			if (entityStream != null) {try {entityStream.close();} catch (Exception e) { }}
			if (response != null) {try {response.close();} catch (Exception e) { }}
		}
	}

	public String do_Delete(URL url, String authorization) throws TransportException {
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		InputStream entityStream = null;
		
		try {
			httpclient = HttpClients.createDefault();
			HttpDelete httpdelete = new HttpDelete(url.toURI());
			httpdelete.addHeader("Authorization", authorization);
			httpdelete.addHeader("Content-Type", "application/json; charset=UTF-8");
			response = httpclient.execute(httpdelete);

			int responseCode = response.getStatusLine().getStatusCode();
			//System.out.println("\nSending 'DELETE' request to URL : " + url);
			//System.out.println("Response Code : " + responseCode + "\n");
			if (responseCode >= 400) {
				throw new TransportException("Error: " + responseCode + " DELETE " + url.toExternalForm() + " " + response.getStatusLine().getReasonPhrase());
			}

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				long length = entity.getContentLength();
				entityStream = entity.getContent();
				StringBuilder strbuilder = new StringBuilder(length > 16  && length < Integer.MAX_VALUE ? (int)length : 200);
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
		} catch (Exception e) {
			throw new TransportException("DELETE " + url.toExternalForm() + " failed", e);
		} finally {
			if (entityStream != null) {try {entityStream.close();} catch (Exception e) { }}
			if (response != null) {try {response.close();} catch (Exception e) { }}
		}
	}
}
