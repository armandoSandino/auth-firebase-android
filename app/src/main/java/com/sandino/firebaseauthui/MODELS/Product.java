package com.sandino.firebaseauthui.MODELS;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Product {

    private String image;
    private String link;
    private String name;


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Product(String image, String link, String name) {
        this.image = image;
        this.link = link;
        this.name = name;
    }
    public Product () {

    }
}
