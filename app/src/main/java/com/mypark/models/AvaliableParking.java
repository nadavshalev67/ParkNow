package com.mypark.models;

import com.google.gson.annotations.SerializedName;



public class AvaliableParking {

    @SerializedName("id")
    public String id;
    @SerializedName("user_name")
    public String username;
    @SerializedName("date")
    public String date;
    @SerializedName("lat")
    public String lat;
    @SerializedName("lng")
    public String lng;
    @SerializedName("start_time")
    public String time_start;
    @SerializedName("finish_time")
    public String time_finish;
    @SerializedName("uuid")
    private String uuid;
}
