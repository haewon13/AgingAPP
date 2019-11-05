package com.example.agingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.net.URL;

public class Result1Activity extends AppCompatActivity {

    ImageView bhome;
    ImageView bagain;
    ImageView bsave;
    ImageView bdownload;

    String uploadFileName=null;

    String downloadFilePath=null;
    String downloadFileName=null;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result1);
        bdownload = (ImageView) findViewById(R.id.combine_result1);
        Intent intent = getIntent();

        uploadFileName=intent.getStringExtra("name2");
        try {
            downloadFilePath = "http://203.255.176.79:13000/save_camera/" + uploadFileName + "_1.jpg";
            Glide.with(this).load(downloadFilePath).into(bdownload);

        }catch (Exception e){
            Log.e("태그:","Glide 작동에 오류");
        }
        bhome = (ImageView) findViewById(R.id.home);
        bagain = (ImageView) findViewById(R.id.again);
        bsave = (ImageView) findViewById(R.id.save);

        bsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "아직 개발 중.", Toast.LENGTH_SHORT).show();
            }
        });

        bhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "홈으로 돌아갑니다.", Toast.LENGTH_SHORT).show();
                //Intent intent1 = new Intent( Result1Activity.this, MainActivity.class); //여러 역할 중 화면 이동
                //startActivity(intent1);
                finish();
            }
        });

        bagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "카메라로 돌아갑니다.", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(Result1Activity.this, CameraActivity.class);
                startActivity(intent2);
                finish();
            }
        });
    }
}
