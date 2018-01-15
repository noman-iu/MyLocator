package com.example.shadabazamfarooqui.mylocator.activity;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import com.example.shadabazamfarooqui.mylocator.R;
import com.example.shadabazamfarooqui.mylocator.adapter.MosqueExpandableListAdapter;
import com.example.shadabazamfarooqui.mylocator.network.request.GetRequest;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DummyActivity extends AppCompatActivity {

    @Bind(R.id.expandableListView)
    ExpandableListView expandableListView;
    public static GetRequest response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);
        ButterKnife.bind(this);
        expandableListView.setAdapter(new MosqueExpandableListAdapter(getApplicationContext(),response));
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousItem = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if(groupPosition != previousItem )
                    expandableListView.collapseGroup(previousItem );
                previousItem = groupPosition;
            }
        });
    }
}
