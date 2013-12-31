package com.mappy;

import java.util.logging.Logger;

public class Team {
	private String name;
	private String location;
	private String abbv;
	private Venue venue;
	private String logoLink;
	private static final Logger log = Logger.getLogger(Team.class.getName());
	
	public Team(){log.info("Creating Team()...");}
	
	public Team(String name, String location, String abbv, Venue venue){
		log.info("Creating Team()...");
		this.name = name;
		this.location = location;
		this.abbv = abbv;
		this.venue = venue;
	}

	public String getName() {
		return name;
	}

	public String getLocation() {
		return location;
	}

	public String getAbbv() {
		return abbv;
	}

	public Venue getVenue() {
		return venue;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setAbbv(String abbv) {
		this.abbv = abbv;
		logoLink = "http://a.espncdn.com/i/teamlogos/nba/lrg/" + abbv.toLowerCase() + ".gif";
	}

	public void setVenue(Venue venue) {
		this.venue = venue;
	}
	
	public String getLogo(){
		return logoLink;
	}

	@Override
	public String toString() {
		String s = "[" + this.name + ", " + this.abbv + " " + this.location + "]";
		return s;
	}
	
	
	
}
