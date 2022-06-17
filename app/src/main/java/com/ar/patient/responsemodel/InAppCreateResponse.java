package com.ar.patient.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InAppCreateResponse extends BaseBean {

    @SerializedName("is_subscribed")
    @Expose
    private String isSubscribed;

    @SerializedName("exp_date")
    @Expose
    private String expDate;

    @SerializedName("is_trial")
    @Expose
    private String isTrial;


    public String getIsTrial() {
        return isTrial;
    }

    public void setIsTrial(String isTrial) {
        this.isTrial = isTrial;
    }

    public String getIsSubscribed() {
        return isSubscribed;
    }

    public void setIsSubscribed(String isSubscribed) {
        this.isSubscribed = isSubscribed;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }
}
