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

                File imgFile = new File(filePath);
                Log.i("태그:","Touched");

                KakaoLinkService.getInstance().uploadImage(Result1Activity.this, false, imgFile, new ResponseCallback<ImageUploadResponse>() {
                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        Logger.e(errorResult.toString());
                        Log.i("태그:","카카오 에러뜸");
                    }

                    @Override
                    public void onSuccess(ImageUploadResponse result) {
                        Logger.d(result.getOriginal().getUrl());
                        Log.i("태그:","카카오 되긴 됨");

                    }
                });

                //String key = getKeyHash(Result1Activity.this);


                /*
                Map<String, String> serverCallbackArgs = new HashMap<String, String>();
                serverCallbackArgs.put("user_id", "${current_user_id}");
                serverCallbackArgs.put("product_id", "${shared_product_id}");
                */

                /*
                FeedTemplate params = FeedTemplate.newBuilder(ContentObject.newBuilder("아이사진관에서 변환된 사진",filePath, LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
                        .setMobileWebUrl("https://developers.kakao.com").build()).setDescrption("아이사진관에서 생성된 사진")
                        .build()).build();

                KakaoLinkService.getInstance().sendDefault(Result1Activity.this, params, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        Logger.e(errorResult.toString());
                    }

                    @Override
                    public void onSuccess(KakaoLinkResponse result) {
                        // 템플릿 밸리데이션과 쿼터 체크가 성공적으로 끝남. 톡에서 정상적으로 보내졌는지 보장은 할 수 없다. 전송 성공 유무는 서버콜백 기능을 이용하여야 한다.
                    }
                });*/



                /*
                KakaoLinkService.getInstance().scrapImage(Result1Activity.this, false, "http://www.kakaocorp.com/images/logo/og_daumkakao_151001.png", new ResponseCallback<ImageUploadResponse>() {
                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        Logger.e(errorResult.toString());
                    }

                    @Override
                    public void onSuccess(ImageUploadResponse result) {
                        Logger.d(result.getOriginal().getUrl());
                    }
                });*/

                //shareKakao(filePath);

            }
        });


        bsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

    /*
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
    */
                /*
    public void shareKakao(String url){
        try{
            final KakaoLink kakaoLink =KakaoLink.getKakaoLink(Result1Activity.this);
            final KakaoTalkLinkMessageBuilder kakaoBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();

            //메세지
            kakaoBuilder.addText("아이사진관에서 생성된 이미지입니다.");
            //이미지
            kakaoBuilder.addImage(url,160,160);

            //메세지 발송
            kakaoLink.sendMessage(kakaoBuilder,Result1Activity.this);

        }catch (Exception e){
            e.printStackTrace();
        }
    }*/

    public Uri getUriFromPath(String path){

        Uri fileUri = Uri.parse(path);
        String filePath = fileUri.getPath();
        Cursor cursor = getContentResolver().query( MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, "_data = '" + filePath + "'", null, null );
        cursor.moveToNext();
        int id = cursor.getInt( cursor.getColumnIndex( "_id" ) );
        Uri uri = ContentUris.withAppendedId( MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id );


        return uri;

    }

}


