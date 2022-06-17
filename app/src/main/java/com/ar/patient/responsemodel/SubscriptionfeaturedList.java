package com.ar.patient.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SubscriptionfeaturedList implements Serializable {
    @SerializedName("post_title")
    @Expose
    public String posttitle;

    public String getPosttitle() {
        return posttitle;
    }

    public void setPosttitle(String posttitle) {
        this.posttitle = posttitle;
    }
}
