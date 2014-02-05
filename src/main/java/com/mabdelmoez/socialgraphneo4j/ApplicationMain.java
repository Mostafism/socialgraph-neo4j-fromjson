package com.mabdelmoez.socialgraphneo4j;

import org.json.*;
import java.io.IOException;
import java.net.URL;
/**
  * @author mabdelmoez
  * Social Network Graph from JSON file URL with Local Neo4j Graph DataBase And Cypher Requests to get Person/s, NodeURL, Friends, Friends of Friends, Suggested Friends with a simple idea of caching data into hashmaps
  * ApplicationMain Class
  * 
 */

public class ApplicationMain 
{
   
    public static void main( String[] args ) throws IOException, JSONException
    {
        URL URL = new URL("");
        SocialGraph dbGraph = new SocialGraph(JSONReaderFromURL.getPersonsFromJSON((URL)));
        LocalGraphDatabaseHandler db = new LocalGraphDatabaseHandler();
 
        System.out.println("====================Get Person NodeURL By Person ID===========================");
        System.out.println(db.getPersonNodeURLByPersonId(2));
        System.out.println("====================================================================");

        System.out.println("====================================================================");
            JSONArray friendsArray = db.getFriends(20);  
            if(friendsArray != null){
                  if(friendsArray.length() > 0){
                    for(int i = 0; i < friendsArray.length(); i++)
                        {
                              JSONObject friendsobject = friendsArray.getJSONObject(i).getJSONObject("data");
                              System.out.println("=========Friend of " + 20 + " : No. "+ (i+1) +"============");
                              System.out.println("ID:" + friendsobject.get("id"));
                              System.out.println("First Name:" + friendsobject.get("firstName"));
                              System.out.println("Surname:"+ friendsobject.get("surname"));
                              System.out.println("Gender:"+friendsobject.get("gender"));
                              System.out.println("Age:"+friendsobject.get("age"));
                              System.out.println("Friends:"+friendsobject.get("friends"));
                        }
                     }
                 else{
                    System.out.println("No Friends for this person");    
                 }
           }
           System.out.println("====================================================================");
       

           
           System.out.println("====================================================================");
            JSONArray friendsOfFriendsArray = db.getFriendsOfFriends(20);
            if(friendsOfFriendsArray != null){
                  if(friendsOfFriendsArray.length() > 0){
                    for(int i = 0; i < friendsOfFriendsArray.length(); i++)
                        {
                              JSONObject friendsOfFriendsobject = friendsOfFriendsArray.getJSONObject(i).getJSONObject("data");
                              System.out.println("=========Friends of friends of " + 20 + " : No. "+ (i+1) +"============");
                              System.out.println("ID:" + friendsOfFriendsobject.get("id"));
                              System.out.println("First Name:" + friendsOfFriendsobject.get("firstName"));
                              System.out.println("Surname:"+ friendsOfFriendsobject.get("surname"));
                              System.out.println("Gender:"+friendsOfFriendsobject.get("gender"));
                              System.out.println("Age:"+friendsOfFriendsobject.get("age"));
                              System.out.println("Friends:"+friendsOfFriendsobject.get("friends"));
                        }    
                    }
                else{
                  System.out.println("No Friends of friends for this person");    
                }
            }
            System.out.println("====================================================================");
        
        
            System.out.println("====================================================================");
            JSONArray suggestedfriendsArray = db.getSuggestedFriends(20);
             if(suggestedfriendsArray != null){
                  if(suggestedfriendsArray.length() > 0){
                        for(int i = 0; i < suggestedfriendsArray.length(); i++)
                            {
                                  JSONObject suggestedFriendsobject = suggestedfriendsArray.getJSONObject(i).getJSONObject("data");
                                  System.out.println("=========Suggested friends of " + 20 + " : No. "+ (i+1) +"============");
                                  System.out.println("ID:" + suggestedFriendsobject.get("id"));
                                  System.out.println("First Name:" + suggestedFriendsobject.get("firstName"));
                                  System.out.println("Surname:"+ suggestedFriendsobject.get("surname"));
                                  System.out.println("Gender:"+suggestedFriendsobject.get("gender"));
                                  System.out.println("Age:"+suggestedFriendsobject.get("age"));
                                  System.out.println("Friends:"+suggestedFriendsobject.get("friends"));
                            }    
                  }
                  else{
                   System.out.println("No suggested friends for this person");    
                  } 
             }
            System.out.println("====================================================================");
        
        
           System.out.println("=================Get person by id 1 from the HashMap if you directly are requesting at the same runtime that the graph is created on ======================");
           System.out.println(SocialGraph.getPersonById(1));  
           System.out.println("====================================================================");
        
          
           System.out.println("=================Get person by id(3) (checks HashMap first)  ======================");
           JSONObject personDataObject = db.getPersonDataByPersonId(3);
           if(personDataObject.length()>0){
                System.out.println("ID:" + personDataObject.get("id"));
                System.out.println("First Name:" + personDataObject.get("firstName"));
                System.out.println("Surname:"+ personDataObject.get("surname"));
                System.out.println("Gender:"+personDataObject.get("gender"));
                System.out.println("Age:"+personDataObject.get("age"));
                System.out.println("Friends:"+personDataObject.get("friends"));
                System.out.println("====================================================================");}
           else{
                System.out.println("No Person found");
           }
     }
}
