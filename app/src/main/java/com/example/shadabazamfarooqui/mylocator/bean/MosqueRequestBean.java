package com.example.shadabazamfarooqui.mylocator.bean;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by BerylSystems on 09-Jan-18.
 */

public class MosqueRequestBean {
    UserBean bean;
    String mosqueName;
    String mosqueAddress;
    String mosqueImage;
    LatLng latLong;

    public LatLng getLatLong() {
        return latLong;
    }

    public void setLatLong(LatLng latLong) {
        this.latLong = latLong;
    }

    public UserBean getBean() {
        return bean;
    }

    public void setBean(UserBean bean) {
        this.bean = bean;
    }

    public String getMosqueName() {
        return mosqueName;
    }

    public void setMosqueName(String mosqueName) {
        this.mosqueName = mosqueName;
    }

    public String getMosqueAddress() {
        return mosqueAddress;
    }

    public void setMosqueAddress(String mosqueAddress) {
        this.mosqueAddress = mosqueAddress;
    }

    public String getMosqueImage() {
        return mosqueImage;
    }

    public void setMosqueImage(String mosqueImage) {
        this.mosqueImage = mosqueImage;
    }
}
