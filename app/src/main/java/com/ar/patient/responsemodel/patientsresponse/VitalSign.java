package com.ar.patient.responsemodel.patientsresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VitalSign implements Serializable {

    @SerializedName("vital_sign")
    @Expose
    private String vitalSign;
    @SerializedName("value")
    @Expose
    private String value;

    public String getVitalSign() {
        return vitalSign;
    }

    public void setVitalSign(String vitalSign) {
        this.vitalSign = vitalSign;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}