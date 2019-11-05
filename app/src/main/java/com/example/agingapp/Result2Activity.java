package com.example.agingapp;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class Result2Activity extends AppCompatActivity {

    ImageView bhome;
    ImageView young_result;
    ImageView now_result;
    String uploadFileName1=null;
    String uploadFileName2=null;

    String downloadFilePath1=null;
    String downloadFilePath2=null;

    String downloadFileName1=null;
    String downloadFileName2=null;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result2);

        young_result=(ImageView)findViewById(R.id.young_result);
        now_result=(ImageView)findViewById(R.id.now_result);

        Intent intent =getIntent();
        uploadFileName1=intent.getStringExtra("name2_1");
        uploadFileName2=intent.getStringExtra("name2_2");

        downloadFilePath1="http://203.255.176.79:13000/save_album/"+uploadFileName1+"_1.jpg";
        downloadFilePath2="http://203.255.176.79:13000/repos_album/"+uploadFileName2;

        Glide.with(this).load(downloadFilePath1).into(young_result);
        Glide.with(this).load(downloadFilePath2).into(now_result);


        bhome = (ImageView)findViewById(R.id.home);

        bhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "홈으로 돌아갑니다.", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent( Result2Activity.this, MainActivity.class); //여러 역할 중 화면 이동
                //startActivity(intent); //넘어가라
                finish();
            }
        });

    }
}
