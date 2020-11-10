package com.example.amazonfakereviewdetection.api;

import com.example.amazonfakereviewdetection.model.ReviewOutput;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JsonPlaceHolderApi {

    @GET("/")
    Call<ReviewOutput> getReview();

    @FormUrlEncoded
    @POST("/")
    Call <ResponseBody> postLink(
            @Field("link") String link);

}
