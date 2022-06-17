package com.ar.patient.responsemodel.myprofile;

import com.ar.patient.responsemodel.BaseBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class MyProfileResponse extends BaseBean implements Serializable {
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("rank")
    @Expose
    private String rank;
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("page_size")
    @Expose
    private Integer pageSize;
    @SerializedName("tests")
    @Expose
    private ArrayList<Test> tests = null;


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public ArrayList<Test> getTests() {
        return tests;
    }

    public void setTests(ArrayList<Test> tests) {
        this.tests = tests;
    }

}
