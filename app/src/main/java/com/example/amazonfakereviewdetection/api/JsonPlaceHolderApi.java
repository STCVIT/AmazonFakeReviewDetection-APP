package com.example.amazonfakereviewdetection.api;

import com.example.amazonfakereviewdetection.model.ReviewOutput;



import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface JsonPlaceHolderApi {

    @GET("/result")
    Call<ReviewOutput> getReview();

    @FormUrlEncoded
    @POST("/result")
    Call <ReviewOutput> postLink(
            @Field("link") String link);

}