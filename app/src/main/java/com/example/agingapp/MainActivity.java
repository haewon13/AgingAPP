package com.example.agingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;

public class MainActivity extends AppCompatActivity {

    ImageView bcompare;
    ImageView bcamera;

    private long backBtTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preference = getSharedPreferences("a", MODE_PRIVATE);
        int firstshow = preference.getInt("First",0);

        if(firstshow!=1){
            Log.e("태그:","이제 main에서 FirstStart으로");
            Intent intent = new Intent(MainActivity.this, FirstStartActivity.class);
            startActivity(intent);
            finish();
        }

        bcompare = (ImageView)findViewById(R.id.compare);
        bcamera = (ImageView)findViewById(R.id.camera);

        bcompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent( MainActivity.this, CompareActivity.class); //여러 역할 중 화면 이동
                startActivity(intent); //넘어가라
                //finish();
            }
        });

        bcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent( MainActivity.this, CameraActivity.class); //여러 역할 중 화면 이동
                startActivity(intent2); //넘어가라
                //finish();
            }
        });


    }


    @Override
    public void onBackPressed() {
        long currTime = System.currentTimeMillis();
        long gapTime = currTime - backBtTime;

        if(0 <= gapTime && 2000>=gapTime){
            super.onBackPressed();
        }else{
            backBtTime = currTime;
            Toast.makeText(this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
