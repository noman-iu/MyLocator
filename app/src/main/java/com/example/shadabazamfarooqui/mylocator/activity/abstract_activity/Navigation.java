package com.example.shadabazamfarooqui.mylocator.activity.abstract_activity;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.shadabazamfarooqui.mylocator.R;
import com.example.shadabazamfarooqui.mylocator.activity.DummyActivity;

/**
 * Created by Shadab Azam Farooqui on 24-Dec-17.
 */

public class Navigation extends Map implements NavigationView.OnNavigationItemSelectedListener{


    public void onCreateNavigation(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add_mosque) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                  startActivity(new Intent(getApplicationContext(), DummyActivity.class));
                    //overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                }
            },500);
        } else if (id == R.id.nav_gallery) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "drawer is not ready", Toast.LENGTH_SHORT).show();
                    //overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                }
            },500);
        } else if (id == R.id.nav_slideshow) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "drawer is not ready", Toast.LENGTH_SHORT).show();
                    //overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                }
            },500);
        } else if (id == R.id.nav_manage) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "drawer is not ready", Toast.LENGTH_SHORT).show();
                    //overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                }
            },500);
        } else if (id == R.id.nav_share) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "drawer is not ready", Toast.LENGTH_SHORT).show();
                    //overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                }
            },500);
        } else if (id == R.id.nav_send) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "drawer is not ready", Toast.LENGTH_SHORT).show();
                    //overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                }
            },500);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
