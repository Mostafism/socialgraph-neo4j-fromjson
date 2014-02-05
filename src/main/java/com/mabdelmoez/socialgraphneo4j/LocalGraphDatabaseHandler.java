package com.mabdelmoez.socialgraphneo4j;

import java.util.Arrays;
import java.util.HashMap;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.json.*;

/**
  * @author mabdelmoez
  * Social Network Graph from JSON file URL with Local Neo4j Graph DataBase And Cypher Requests to get Person/s, NodeURL, Friends, Friends of Friends, Suggested Friends with a simple idea of caching data into hashmaps
  * LocalDataBaseHandler Class
  * 
 */

public class LocalGraphDatabaseHandler {

    private static final String SERVER_ROOT_URI = "http://localhost:7474"; 
    public static final HashMap<Integer, String> nodesURLs = new HashMap<Integer, String>(); //for faster access so no need to query the server


    /**
     * Checks if server is OK
     * @return
     */
    public int getServerStatus(){
        int status = 500;
        try{
            String url = SERVER_ROOT_URI;
            HttpClient client = new HttpClient();
            GetMethod mGet =   new GetMethod(url);
            status = client.executeMethod(mGet);
            mGet.releaseConnection( );
        }catch(java.io.IOException e){
            System.out.println("Exception in neo4j server/database status : " + e);
        }
        return status;
    }

    /**
     * creates an empty node and returns its URI to edit further
     * @return
     */
    private String createNode(){
        String location = null; //since there is no special characters in the url so we can have it as string .. 
        try{
            String nodePointUrl = LocalGraphDatabaseHandler.SERVER_ROOT_URI + "/db/data/node/";
            HttpClient client = new HttpClient();
            PostMethod mPost = new PostMethod(nodePointUrl);
            Header mtHeader = new Header();
            mtHeader.setName("content-type");
            mtHeader.setValue("application/json");
            mtHeader.setName("accept");
            mtHeader.setValue("application/json");
            mPost.addRequestHeader(mtHeader);
            StringRequestEntity requestEntity = new StringRequestEntity("{}","application/json", "UTF-8");
            mPost.setRequestEntity(requestEntity);
            client.executeMethod(mPost); //status is here returned
            Header locationHeader =  mPost.getResponseHeader("location");
            location = locationHeader.getValue();
            mPost.releaseConnection( );
        }catch(java.io.IOException e){
             System.out.println("Exception in createing the node in neo4j database: " + e);
        }

        return location;
    }

     /**
     * Creates an indexing value to index with (if needed) but node_auto_index does the job for now (when activated from the properties file of the database)
     * @return
     * @param indexName
     * @param indexValue
     */
     private String createNodeIndex(String indexName, String indexValue){
        String location = null;
        try{
            String nodePointUrl = LocalGraphDatabaseHandler.SERVER_ROOT_URI + "/db/data/index/node/";
            HttpClient client = new HttpClient();
            PostMethod mPost = new PostMethod(nodePointUrl);
            Header mtHeader = new Header();
            mtHeader.setName("content-type");
            mtHeader.setValue("application/json");
            mtHeader.setName("accept");
            mtHeader.setValue("application/json");
            mPost.addRequestHeader(mtHeader);
            String jsonString = toJsonNameValuePairCollection(indexName,indexValue );
            StringRequestEntity requestEntity = new StringRequestEntity(jsonString,"application/json", "UTF-8");
            mPost.setRequestEntity(requestEntity);
            client.executeMethod(mPost); //status is here returned
            Header locationHeader =  mPost.getResponseHeader("location");
            location = locationHeader.getValue();
            mPost.releaseConnection( );
        }catch(java.io.IOException e){
             System.out.println("Exception in creating the node index in neo4j database: " + e);
        }

        return location;
    }
     
    /**
     * Adds property to a node
     * @param nodeURI - URI of node to which the property is to be added
     * @param propertyName - name of property which we want to add
     * @param propertyValue - Value of above property
     */
    private void addProperty(String nodeURI,String propertyName,String propertyValue){
        try{
            String nodePointUrl = nodeURI + "/properties/" + propertyName;
            HttpClient client = new HttpClient();
            PutMethod mPut = new PutMethod(nodePointUrl);
            Header mtHeader = new Header();
            mtHeader.setName("content-type");
            mtHeader.setValue("application/json");
            mtHeader.setName("accept");
            mtHeader.setValue("application/json");
            mPut.addRequestHeader(mtHeader);
            String jsonString = "\"" + propertyValue + "\"";
            StringRequestEntity requestEntity = new StringRequestEntity(jsonString,"application/json","UTF-8");
            mPut.setRequestEntity(requestEntity);
            client.executeMethod(mPut); //status is here
            mPut.releaseConnection( );

        }catch(java.io.IOException e){
             System.out.println("Exception in add a property to node in neo4j database: " + e);
        }

    }

