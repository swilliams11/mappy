package com.mappy;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.LatLngBounds;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
//import javax.inject.Named;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;

@ManagedBean(name = "mapBean")
// @Named
@SessionScoped
public class MapBean implements Serializable {
	private static final Logger log = Logger.getLogger(MapBean.class.getName()); 
	//static Logger log = Logger.getLogger("MapBean");
	static String urltext = "http://api.espn.com/v1/sports/basketball/nba/teams?apikey=kvv565tntjd2j67y3fr9c4xt";
	static ArrayList<String> tms;

	public MapBean() {
		try {
			log.info("Instantiating MapBean...");
			URLConnection url = openURL(urltext);
			log.info("URL opened..");
			readResponse(url);
			log.info("Read Response");
		} catch (MalformedURLException e) {
			log.log(Level.SEVERE, "Invalid URI", urltext);
			addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Invalid URI", "This is an invalid URI - " + urltext));
		} catch (IOException e) {
			log.log(Level.SEVERE, "Unable to open the connection", urltext);
			addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Bad Connection",
					"There was a problem with the request. Please try again."));
		}
	}

	public static URLConnection openURL(String urltext)
			throws MalformedURLException, IOException {
		URL url = new URL(urltext);
		return url.openConnection();
	}

	public static void readResponse(URLConnection url) {
		// URL url = new
		// URL("https://graph.facebook.com/search?q=java&type=post");
		try (InputStream is = url.getInputStream();
				JsonReader rdr = Json.createReader(is)) {
			log.info("Opened input stream...");
			tms = parseTeamsJson(rdr);
			log.info("parsed Teams...");
			
			/*
			 * JsonObject obj = rdr.readObject(); JsonArray results =
			 * obj.getJsonArray("data"); for (JsonObject result :
			 * results.getValuesAs(JsonObject.class)) { System.out
			 * .print(result.getJsonObject("from").getString("name"));
			 * System.out.print(": ");
			 * System.out.println(result.getString("message", ""));
			 * System.out.println("-----------"); }
			 */
		} catch (Exception e) {
			log.log(Level.SEVERE, "Error reading the JSON.");
		}
	}

	public static ArrayList<String> parseTeamsJson(JsonReader rdr) {
		JsonObject jsonobj = rdr.readObject();
		log.info("Read the first object");
		JsonArray teamsarray = jsonobj.getJsonArray("sports").getJsonObject(0)
				.getJsonArray("leagues").getJsonObject(0)
				.getJsonArray("teams");
		log.info("got the teams array...");
		JsonObject obj = null;
		ArrayList<String> list = new ArrayList<>();
		log.info("Populate the arrays list...");
		for (JsonValue team : teamsarray) {
			if (team.getValueType() == ValueType.OBJECT) {				
				obj = (JsonObject) team;
				log.info("got an object - " + obj);
				JsonString jsonstring = obj.getJsonString("location");
				list.add(jsonstring.getString());
			}
		}
		log.info("populated the arrays list");
		return list;
	}

	public void onStateChange(StateChangeEvent event) {
		LatLngBounds bounds = event.getBounds();
		int zoomLevel = event.getZoomLevel();
		addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Teams:",
				tms.toString()));
		addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Zoom Level",
				String.valueOf(zoomLevel)));
		addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Center", event
				.getCenter().toString()));
		addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "NorthEast",
				bounds.getNorthEast().toString()));
		addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "SouthWest",
				bounds.getSouthWest().toString()));
	}

	public void onPointSelect(PointSelectEvent event) {
		LatLng latlng = event.getLatLng();
		addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Teams:",
				tms.toString()));
		addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Point Selected", "Lat:" + latlng.getLat() + ", Lng:"
						+ latlng.getLng()));
	}

	public void addMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
}
