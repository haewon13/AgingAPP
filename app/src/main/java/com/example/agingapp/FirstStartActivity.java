package com.example.agingapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class FirstStartActivity extends AppCompatActivity {

    ViewPager mPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firststart);

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(new pagerAdapter(getApplicationContext()));

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mPager);
    }

    private View.OnClickListener movePageListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            int infoFirst = 1;
            SharedPreferences a = getSharedPreferences("a", MODE_PRIVATE);
            SharedPreferences.Editor editor = a.edit();
            editor.putInt("First", infoFirst);
            editor.commit();
            finish();
        }
    };

    //PagerAdapter
    private class pagerAdapter extends PagerAdapter {

        private LayoutInflater mInflater;
        public pagerAdapter(Context c){
            super();
            mInflater = LayoutInflater.from(c);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return "*";
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup pager, int position) {
            View v = null;
            if(position==0){
                v= mInflater.inflate(R.layout.firststart1, null);
            }else if(position==1){
                v=mInflater.inflate(R.layout.firststart2, null);
            }else{
                v=mInflater.inflate(R.layout.firststart3,null);
                v.findViewById(R.id.close).setOnClickListener(movePageListener);
            }
            ((ViewPager)pager).addView(v,0);
            return v;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup pager, int position, @NonNull Object view) {
            ((ViewPager)pager).removeView((View)view);
        }

        @Override
        public boolean isViewFromObject(@NonNull View pager, @NonNull Object obj) {
            return pager==obj;
        }

        @Override
        public void restoreState(@Nullable Parcelable arg0, @Nullable ClassLoader arg1) {}

        @Nullable
        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(@NonNull ViewGroup arg0) {}

        @Override
        public void finishUpdate(@NonNull ViewGroup arg0) {}
    }
}


