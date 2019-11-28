package com.example.proyectofinalappmoviles.model;

public class Message {

    private String uid;
    private String senderId;
    private String recieverId;
    private String message;
    private boolean recieved;

    public Message(String uid, String senderId, String recieverId, String message, boolean recieved) {
        this.uid = uid;
        this.senderId = senderId;
        this.recieverId = recieverId;
        this.message = message;
        this.recieved = recieved;
    }

    public Message() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRecieverId() {
        return recieverId;
    }

    public void setRecieverId(String recieverId) {
        this.recieverId = recieverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRecieved() {
        return recieved;
    }

    public void setRecieved(boolean recieved) {
        this.recieved = recieved;
    }
}
