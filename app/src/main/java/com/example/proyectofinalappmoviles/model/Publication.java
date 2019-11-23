package com.example.proyectofinalappmoviles.model;

public class Publication {

    private String uid;
    private String title;
    private String description;
    private int price;
    private String category;
    private String sellerId;

    public Publication(String uid, String title, String description, int price, String category, String sellerId) {
        this.uid = uid;
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = category;
        this.sellerId = sellerId;
    }

    public Publication() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
}
