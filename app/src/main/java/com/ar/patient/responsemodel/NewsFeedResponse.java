package com.ar.patient.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NewsFeedResponse extends BaseBean {
    @SerializedName("newsfeeds")
    @Expose
    private ArrayList<Newsfeed> newsfeeds = new ArrayList<>();

    public ArrayList<Newsfeed> getNewsfeeds() {
        return newsfeeds;
    }

    public void setNewsfeeds(ArrayList<Newsfeed> newsfeeds) {
        this.newsfeeds = newsfeeds;
    }
}