    /**
     * adds a relationship between two nodes
     * @param startNodeURI
     * @param endNodeURI
     * @param relationshipType
     * @param jsonAttributes
     * @return
     */
    private String addRelationship(String startNodeURI,String endNodeURI,String relationshipType,String jsonAttributes){
        String location = null;
        try{
            String fromUrl = startNodeURI + "/relationships";
            String relationshipJson = generateJsonRelationship( endNodeURI,relationshipType,jsonAttributes );
            /*System.out.println("relationshipJson : " + relationshipJson);*/
            HttpClient client = new HttpClient();
            PostMethod mPost = new PostMethod(fromUrl);
            Header mtHeader = new Header();
            mtHeader.setName("content-type");
            mtHeader.setValue("application/json");
            mtHeader.setName("accept");
            mtHeader.setValue("application/json");
            mPost.addRequestHeader(mtHeader);
            StringRequestEntity requestEntity = new StringRequestEntity(relationshipJson,"application/json","UTF-8");
            mPost.setRequestEntity(requestEntity);
            client.executeMethod(mPost); //status is here
            Header locationHeader =  mPost.getResponseHeader("location");
            location = locationHeader.getValue();
            mPost.releaseConnection( );

        }catch(java.io.IOException e){
             System.out.println("Exception in adding a relationship between two nodes in neo4j database: " + e);
        }

        return location;

    }

    /**
     * generates the relationship JSON to be sent to the server as a request afterwards
     * @param endNodeURL
     * @param relationshipType
     * @param jsonAttributes
     * @return
     */
    private String generateJsonRelationship(String endNodeURL,String relationshipType,String ... jsonAttributes) {
        StringBuilder sb = new StringBuilder();
        sb.append("{ \"to\" : \"");
        sb.append(endNodeURL);
        sb.append("\", ");
        sb.append("\"type\" : \"");
        sb.append(relationshipType);
        if(jsonAttributes == null || jsonAttributes.length < 1) {
            sb.append("\"");
        } else {
            sb.append("\", \"data\" : ");
            for(int i = 0; i < jsonAttributes.length; i++) {
                sb.append(jsonAttributes[i]);
                if(i < jsonAttributes.length -1) { // Miss off the final comma
                    sb.append(", ");
                }
            }
        }

        sb.append(" }");
        return sb.toString();
    }

    /**
     * adds property to an already existing relationship
     * @param relationshipUri
     * @param propertyName
     * @param propertyValue
     */
    private void addPropertyToRelation( String relationshipUri,String propertyName,String propertyValue ){
        try{
            String relPropUrl = relationshipUri + "/properties";
            HttpClient client = new HttpClient();
            PutMethod mPut = new PutMethod(relPropUrl);
            Header mtHeader = new Header();
            mtHeader.setName("content-type");
            mtHeader.setValue("application/json");
            mtHeader.setName("accept");
            mtHeader.setValue("application/json");
            mPut.addRequestHeader(mtHeader);
            String jsonString = toJsonNameValuePairCollection(propertyName,propertyValue );
            StringRequestEntity requestEntity = new StringRequestEntity(jsonString,"application/json","UTF-8");
            mPut.setRequestEntity(requestEntity);
            client.executeMethod(mPut); //status is here
            mPut.releaseConnection( );

        }catch(java.io.IOException e){
             System.out.println("Exception in adding a property to an already existing relationship in neo4j database: " + e);
        }


    }

    /**
     * helper function that generates json value and pair collection
     * @param name
     * @param value
     * @return
     */
    private String toJsonNameValuePairCollection(String name, String value) {
        return String.format("{ \"%s\" : \"%s\" }", name, value);
    }
    
