package com.example.shadabazamfarooqui.mylocator.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.shadabazamfarooqui.mylocator.R;
import com.example.shadabazamfarooqui.mylocator.activity.abstract_activity.Map;
import com.example.shadabazamfarooqui.mylocator.activity.abstract_activity.Navigation;
import com.example.shadabazamfarooqui.mylocator.adapter.MosqueAdapter;
import com.example.shadabazamfarooqui.mylocator.network.request.GetRequest;
import com.example.shadabazamfarooqui.mylocator.utils.Networking;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Response;

public class HomeActivity extends Navigation {

    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.mapLayout)
    LinearLayout mapLayout;
    @Bind(R.id.listLayout)
    LinearLayout listLayout;
    @Bind(R.id.mosqueList)
    ListView mosqueList;
    private Boolean boolForMenu = true;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        context = this;
        if (Networking.isNetworkAvailable(this)){
            progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
//            progressDialog.show();
            checkGps(this);
            Map.boolForMapList=true;
            onCreateNavigation();
            onCreateMap(mosqueList);
        }else {
            Snackbar.make(coordinatorLayout,"Check your internet connection",Snackbar.LENGTH_LONG).show();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if (boolForMenu) {
                mapLayout.setVisibility(View.GONE);
                listLayout.setVisibility(View.VISIBLE);
                item.setTitle("Map");
                boolForMenu = false;

            } else {
                mapLayout.setVisibility(View.VISIBLE);
                listLayout.setVisibility(View.GONE);
                item.setTitle("List");
                boolForMenu = true;
            }
            return true;
        }
        if (id == R.id.action_refresh) {
            checkGps(this);
            Map.boolForMapList=true;
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
