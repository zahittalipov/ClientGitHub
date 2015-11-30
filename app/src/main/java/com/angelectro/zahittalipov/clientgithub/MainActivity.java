package com.angelectro.zahittalipov.clientgithub;

import android.app.ListFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.Click;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static String FRAGMENT_INSTANCE_NAME = "fragment";
    View viewHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        viewHeader = navigationView.inflateHeaderView(R.layout.nav_header_main);
        ReposFragment reposFragment = (ReposFragment) getFragmentManager().findFragmentByTag(FRAGMENT_INSTANCE_NAME);
        if (reposFragment == null) {
            ListReposFragment fragment = null;
            fragment = (ListReposFragment) getFragmentManager().findFragmentByTag("list");
            if (fragment == null) {
                Log.d("ef", "fragment");
                fragment = new ListReposFragment_();
            }
            getFragmentManager().beginTransaction().replace(R.id.frameLayoutMy, fragment, "list").commit();
        } else {
            getFragmentManager().beginTransaction().replace(R.id.frameLayoutMy, reposFragment).commit();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        TextView sLogin = (TextView) viewHeader.findViewById(R.id.session_login);
        ImageView avatar = (ImageView) viewHeader.findViewById(R.id.session_avatar);
        sLogin.setText(AppDelegate.getCurrentUser().getLogin());
        Log.d("usrl",AppDelegate.getCurrentUser().getmAvatarUrl());
        new DownloadImage().execute(AppDelegate.getCurrentUser().getmAvatarUrl(), avatar);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (!getFragmentManager().popBackStackImmediate())
                super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_repositories: {
                getFragmentManager().beginTransaction().replace(R.id.frameLayoutMy, new ListReposFragment_(), "list").commit();
                break;
            }
            case R.id.nav_exit: {
                if (AppDelegate.logout(this)) {
                    Login_.intent(this).start();
                    finish();
                }
                break;
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }






}
