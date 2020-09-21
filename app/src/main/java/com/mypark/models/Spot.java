package com.mypark.models;

import com.google.android.gms.maps.model.LatLng;

public class Spot {

    private LatLng mLatLng;
    private String mUserName;
    private int mPrice;
    private String mHashCode;

    public LatLng getLatLng() {
        return mLatLng;
    }

    public String getUserName() {
        return mUserName;
    }

    public int getPrice() {
        return mPrice;
    }

    public String getHashCode() {
        return mHashCode;
    }

    public Spot(LatLng latLng, String userName, int price, String hashCode) {
        this.mLatLng = latLng;
        this.mUserName = userName;
        this.mPrice = price;
        this.mHashCode = hashCode;
    }
}
