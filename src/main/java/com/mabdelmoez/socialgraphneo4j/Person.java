package com.mabdelmoez.socialgraphneo4j;

import java.util.Arrays;

/**
  * @author mabdelmoez
  * Social Network Graph from JSON file URL with Local Neo4j Graph DataBase And Cypher Requests to get Person/s, NodeURL, Friends, Friends of Friends, Suggested Friends with a simple idea of caching data into hashmaps
  * Person Class
  * 
 */


public class Person { 
    int id; //long might be considered for large data numbers
    String firstName;
    String surname;
    String gender;
    int age;
    int friends[]; // friends array when we use it to retreive persons by id from the cahced data or even from the database

    public Person(String firstName,String surname,String gender,int age, int id, int friends[]){
       this.firstName=firstName;
       this.surname=surname;
       this.gender=gender;
       this.age=age;
       this.id=id;
       this.friends=friends;
    }
    
    public Person(){
    }
    
    public Person(int id){
      this.id=id;
    }
    
       public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getsurname() {
		return surname;
	}

	public void setsurname(String surname) {
		this.surname = surname;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public int getAge() {
		return age;
	}
        
        public void setAge(int age) {
		this.age=age;
	}
        
        public int[] getFriends() {
		return friends;
	}
        
        public void setFriends(int friends[]) {
		this.friends=friends;
	}

     @Override
    public String toString(){
      return "Person :[ firstName : "+ firstName +", surname : "+ surname + ", age :" + age  +", gender : "+ gender + ", id :" + id + " Friends : [ " + Arrays.toString(friends)+"]" ;
    }
 }  
