package com.example.agingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {

    Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        handler = new Handler();
        handler.postDelayed(mrun,1000);

    }

    Runnable mrun = new Runnable() {
        @Override
        public void run() {

            try {
                Intent home = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(home);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }catch(Exception e){
                Log.e("태그:",e.getMessage());
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(mrun);
    }
}
