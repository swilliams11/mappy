package com.mappy;

import java.util.logging.Logger;

public class Venue {
	private String name;
	private long capacity;
	private String city;
	private String state;
	private String country;
	private GeoLocation geolocation;
	private static final Logger log = Logger.getLogger(Venue.class.getName());
	
	public Venue(){log.info("Creating Venue()...");}
	
	public Venue(String name, long capacity, GeoLocation location){
		log.info("Creating Venue()...");
		this.name = name;
		this.capacity = capacity;
		this.geolocation = location;
	}

	public String getName() {
		return name;
	}

	public long getCapacity() {
		return capacity;
	}

	public GeoLocation getGeolocation() {
		return geolocation;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCapacity(long capacity) {
		this.capacity = capacity;
	}

	public void setGeolocation(GeoLocation location) {
		this.geolocation = location;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	
	
}
