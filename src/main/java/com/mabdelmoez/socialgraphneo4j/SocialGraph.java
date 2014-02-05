package com.mabdelmoez.socialgraphneo4j;

import java.util.HashMap;
import java.util.Arrays;

/**
  * @author mabdelmoez
  * Social Network Graph from JSON file URL with Local Neo4j Graph DataBase And Cypher Requests to get Person/s, NodeURL, Friends, Friends of Friends, Suggested Friends with a simple idea of caching data into hashmaps
  * SocialGraph Class
  * 
 */

public class SocialGraph{
    public static HashMap<Integer, Person> personsHashMap = new HashMap<Integer, Person> (); //for faster access (if you are NOT requesting at the same runtime that the graph is created on)
    LocalGraphDatabaseHandler dbHandler = new LocalGraphDatabaseHandler();
    
    public SocialGraph(Person[] persons) throws org.json.JSONException{
            for (Person person : persons) {
                 personsHashMap.put(person.getID(), person);
                 dbHandler.addPersonToGraph(person);
        }
       persons = null; //free up memory after transfer to the hashmap
    }

    public static Person getPersonById(int id) {
       if(personsHashMap.containsKey(id))
       {
            return personsHashMap.get(id);
       }
       else{
           return null;
       }
    }

}
