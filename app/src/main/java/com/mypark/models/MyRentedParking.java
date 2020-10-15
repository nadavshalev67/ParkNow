package com.mypark.models;

import com.google.gson.annotations.SerializedName;

public class MyRentedParking {

    @SerializedName("Start_time")
    public String start_time;
    @SerializedName("Finish_time")
    public String finish_time;
    @SerializedName("adress")
    public String adress;
}
