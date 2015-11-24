package com.angelectro.zahittalipov.clientgithub.inteface;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Zahit Talipov on 17.11.2015.
 */
public class ApiGitHub {
    public static int  NO_INTERNET_CODE=600;
    public static final OkHttpClient client = new OkHttpClient();
    public static final String CLIENT_SECRET = "b7c44146a0031703ea27c6765c360a4cd15f69d9";
    public static final String URL_AUTH = "https://github.com/";
    public static final String URL = "https://api.github.com/";
    public static final String RETURN_TO = "/login/oauth/authorize?client_id=141eb265c77f0fdc1589&state=com.angelectro";
    public static final String REDIRECT_URI = "https://github.com";
    public static final String CLIENT_ID = "141eb265c77f0fdc1589";
    public static final String STATE = "com.angelectro";
    public static final String AUTH_TOKEN_PATTERN = "(?<=authenticity_token\" type=\"hidden\" value=\")[^\"]*(?=\" ?/>)";
    public static String ACCESS_TOKEN = null;
    public static String CODE = null;
    public static String AUTH_TOKEN = null;
    private static Context context;

    static {
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        client.setCookieHandler(cookieManager);
        client.interceptors().add(new Interceptor() {
            @Override
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo ni = cm.getActiveNetworkInfo();
                if (ni != null && ni.isConnected()) {
                    Request request = chain.request();
                    if (ACCESS_TOKEN != null)
                        request = request.newBuilder().addHeader("Authorization", "token " + ACCESS_TOKEN).build();
                    return chain.proceed(request);
                }
                return new Response.Builder().code(NO_INTERNET_CODE).request(chain.request()).protocol(Protocol.HTTP_1_0).build();
            }
        });
    }


    @NonNull
    public static GitHubService getAuthorizationService(Context context) {
        ApiGitHub.context = context;
        return getAutRetrofit().create(GitHubService.class);
    }

    @NonNull
    private static Retrofit getAutRetrofit() {
        return new Retrofit.Builder().baseUrl(URL_AUTH).client(client).build();
    }

    @NonNull
    public static GitHubService getAPI(Context context) {
        ApiGitHub.context = context;
        return getCreateApi().create(GitHubService.class);
    }

    @NonNull
    private static Retrofit getCreateApi() {
        return new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client).build();
    }


}
