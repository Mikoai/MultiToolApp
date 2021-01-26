package com.example.multiappvjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageCapture;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Presentation;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;

public class MediaCenter extends AppCompatActivity {

//    private Camera camera;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_media_center);
//
//        SurfaceView view = new SurfaceView(this);
//        camera = Camera.open();
//        try {
//            camera.setPreviewDisplay(view.getHolder()); // feed dummy surface to surface
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        camera.startPreview();
//        startTimer(view);
//        Toast.makeText(this, "DUpaa", Toast.LENGTH_SHORT).show();
//
//    }
//    Camera.PictureCallback jpegCallBack=new Camera.PictureCallback() {
//        public void onPictureTaken(byte[] data, Camera camera) {
//            // set file destination and file name
//            File destination=new File(Environment.getExternalStorageDirectory(),"ddddddss.jpg");
//            try {
//                Bitmap userImage = BitmapFactory.decodeByteArray(data, 0, data.length);
//                // set file out stream
//                FileOutputStream out = new FileOutputStream(destination);
//                // set compress format quality and stream
//                userImage.compress(Bitmap.CompressFormat.JPEG, 90, out);
//
//            } catch (FileNotFoundException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//        }
//    };
//    public void startTimer(View v){
//
//        new CountDownTimer(5000, 1000) {//CountDownTimer(edittext1.getText()+edittext2.getText()) also parse it to long
//
//            public void onTick(long millisUntilFinished) {
//
//            }
//
//            public void onFinish() {
//                camera.takePicture(null, null, null, jpegCallBack);
//            }
//        }
//                .start();
//    }
//    @Override
//    protected void onPause() {
//        // TODO Auto-generated method stub
//        super.onPause();
//        camera.release();
//    }
//}

    private static final String TAG = "CameraActivity";
    static Camera cam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_center);

        Button camera = findViewById(R.id.CameraBtn);
        Button drawer = findViewById(R.id.DrawerBtn);
        Button player = findViewById(R.id.MusicBtn);
        Button web = findViewById(R.id.WebpageBtn);

        camera.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 50);
            }
            cam = Camera.open(Camera.getNumberOfCameras() - 1);
            SurfaceTexture st = new SurfaceTexture(MODE_PRIVATE);
            try {
                cam.setPreviewTexture(st);
                cam.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(MediaCenter.this, "Taking photo in 5 seconds!", Toast.LENGTH_SHORT).show();
            startTimer(v);

        });

        drawer.setOnClickListener(v -> {
            Intent intent = new Intent(this, Drawer.class);
            startActivity(intent);
        });

        web.setOnClickListener(v -> {
            Intent pageIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.wiea.uz.zgora.pl"));
            startActivity(pageIntent);
        });
    }


    private Camera.PictureCallback pic = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.v(TAG, "Getting output media file");
            File pictureFile = getOutputMediaFile();
            if (pictureFile == null) {
                Log.v(TAG, "Error creating output file");
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            camera.release();
        }
    };

    public void startTimer(View v) {
        new CountDownTimer(5000, 1) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                cam.takePicture(null, null, pic);
            }
        }.start();
    }

    private static File getOutputMediaFile() {
        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            return null;
        } else {
            File folder_gui = new File(Environment.getExternalStorageDirectory(), "Pictures");
            if (!folder_gui.exists()) {
                Log.v(TAG, "Creating folder: " + folder_gui.getAbsolutePath());
                folder_gui.mkdirs();
            }
            File outFile = new File(folder_gui, "picture.jpg");
            Log.v(TAG, "Returnng file: " + outFile.getAbsolutePath());
            return outFile;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}