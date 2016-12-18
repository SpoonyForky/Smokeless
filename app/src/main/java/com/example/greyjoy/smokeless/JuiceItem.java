package com.example.greyjoy.smokeless;

import android.media.Image;

public class JuiceItem {

    public JuiceItem(String brand, String name, String[] flavours, int rank, String comments, Store store) {
        this.brand = brand;
        this.name = name;
        this.flavours = flavours;
        this.rank = rank;
        this.comments = comments;
    }
    private Image juiceImage;
    private String brand;
    private String name;
    private String[] flavours;
    private int rank;
    private String comments;
    private Store store;

    public JuiceItem() {
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getFlavours() {
        return flavours;
    }

    public void setFlavours(String[] flavours) {
        this.flavours = flavours;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }
}
