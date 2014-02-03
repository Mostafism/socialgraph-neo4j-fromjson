package com.mabdelmoez.socialgraphneo4j;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;


import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;
import org.json.*;

/**
  * @author mabdelmoez
  * Social Network Graph from JSON file URL with Local Neo4j Graph DataBase And Cypher Requests to get Person/s, NodeURL, Friends, Friends of Friends, Suggested Friends with a simple idea of caching data into hashmaps
  * TestApplication Class
  * 
 */

public class TestApplication {
        
        LocalGraphDatabaseHandler db = new LocalGraphDatabaseHandler();
        
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void testGraphCreation() throws Exception {
	   String URL = "";
           SocialGraph dbGraph = new SocialGraph(JSONReaderFromURL.getPersonsFromJSON((URL)));
	}

        @Before
	public void testServerAvailability() {
	    int serverStatus = db.getServerStatus();
            assertEquals("Server Status: ",200,serverStatus);
	}
	
        @Test
	public void testJSONFilledClass() throws JSONException{
            Person person = SocialGraph.getPersonById(1);
            assertNotNull( person );
            assertTrue( SocialGraph.personsHashMap.size()==20 );
	}
        
        @Test
	public void testGetPersonByIDFromGraphDB() throws JSONException{
            boolean dataReceived = false;
            boolean isBen = false;
            JSONObject personDataObject = db.getPersonDataByPersonId(3);
              if(personDataObject.length()>0){
                 dataReceived = true; 
              }
               if(personDataObject.get("firstName").toString().equals("Ben")){
                  isBen = true; 
               }
              assertTrue( dataReceived && isBen );     
	}
        
        @Test
	public void testGetNodeURLFromGraphDB() throws JSONException{
            String nodeURL = db.getPersonNodeURLByPersonId(2);
            assertNotNull( nodeURL );
	}
        
	@Test
	public void testGetFriends() throws JSONException{
		JSONArray friendsArray = db.getFriends(20);
                boolean isRightLength = false;
                if(friendsArray.length() == 7){
                    isRightLength = true;
                }
		assertTrue( isRightLength );	
	}
	
	@Test
	public void testGetFriendsOfFriends() throws JSONException{
		JSONArray friendsOfFriendsArray = db.getFriendsOfFriends(20);
                boolean isRightLength = false;
                
                if(friendsOfFriendsArray.length() == 7){
                    isRightLength = true;
                }
		assertTrue( isRightLength );
	}
	
	@Test
	public void testSuggestedFriends() throws Exception{
		JSONArray suggestedfriendsArray = db.getSuggestedFriends(20);
		
		boolean foundNumberFive = false;
                boolean notNull = false;
		
		JSONObject suggestedFriendsobject = suggestedfriendsArray.getJSONObject(0).getJSONObject("data");

                if(suggestedFriendsobject != null){
                   notNull = true;
                }
                if(suggestedFriendsobject.get("id").toString().equals("5")){
                    foundNumberFive = true;
                }
		assertTrue( notNull && foundNumberFive );
	}

}
