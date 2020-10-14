package com.mypark.network;

import com.mypark.models.AvaliableParking;
import com.mypark.models.OwnerSpot;


import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST("/getAvilableSpots")
    Call<List<AvaliableParking>> executeGetAvaialbeParking(@Body HashMap<String, String> map);

    @POST("/createSpot")
    Call<Void> executeCreateNewSpot(@Body HashMap<String, Object> map);


    @POST("/getOwnerSpot")
    Call<List<OwnerSpot>> executeGetOwnerSpot(@Body HashMap<String, Object> map);

    @POST("/deleteParkingSpot")
    Call<Void> executetDeleteParkingSpot(@Body HashMap<String, Object> map);


    @POST("/disable_parking")
    Call<Void> executeDisableParking(@Body HashMap<String, Object> map);
}
