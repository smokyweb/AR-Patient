package com.ar.patient.responsemodel.patientsresponse;

import com.ar.patient.helper.Utils;
import com.ar.patient.responsemodel.BaseBean;
import com.ar.patient.responsemodel.SubscriptionfeaturedList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class PatientsListResponse extends BaseBean implements Serializable {

    @SerializedName("types")
    @Expose
    private ArrayList<Type> types = new ArrayList<>();

    @SerializedName("body_parts")
    @Expose
    private ArrayList<BodyParts> bodyPartsArrayList = new ArrayList<>();

    @SerializedName("subscription_featured_list")
    @Expose
    private ArrayList<SubscriptionfeaturedList> subscriptionfeaturedlist = new ArrayList<>();

    @SerializedName("is_subscribed")
    @Expose
    private String issubscribed;

    @SerializedName("exp_date")
    @Expose
    private String expDate;

    @SerializedName("is_trial")
    @Expose
    private String isTrial;

    @SerializedName("subscription_token")
    @Expose
    private String subscription_token = "";

    @SerializedName("subscription_bundle_id")
    @Expose
    private String subscriptionBundleId;

    @SerializedName("subscription_platform")
    @Expose
    private String subscriptionPlatform;

    public ArrayList<SubscriptionfeaturedList> getSubscriptionfeaturedlist() {
        return subscriptionfeaturedlist;
    }

    public void setSubscriptionfeaturedlist(ArrayList<SubscriptionfeaturedList> subscriptionfeaturedlist) {
        this.subscriptionfeaturedlist = subscriptionfeaturedlist;
    }

    public String getIssubscribed() {
        return Utils.checkNullValueString(issubscribed);
    }

    public void setIssubscribed(String issubscribed) {
        this.issubscribed = issubscribed;
    }

    public void setBodyPartsArrayList(ArrayList<BodyParts> bodyPartsArrayList) {
        this.bodyPartsArrayList = bodyPartsArrayList;
    }

    public String getExpDate() {
        return Utils.checkNullValueString(expDate);
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getIsTrial() {
        return isTrial;
    }

    public void setIsTrial(String isTrial) {
        this.isTrial = isTrial;
    }

    public String getSubscription_token() {
        return Utils.checkNullValueString(subscription_token);
    }

    public void setSubscription_token(String subscription_token) {
        this.subscription_token = subscription_token;
    }

    public String getSubscriptionBundleId() {
        return Utils.checkNullValueString(subscriptionBundleId);
    }

    public void setSubscriptionBundleId(String subscriptionBundleId) {
        this.subscriptionBundleId = subscriptionBundleId;
    }

    public String getSubscriptionPlatform() {
        return Utils.checkNullValueString(subscriptionPlatform);
    }

    public void setSubscriptionPlatform(String subscriptionPlatform) {
        this.subscriptionPlatform = subscriptionPlatform;
    }

    public ArrayList<BodyParts> getBodyPartsArrayList() {
        return bodyPartsArrayList;
    }

    public ArrayList<Type> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<Type> types) {
        this.types = types;
    }

    public class BodyParts implements Serializable {
        @SerializedName("id")
        @Expose
        private String id;

        @SerializedName("name")
        @Expose
        private String name;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

}
