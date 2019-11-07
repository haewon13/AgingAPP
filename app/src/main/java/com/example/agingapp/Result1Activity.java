package com.example.agingapp;

import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import java.lang.String;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.SocialObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.network.storage.ImageUploadResponse;
import com.kakao.util.helper.log.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.kakao.util.helper.Utility.getPackageInfo;


public class Result1Activity extends AppCompatActivity {


    ImageView bhome;
    ImageView bshare;
    ImageView bsave;
    ImageView bdownload;

    int savefirst=0;

    String uploadFileName=null;

    String downloadFilePath=null;
    String downloadFileName=null;

    String filePath=null;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result1);
        bdownload = (ImageView) findViewById(R.id.combine_result1);

        bsave=(ImageView)findViewById(R.id.save);
        bshare=(ImageView)findViewById(R.id.share);

        Intent intent = getIntent();

        uploadFileName=intent.getStringExtra("name2");
        try {
            downloadFilePath = "http://203.255.176.79:13000/save_camera/" + uploadFileName + "_1.jpg";
            Glide.with(this).load(downloadFilePath).into(bdownload);

        }catch (Exception e){
            Log.e("태그:","Glide 작동에 오류");
        }
        bhome = (ImageView) findViewById(R.id.home);

        bshare.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                if(savefirst==0){
                    Toast.makeText(Result1Activity.this,"저장 후에 공유가 가능합니다!",Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        Uri uri = Uri.parse(filePath);
                        intent.setType("image/*");

                        intent.putExtra(Intent.EXTRA_STREAM, uri);
                        intent.setPackage("com.kakao.talk");
                        startActivity(intent);

                    } catch (ActivityNotFoundException e) {
                        Uri uriMarket = Uri.parse("market://deatils?id=com.kakao.talk");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uriMarket);
                        startActivity(intent);
                    }
                }


            }
        });


        bsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savefirst=1;
                String path="/storage/emulated/0/AgingCapture";

                final FrameLayout capture = (FrameLayout) findViewById(R.id.framelayout);//캡쳐할영역(프레임레이아웃)

                File file=new File(path);
                if(!file.exists()){
                    file.mkdirs();
                    Toast.makeText(Result1Activity.this,  "폴더가 생성되었습니다.",  Toast.LENGTH_SHORT).show();
                }
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String gFileName = "Generated_" + timeStamp + "_"+".jpg";

                Bitmap captureview  = Bitmap.createBitmap(capture.getWidth(), capture.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(captureview);
                Drawable bgDrawable = capture.getBackground();
                if (bgDrawable != null) {
                    bgDrawable.draw(canvas);
                } else {
                    canvas.drawColor(Color.WHITE);
                }
                capture.draw(canvas);

                FileOutputStream fos = null;
                filePath=path+"/"+gFileName;
                try{
                    fos = new FileOutputStream(filePath);
                    captureview.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                    //sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path+"/"+gFileName)));
                    Toast.makeText(Result1Activity.this, "저장완료", Toast.LENGTH_SHORT).show();
                    fos.flush();
                    fos.close();
                    //capture.destroyDrawingCache();

                }catch(FileNotFoundException e){
                    e.printStackTrace();
                }catch(IOException e){
                    e.printStackTrace();
                }

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

    }


    public static String getKeyHash(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.w("태그:", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }



}


