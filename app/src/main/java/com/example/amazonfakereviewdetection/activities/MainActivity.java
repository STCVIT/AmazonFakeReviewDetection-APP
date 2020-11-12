package com.example.amazonfakereviewdetection.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
import com.google.android.material.snackbar.Snackbar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText etLink;
    private Button btnPost;
    public Intent intent;
    private TextView tvText,tvResult,tvError;
    private ImageView imageViewEmoticon,imageViewError;
    private int backButtonCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etLink =findViewById(R.id.etLink);
        btnPost=findViewById(R.id.btnPost);
        tvText= findViewById(R.id.tvText);
        tvResult=findViewById(R.id.tvResult);
        imageViewEmoticon=findViewById(R.id.imageviewEmoticon);
        imageViewError=findViewById(R.id.imageviewError);
        tvError=findViewById(R.id.tvError);


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

                                     backButtonCount=0;
                                     etLink.setText(" ");
                                     etLink.setVisibility(View.GONE);
                                     btnPost.setVisibility(View.GONE);

                                     ReviewOutput reviewOutput= response.body();
                                     Log.d("POST WORKING",response.message());

                                     Double fPercent =  reviewOutput.getPercentFakeReview();
                                     Double avgConfidence = reviewOutput.getAverageConfidence();

                                     if((fPercent==0.0) &&(avgConfidence==0.0)){
                                         tvError.setVisibility(View.VISIBLE);
                                         imageViewError.setImageResource(R.drawable.ic_undraw_online_posts_h475);
                                     }

                                     else if(fPercent>66){
                                         tvText.setVisibility(View.VISIBLE);
                                         tvResult.setVisibility(View.VISIBLE);
                                         tvResult.setText(fPercent + " " + "reviews are fake. We will suggest you to be completely sure before buying this product");
                                         imageViewEmoticon.setImageResource(R.drawable.ic_frame_3);
                                         imageViewEmoticon.setVisibility(View.VISIBLE);
                                         imageViewError.setVisibility(View.GONE);
                                     }

                                     else if(fPercent >33){
                                         tvText.setVisibility(View.VISIBLE);
                                         tvResult.setVisibility(View.VISIBLE);
                                         tvResult.setText(fPercent + " " + "reviews are fake. We will suggest you to ");
                                         imageViewEmoticon.setImageResource(R.drawable.ic_frame_2);
                                         imageViewEmoticon.setVisibility(View.VISIBLE);
                                         imageViewError.setVisibility(View.GONE);
                                     }

                                     else{
                                         tvText.setVisibility(View.VISIBLE);
                                         tvResult.setVisibility(View.VISIBLE);
                                         tvResult.setText("Only"+ " "+ fPercent + " " + "reviews are fake. We will suggest you to go for this product");
                                         imageViewEmoticon.setImageResource(R.drawable.ic_frame_1);
                                         imageViewEmoticon.setVisibility(View.VISIBLE);
                                         imageViewError.setVisibility(View.GONE);
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

    @Override
    public void onBackPressed() {
        backButtonCount++;
        if (backButtonCount == 1) {

            etLink.setText("");
            imageViewError.setImageResource(R.drawable.ic_undraw_online_shopping_re_k1sv);
            tvError.setVisibility(View.GONE);
            tvText.setVisibility(View.GONE);
            tvResult.setVisibility(View.GONE);
            imageViewEmoticon.setVisibility(View.GONE);
            imageViewError.setVisibility(View.VISIBLE);
            etLink.setVisibility(View.VISIBLE);
            btnPost.setVisibility(View.VISIBLE);
        }

        else if (backButtonCount ==2) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Snackbar.make(findViewById(android.R.id.content),"Press back again to exit.",Snackbar.LENGTH_SHORT).setBackgroundTint(getColor(R.color.purple_200)).show();
            }
            backButtonCount++;
        }

        else{
            backButtonCount = 0;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

}