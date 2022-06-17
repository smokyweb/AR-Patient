package com.ar.patient.responsemodel.myprofile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Test {

    @SerializedName("test_id")
    @Expose
    private String testId;
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("patient_name")
    @Expose
    private String patientName;
    @SerializedName("patient_avatar")
    @Expose
    private String patientAvatar;
    @SerializedName("patient_type")
    @Expose
    private String patientType;

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientAvatar() {
        return patientAvatar;
    }

    public void setPatientAvatar(String patientAvatar) {
        this.patientAvatar = patientAvatar;
    }

    public String getPatientType() {
        return patientType;
    }

    public void setPatientType(String patientType) {
        this.patientType = patientType;
    }

}