package com.ar.patient.responsemodel.patientsresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Type implements Serializable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("patient_id")
    @Expose
    private String patientId;
    @SerializedName("symptoms")
    @Expose
    private ArrayList<String> symptoms = null;
    @SerializedName("pt_name")
    @Expose
    private String ptName;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("model_android")
    @Expose
    private String modelandroid;
    @SerializedName("front_image")
    @Expose
    private String frontimage;
    @SerializedName("back_image")
    @Expose
    private String backimage;
    @SerializedName("image_id")
    @Expose
    private String imageId;
    @SerializedName("hint_text")
    @Expose
    private String hintText;
    @SerializedName("user_type")
    @Expose
    private String usertype;
    @SerializedName("voice_exam_counter")
    @Expose
    private String voiceExamCounter;
    @SerializedName("physical_exam_counter")
    @Expose
    private String physicalExamCounter;
    @SerializedName("vital_signs")
    @Expose
    private ArrayList<VitalSign> vitalSigns = null;
    @SerializedName("body_parts")
    @Expose
    private ArrayList<BodyPart> bodyParts = null;

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getImageId() {
        return imageId;
    }

    public String getFrontimage() {
        return frontimage;
    }

    public void setFrontimage(String frontimage) {
        this.frontimage = frontimage;
    }

    public String getBackimage() {
        return backimage;
    }

    public void setBackimage(String backimage) {
        this.backimage = backimage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public ArrayList<String> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(ArrayList<String> symptoms) {
        this.symptoms = symptoms;
    }

    public String getPtName() {
        return ptName;
    }

    public void setPtName(String ptName) {
        this.ptName = ptName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getHintText() {
        return hintText;
    }

    public void setHintText(String hintText) {
        this.hintText = hintText;
    }

    public String getVoiceExamCounter() {
        return voiceExamCounter;
    }

    public void setVoiceExamCounter(String voiceExamCounter) {
        this.voiceExamCounter = voiceExamCounter;
    }

    public String getPhysicalExamCounter() {
        return physicalExamCounter;
    }

    public void setPhysicalExamCounter(String physicalExamCounter) {
        this.physicalExamCounter = physicalExamCounter;
    }

    public ArrayList<VitalSign> getVitalSigns() {
        return vitalSigns;
    }

    public void setVitalSigns(ArrayList<VitalSign> vitalSigns) {
        this.vitalSigns = vitalSigns;
    }

    public ArrayList<BodyPart> getBodyParts() {
        return bodyParts;
    }

    public void setBodyParts(ArrayList<BodyPart> bodyParts) {
        this.bodyParts = bodyParts;
    }

    public String getModelandroid() {
        return modelandroid;
    }

    public void setModelandroid(String modelandroid) {
        this.modelandroid = modelandroid;
    }
}