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

/**
 * Created by Alexander on 10.08.2016.
 */
public class Connection {


	public String do_Get(URL url, String authorization) throws Exception{
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url.toURI());
		httpget.addHeader("Authorization", authorization);
		CloseableHttpResponse response = httpclient. execute(httpget);

		InputStream entityStream = null;
		try {
			int responseCode = response.getStatusLine().getStatusCode();
			//System.out.println("\nSending 'GET' request to URL : " + url);
			//System.out.println("Response Code : " + responseCode + "\n");

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
		} finally {
			if (entityStream != null) {entityStream.close();}
			response.close();
		}
	}

	public String do_Post(URL url, String post_string, String authorization) throws Exception{
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(url.toURI());
		httppost.addHeader("Authorization", authorization);
		httppost.addHeader("Content-Type", "application/json; charset=UTF-8");
		httppost.setEntity(EntityBuilder.create().setText(post_string).build());
		CloseableHttpResponse response = httpclient. execute(httppost);

		InputStream entityStream = null;
		try {
			int responseCode = response.getStatusLine().getStatusCode();
			//System.out.println("\nSending 'POST' request to URL : " + url + System.lineSeparator() + "Send data: " + (post_string.length() > 500 ? post_string.substring(0, 500)+"..." : post_string));
			//System.out.println("Response Code : " + responseCode + "\n");

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
		} finally {
			if (entityStream != null) {entityStream.close();}
			response.close();
		}
	}

	public String do_Patch(URL url, String patch_string, String authorization) throws Exception{
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPatch httppatch = new HttpPatch(url.toURI());
		httppatch.addHeader("Authorization", authorization);
		httppatch.addHeader("Content-Type", "application/json; charset=UTF-8");
		httppatch.setEntity(EntityBuilder.create().setText(patch_string).build());
		CloseableHttpResponse response = httpclient. execute(httppatch);

		InputStream entityStream = null;
		try {
			int responseCode = response.getStatusLine().getStatusCode();
			//System.out.println("\nSending 'PATCH' request to URL : " + url + System.lineSeparator() + "Send data: " + (patch_string.length() > 500 ? patch_string.substring(0, 500)+"..." : patch_string));
			//System.out.println("Response Code : " + responseCode + "\n");

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
		} finally {
			if (entityStream != null) {entityStream.close();}
			response.close();
		}
	}

	public String do_Put(URL url, String put_string, String authorization) throws Exception{
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPut httpput = new HttpPut(url.toURI());
		httpput.addHeader("Authorization", authorization);
		httpput.addHeader("Content-Type", "application/json; charset=UTF-8");
		httpput.setEntity(EntityBuilder.create().setText(put_string).build());
		CloseableHttpResponse response = httpclient. execute(httpput);

		InputStream entityStream = null;
		try {
			int responseCode = response.getStatusLine().getStatusCode();
			//System.out.println("\nSending 'PUT' request to URL : " + url + System.lineSeparator() + "Send data: " + (put_string.length() > 500 ? put_string.substring(0, 500)+"..." : put_string));
			//System.out.println("Response Code : " + responseCode + "\n");

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
		} finally {
			if (entityStream != null) {entityStream.close();}
			response.close();
		}
	}

	public String do_Post(URL url, String authorization) throws Exception{
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(url.toURI());
		httppost.addHeader("Authorization", authorization);
		httppost.addHeader("Content-Type", "application/json; charset=UTF-8");
		CloseableHttpResponse response = httpclient. execute(httppost);

		InputStream entityStream = null;
		try {
			int responseCode = response.getStatusLine().getStatusCode();
			//System.out.println("\nSending 'POST' request to URL : " + url);
			//System.out.println("Response Code : " + responseCode + "\n");

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
		} finally {
			if (entityStream != null) {entityStream.close();}
			response.close();
		}
	}

	public String do_Delete(URL url, String authorization) throws Exception{
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpDelete httpdelete = new HttpDelete(url.toURI());
		httpdelete.addHeader("Authorization", authorization);
		httpdelete.addHeader("Content-Type", "application/json; charset=UTF-8");
		CloseableHttpResponse response = httpclient. execute(httpdelete);

		InputStream entityStream = null;
		try {
			int responseCode = response.getStatusLine().getStatusCode();
			//System.out.println("\nSending 'DELETE' request to URL : " + url);
			//System.out.println("Response Code : " + responseCode + "\n");

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
		} finally {
			if (entityStream != null) {entityStream.close();}
			response.close();
		}
	}
}
