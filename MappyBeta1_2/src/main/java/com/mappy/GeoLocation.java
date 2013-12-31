package com.mappy;

import java.util.ArrayList;
import java.util.logging.Logger;

public class GeoLocation {
	private double latitude;
	private double longitude;
	private static final Logger log = Logger.getLogger(GeoLocation.class.getName());
	
	public GeoLocation(){log.info("Creating GeoLocation()...");	}
	
	public GeoLocation(ArrayList<String> geos){
		log.info("Creating GeoLocation()...");	
		latitude = Float.parseFloat(geos.get(0));
		longitude = Float.parseFloat(geos.get(1));
	}
	
	public GeoLocation(double lat, double lng){
		log.info("Creating GeoLocation()...");	
		latitude = lat;
		longitude = lng;
	}
	
	public GeoLocation(String lat, String lng){
		log.info("Creating GeoLocation()...");	
		latitude = Double.parseDouble(lat);
		longitude = Double.parseDouble(lng);
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}
	
	
}
