package com.angelectro.zahittalipov.clientgithub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.angelectro.zahittalipov.clientgithub.entity.User;
import com.angelectro.zahittalipov.clientgithub.inteface.ApiGitHub;
import com.angelectro.zahittalipov.clientgithub.inteface.GitHubService;
import com.squareup.okhttp.ResponseBody;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.CookieManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Zahit Talipov on 21.10.2015.
 */
@EActivity(R.layout.login)
public class Login extends AppCompatActivity {
    @ViewById
    EditText login;
    @ViewById
    EditText password;
    @ViewById(R.id.rootLoginLayout)
    LinearLayout rootLayout;
    @ViewById(R.id.progressLoginLayout)
    RelativeLayout progressLayout;
    @ViewById(R.id.signInBtn)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("view", "login");
        if (AppDelegate.isExistUser())
            startMain();
    }

    public void sigInClick(View view) {
        progress(true);
        login.setText("zahittalipov");
        password.setText("aminaballuRR48");
        login(login.getText().toString(), password.getText().toString());
    }

    private void login(final String login, final String password) {
        final GitHubService serviceGit = ApiGitHub.getAuthorizationService(this);
        final GitHubService serviceApiGit = ApiGitHub.getAPI(this);
        final Callback<User> callbackGetUser = new Callback<User>() {
            @Override
            public void onResponse(Response<User> response, Retrofit retrofit) {
                Log.d("getUserResponseCode", "" + response.code());
                final User user = response.body();
                Log.d("Authorization", user.getLogin() + user.getEmail());
                AppDelegate.saveUser(user, getApplicationContext());
                progress(false);
                startMain();
            }

            @Override
            public void onFailure(Throwable t) {

            }
        };
        final Callback<ResponseBody> callbackGetAccessToken = new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                try {
                    Log.d("query", response.raw().request().urlString());
                    String result = response.body().string();
                    JSONObject object = new JSONObject(result);
                    ApiGitHub.ACCESS_TOKEN = object.getString("access_token");
                    Log.d("AccessToken", ApiGitHub.ACCESS_TOKEN);
                    Call<User> call = serviceApiGit.getUser();
                    call.enqueue(callbackGetUser);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        };
        final Callback<ResponseBody> callbackAuthorizeClientGitHubAngelectro = new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                ApiGitHub.CODE = response.raw().request().url().getQuery().split("&")[0].split("=")[1];
                Call<ResponseBody> call = serviceGit.getAccessToken(ApiGitHub.CLIENT_ID, ApiGitHub.CLIENT_SECRET, ApiGitHub.CODE, ApiGitHub.STATE);
                call.enqueue(callbackGetAccessToken);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        };
        final Callback<ResponseBody> callbackGetCode = new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                try {

                    String body = response.body().string();
                    String paramName = response.raw().request().url().getPath();
                    Call<ResponseBody> call = null;
                    if (paramName.contains("/login/oauth/authorize")) {
                        ApiGitHub.AUTH_TOKEN = getAuthToken(body);
                        call = serviceGit.authorizeClientGitHubAngelectro(ApiGitHub.AUTH_TOKEN, ApiGitHub.CLIENT_ID, ApiGitHub.REDIRECT_URI, ApiGitHub.STATE, "", "1");
                        call.enqueue(callbackAuthorizeClientGitHubAngelectro);
                    } else {
                        ApiGitHub.CODE = response.raw().request().url().getQuery().split("&")[0].split("=")[1];
                        call = serviceGit.getAccessToken(ApiGitHub.CLIENT_ID, ApiGitHub.CLIENT_SECRET, ApiGitHub.CODE, ApiGitHub.STATE);
                        call.enqueue(callbackGetAccessToken);
                    }

                } catch (NullPointerException e) {
                    progress(false);
                    Toast.makeText(Login.this, "Неправильный логин или пароль", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        };
        final Callback<ResponseBody> callbackGetAutToken = new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                Log.d("q","asdasdg");
                if (response.code() == ApiGitHub.NO_INTERNET_CODE) {
                       progress(false);
                       Toast.makeText(Login.this,"Нет доступа к интернету",Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        String result = null;
                        result = response.body().string();
                        ApiGitHub.AUTH_TOKEN = getAuthToken(result);
                        Call<ResponseBody> call = serviceGit.getCode(login, password, ApiGitHub.AUTH_TOKEN, ApiGitHub.RETURN_TO);
                        call.enqueue(callbackGetCode);
                    } catch (IOException e) {
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        };
        CookieManager cookieManager = (CookieManager) ApiGitHub.client.getCookieHandler();
        cookieManager.getCookieStore().removeAll();
        Call<ResponseBody> call;
        Response<ResponseBody> bodyResponse = null;
        call = serviceGit.getAuthenticityToken(ApiGitHub.CLIENT_ID, ApiGitHub.STATE, "user");
        call.enqueue(callbackGetAutToken);
    }

    private String getAuthToken(String html) {
        Pattern pattern = Pattern.compile(ApiGitHub.AUTH_TOKEN_PATTERN);
        Matcher matcher = pattern.matcher(html);
        matcher.find();
        return matcher.group();

    }

    private void startMain() {
        startActivity(new Intent(Login.this, MainActivity.class));
        finish();
    }

    private void progress(boolean visible) {
        if (visible) {
            login.setEnabled(false);
            password.setEnabled(false);
            button.setEnabled(false);
            progressLayout.setVisibility(View.VISIBLE);
        } else {
            login.setEnabled(true);
            password.setEnabled(true);
            button.setEnabled(true);
            progressLayout.setVisibility(View.INVISIBLE);
        }
    }
}
