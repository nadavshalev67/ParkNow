package com.mypark.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInst {

    private static String BASE_URL = "http://10.0.0.7:3000";
    private static RetrofitInterface mRetrofitInterface;

    public static RetrofitInterface getInstance() {
        if (mRetrofitInterface == null) {
            mRetrofitInterface = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(RetrofitInterface.class);
        }
        return mRetrofitInterface;
    }

}
