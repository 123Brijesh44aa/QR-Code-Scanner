package com.example.qrcodescanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static int SELECT_PICTURE = 200;
    private TextView tvProfile;
    private ImageButton cameraBtn, galleryBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvProfile = findViewById(R.id.profile);
        cameraBtn = findViewById(R.id.openCamera);
        galleryBtn = findViewById(R.id.openGallery);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);

        tvProfile.setOnClickListener(view -> {
            Toast.makeText(MainActivity.this,"profile clicked", Toast.LENGTH_LONG).show();
        });


        galleryBtn.setOnClickListener(view -> {
            imageChooser();
        });

        cameraBtn.setOnClickListener(view -> {
            IntentIntegrator intentIntegrator = new IntentIntegrator(this);
            intentIntegrator.initiateScan();
        });

    }

    private void imageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select QR code"),SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        if (intentResult != null){
            if (intentResult.getContents() == null){
                Toast.makeText(MainActivity.this,"Cancelled due to error",Toast.LENGTH_LONG).show();
            }
            else {
                Intent intent = new Intent(MainActivity.this,ScannerActivity.class);
                intent.putExtra("RES",intentResult.getContents());
                startActivity(intent);
            }
        }


        if (resultCode==RESULT_OK){
            if (requestCode==SELECT_PICTURE){
                assert data != null;
                Uri imageUri = data.getData();
                if (imageUri != null) {
                    try {
                        String imagePath = (imageUri.getEncodedPath());
                        // send image to another activity
                        Intent intent = new Intent(MainActivity.this, GalleryImageQRScanner.class);
                        intent.putExtra("QRCODE", imageUri);
                        intent.putExtra("PATH",imagePath);
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this,"file size is too large",Toast.LENGTH_LONG).show();
                    }
                }
            }
        }



    }


}