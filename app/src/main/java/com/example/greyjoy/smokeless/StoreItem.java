package com.example.greyjoy.smokeless;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class StoreItem implements Parcelable {

    private String name;
    private String location;
    private int rank;
    private String phone;
    private String website;

    public StoreItem() {
    }

    private String comments;

    public StoreItem(String name) {
        this.name = name;
    }

    public StoreItem(String name, String location, int rank, String phone, String comments,String website) {
        this.name = name;
        this.location = location;
        this.rank = rank;
        this.phone = phone;
        this.comments = comments;
        this.setWebsite(website);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.location);
        dest.writeInt(this.rank);
        dest.writeString(this.phone);
        dest.writeString(this.comments);
    }

    protected StoreItem(Parcel in) {
        this.name = in.readString();
        this.location = in.readString();
        this.rank = in.readInt();
        this.phone = in.readString();
        this.comments = in.readString();
    }
    public static ArrayList<StoreItem> getStores (Context context, ArrayList<StoreItem> storesArList) {
        // get the names
        ArrayList<StoreItem> stores = new ArrayList<StoreItem>();
        return storesArList;
    }
    public static final Creator<StoreItem> CREATOR = new Creator<StoreItem>() {
        @Override
        public StoreItem createFromParcel(Parcel source) {
            return new StoreItem(source);
        }

        @Override
        public StoreItem[] newArray(int size) {
            return new StoreItem[size];
        }
    };

    @Override
    public String toString(){
        return getName()+";" +getLocation()+";"+getRank() +";" +getPhone() +";"+getComments()+";"+getWebsite() ;
    }


    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
