package com.example.shadabazamfarooqui.mylocator.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shadabazamfarooqui.mylocator.R;
import com.example.shadabazamfarooqui.mylocator.activity.abstract_activity.Map;
import com.example.shadabazamfarooqui.mylocator.activity.abstract_activity.Navigation;
import com.example.shadabazamfarooqui.mylocator.adapter.MoreListAdapter;
import com.example.shadabazamfarooqui.mylocator.adapter.MosqueAdapter;
import com.example.shadabazamfarooqui.mylocator.network.request.GetRequest;
import com.example.shadabazamfarooqui.mylocator.utils.BottomNavigationViewHelper;
import com.example.shadabazamfarooqui.mylocator.utils.CustomeTittle;
import com.example.shadabazamfarooqui.mylocator.utils.Networking;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Response;

public class HomeActivity extends Navigation {
    @Bind(R.id.mapLayout)
    LinearLayout mapLayout;
    @Bind(R.id.listLayout)
    LinearLayout listLayout;
    @Bind(R.id.mosqueList)
    ListView mosqueList;
    @Bind(R.id.qiblaLayout)
    LinearLayout qiblaLayout;
    @Bind(R.id.moreLayout)
    LinearLayout moreLayout;
    @Bind(R.id.moreList)
    ListView moreList;
    public static Context context;
    boolean doubleBackToExitPressedOnce = false;
    List<String> listName;
    List<Integer> listImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        context = this;
        initActionbar("Mosque Locator");
//        onCreateNavigation(toolbar);
        if (Networking.isNetworkAvailable(this)) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            checkGps(this);
            Map.boolForMapList = true;
            onCreateMap(mosqueList);
        } else {
            Toast.makeText(context, "Check your internet connection", Toast.LENGTH_LONG).show();
        }
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navigation);
    }
    private void initActionbar(String tittleText) {
        ActionBar actionBar = getSupportActionBar();
        View viewActionBar = getLayoutInflater().inflate(R.layout.action_bar_tittle_text_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#009DE0")));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(viewActionBar, params);
        TextView actionbarTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        actionbarTitle.setText(tittleText);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_mosue) {
            Intent intent = new Intent(getApplicationContext(), AddMosqueActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_refresh) {
            checkGps(this);
            Map.boolForMapList = true;
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_map:
                    mapLayout.setVisibility(View.VISIBLE);
                    listLayout.setVisibility(View.GONE);
                    qiblaLayout.setVisibility(View.GONE);
                    moreLayout.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_list:
                    mapLayout.setVisibility(View.GONE);
                    listLayout.setVisibility(View.VISIBLE);
                    qiblaLayout.setVisibility(View.GONE);
                    moreLayout.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_qibla:
                    mapLayout.setVisibility(View.GONE);
                    listLayout.setVisibility(View.GONE);
                    qiblaLayout.setVisibility(View.VISIBLE);
                    moreLayout.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_more:
                    mapLayout.setVisibility(View.GONE);
                    listLayout.setVisibility(View.GONE);
                    qiblaLayout.setVisibility(View.GONE);
                    moreLayout.setVisibility(View.VISIBLE);
                    moreListContent();
                    return true;
            }
            return false;
        }

    };

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void moreListContent() {
        listName = new ArrayList<>();
        listImage = new ArrayList<>();

        listName.add("Abc");
        listName.add("Abc");
        listName.add("Abc");
        listName.add("Abc");
        listName.add("Abc");
        listName.add("Abc");
        listName.add("Abc");
        listName.add("Abc");
        listName.add("Abc");
        listName.add("Abc");
        listName.add("Abc");
        listName.add("Abc");
        listName.add("Abc");
        listName.add("Abc");
        listName.add("Abc");
        listName.add("Abc");

        listImage.add(R.drawable.anim_message_icon);
        listImage.add(R.drawable.anim_message_icon);
        listImage.add(R.drawable.anim_message_icon);
        listImage.add(R.drawable.anim_message_icon);
        listImage.add(R.drawable.anim_message_icon);
        listImage.add(R.drawable.anim_message_icon);
        listImage.add(R.drawable.anim_message_icon);
        listImage.add(R.drawable.anim_message_icon);
        listImage.add(R.drawable.anim_message_icon);
        listImage.add(R.drawable.anim_message_icon);
        listImage.add(R.drawable.anim_message_icon);
        listImage.add(R.drawable.anim_message_icon);
        listImage.add(R.drawable.anim_message_icon);
        listImage.add(R.drawable.anim_message_icon);
        listImage.add(R.drawable.anim_message_icon);
        listImage.add(R.drawable.anim_message_icon);
        moreList.setAdapter(new MoreListAdapter(getApplicationContext(), listName, listImage));
    }
}
