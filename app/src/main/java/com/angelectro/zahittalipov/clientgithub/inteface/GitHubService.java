package com.angelectro.zahittalipov.clientgithub.inteface;


import com.angelectro.zahittalipov.clientgithub.entity.User;
import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by Zahit Talipov on 17.11.2015.
 */
public interface GitHubService {

    //авторизация
    @GET("/login/oauth/authorize")
    Call<ResponseBody> getAuthenticityToken(@Query("client_id") String s,
                                            @Query("state") String state,
                                            @Query("Scope") String scope);

    @FormUrlEncoded
    @POST("session")
    Call<ResponseBody> getCode(@Field("login") String login,
                               @Field("password") String password,
                               @Field("authenticity_token") String token,
                               @Field("return_to") String return_to);

    @Headers({"Accept: application/json"})
    @POST("login/oauth/access_token")
    Call<ResponseBody> getAccessToken(@Query("client_id") String client_id,
                                      @Query("client_secret") String client_secret,
                                      @Query("code") String code,
                                      @Query("state") String state);

    @GET("authorizations")
    Call<ResponseBody> getAuthorizations();

    @GET("user")
    Call<User> getUser();

    @POST("authorizations")
    Call<ResponseBody> createNewAuthorizations(@Query("scopes") String[] scopes,
                                               @Query("client_id") String client,
                                               @Query("client_secret") String secret);
    @FormUrlEncoded
    @POST("login/oauth/authorize")
    Call<ResponseBody> authorizeClientGitHubAngelectro(@Field("authenticity_token") String authenticity_token,
                                                       @Field("client_id") String client_id,
                                                       @Field("redirect_uri") String redirect_uri,
                                                       @Field("state") String state,
                                                       @Field("scope") String scope,
                                                       @Field("authorize")String button);
}
