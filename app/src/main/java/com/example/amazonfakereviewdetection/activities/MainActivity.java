package com.example.amazonfakereviewdetection.activities;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.amazonfakereviewdetection.R;
import com.example.amazonfakereviewdetection.api.RetrofitClient;
import com.example.amazonfakereviewdetection.model.ReviewOutput;
import com.google.android.material.snackbar.Snackbar;
import java.math.BigDecimal;
import java.math.RoundingMode;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private EditText etLink;
    private Button btnPost;
    private TextView tvText,tvResult,tvError;
    private ImageView imageViewEmoticon,imageViewError;
    private ProgressBar progressBar;
    private int backButtonCount=0;
    private NestedScrollView parentLayout;
    private CardView cardView;

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
        progressBar =findViewById(R.id.progressBar);
        parentLayout=findViewById(R.id.parentLayout);
        cardView=findViewById(R.id.cardView);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);



        btnPost.setOnClickListener(v -> {

            String link= etLink.getText().toString().trim();

            imageViewError.setVisibility(View.GONE);
            etLink.setVisibility(View.GONE);
            btnPost.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            Call<ReviewOutput> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .postLink(link);

            call.enqueue(new Callback<ReviewOutput>() {

                             @RequiresApi(api = Build.VERSION_CODES.M)
                             @Override
                             public void onResponse(Call<ReviewOutput> call, Response<ReviewOutput> response) {

                                 backButtonCount=0;
                                 etLink.setText(" ");


                                 ReviewOutput reviewOutput= response.body();
                                 Log.d("POST WORKING",response.message());

                                 double fakePercent =  reviewOutput.getPercentFakeReview();
                                 double fPercent= round(fakePercent);
                                 double avgConfidence = reviewOutput.getAverageConfidence();

                                 if((fPercent==0.0) &&(avgConfidence==0.0)){
                                     progressBar.setVisibility(View.GONE);
                                     tvError.setVisibility(View.VISIBLE);
                                     imageViewError.setImageResource(R.drawable.ic_undraw_online_posts_h475);

                                 }

                                 else if(fPercent>66.66){
                                     progressBar.setVisibility(View.GONE);
                                     tvText.setVisibility(View.VISIBLE);
                                     cardView.setVisibility(View.VISIBLE);
                                     String text=(fPercent + "%"+ " " + "reviews are fake. We will suggest you to be completely sure before buying this product.");
                                     imageViewEmoticon.setImageResource(R.drawable.ic_frame_3);
                                     imageViewEmoticon.setVisibility(View.VISIBLE);
                                     imageViewError.setVisibility(View.GONE);
                                     SpannableString ss = new SpannableString(text);
                                     StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
                                     ss.setSpan(boldSpan, 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                     parentLayout.setBackgroundColor(getColor(R.color.notatall));
                                     tvResult.setText(ss);

                                 }

                                 else if(fPercent >33.33){
                                     progressBar.setVisibility(View.GONE);
                                     tvText.setVisibility(View.VISIBLE);
                                     cardView.setVisibility(View.VISIBLE);
                                     String text= (fPercent + "%"+ " " +"reviews are fake. We will suggest you to think twice before buying this product.");
                                     imageViewEmoticon.setImageResource(R.drawable.ic_frame_2);
                                     imageViewEmoticon.setVisibility(View.VISIBLE);
                                     imageViewError.setVisibility(View.GONE);
                                     parentLayout.setBackgroundColor(getColor(R.color.notcompletly));
                                     SpannableString ss = new SpannableString(text);
                                     StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
                                     ss.setSpan(boldSpan, 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                     tvResult.setText(ss);
                                 }

                                 else{
                                     progressBar.setVisibility(View.GONE);
                                     tvText.setVisibility(View.VISIBLE);
                                     cardView.setVisibility(View.VISIBLE);
                                     String text=("Only"+ " "+ fPercent + "%"+ " " + "reviews are fake. We will suggest you to go for this product.");
                                     imageViewEmoticon.setImageResource(R.drawable.ic_frame_1);
                                     imageViewEmoticon.setVisibility(View.VISIBLE);
                                     imageViewError.setVisibility(View.GONE);
                                     parentLayout.setBackgroundColor(getColor(R.color.completly));
                                     SpannableString ss = new SpannableString(text);
                                     StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
                                     ss.setSpan(boldSpan, 5, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                     tvResult.setText(ss);
                                 }
                             }

                             @Override
                             public void onFailure(Call<ReviewOutput> call, Throwable t) {
                                 progressBar.setVisibility(View.GONE);
                                 imageViewError.setVisibility(View.VISIBLE);
                                 tvError.setVisibility(View.VISIBLE);
                                 imageViewError.setImageResource(R.drawable.ic_undraw_online_posts_h475);
                                 Log.d("CANNOT POSTTTTT", t.getMessage());
                             }
                         }
            );
        });
    }

    private double round(Double fakePercent) {
        BigDecimal bd = BigDecimal.valueOf(fakePercent);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBackPressed() {
        backButtonCount++;
        if (backButtonCount == 1) {

            parentLayout.setBackgroundColor(getColor(R.color.purple_500));
            etLink.setText("");
            imageViewError.setImageResource(R.drawable.ic_undraw_online_shopping_re_k1sv);
            tvError.setVisibility(View.GONE);
            tvText.setVisibility(View.GONE);
            cardView.setVisibility(View.GONE);
            imageViewEmoticon.setVisibility(View.GONE);
            imageViewError.setVisibility(View.VISIBLE);
            etLink.setVisibility(View.VISIBLE);
            btnPost.setVisibility(View.VISIBLE);
        }

        else if (backButtonCount ==2) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Snackbar.make(findViewById(android.R.id.content),"Press back again to exit.",Snackbar.LENGTH_SHORT).setBackgroundTint(getColor(R.color.purple_500)).setTextColor(Color.WHITE).show();
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