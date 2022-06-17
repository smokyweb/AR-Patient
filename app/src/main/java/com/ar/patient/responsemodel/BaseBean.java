package com.ar.patient.responsemodel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BaseBean implements Serializable {
    @SerializedName("code")
    public String code;
    @SerializedName("message")
    public String message;

    public boolean isSuccess() {
        if (code.equalsIgnoreCase("0"))
            return true;
        else
            return false;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
