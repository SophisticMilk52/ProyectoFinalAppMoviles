package com.example.proyectofinalappmoviles.model;

public class User {

    private String uid;
    private String name;
    private String email;
    private String username;
    private String phone;
    private String password;

    public User(String uid, String name, String email, String username, String phone, String password) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.username = username;
        this.phone = phone;
        this.password = password;
    }

    public User() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
