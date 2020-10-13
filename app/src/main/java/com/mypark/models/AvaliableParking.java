package com.mypark.models;

import com.google.gson.annotations.SerializedName;


public class AvaliableParking {

    @SerializedName("id")
    public int id;
    @SerializedName("uuid")
    private String uuid;
    @SerializedName("Start_time")
    public int time_start;
    @SerializedName("Finish_time")
    public int time_finish;
    @SerializedName("parking_Status")
    public String parkingStatus;
    @SerializedName("date_parking")
    public String date;
    @SerializedName("adress")
    public String adress;
    @SerializedName("lat")
    public double lat;
    @SerializedName("lng")
    public double lng;
    @SerializedName("user_name")
    public String username;
    @SerializedName("price")
    public int price;


}
