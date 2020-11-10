package com.example.amazonfakereviewdetection.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.amazonfakereviewdetection.R;
import com.example.amazonfakereviewdetection.api.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText etLink;
    private Button btnPost;
    public Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etLink =findViewById(R.id.etLink);
        btnPost=findViewById(R.id.btnPost);




        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String link= etLink.getText().toString().trim();

                Call<ResponseBody> call = RetrofitClient
                        .getInstance()
                        .getApi()
                        .postLink(link);

                call.enqueue(new Callback<ResponseBody>() {
                                 @Override
                                 public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                     String s= response.body().toString();
                                     Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG);
                                     Log.d("POST SUCCESSFUL",link);
                                     intent = new Intent(MainActivity.this, ResultActivity.class);
                                     startActivity(intent);
                                 }

                                 @Override
                                 public void onFailure(Call<ResponseBody> call, Throwable t) {
                                     Log.d("CANNOT POSTTTTT", t.getMessage());
                                 }
                             }
                );
            }
        });
    }




}