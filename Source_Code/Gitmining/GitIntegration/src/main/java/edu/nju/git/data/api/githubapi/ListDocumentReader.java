package edu.nju.git.data.api.githubapi;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import com.fasterxml.jackson.databind.JsonNode;

import edu.nju.git.data.api.JacksonConfig;

public abstract class ListDocumentReader {

	protected JsonNode jsonNode;
	public ListDocumentReader() {
	}
	public void setUrl(String url){
		init(url);
	}
	protected void init(String url){
		try {
			/*if(APIconfig.isGithub){
				
				HttpURLConnection httpUrlConnection = (HttpURLConnection) new URL(url).openConnection();
				httpUrlConnection.setRequestProperty("Authrorization", APIconfig.username);
				httpUrlConnection.setDoOutput(true);   
				httpUrlConnection.setUseCaches(false);   
				jsonNode = JacksonConfig.getObjectMapper().readTree(httpUrlConnection.getInputStream());
			}else{}*/
				jsonNode = JacksonConfig.getObjectMapper().readTree(new URL(url));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public JsonNode getNode(){
		return jsonNode;
	}
}
