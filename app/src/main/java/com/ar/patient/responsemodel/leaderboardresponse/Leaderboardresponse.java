package com.ar.patient.responsemodel.leaderboardresponse;

import com.ar.patient.responsemodel.BaseBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Leaderboardresponse extends BaseBean implements Serializable {
    @SerializedName("page_size")
    @Expose
    private Integer pageSize;
    @SerializedName("students")
    @Expose
    private ArrayList<Student> students = null;


    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }

}