    /**
     * adding a person to a graph
     * @param person
     * @throws org.json.JSONException
     */
    public void addPersonToGraph(Person person) throws JSONException{
        String nodeURL = createNode();
        nodesURLs.put(person.id, nodeURL); //to be used later as cached data if needed in the same run time
        addProperty(nodeURL, "id", String.valueOf(person.id));
        addProperty(nodeURL, "age", String.valueOf(person.age));
        addProperty(nodeURL, "firstName", person.firstName);
        addProperty(nodeURL, "surname", person.surname);
        addProperty(nodeURL, "friends", Arrays.toString(person.friends)); //database does not permit arrays
        addProperty(nodeURL, "gender", person.gender);
        for(int friendId: person.friends){
            if(nodesURLs.get(friendId)!=null){ //if you are requesting at this compiling time (when the graph is created by the application)
                addRelationship(nodeURL, nodesURLs.get(friendId), "FRIEND", null);
            }
            else{ //Then it might be a new json file or the node does not exist
                 String friendNodeURL = getPersonNodeURLByPersonId(friendId);
                 if(!friendNodeURL.equals("noURL")){//Then this is a new json file
                    addRelationship(nodeURL, friendNodeURL, "FRIEND", null); 
                 }
                 
            }
        }
    }
    
    /**
     * get friends by passing the personid
     * @param personId
     * @throws org.json.JSONException
     * @return JSONArray
     */
    public JSONArray getFriends(int personId) throws JSONException{
        JSONArray friendsArray = null;
        try{
            String nodeURI = getPersonNodeURLByPersonId(personId);
            if(!nodeURI.equals("noURL")){
            StringBuilder sb = new StringBuilder();
            sb.append("start person = node(*) match person-[:FRIEND]-friend where person.id=");  
            sb.append("\'");
            sb.append(personId);
            sb.append("\'");
            sb.append(" return collect(distinct friend) as friends;");
            String jsonToSend = generateJsonQuery(sb.toString(), "");
            //System.out.println(jsonToSend);
            HttpClient client = new HttpClient();
            PostMethod mPost = new PostMethod(SERVER_ROOT_URI+"/db/data/cypher");
            Header mtHeader = new Header();
            mtHeader.setName("content-type");
            mtHeader.setValue("application/json");
            mtHeader.setName("accept");
            mtHeader.setValue("application/json");
            mPost.addRequestHeader(mtHeader);
            StringRequestEntity requestEntity = new StringRequestEntity(jsonToSend,"application/json","UTF-8");
            mPost.setRequestEntity(requestEntity);
            int status = client.executeMethod(mPost); //status is here
            if(status == 200 || status == 201 || status == 200){
            JSONObject responseJSONFirstObj = new JSONObject(mPost.getResponseBodyAsString());
            JSONArray responseJSONFirstArr = new JSONArray(responseJSONFirstObj.get("data").toString());
            JSONArray responseJSONSecondArr = new JSONArray(responseJSONFirstArr.get(0).toString());
                if(responseJSONSecondArr.length()>0){
                      JSONArray responseJSONThirdArr = new JSONArray(responseJSONSecondArr.get(0).toString());
                      friendsArray = responseJSONThirdArr;
                   }
                else{
                      friendsArray = responseJSONSecondArr;
                }
            } 
            mPost.releaseConnection();
            }
        }catch(java.io.IOException e){
             System.out.println("Exception in getting friends by person id in neo4j database : " + e);
        }
        return friendsArray;
    }
    
    /**
     * getting friends of friends of a person by passing him/her id
     * @param personId
     * @throws JSONException
     * @return JSONArray
     */
    public JSONArray getFriendsOfFriends(int personId) throws JSONException{
        JSONArray friendOfFriendsArray = null;
        try{
            String nodeURI = getPersonNodeURLByPersonId(personId);
            if(!nodeURI.equals("noURL")){
            StringBuilder sb = new StringBuilder();
            sb.append("start person = node(*) match person-[:FRIEND*1..2]-friend where person.id=");  
            sb.append("\'");
            sb.append(personId);
            sb.append("\'");
            sb.append(" and not(person-[:FRIEND]-friend) return collect(distinct friend) as friendsoffriends;");
            String jsonToSend = generateJsonQuery(sb.toString(), "");
            //System.out.println(jsonToSend);
            HttpClient client = new HttpClient();
            PostMethod mPost = new PostMethod(SERVER_ROOT_URI+"/db/data/cypher");
            Header mtHeader = new Header();
            mtHeader.setName("content-type");
            mtHeader.setValue("application/json");
            mtHeader.setName("accept");
            mtHeader.setValue("application/json");
            mPost.addRequestHeader(mtHeader);
            StringRequestEntity requestEntity = new StringRequestEntity(jsonToSend,"application/json","UTF-8");
            mPost.setRequestEntity(requestEntity);
            int status = client.executeMethod(mPost); //status is here
            if(status == 200 || status == 201 || status == 200){
            JSONObject responseJSONFirstObj = new JSONObject(mPost.getResponseBodyAsString());
            JSONArray responseJSONFirstArr = new JSONArray(responseJSONFirstObj.get("data").toString());
            JSONArray responseJSONSecondArr = new JSONArray(responseJSONFirstArr.get(0).toString());
               if(responseJSONSecondArr.length()>0){
                   JSONArray responseJSONThirdArr = new JSONArray(responseJSONSecondArr.get(0).toString());
                   friendOfFriendsArray = responseJSONThirdArr;
                }
               else{
               friendOfFriendsArray = responseJSONSecondArr;
               }
            } 
            mPost.releaseConnection();
            }
        }catch(java.io.IOException e){
             System.out.println("Exception in getting friends of friends of a person by person id in neo4j database: " + e);
        }
        return friendOfFriendsArray;
    }
    
