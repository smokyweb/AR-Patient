package com.ar.patient.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class signupResoponse extends BaseBean implements Serializable {


    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("session_id")
    @Expose
    private String sessionId;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("is_public")
    @Expose
    private String isPublic;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }

}
