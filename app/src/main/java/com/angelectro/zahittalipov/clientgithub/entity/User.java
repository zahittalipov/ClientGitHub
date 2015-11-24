package com.angelectro.zahittalipov.clientgithub.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Zahit Talipov on 19.11.2015.
 */
public class User {

    private String accessToken;
    private String urlSystemAvatar;
    @SerializedName("login")
    private String Login;

    @SerializedName("id")
    private String Id;

    @SerializedName("email")
    private String Email;

    @SerializedName("name")
    private String Name;
    @SerializedName("avatar_url")
    private String mAvatarUrl;


    public String getLogin() {
        return Login;
    }

    public void setLogin(String login) {
        this.Login = login;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getmAvatarUrl() {
        return mAvatarUrl;
    }

    public void setmAvatarUrl(String mAvatarUrl) {
        this.mAvatarUrl = mAvatarUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUrlSystemAvatar() {
        return urlSystemAvatar;
    }

    public void setUrlSystemAvatar(String urlSystemAvatar) {
        this.urlSystemAvatar = urlSystemAvatar;
    }
}
