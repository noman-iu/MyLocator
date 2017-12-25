package com.example.shadabazamfarooqui.mylocator.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Shadab Azam Farooqui on 25-Dec-17.
 */

public class Networking {

   /*  <com.example.shadabazamfarooqui.mylocator.utils.CircleImageView
    android:id="@+id/userImage"
    android:layout_width="70dp"
    android:layout_height="70dp"

    android:paddingTop="@dimen/nav_header_vertical_spacing"
    android:src="@drawable/shadab2" />*/

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
