package com.example.shadabazamfarooqui.mylocator.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shadabazamfarooqui.mylocator.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddMosqueActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    int PLACE_PICKER_REQUEST = 1;
    GoogleApiClient mGoogleApiClient;
    private static final int CAMERA_REQUEST = 1888;
    private static final int GALLERY_REQUEST = 4565;

    ImageView photoIcon;


    @Bind(R.id.address)
    EditText address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        setContentView(R.layout.activity_add_mosque);
        ButterKnife.bind(this);
        initActionbar();
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        Intent intent;
        try {

            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);

        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

        photoIcon = (ImageView) this.findViewById(R.id.camraIcon);
        photoIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog();
            }
        });
    }

    private void initActionbar() {
        ActionBar actionBar = getSupportActionBar();
        View viewActionBar = getLayoutInflater().inflate(R.layout.action_bar_tittle_text_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#009DE0")));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(viewActionBar, params);
        TextView actionbarTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        actionbarTitle.setText("ADD MASQUE");
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    void dialog() {
        final CharSequence[] items = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddMosqueActivity.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                } else if (item == 1) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY_REQUEST);
                }
            }

        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            LayoutParams params = photoIcon.getLayoutParams();
            params.height = LayoutParams.MATCH_PARENT;
            //params.width = Utils.getScreenWidth();

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 0, 0);
            photoIcon.setLayoutParams(lp);

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            photoIcon.setImageBitmap(photo);
            //photoIcon.setImageBitmap(Bitmap.createScaledBitmap(photo, Utils.getScreenWidth(), Utils.getPercentageScreenWidth(35), false));

            //Log.i("mytag","Enddd:"+Utils.getPercentageScreenWidth(35) +"   "+Utils.getScreenWidth());
        }
        if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {

            Uri uri = data.getData();
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                photoIcon.setImageBitmap(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                final Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Log.i("info", "Place ID:" + place.getId() + " Name :" + place.getName() + " Type :" + place.getPlaceTypes() + " Long :" + place.getLatLng() + " Locale :" + place.getLocale());
                //Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                address.setText("" + place.getAddress());
                //latitude.setText(""+place.getAddress());

               /* AddPlaceRequest newplace =
                        new AddPlaceRequest(
                                "Multikbis Technology Pvt. Ltd.", // Name
                                //new LatLng(-33.7991, 151.2813), // Latitude and longitude
                                place.getLatLng(), // Latitude and longitude
                                "Dwarka, New Delhi 110075", // Address
                                Collections.singletonList(Place.TYPE_RESTAURANT), // Place types
                                "+91 1800 199 742", // Phone number
                                Uri.parse("http://www.multikbis.com/") // Website
                        );*/

            }

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