    /**
     * getting suggested friends of a person by passing his/her id
     * @param personId
     * @throws JSONException
     * @return JSONArray
     */
    public JSONArray getSuggestedFriends(int personId) throws JSONException{
        JSONArray suggestedFriendsArray = null;
        try{
            String nodeURI = getPersonNodeURLByPersonId(personId);
            if(!nodeURI.equals("noURL")){
            StringBuilder sb = new StringBuilder();
            sb.append("START person=node(*) MATCH person-[:FRIEND]-()-[:FRIEND]-sugfof, person-[:FRIEND]-()-[:FRIEND]-sugfof where person.id=");   
            sb.append("\'");
            sb.append(personId);
            sb.append("\'");
            sb.append(" and not(person-[:FRIEND]-sugfof) RETURN DISTINCT sugfof;");
            String jsonToSend = generateJsonQuery(sb.toString(), "");
            //System.out.println(jsonToSend);
            HttpClient client = new HttpClient();
            PostMethod mPost = new PostMethod(SERVER_ROOT_URI+"/db/data/cypher");
            Header mtHeader = new Header();
            mtHeader.setName("content-type");
            mtHeader.setValue("application/json");
            mtHeader.setName("accept");
            mtHeader.setValue("application/json");
            mPost.addRequestHeader(mtHeader);
            StringRequestEntity requestEntity = new StringRequestEntity(jsonToSend,"application/json","UTF-8");
            mPost.setRequestEntity(requestEntity);
            int status = client.executeMethod(mPost); //status is here
            if(status == 200 || status == 201 || status == 200){
                JSONObject responseJSONFirstObj = new JSONObject(mPost.getResponseBodyAsString());
                JSONArray responseJSONFirstArr = new JSONArray(responseJSONFirstObj.get("data").toString());
                if(responseJSONFirstArr.length()>0){
                JSONArray responseJSONSecondArr = new JSONArray(responseJSONFirstArr.get(0).toString());
                suggestedFriendsArray = responseJSONSecondArr;
                }
                else{
                suggestedFriendsArray = responseJSONFirstArr;
                }
            } 
            mPost.releaseConnection();
            }
        }catch(java.io.IOException e){
             System.out.println("Exception in getting suggested friends of a person by passing his/her id in neo4j database : " + e);
        }
        return suggestedFriendsArray;
    }
    
    
    /**
     * helper function for getting the node URL of a person by his/her id to be used later for other operations
     * @param PersonId
     * @throws JSONException
     * @return String
     */
     public String getPersonNodeURLByPersonId(int PersonId) throws JSONException{
          String location = null;    
         try{
            StringBuilder sb = new StringBuilder();
            sb.append("start a=node(*) match a where a.id=");
            sb.append("\'");
            sb.append(PersonId);
            sb.append("\'");
            sb.append(" RETURN a;");
            String jsonToSend = generateJsonQuery(sb.toString(), "");
            //System.out.println(jsonToSend);
            HttpClient client = new HttpClient();
            PostMethod mPost = new PostMethod(SERVER_ROOT_URI+"/db/data/cypher");
            Header mtHeader = new Header();
            mtHeader.setName("content-type");
            mtHeader.setValue("application/json");
            mtHeader.setName("accept");
            mtHeader.setValue("application/json");
            mPost.addRequestHeader(mtHeader);
            StringRequestEntity requestEntity = new StringRequestEntity(jsonToSend,"application/json","UTF-8");
            mPost.setRequestEntity(requestEntity);
            int status = client.executeMethod(mPost); //status is here
            //System.out.println(mPost.getResponseBodyAsString());
            if(status == 200 || status == 201 || status == 200){
            JSONObject responseJSONFirstObj = new JSONObject(mPost.getResponseBodyAsString());
            JSONArray responseJSONFirstArr = new JSONArray(responseJSONFirstObj.get("data").toString());
            //System.out.println(responseJSONFirstArr.length());
                if(responseJSONFirstArr.length() != 0){
                JSONArray responseJSONSecondArr = new JSONArray(responseJSONFirstArr.get(0).toString());
                JSONObject responseJSONSecondObj = new JSONObject(responseJSONSecondArr.get(0).toString());
                location = responseJSONSecondObj.getString("self").toString();  
                }
                else{
                location = "noURL";      
                }
            } 
            else{
            location = "noURL";      
            }
            mPost.releaseConnection( );
        }catch(java.io.IOException e){
             System.out.println("Exception in getting the node URL of a person by his/her id in neo4j database : " + e);
        }
        return location;
    }
    
