package com.example.amazonfakereviewdetection.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amazonfakereviewdetection.R;
import com.example.amazonfakereviewdetection.api.RetrofitClient;
import com.example.amazonfakereviewdetection.model.ReviewOutput;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText etLink;
    private Button btnPost;
    public Intent intent;
    private TextView tvText,tvResult;
    private ImageView imageViewEmoticon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etLink =findViewById(R.id.etLink);
        btnPost=findViewById(R.id.btnPost);
        tvText= findViewById(R.id.tvText);
        tvResult=findViewById(R.id.tvResult);
        imageViewEmoticon=findViewById(R.id.imageviewEmoticon);





        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String link= etLink.getText().toString().trim();

                Call<ReviewOutput> call = RetrofitClient
                        .getInstance()
                        .getApi()
                        .postLink(link);

                call.enqueue(new Callback<ReviewOutput>() {

                                 @Override
                                 public void onResponse(Call<ReviewOutput> call, Response<ReviewOutput> response) {

                                     etLink.setText(" ");

                                     etLink.setVisibility(View.GONE);
                                     btnPost.setVisibility(View.GONE);



                                     ReviewOutput reviewOutput= response.body();
                                     Log.d("POST WORKING",response.message());

                                     Double fPercent =  reviewOutput.getPercentFakeReview();
                                     Double avgConfidence = reviewOutput.getAverageConfidence();

                                     if((fPercent==0.0) &&(avgConfidence==0.0)){
                                         tvResult.setVisibility(View.VISIBLE);
                                         tvResult.setText("Amazon Fake Review Detection is currently unavailable. We are sorry for the inconvenience caused. Please try again later");
                                     }

                                     else if(fPercent>70){
                                         tvText.setVisibility(View.VISIBLE);
                                         tvResult.setVisibility(View.VISIBLE);
                                         tvResult.setText(fPercent + "reviews are fake");
                                         imageViewEmoticon.setImageResource(R.drawable.ic_frame_3);
                                         imageViewEmoticon.setVisibility(View.VISIBLE);
                                     }

                                     else if(fPercent >25){
                                         tvText.setVisibility(View.VISIBLE);
                                         tvResult.setVisibility(View.VISIBLE);
                                         tvResult.setText(fPercent + "reviews are fake");
                                         imageViewEmoticon.setImageResource(R.drawable.ic_frame_2);
                                         imageViewEmoticon.setVisibility(View.VISIBLE);
                                     }

                                     else{
                                         tvText.setVisibility(View.VISIBLE);
                                         tvResult.setVisibility(View.VISIBLE);
                                         tvResult.setText(fPercent + "reviews are fake");
                                         imageViewEmoticon.setImageResource(R.drawable.ic_frame_1);
                                         imageViewEmoticon.setVisibility(View.VISIBLE);
                                     }
                                 }



                                 @Override
                                 public void onFailure(Call<ReviewOutput> call, Throwable t) {
                                     Log.d("CANNOT POSTTTTT", t.getMessage());
                                 }

                             }
                );
            }
        });
    }




}