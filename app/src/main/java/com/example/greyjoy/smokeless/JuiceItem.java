package com.example.greyjoy.smokeless;

import android.media.Image;

public class JuiceItem {

    // Lets keep this super simple object
    private String name;
    private float rank;
    private String comments;
    private String picLoc;

    private JuiceItem() {
    }

    public static JuiceItem createJuiceItem() {
        return new JuiceItem();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRank() {
        return rank;
    }

    public void setRank(float rank) {
        this.rank = rank;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getPicLoc() {
        return picLoc;
    }

    public void setPicLoc(String picLoc) {
        this.picLoc = picLoc;
    }
}