    /**
    * getting person data by his/her id
    * @param PersonId
    * @throws JSONException
    * @return JSONObject
    */
    public JSONObject getPersonDataByPersonId(int PersonId) throws JSONException{
       JSONObject personDataObj = new JSONObject("{}");  
       Person personData = SocialGraph.getPersonById(PersonId);
       if(personData!=null){
              personDataObj.put("id", personData.id);
              personDataObj.put("firstName", personData.firstName);
              personDataObj.put("surname", personData.surname);
              personDataObj.put("age", personData.age);
              personDataObj.put("gender", personData.gender);
              personDataObj.put("friends", Arrays.toString(personData.friends)); //to Store in a json object and in the database(which does not permit arrays)
       }
       else{
              try{
                 StringBuilder sb = new StringBuilder();
                 sb.append("start a=node(*) match a where a.id=");
                 sb.append("\'");
                 sb.append(PersonId);
                 sb.append("\'");
                 sb.append(" RETURN a;");
                 String jsonToSend = generateJsonQuery(sb.toString(), "");
                 //System.out.println(jsonToSend);
                 HttpClient client = new HttpClient();
                 PostMethod mPost = new PostMethod(SERVER_ROOT_URI+"/db/data/cypher");
                 Header mtHeader = new Header();
                 mtHeader.setName("content-type");
                 mtHeader.setValue("application/json");
                 mtHeader.setName("accept");
                 mtHeader.setValue("application/json");
                 mPost.addRequestHeader(mtHeader);
                 StringRequestEntity requestEntity = new StringRequestEntity(jsonToSend,"application/json","UTF-8");
                 mPost.setRequestEntity(requestEntity);
                 int status = client.executeMethod(mPost); //status is here
                 if(status == 200 || status == 201 || status == 200){
                 JSONObject responseJSONFirstObj = new JSONObject(mPost.getResponseBodyAsString());
                 JSONArray responseJSONFirstArr = new JSONArray(responseJSONFirstObj.get("data").toString());
                 //System.out.println(responseJSONFirstArr.length());
                     if(responseJSONFirstArr.length() > 0){
                          JSONArray responseJSONSecondArr = new JSONArray(responseJSONFirstArr.get(0).toString());
                          JSONObject responseJSONSecondObj = new JSONObject(responseJSONSecondArr.get(0).toString());
                          personDataObj = responseJSONSecondObj.getJSONObject("data");  
                     }
                     else{
                          JSONObject emptyObj = new JSONObject("{}");
                          personDataObj = emptyObj;      
                     }
                 } 
                 mPost.releaseConnection( );
             }catch(java.io.IOException e){
                  System.out.println("Exception in getting person data by his/her id in neo4j database: " + e);
             }
        }
        return personDataObj;
    }
     
    /**
     * helper function which generates the json of the Cypher query to be sent later as REST API request to the database
     * @param query
     * @param jsonAttributes
     * @return String
     */
    private String generateJsonQuery(String query,String ... jsonAttributes) {
        StringBuilder sb = new StringBuilder();
        sb.append("{ \"query\" : \"");
        sb.append(query);
        if(jsonAttributes == null || jsonAttributes.length < 1) {
            sb.append("\"");
            sb.append("{}");
        } else {
            sb.append("\", \"params\" : ");
            sb.append("{");
            for(int i = 0; i < jsonAttributes.length; i++) {
                sb.append(jsonAttributes[i]);
                if(i < jsonAttributes.length -1) { // Miss off the final comma
                    sb.append(", ");
                }
            }
        }
        sb.append(" }");
        sb.append(" }");
        return sb.toString();
    }
    

}
