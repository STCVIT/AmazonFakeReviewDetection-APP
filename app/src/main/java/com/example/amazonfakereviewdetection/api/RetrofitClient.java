package com.example.amazonfakereviewdetection.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static RetrofitClient mInstance;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://afrd-stc.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static synchronized RetrofitClient getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public JsonPlaceHolderApi getApi() {
        return retrofit.create(JsonPlaceHolderApi.class);
    }
}