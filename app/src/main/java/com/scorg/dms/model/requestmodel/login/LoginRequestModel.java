package com.scorg.dms.model.requestmodel.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;

public class LoginRequestModel implements CustomResponse {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("grant_type")
    @Expose
    private String grant_type;

    @SerializedName("client_Id")
    @Expose
    private String client_Id;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }

    public String getClient_Id() {
        return client_Id;
    }

    public void setClient_Id(String client_Id) {
        this.client_Id = client_Id;
    }
}