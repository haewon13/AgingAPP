package com.example.agingapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Result2Activity extends AppCompatActivity {

    ImageView bhome;
    ImageView bsave;
    ImageView bshare;

    ImageView young_result;
    ImageView now_result;
    String uploadFileName1=null;
    String uploadFileName2=null;

    String uploadFilePath2=null;

    String downloadFilePath1=null;
    String downloadFilePath2=null;

    String filePath=null;

    int savefirst2=0;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result2);

        bsave=(ImageView)findViewById(R.id.save);
        bshare=(ImageView)findViewById(R.id.share);

        young_result=(ImageView)findViewById(R.id.young_result);
        now_result=(ImageView)findViewById(R.id.now_result);

        Intent intent =getIntent();
        uploadFilePath2=intent.getStringExtra("image3");
        uploadFileName1=intent.getStringExtra("name3_1");
        uploadFileName2=intent.getStringExtra("name3_2");

        downloadFilePath1="http://203.255.176.79:13000/save_camera/"+uploadFileName1+"_1.jpg";
        File imgFile2=new File(uploadFilePath2);
        //downloadFilePath2="http://203.255.176.79:13000/repos_album/"+uploadFileName2;

        Log.i("태그:","Glide에서 쓸 path:"+downloadFilePath1);

        Glide.with(this).load(downloadFilePath1).into(young_result);
        //Glide.with(this).load(downloadFilePath2).into(now_result);

        if(imgFile2.exists()){
            Bitmap bitmap= BitmapFactory.decodeFile(imgFile2.getAbsolutePath());
            ExifInterface exif = null;

            try{
                exif = new ExifInterface(uploadFilePath2);
            }catch(IOException e){
                e.printStackTrace();
            }

            int exifOrientation;
            int exifDegree;

            if(exif != null){
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
            }else{
                exifDegree = 0;
            }
            bitmap=rotate(bitmap,exifDegree);

            now_result.setImageBitmap(bitmap);
        }

        bshare.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                if(savefirst2==0){
                    Toast.makeText(Result2Activity.this,"저장 후에 공유가 가능합니다!",Toast.LENGTH_SHORT).show();
                }
                else {
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

        bhome = (ImageView)findViewById(R.id.home);

        bhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "홈으로 돌아갑니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        bsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String path="/storage/emulated/0/AgingCapture";
                savefirst2=1;
                final LinearLayout capture = (LinearLayout) findViewById(R.id.linearlayout);//캡쳐할영역(리니어레이아웃)

                File file=new File(path);
                if(!file.exists()){
                    file.mkdirs();
                    Toast.makeText(Result2Activity.this,  "폴더가 생성되었습니다.",  Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(Result2Activity.this, "저장완료", Toast.LENGTH_SHORT).show();
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

    }


    //카메라 회전에 대한 이미지 회전 구문
    private int exifOrientationToDegrees(int exifOrientation){
        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90){
            return 90;
        }else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180){
            return 180;
        }else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270){
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap bitmap, float degree){
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
