package com.example.hansi.finalproject;

public class HappyHomes {
    private String location, desc, price, imageUrl, username;
    public HappyHomes() {
    }

    public HappyHomes(String location, String desc, String price, String imageUrl, String username ){
        this.location = location;
        this.desc = desc;
        this.price = price;
        this.imageUrl = imageUrl;
        this.username = username;
    }
    public void setPrice(String price) {
        this.price = price;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public HappyHomes(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public String getDesc() {
        return desc;
    }

    public String getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getUsername() {
        return username;
    }
}
