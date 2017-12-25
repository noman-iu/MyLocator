package com.example.shadabazamfarooqui.mylocator.bean;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Shadab Azam Farooqui on 17-Dec-17.
 */

public class ReferenceWrapper {
    UserBean userBean;
    Context context;
    public static ReferenceWrapper referenceWrapper;
    public static ReferenceWrapper getRefrenceWrapper(Context context) {
        if (referenceWrapper == null) {
            referenceWrapper = new ReferenceWrapper(context);
        }
        return referenceWrapper;
    }

    private ReferenceWrapper(Context activity) {
        context = activity;

    }
    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }
}
