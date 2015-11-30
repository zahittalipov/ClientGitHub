package com.angelectro.zahittalipov.clientgithub;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.angelectro.zahittalipov.clientgithub.entity.User;
import com.angelectro.zahittalipov.clientgithub.inteface.ApiGitHub;

/**
 * Created by Zahit Talipov on 19.11.2015.
 */
public class AppDelegate extends Application {
    public static final String NAME = "gitHubClient";
    public static boolean existUser = false;
    private static User currentUser = null;
    private static SharedPreferences preferences;

    public static User getCurrentUser() {

        return currentUser;
    }

    public static void saveUser(User currentUser, Context context) {
        preferences = context.getSharedPreferences(NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("access_token", currentUser.getAccessToken());
        editor.putString("login", currentUser.getLogin());
        editor.putString("id", currentUser.getId());
        editor.putString("avatar", currentUser.getmAvatarUrl());
        editor.putString("urlSystemAvatar", currentUser.getUrlSystemAvatar());
        Log.d("editor", "" + editor.commit());
        AppDelegate.currentUser = currentUser;
    }

    public static boolean logout(Context context) {
        preferences = context.getSharedPreferences(NAME, MODE_PRIVATE);
        ApiGitHub.ACCESS_TOKEN = null;
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("access_token");
        editor.remove("login");
        editor.remove("id");
        editor.remove("avatar");
        editor.remove("urlSystemAvatar");
        editor.commit();
        Log.d("log", "logout");
        return true;
    }

    public static boolean isExistUser() {
        existUser = preferences.contains("login");
        return existUser;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = null;
        preferences = getSharedPreferences(NAME, MODE_PRIVATE);
        existUser = preferences.contains("login");
        if (existUser) {
            User user = new User();
            user.setAccessToken(preferences.getString("access_token", null));
            user.setLogin(preferences.getString("login", "login"));
            user.setId(preferences.getString("id", "id"));
            user.setmAvatarUrl(preferences.getString("avatar", "avatar"));
            user.setEmail(preferences.getString("email", null));
            user.setName(preferences.getString("name", null));
            user.setUrlSystemAvatar(preferences.getString("urlSystemAvatar", null));
            ApiGitHub.ACCESS_TOKEN=user.getAccessToken();
            currentUser=user;
            Log.d("load", "appDelegate");
        }
    }

}
