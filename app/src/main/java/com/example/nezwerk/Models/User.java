package com.example.nezwerk.Models;

public class User {
    public String name,email,password,userId;

    public User(String name, String email, String password, String userId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userId = userId;
    }
   // public User(){}

    public User() {}

    public User(String fullName, String email, String password, String phone, String uid) {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    /*
    public User(String name,String email,String password,String userId){
        this.name=name;
        this.email=email;
        this.password=password;
        this.userId=userId;
    }
    public User(){}

    public User(String fullName, String email, String password) {

    }

    public User(String fullName, String email, String password, String phone, String uid) {
    }

    public String getName(){
        String name = this.name;
        return name;
    }*/
}
