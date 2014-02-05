package com.mabdelmoez.socialgraphneo4j;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import java.io.IOException;
import java.net.URL;
import org.json.JSONException;

/**
  * @author mabdelmoez
  * Social Network Graph from JSON file URL with Local Neo4j Graph DataBase And Cypher Requests to get Person/s, NodeURL, Friends, Friends of Friends, Suggested Friends with a simple idea of caching data into hashmaps
  * JSONReaderFromURL Class
  * 
 */


public class JSONReaderFromURL {

  public static Person[] getPersonsFromJSON(URL url) throws IOException, JSONException{ //Parse the file to the class structure directly 
      JsonFactory jfactory = new JsonFactory();
      JsonParser jParser = jfactory.createJsonParser(url);
      ObjectMapper mapper = new ObjectMapper();
      Person[] SocialGraphOfPersons = mapper.readValue(jParser, Person[].class);
      return SocialGraphOfPersons;
  }
}