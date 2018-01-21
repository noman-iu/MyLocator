package com.example.shadabazamfarooqui.mylocator.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.shadabazamfarooqui.mylocator.R;
import com.example.shadabazamfarooqui.mylocator.bean.MosqueRequestBean;
import com.example.shadabazamfarooqui.mylocator.bean.ReferenceWrapper;
import com.example.shadabazamfarooqui.mylocator.bean.UserBean;
import com.example.shadabazamfarooqui.mylocator.local_db.DatabaseHandler;
import com.example.shadabazamfarooqui.mylocator.utils.Conversion;
import com.example.shadabazamfarooqui.mylocator.utils.CustomeTittle;
import com.example.shadabazamfarooqui.mylocator.utils.Networking;
import com.example.shadabazamfarooqui.mylocator.utils.ParameterConstants;
import com.example.shadabazamfarooqui.mylocator.utils.Preferences;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddMosqueActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    int PLACE_PICKER_REQUEST = 1;
    GoogleApiClient mGoogleApiClient;
    private static final int IMAGE_1_CAMERA_REQUEST = 11;
    private static final int IMAGE_1_GALLERY_REQUEST = 12;
    private static final int IMAGE_2_CAMERA_REQUEST = 21;
    private static final int IMAGE_2_GALLERY_REQUEST = 22;
    private static final int IMAGE_3_CAMERA_REQUEST = 31;
    private static final int IMAGE_3_GALLERY_REQUEST = 32;
    private static final int IMAGE_4_CAMERA_REQUEST = 41;
    private static final int IMAGE_4_GALLERY_REQUEST = 42;
    private static final int IMAGE_5_CAMERA_REQUEST = 51;
    private static final int IMAGE_5_GALLERY_REQUEST = 52;
    @Bind(R.id.image_1_layout)
    LinearLayout image_1_layout;
    @Bind(R.id.image_2_layout)
    LinearLayout image_2_layout;
    @Bind(R.id.image_3_layout)
    LinearLayout image_3_layout;
    @Bind(R.id.image_4_layout)
    LinearLayout image_4_layout;
    @Bind(R.id.image_5_layout)
    LinearLayout image_5_layout;
    @Bind(R.id.image1)
    ImageView image1;
    @Bind(R.id.image2)
    ImageView image2;
    @Bind(R.id.image3)
    ImageView image3;
    @Bind(R.id.image4)
    ImageView image4;
    @Bind(R.id.image5)
    ImageView image5;
    @Bind(R.id.mosque_name)
    EditText mosque_name;
    @Bind(R.id.mosque_address)
    EditText mosque_address;
    @Bind(R.id.submitLayout)
    LinearLayout submit;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    LatLng latLng;
    Snackbar snackbar;
    Bitmap bitmap1, bitmap2, bitmap3, bitmap4, bitmap5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        setContentView(R.layout.activity_add_mosque);
        ButterKnife.bind(this);
        CustomeTittle.initActionbar(this, "Add Mosque", true);
        progressDialog = new ProgressDialog(AddMosqueActivity.this);
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

        image_1_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog(IMAGE_1_CAMERA_REQUEST, IMAGE_1_GALLERY_REQUEST);
            }
        });
        image_2_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog(IMAGE_2_CAMERA_REQUEST, IMAGE_2_GALLERY_REQUEST);
            }
        });
        image_3_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog(IMAGE_3_CAMERA_REQUEST, IMAGE_3_GALLERY_REQUEST);
            }
        });
        image_4_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog(IMAGE_4_CAMERA_REQUEST, IMAGE_4_GALLERY_REQUEST);
            }
        });
        image_5_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog(IMAGE_5_CAMERA_REQUEST, IMAGE_5_GALLERY_REQUEST);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                UserBean userBean = ReferenceWrapper.getRefrenceWrapper(AddMosqueActivity.this).getUserBean();
                Boolean check = Preferences.getInstance(AddMosqueActivity.this).getLogin();
                Boolean isAlreadySignUp = new DatabaseHandler(getApplicationContext()).isAlreadyLoggedIn();
                if (isAlreadySignUp) {
                    userBean = new UserBean();
                    userBean.setName(Preferences.getInstance(getApplicationContext()).getName());
                    userBean.setEmail(Preferences.getInstance(getApplicationContext()).getEmail());
                    userBean.setMobile(Preferences.getInstance(getApplicationContext()).getMobile());
                    MosqueRequestBean mosqueRequestBean = new MosqueRequestBean();
                    mosqueRequestBean.setBean(userBean);
                    mosqueRequestBean.setMosqueName(mosque_name.getText().toString());
                    mosqueRequestBean.setMosqueAddress(mosque_address.getText().toString());

                    mosqueRequestBean.setImage1(Conversion.toString(bitmap1));
                    mosqueRequestBean.setImage2(Conversion.toString(bitmap2));
                    mosqueRequestBean.setImage3(Conversion.toString(bitmap3));
                    mosqueRequestBean.setImage4(Conversion.toString(bitmap4));
                    mosqueRequestBean.setImage5(Conversion.toString(bitmap5));
                    mosqueRequestBean.setLatLong(latLng);

                    databaseReference = FirebaseDatabase.getInstance().getReference(ParameterConstants.MOSQUE);
                    Date today = new Date();
                    long id = today.getTime();
                    databaseReference.child("" + id).setValue(mosqueRequestBean, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                progressDialog.dismiss();
                                Snackbar.make(coordinatorLayout, "your request is submitted", Toast.LENGTH_SHORT).show();
                            } else {
                                Snackbar.make(coordinatorLayout, "Sorry! something is wrong", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
                } else {
                    progressDialog.dismiss();
                    snackbar = Snackbar
                            .make(coordinatorLayout, "Please Sign Up!", Snackbar.LENGTH_LONG)
                            .setAction("SIGN UP", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    errorMessage();
                                }
                            });
                    snackbar.show();
                }
            }
        });
    }


    void dialog(final int requestFirst, final int requestSecond) {
        final CharSequence[] items = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddMosqueActivity.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, requestFirst);
                } else if (item == 1) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select File"), requestSecond);
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

        if (requestCode == IMAGE_1_CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            LayoutParams params = image1.getLayoutParams();
            params.height = LayoutParams.MATCH_PARENT;
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lp.setMargins(0, 0, 0, 0);
            image1.setLayoutParams(lp);
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            image1.setImageBitmap(photo);
            bitmap1 = photo;
        }
        if (requestCode == IMAGE_1_GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                image1.setImageBitmap(bm);
                bitmap1 = bm;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if (requestCode == IMAGE_2_CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            LayoutParams params = image2.getLayoutParams();
            params.height = LayoutParams.MATCH_PARENT;
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lp.setMargins(0, 0, 0, 0);
            image2.setLayoutParams(lp);
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            image2.setImageBitmap(photo);
            bitmap2 = photo;
        }
        if (requestCode == IMAGE_2_GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                image2.setImageBitmap(bm);
                bitmap2 = bm;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == IMAGE_3_CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            LayoutParams params = image3.getLayoutParams();
            params.height = LayoutParams.MATCH_PARENT;
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lp.setMargins(0, 0, 0, 0);
            image3.setLayoutParams(lp);
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            image3.setImageBitmap(photo);
            bitmap3 = photo;
        }
        if (requestCode == IMAGE_3_GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {

            Uri uri = data.getData();
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                image3.setImageBitmap(bm);
                bitmap3 = bm;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if (requestCode == IMAGE_4_CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            LayoutParams params = image4.getLayoutParams();
            params.height = LayoutParams.MATCH_PARENT;
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lp.setMargins(0, 0, 0, 0);
            image4.setLayoutParams(lp);
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            image4.setImageBitmap(photo);
            bitmap4 = photo;
        }

        if (requestCode == IMAGE_4_GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {

            Uri uri = data.getData();
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                image4.setImageBitmap(bm);
                bitmap4 = bm;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        if (requestCode == IMAGE_5_CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            LayoutParams params = image5.getLayoutParams();
            params.height = LayoutParams.MATCH_PARENT;
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lp.setMargins(0, 0, 0, 0);
            image5.setLayoutParams(lp);
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            image5.setImageBitmap(photo);
            bitmap5 = photo;
        }

        if (requestCode == IMAGE_5_GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                image5.setImageBitmap(bm);
                bitmap5 = bm;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                final Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Log.i("info", "Place ID:" + place.getId() + " Name :" + place.getName() + " Type :" + place.getPlaceTypes() + " Long :" + place.getLatLng() + " Locale :" + place.getLocale());
                latLng = place.getLatLng();
                mosque_address.setText("" + place.getAddress());
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

    public void errorMessage() {

        if (Networking.isNetworkAvailable(getApplicationContext())) {
            startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
        } else {
            snackbar = Snackbar
                    .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            networkError();
                        }
                    });
            snackbar.show();
        }
    }

    private void networkError() {
        if (Networking.isNetworkAvailable(getApplicationContext())) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
                    finish();
                }
            }, 3 * 1000);
        } else {
            snackbar = Snackbar
                    .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            networkError();
                        }
                    });
            snackbar.show();
        }
    }

    public String toString(Bitmap bmp) {

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, bao); // bmp is bitmap from user image file
        bmp.recycle();
        byte[] byteArray = bao.toByteArray();
        String imageB64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return imageB64;
    }
}
