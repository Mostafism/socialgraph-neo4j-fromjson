socialgraph-neo4j-fromjson
==========================

Social Network Graph from JSON file URL with Local Neo4j Graph DataBase And Cypher Requests to get Person/s, NodeURL, Friends, Friends of Friends, Suggested Friends with a simple idea of caching data into hashmaps.

The project is a maven project which has been developed in java using NetBeans IDE with Junit tests as well.
ApplicationMain is the main class which outputs the data on the console (Spring further enhancements has been planned)
TestApplication is the testing class.


-----Installation Steps-----

---Neo4j Server Setup
1- Download Neo4j Server from http://www.neo4j.org/download and set it up (it is really simple).
2- Before Starting the Server go to settings (by pressing the settings button at the bottom left in neo4j community menu if you are on windows platform).
3- Go to neo4j properties un-comment node_auto_indexing=true and add node_keys_indexable=id, so you could traverse the graph by the auto index property when you do cypher queries.
4- Start the server.

---Project Build
1- Check out the project.
2- Build the project it will run the tests automatically.
3- Run the project will display the output in the console.


JSON File (data.json):
[
  {
    "id": 1,
    "firstName": "Paul",
    "surname": "Crowe",
    "age": 28,
    "gender": "male",
    "friends": [
      2
    ]
  },
  {
    "id": 2,
    "firstName": "Rob",
    "surname": "Fitz",
    "age": 23,
    "gender": "male",
    "friends": [
      1,
      3
    ]
  },
  {
    "id": 3,
    "firstName": "Ben",
    "surname": "O'Carolan",
    "age": null,
    "gender": "male",
    "friends": [
      2,
      4,
      5,
      7
    ]
  },
  {
    "id": 4,
    "firstName": "Victor",
    "surname": "",
    "age": 28,
    "gender": "male",
    "friends": [
      3
    ]
  },
  {
    "id": 5,
    "firstName": "Peter",
    "surname": "Mac",
    "age": 29,
    "gender": "male",
    "friends": [
      3,
      6,
      11,
      10,
      7
    ]
  },
  {
    "id": 6,
    "firstName": "John",
    "surname": "Barry",
    "age": 18,
    "gender": "male",
    "friends": [
      5
    ]
  },
  {
    "id": 7,
    "firstName": "Sarah",
    "surname": "Lane",
    "age": 30,
    "gender": "female",
    "friends": [
      3,
      5,
      20,
      12,
      8
    ]
  },
  {
    "id": 8,
    "firstName": "Susan",
    "surname": "Downe",
    "age": 28,
    "gender": "female",
    "friends": [
      7
    ]
  },
  {
    "id": 9,
    "firstName": "Jack",
    "surname": "Stam",
    "age": 28,
    "gender": "male",
    "friends": [
      12
    ]
  },
  {
    "id": 10,
    "firstName": "Amy",
    "surname": "Lane",
    "age": 24,
    "gender": "female",
    "friends": [
      5,
      11
    ]
  },
  {
    "id": 11,
    "firstName": "Sandra",
    "surname": "Phelan",
    "age": 28,
    "gender": "female",
    "friends": [
      5,
      10,
      19,
      20
    ]
  },
  {
    "id": 12,
    "firstName": "Laura",
    "surname": "Murphy",
    "age": 33,
    "gender": "female",
    "friends": [
      7,
      9,
      13,
      20
    ]
  },
  {
    "id": 13,
    "firstName": "Lisa",
    "surname": "Daly",
    "age": 28,
    "gender": "female",
    "friends": [
      12,
      14,
      20
    ]
  },
  {
    "id": 14,
    "firstName": "Mark",
    "surname": "Johnson",
    "age": 28,
    "gender": "male",
    "friends": [
      13,
      15
    ]
  },
  {
    "id": 15,
    "firstName": "Seamus",
    "surname": "Crowe",
    "age": 24,
    "gender": "male",
    "friends": [
      14
    ]
  },
  {
    "id": 16,
    "firstName": "Daren",
    "surname": "Slater",
    "age": 28,
    "gender": "male",
    "friends": [
      18,
      20
    ]
  },
  {
    "id": 17,
    "firstName": "Dara",
    "surname": "Zoltan",
    "age": 48,
    "gender": "male",
    "friends": [
      18,
      20
    ]
  },
  {
    "id": 18,
    "firstName": "Marie",
    "surname": "D",
    "age": 28,
    "gender": "female",
    "friends": [
      17
    ]
  },
  {
    "id": 19,
    "firstName": "Catriona",
    "surname": "Long",
    "age": 28,
    "gender": "female",
    "friends": [
      11,
      20
    ]
  },
  {
    "id": 20,
    "firstName": "Katy",
    "surname": "Couch",
    "age": 28,
    "gender": "female",
    "friends": [
      7,
      11,
      12,
      13,
      16,
      17,
      19
    ]
  }
]
