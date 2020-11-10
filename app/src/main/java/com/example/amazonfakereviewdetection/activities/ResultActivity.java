package com.example.amazonfakereviewdetection.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.amazonfakereviewdetection.R;
import com.example.amazonfakereviewdetection.api.RetrofitClient;
import com.example.amazonfakereviewdetection.model.ReviewOutput;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultActivity extends AppCompatActivity {

    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvResult=findViewById(R.id.tvResult);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        Call<List<ReviewOutput>> reviewOutputCall = RetrofitClient
                .getInstance()
                .getApi()
                .getReview();





        reviewOutputCall.enqueue(new Callback<List<ReviewOutput>>() {
            @Override
            public void onResponse(Call<List<ReviewOutput>> call, Response<List<ReviewOutput>> response) {

                if (!response.isSuccessful()) {
                    tvResult.setText("Code: " + response.code());
                    Log.d("GET UNSUCCESSFUL","Kyu hua bhai");
                    return;
                }

                List<ReviewOutput> reviewOutputs = response.body();
                Log.d("GET WORKING",response.message());
                for (ReviewOutput reviewOutput : reviewOutputs) {
                    String content = "";
                    content += "Percentage: " + reviewOutput.getPercentFakeReview() + "\n";
                    content += "Average Confidence: " + reviewOutput.getAverageConfidence() + "\n";

                    tvResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<ReviewOutput>> call, Throwable t) {
                tvResult.setText(t.getMessage());
                Log.d("GET ERROR",t.getMessage());
            }
        });
    }
}