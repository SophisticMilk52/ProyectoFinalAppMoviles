package com.example.proyectofinalappmoviles.model;

import androidx.annotation.NonNull;

public class Category {

    private String uid;
    private String name;

    public Category(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public Category() {
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

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
