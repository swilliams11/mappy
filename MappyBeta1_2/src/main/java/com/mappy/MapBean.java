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

import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.LatLngBounds;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import com.mappy.parsers.TeamJsonParser;
import com.mappy.Team;

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
	private MapModel advancedModel;
	private Marker marker;
	private static final Logger log = Logger.getLogger(MapBean.class.getName());
	// static Logger log = Logger.getLogger("MapBean");
	static String urltext = "http://api.espn.com/v1/sports/basketball/nba/teams?apikey=kvv565tntjd2j67y3fr9c4xt&enable=venues";
	static ArrayList<Team> tms;

	public MapBean() {
		try {
			log.info("Instantiating MapBean...");
			URLConnection url = openURL(urltext);
			log.info("URL opened..");
			readResponse(url);
			log.info("successfully read the response..");
			populateMap();
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
		try (InputStream is = url.getInputStream();
				JsonReader rdr = Json.createReader(is)) {
			log.info("Opened input stream...");
			tms = new TeamJsonParser(rdr).parse();
			log.info("parsed Teams..." + tms);
		} catch (Exception e) {
			log.log(Level.SEVERE, "Error reading the JSON.");
		}
	}

	public void populateMap() {
		log.info("entered populateMap()");
		advancedModel = new DefaultMapModel();
		createMarkersAndCoordinates();
		log.info("exiting populateMap()");
		
		 //Shared coordinates 
		  /*LatLng coord1 = new LatLng(36.879466,
		  30.667648); LatLng coord2 = new LatLng(36.883707, 30.689216); LatLng
		  coord3 = new LatLng(36.879703, 30.706707); LatLng coord4 = new
		  LatLng(36.885233, 30.702323);*/
		  
		 //Icons and Data 
		  /*advancedModel.addOverlay(new Marker(coord1,
		  "Konyaalti", "konyaalti.png",
		  "http://maps.google.com/mapfiles/ms/micons/blue-dot.png"));
		  advancedModel.addOverlay(new Marker(coord2, "Ataturk Parki",
		  "ataturkparki.png")); advancedModel.addOverlay(new Marker(coord4,
		  "Kaleici", "kaleici.png",
		  "http://maps.google.com/mapfiles/ms/micons/pink-dot.png"));
		  advancedModel.addOverlay(new Marker(coord3, "Karaalioglu Parki",
		  "karaalioglu.png",
		  "http://maps.google.com/mapfiles/ms/micons/yellow-dot.png"));
	*/	 
	}

	private void createMarkersAndCoordinates() {
		log.info("entered createMarkersAndCoordinates()");
		Venue venue = null;
		LatLng coord1 = null;
		
		for (Team team : tms) {
			log.info("get team: " + team.getName() + "; team logo: " + team.getLogo());
			venue = team.getVenue();
			log.info("get team: " + venue.getName() + "; latitude: " 
					+ venue.getGeolocation().getLatitude() + "; longitude: " 
					+ venue.getGeolocation().getLongitude() );
			coord1 = new LatLng(venue.getGeolocation().getLatitude(), 
						venue.getGeolocation().getLongitude());
			advancedModel.addOverlay(
					new Marker(coord1, 
							team.getLogo(),
							team.getLogo(),
							"http://maps.google.com/mapfiles/ms/micons/blue-dot.png"));
			//"http://maps.google.com/mapfiles/ms/micons/blue-dot.png"
		}
		log.info("exiting createMarkersAndCoordinates()");
	}

	/*
	 * public static ArrayList<String> parseTeamsJson(JsonReader rdr) {
	 * JsonObject jsonobj = rdr.readObject(); log.info("Read the first object");
	 * JsonArray teamsarray = jsonobj.getJsonArray("sports").getJsonObject(0)
	 * .getJsonArray("leagues").getJsonObject(0) .getJsonArray("teams");
	 * log.info("got the teams array..."); return getTeamsLocations(teamsarray);
	 * }
	 * 
	 * 
	 * public static ArrayList<String> getTeamsLocations(JsonArray teamsarray){
	 * ArrayList<String> list = new ArrayList<>(); JsonObject obj = null; for
	 * (JsonValue team : teamsarray) { if (team.getValueType() ==
	 * ValueType.OBJECT) { obj = (JsonObject) team; log.info("got an object - "
	 * + obj); JsonString jsonstring = obj.getJsonString("location");
	 * list.add(jsonstring.getString()); } } return list; }
	 */
	public void onStateChange(StateChangeEvent event) {
		LatLngBounds bounds = event.getBounds();
		int zoomLevel = event.getZoomLevel();
		/*addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Teams:",
				tms.toString()));*/
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
		/*addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Teams:",
				tms.toString()));*/
		addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Point Selected", "Lat:" + latlng.getLat() + ", Lng:"
						+ latlng.getLng()));
	}

	public void addMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public void onMarkerSelect(OverlaySelectEvent event) {  
        marker = (Marker) event.getOverlay();  
    }  
      
	 public MapModel getAdvancedModel() {  
	        return advancedModel;  
	 }  
	 
    public Marker getMarker() {  
        return marker;  
    }  
}
