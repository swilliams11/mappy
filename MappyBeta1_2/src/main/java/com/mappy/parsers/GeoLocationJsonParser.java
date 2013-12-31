package com.mappy.parsers;

import java.util.logging.Logger;

import javax.json.JsonArray;
import javax.json.JsonObject;

import com.mappy.GeoLocation;

public class GeoLocationJsonParser {

	private JsonObject jo;
	private static final Logger log = Logger.getLogger(GeoLocationJsonParser.class
			.getName());
	
	public GeoLocationJsonParser(JsonObject jo){
		log.info("Creating GeoLocationJsonParser");
		this.jo = jo;
	}
	
	public GeoLocation parse(){
		log.info("Entering GeoLocationJsonParser.parse()");
		JsonArray ja = getGeocodePoints(jo);
		GeoLocation geo = parseJsonArray(ja);
		log.info("Exiting GeoLocationJsonParser.parse()");
		return geo;
	}
	
	private JsonArray getGeocodePoints(JsonObject jo){
		log.info("Entering and Exiting GeoLocation.getGeocodePoints()... getting the geocode points array");
		return  jo.getJsonArray("resourceSets").getJsonObject(0)
				.getJsonArray("resources").getJsonObject(0)
				.getJsonObject("point")
				.getJsonArray("coordinates");		
	}
	
	private GeoLocation parseJsonArray(JsonArray ja){
		log.info("Entering and Exiting GeoLocationJsonParser.parseJsonArray()");
		log.info("latitude: " + ja.getJsonNumber(0).toString()
				+ "longitude: " + ja.getJsonNumber(1).toString());
		return new GeoLocation(ja.getJsonNumber(0).toString(),
				ja.getJsonNumber(1).toString());		
	}
}
