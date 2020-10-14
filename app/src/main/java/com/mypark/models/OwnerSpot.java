package com.mypark.models;

import com.google.gson.annotations.SerializedName;

public class OwnerSpot {

    @SerializedName("uuid")
    public String uuid;
    @SerializedName("spot_key")
    public String spot_key;
    @SerializedName("start_time")
    public String start_time;
    @SerializedName("finish_time")
    public String finish_time;
    @SerializedName("price")
    public String price;
    @SerializedName("adress")
    public String adress;
    @SerializedName("lng")
    public double lng;
    @SerializedName("lat")
    public double lat;
}
