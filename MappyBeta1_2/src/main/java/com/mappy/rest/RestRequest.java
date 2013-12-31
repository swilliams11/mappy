package com.mappy.rest;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.mappy.MapBean;
import com.mappy.parsers.TeamJsonParser;

public class RestRequest {
	private String uri;
	private String tempholder = "TD%20Garden%20Boston%20MA";
	private String baseUri = "http://dev.virtualearth.net/REST/v1/Locations/";
	private String search;	
	private String apiKey = "?key=AnEiBrgq1HgOzUB3fCYUGXn1BZ2fIZpp2Vg1cDqSNP0EoJmGKYgMfxNHb2oyJhNA"; 
	private JsonObject jsonobj;
	private static final Logger log = Logger.getLogger(RestRequest.class.getName()); 
	
	public RestRequest(String search){
		log.info("Instantiating RestRequest...");
		this.search = search;
	}
	
	public JsonObject sendRequest(){
		try {
			encodeSearch();
			buildUri();
			URLConnection url = openURL(uri);			
			log.info("URL opened.." + uri);
			readResponse(url);
			log.info("Read Response");			
		} catch (MalformedURLException e) {
			log.log(Level.SEVERE, "Invalid URI", uri);
		} catch (IOException e) {
			log.log(Level.SEVERE, "Unable to open the connection", uri);			
		} finally {
			return jsonobj;
		}		
	}
	
	private void encodeSearch(){
		search = search.replaceAll(" ", "%20");
		if(search.contains("&")){
			log.info("before:" + search);
			search = search.replaceAll("&", "");
			log.info("after:" + search);
		}
		//search = search.replaceAll("&", "");
	}
	
	private void buildUri(){
		uri = baseUri + search + apiKey;
	}
	
	private URLConnection openURL(String urltext)
			throws MalformedURLException, IOException {
		URL url = new URL(urltext);
		return url.openConnection();
	}

	private void readResponse(URLConnection url) {
		try (InputStream is = url.getInputStream();
				JsonReader rdr = Json.createReader(is)) {
			log.info("Opened input stream...");
			//tms = parseTeamsJson(rdr);
			jsonobj = rdr.readObject();
			log.info("read the json object...");
		} catch (Exception e) {
			log.log(Level.SEVERE, "RestRequest - Error reading the JSON.");
		}
	}
}
