package com.mappy.parsers;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;
import javax.json.stream.JsonParser;

import com.mappy.GeoLocation;
import com.mappy.MapBean;
import com.mappy.Team;
import com.mappy.Venue;
import com.mappy.rest.RestRequest;

public class TeamJsonParser {
	private JsonReader jsr;
	private static final Logger log = Logger.getLogger(TeamJsonParser.class
			.getName());

	public TeamJsonParser(JsonReader jsr) {
		this.jsr = jsr;
	}

	public ArrayList<Team> parse() {
		log.info("entered TeamJsonParser.parse()");
		JsonObject jo = jsr.readObject();
		JsonArray teamsarray = getTeams(jo);
		log.info("exiting TeamJsonParser.parse()");
		return buildTeamsList(teamsarray);
	}
	
	private JsonArray getTeams(JsonObject jo){
		log.info("getTeams()... getting the teams array");
		return  jo.getJsonArray("sports").getJsonObject(0)
				.getJsonArray("leagues").getJsonObject(0).getJsonArray("teams");		
	}
	
	private ArrayList<Team> buildTeamsList(JsonArray ja){
		log.info("Entering TeamJsonParser.buildTeamsList()");
		ArrayList<Team> list = new ArrayList<>();
		JsonObject obj = null;
		for (JsonValue teamjson : ja) {
			if (teamjson.getValueType() == ValueType.OBJECT) {
				obj = (JsonObject) teamjson;
				log.info("got an object - " + obj);
				Team team = parseTeam(obj);
				list.add(team);
			}
		}
		log.info("Exiting TeamJsonParser.buildTeamsList()");
		return list;
	}
	
	private Team parseTeam(JsonObject teamjson){
		log.info("Entering TeamJsonParser.parseTeam()");
		Team team = new Team();
		team.setAbbv(teamjson.getString("abbreviation"));
		team.setLocation(teamjson.getString("location"));
		team.setName(teamjson.getString("name"));
		log.info(team.getAbbv() + "; " + team.getLocation() + "; " + team.getName());
		Venue venue = parseVenue(teamjson);
		team.setVenue(venue);
		log.info("Exiting TeamJsonParser.parseTeam()");
		return team;
	}
	
	private Venue parseVenue(JsonObject teamjson){
		log.info("Entering TeamJsonParser.parseVenue()");
		JsonObject venuejson = teamjson.getJsonArray("venues").getJsonObject(0);
		Venue venue = new Venue();		
		venue.setCapacity(venuejson.getInt("capacity"));
		venue.setName(venuejson.getString("name"));
		venue.setCity(venuejson.getString("city")); 
		venue.setState(venuejson.getString("state"));
		GeoLocation loc = getGeoLocation(venue);
		venue.setGeolocation(loc);
		log.info("Exiting TeamJsonParser.parseVenue()");
		return venue;
	}
	
	private GeoLocation getGeoLocation(Venue venue){
		log.info("Entering TeamJsonParser.getGeoLocation()");
		StringBuilder search = new StringBuilder();
		search.append(venue.getName());
		//search.append("%20");
		//search.append(venue.getCity());
		search.append("%20");
		search.append(venue.getState());
		JsonObject jo = new RestRequest(search.toString()).sendRequest();
		log.info("Exiting TeamJsonParser.getGeoLocation()");
		return new GeoLocationJsonParser(jo).parse();
	}

	private static void navigateTree(JsonValue tree, String key) {
		if (key != null)
			System.out.print("Key " + key + ": ");
		switch (tree.getValueType()) {
		case OBJECT:
			System.out.println("OBJECT");
			JsonObject object = (JsonObject) tree;
			for (String name : object.keySet())
				navigateTree(object.get(name), name);
			break;
		case ARRAY:
			System.out.println("ARRAY");
			JsonArray array = (JsonArray) tree;
			for (JsonValue val : array)
				navigateTree(val, null);
			break;
		case STRING:
			JsonString st = (JsonString) tree;
			System.out.println("STRING " + st.getString());
			break;
		case NUMBER:
			JsonNumber num = (JsonNumber) tree;
			System.out.println("NUMBER " + num.toString());
			break;
		case TRUE:
		case FALSE:
		case NULL:
			System.out.println(tree.getValueType().toString());
			break;
		}
	}

	private static ArrayList<String> parseTeamsJson(JsonReader rdr) {
		JsonObject jsonobj = rdr.readObject();
		log.info("Read the first object");
		JsonArray teamsarray = jsonobj.getJsonArray("sports").getJsonObject(0)
				.getJsonArray("leagues").getJsonObject(0).getJsonArray("teams");
		log.info("got the teams array...");
		return getTeamsLocations(teamsarray);
	}

	private static ArrayList<String> getTeamsLocations(JsonArray teamsarray) {
		ArrayList<String> list = new ArrayList<>();
		JsonObject obj = null;
		for (JsonValue team : teamsarray) {
			if (team.getValueType() == ValueType.OBJECT) {
				obj = (JsonObject) team;
				log.info("got an object - " + obj);
				JsonString jsonstring = obj.getJsonString("location");
				list.add(jsonstring.getString());
			}
		}
		return list;
	}
}
