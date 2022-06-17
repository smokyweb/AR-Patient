package com.ar.patient.responsemodel.patientsresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class BodyPart implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("body_part_id")
    @Expose
    private String bodyPartId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("symptoms")
    @Expose
    private ArrayList<String> symptoms = null;

    public String getBodyPartId() {
        return bodyPartId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(ArrayList<String> symptoms) {
        this.symptoms = symptoms;
    }

}