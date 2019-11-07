package com.example.agingapp;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class CompareActivity extends AppCompatActivity {

    ImageView bhome;
    ImageView bresult;

    ImageView binclude1;
    ImageView binclude2;

    Uri ImageUri1;
    Uri ImageUri2;

    private static final int PICK_IMAGE = 100;
    private static final int PICK_IMAGE2 = 120;

    private CustomAnimationDialog customAnimationDialog2;

    String imageFileName1=null;
    String imageFileName2=null;
    DataOutputStream dos;

    int serverResponse=0;
    int bufferSize;
    int maxBufferSize=1*1024*1024;

    String uploadFilePath1=null;
    String uploadFilePath2=null;

    String uploadFileName1=null;
    String uploadFileName2=null;

    private String imageFilePath1;
    private String imageFilePath2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        bhome = (ImageView) findViewById(R.id.home);
        bresult = (ImageView) findViewById(R.id.result);
        binclude1 = (ImageView)findViewById(R.id.young_before);
        binclude2 = (ImageView)findViewById(R.id.now_before);

        Spinner youngSpinner=(Spinner) findViewById(R.id.spinner_young);
        ArrayAdapter youngAdapter = ArrayAdapter.createFromResource(this, R.array.young, android.R.layout.simple_spinner_dropdown_item);
        youngAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        youngSpinner.setAdapter(youngAdapter);

        Spinner nowSpinner=(Spinner)findViewById(R.id.spinner_now);
        ArrayAdapter nowAdapter=ArrayAdapter.createFromResource(this,R.array.now,android.R.layout.simple_spinner_dropdown_item);
        nowAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nowSpinner.setAdapter(nowAdapter);


        binclude1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TedPermission.with(getApplicationContext())
                        .setPermissionListener(permissionListener)
                        .setRationaleMessage("앨범 접근 권한이 필요합니다.")
                        .setDeniedMessage("앨범 접근 권한을 거부하셨습니다.")
                        .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .check();

                openGallery(1);
            }
        });

        binclude2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TedPermission.with(getApplicationContext())
                        .setPermissionListener(permissionListener)
                        .setRationaleMessage("앨범 접근 권한이 필요합니다.")
                        .setDeniedMessage("앨범 접근 권한을 거부하셨습니다.")
                        .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .check();

                openGallery(2);
            }
        });

        bhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        bresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Thread(new Runnable() {
                    public void run() {
                        runOnUiThread(new Runnable() {

                            public void run() {

                                customAnimationDialog2 = new CustomAnimationDialog(CompareActivity.this);
                                customAnimationDialog2.show();
                            }
                        });
                        doFileUpload();
                    }
                }).start();
            }
        });
    }

    private void doFileUpload(){

        uploadFilePath1=imageFilePath1;
        uploadFileName1=imageFileName1+".jpg";

        uploadFilePath2=imageFilePath2;
        uploadFileName2=imageFileName2+".jpg";

        File sourceFile1 = new File(uploadFilePath1);


        if(!sourceFile1.isFile()){
            Log.e("태그:", "Source File not exist :" +sourceFile1);
            return;
        }
        else{
            Log.i("태그:","Source File exists.");
            try{
                URL url = new URL("http://203.255.176.79:13000/updown.php");
                Log.i("태그:",uploadFileName1);
                Log.i("태그:","http://203.255.176.79:13000/repos_camera/"+uploadFileName1);

                FileInputStream mFileInputStream = new FileInputStream(uploadFilePath1);
                Log.i("태그:","기능 2 FileInputStream:"+uploadFilePath1);
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
                con.setRequestProperty("fileToUpload",uploadFilePath1);

                // write data
                dos = new DataOutputStream(con.getOutputStream());
                Log.i("태그:", "Open OutputStream" );
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                // 파일 전송시 파라메터명은 fileToUpload 파일명은 uploadFileName 설정하여 전송
                dos.writeBytes("Content-Disposition: form-data; name=\"fileToUpload\";filename=\""+uploadFileName1+"\"" + lineEnd);
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
                if(serverResponse==0) Log.i("태그:","No HTTP return");
                String serverMessage=con.getResponseMessage();


                Log.i("태그:", "HTTP Response is : " + serverMessage + ": " + serverResponse);

                if(serverResponse == HttpURLConnection.HTTP_OK){
                    runOnUiThread(new Runnable() {

                        public void run() {

                            Intent intent = new Intent(CompareActivity.this, Result2Activity.class);

                            intent.putExtra("name3_1",uploadFileName1);
                            intent.putExtra("name3_2",uploadFileName2);
                            intent.putExtra("image3",uploadFilePath2);
                            startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
                            customAnimationDialog2.dismiss();
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
                Toast.makeText(CompareActivity.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                Log.e("Upload file to server", "error: " + e.getMessage(), e);

            }catch(Exception e2){
                e2.printStackTrace();
                Toast.makeText(CompareActivity.this, "Got Exception See logcat", Toast.LENGTH_SHORT).show();
                Log.e("Upload server Exception", "Exception : "+ e2.getMessage(), e2);

            }

        }

    }



/*
    private void doFileUpload(){

        //uploadFilePath : /storage/emulated/0/Android/data/com.example.agingapp/files/Pictures/TEST_20191104_114302_7951340829363164521.jpg
        //uploadFileName : TEST_20191104_114302_.jpg

        uploadFilePath1=imageFilePath1;
        uploadFileName1=imageFileName1+".jpg";

        uploadFilePath2=imageFilePath2;
        uploadFileName2=imageFileName2+".jpg";

        Log.i("태그:",uploadFileName1);
        Log.i("태그:",uploadFileName2);

        String sourceFileUri1=uploadFilePath1;
        String sourceFileUri2=uploadFilePath2;

        File sourceFile1 = new File(sourceFileUri1);
        File sourceFile2=new File(sourceFileUri2);
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        if(!sourceFile1.isFile() || !sourceFile2.isFile()){
            Log.e("태그:", "Source File not exist :" +sourceFileUri1);
            return;
        }
        else{
            Log.i("태그:","Source File exists.");
            try{
                URL url = new URL("http://203.255.176.79:13000/updownalbum.php");
                Log.i("태그:","http://203.255.176.79:13000/repos_album/");

                // open connection
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setDoInput(true); //input 허용
                con.setDoOutput(true);  // output 허용
                con.setUseCaches(false);   // cache copy를 허용하지 않는다.
                con.setRequestMethod("POST");
                con.setRequestProperty("Connection", "Keep-Alive");
                con.setRequestProperty("ENCTYPE","multipart/form-data");
                //con.setRequestProperty("Connection", "Keep-Alive");
                con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                con.setRequestProperty("fileToUpload",uploadFilePath1);
                con.setRequestProperty("fileToUpload2",uploadFilePath2);
                //con.connect();

                FileInputStream mFileInputStream = new FileInputStream(uploadFilePath1);
                // write data
                dos = new DataOutputStream(con.getOutputStream());
                Log.i("태그:", "Open OutputStream" );

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                // 파일 전송시 파라메터명은 fileToUpload 파일명은 imageFileName로 설정하여 전송
                dos.writeBytes("Content-Disposition: form-data; name=\"fileToUpload\";filename1=\""+uploadFileName1+"\"" + lineEnd);
                dos.writeBytes("Content-Type: application/octet-stream" + lineEnd);
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
                    //dos.flush();
                }
                mFileInputStream.close();


                //두번째 image
                dos = new DataOutputStream(con.getOutputStream());
                FileInputStream mFileInputStream2 = new FileInputStream(uploadFilePath2);

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"fileToUpload2\";filename2=\""+uploadFileName2 +"\"" + lineEnd);
                dos.writeBytes("Content-Type: application/octet-stream" + lineEnd);
                dos.writeBytes(lineEnd);

                int bytesAvailable2 = mFileInputStream.available();
                bufferSize=Math.min(bytesAvailable,maxBufferSize);
                byte[] buffer2=new byte[bufferSize];
                int bytesRead2= mFileInputStream.read(buffer2,0,bufferSize);
                while(bytesRead2>0){
                    dos.write(buffer2,0,bufferSize);
                    bytesAvailable2 = mFileInputStream2.available();
                    bufferSize=Math.min(bytesAvailable2,maxBufferSize);
                    bytesRead2=mFileInputStream2.read(buffer2,0,bufferSize);
                    //dos.flush();
                }
                mFileInputStream2.close();


                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                dos.flush();

                serverResponse=con.getResponseCode();
                String serverMessage=con.getResponseMessage();

                Log.i("태그:", "HTTP Response is : " + serverMessage + ": " + serverResponse);

                if(serverResponse == HttpURLConnection.HTTP_OK){
                    runOnUiThread(new Runnable() {

                        public void run() {

                            Intent intent = new Intent(CompareActivity.this, Result2Activity.class);
                            intent.putExtra("name2_1",uploadFileName1);
                            intent.putExtra("name2_2",uploadFileName2);
                            startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
                            customAnimationDialog2.dismiss();
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
                Toast.makeText(CompareActivity.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                Log.e("태그:", "error: " + e.getMessage(), e);

            }catch(Exception e2){
                e2.printStackTrace();
                Toast.makeText(CompareActivity.this, "Got Exception See logcat", Toast.LENGTH_SHORT).show();
                Log.e("태그:", "Exception : "+ e2.getMessage(), e2);

            }

        }

    }
*/

    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() { //허용이 되었을 때 일어나는 action
            //Toast.makeText(getApplicationContext(), "권한이 허용됨.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(getApplicationContext(), "권한이 거부됨.", Toast.LENGTH_SHORT).show();
        }
    };

    private void openGallery(int x){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        if(x==1)
            startActivityForResult(gallery, PICK_IMAGE);
        else if(x==2)
            startActivityForResult(gallery, PICK_IMAGE2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==PICK_IMAGE){
            ImageUri1 = data.getData();
            binclude1.setImageURI(ImageUri1);

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            imageFileName1="Second_" + timeStamp + "_";
            imageFilePath1=getPathFromUri(ImageUri1);
        }

        else if(resultCode==RESULT_OK && requestCode==PICK_IMAGE2){
            ImageUri2 = data.getData();
            binclude2.setImageURI(ImageUri2);

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            imageFileName2="Second_" + timeStamp + "_";
            imageFilePath2=getPathFromUri(ImageUri2);
        }
    }

    public String getPathFromUri(Uri uri){

        Cursor cursor = getContentResolver().query(uri, null, null, null, null );
        cursor.moveToNext();
        String path = cursor.getString( cursor.getColumnIndex( "_data" ) );
        cursor.close();

        return path;

    }

}


