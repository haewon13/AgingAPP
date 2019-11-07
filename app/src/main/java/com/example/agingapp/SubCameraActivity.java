package com.example.agingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class SubCameraActivity extends AppCompatActivity {

    private CustomAnimationDialog customAnimationDialog;

    private String imageFilePath;
    private Uri photoUri;

    ImageView bresult;
    ImageView bback;
    ImageView cap_result;
    ProgressDialog dialog = null;

    String imageFileName=null;
    DataOutputStream dos;

    int serverResponse;
    int bufferSize;
    int maxBufferSize=1*1024*1024;

    String uploadFilePath=null;
    String uploadFileName=null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_subcamera);

        Intent intent = getIntent();

        cap_result=(ImageView)findViewById(R.id.cap_result);
        //byte[] byteArray=intent.getByteArrayExtra("image");
        //Bitmap bitmap= BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);

        imageFilePath=intent.getStringExtra("image");
        imageFileName=intent.getStringExtra("name");
        File imgFile=new File(imageFilePath);

        if(imgFile.exists()){
            Bitmap bitmap=BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            ExifInterface exif = null;

            try{
                exif = new ExifInterface(imageFilePath);
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

            cap_result.setImageBitmap(bitmap);
        }



        bback = (ImageView)findViewById(R.id.back);
        bresult = (ImageView)findViewById(R.id.result);
        bresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dialog = ProgressDialog.show(SubCameraActivity.this, "", "Uploading file...", true);

                new Thread(new Runnable() {
                    public void run() {
                        runOnUiThread(new Runnable() {

                            public void run() {
                                customAnimationDialog = new CustomAnimationDialog(SubCameraActivity.this);
                                customAnimationDialog.show();
                            }
                        });
                        doFileUpload();
                    }
                }).start();

            }
        });

        bback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(SubCameraActivity.this, CameraActivity.class);
                //startActivity(intent);
                finish();
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



    private void doFileUpload(){

        //uploadFilePath : /storage/emulated/0/Android/data/com.example.agingapp/files/Pictures/TEST_20191104_114302_7951340829363164521.jpg
        //uploadFileName : TEST_20191104_114302_.jpg

        uploadFilePath=imageFilePath;
        uploadFileName=imageFileName+".jpg";

        Log.i("태그:","기능 1 FileInputStream:"+uploadFilePath);

        String sourceFileUri=uploadFilePath;
        File sourceFile = new File(sourceFileUri);

        if(!sourceFile.isFile()){
            Log.e("태그:", "Source File not exist :" +sourceFileUri);
            return;
        }
        else{
            Log.i("태그:","Source File exists.");
            try{
                URL url = new URL("http://203.255.176.79:13000/updown.php");
                Log.i("태그:","http://203.255.176.79:13000/repos_camera/");

                FileInputStream mFileInputStream = new FileInputStream(uploadFilePath);
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";

                // open connection
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setDoInput(true); //input 허용
                con.setDoOutput(true);  // output 허용
                con.setUseCaches(false);   // cache copy를 허용하지 않는다.
                con.setRequestMethod("POST");
                con.setRequestProperty("Connection", "Keep-Alive");
                con.setRequestProperty("ENCTYPE","multipart/form-data");
                con.setRequestProperty("Connection", "Keep-Alive");
                con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                con.setRequestProperty("fileToUpload",sourceFileUri);

                // write data
                dos = new DataOutputStream(con.getOutputStream());
                //Log.i("태그:", "Open OutputStream" );
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                // 파일 전송시 파라메터명은 fileToUpload 파일명은 uploadFileName 설정하여 전송
                dos.writeBytes("Content-Disposition: form-data; name=\"fileToUpload\";filename=\""+uploadFileName+"\"" + lineEnd);
                dos.writeBytes(lineEnd);

                int bytesAvailable = mFileInputStream.available();
                bufferSize=Math.min(bytesAvailable,maxBufferSize);
                byte[] buffer=new byte[bufferSize];
                int bytesRead = mFileInputStream.read(buffer,0,bufferSize);
                while(bytesRead>0){
                    dos.write(buffer,0,bufferSize);
                    bytesAvailable = mFileInputStream.available();
                    bufferSize=Math.min(bytesAvailable,maxBufferSize);
                    bytesRead=mFileInputStream.read(buffer,0,bufferSize);
                    dos.flush();
                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponse=con.getResponseCode();
                String serverMessage=con.getResponseMessage();

                Log.i("태그:", "HTTP Response is : " + serverMessage + ": " + serverResponse);

                if(serverResponse == HttpURLConnection.HTTP_OK){
                    runOnUiThread(new Runnable() {

                        public void run() {

                            Intent intent = new Intent(SubCameraActivity.this, Result1Activity.class);
                            intent.putExtra("name2",uploadFileName);
                            startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
                            customAnimationDialog.dismiss();
                            finish();


                        }
                    });


                }
                dos.flush(); // finish upload...
                dos.close();

            }catch(MalformedURLException e) {
                Log.i("태그:", "exception " + e.getMessage());
                // TODO: handle exception
                e.printStackTrace();
                Toast.makeText(SubCameraActivity.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                Log.e("Upload file to server", "error: " + e.getMessage(), e);

            }catch(Exception e2){
                e2.printStackTrace();
                Toast.makeText(SubCameraActivity.this, "Got Exception See logcat", Toast.LENGTH_SHORT).show();
                Log.e("Upload server Exception", "Exception : "+ e2.getMessage(), e2);

            }

        }

    }
}
