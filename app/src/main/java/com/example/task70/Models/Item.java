package com.example.task70.Models;

public class Item {
    public String name;
    public String phoneNumber;
    public String description;
    public String location;
    public String catergory;
    public String image;
    public int id;
    public int postType;
    public String date;

    public Item(String name, String phoneNumber, String description, String location, String catergory, String image, int id, int postType, String date) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.location = location;
        this.catergory = catergory;
        this.image = image;
        this.id = id;
        this.postType = postType;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCatergory() {
        return catergory;
    }

    public void setCatergory(String catergory) {
        catergory = catergory;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostType() {
        return postType;
    }

    public void setPostType(int postType) {
        this.postType = postType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
