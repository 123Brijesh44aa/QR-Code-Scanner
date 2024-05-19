package com.example.qrcodescanner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

public class GalleryImageQRScanner extends AppCompatActivity {

    private ImageView imageView;
    private Button scanQr;
    private TextView imagePath;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_image_qrscanner);

        imageView = findViewById(R.id.imageView);
        scanQr = findViewById(R.id.scan_this_qr_code);
        imagePath = findViewById(R.id.path);

        Intent intent = getIntent();
        Uri imageUri = (Uri) intent.getExtras().get("QRCODE");
        try {
            imageView.setImageURI(imageUri);
        }
        catch (Exception e){
            Toast.makeText(GalleryImageQRScanner.this,"file size is too large",Toast.LENGTH_LONG).show();
        }

        scanQr.setOnClickListener(view -> {
            Bitmap bitmap = convertBitmap(GalleryImageQRScanner.this,imageUri);
            Log.e("TAG", "onCreate: "+bitmap.getHeight());
//            Toast.makeText(this,bitmap.getHeight(),Toast.LENGTH_LONG).show();
            String decodedText = null;
            int[] intArray = new int[bitmap.getWidth()*bitmap.getHeight()];
            bitmap.getPixels(intArray,0,bitmap.getWidth(),0,0,bitmap.getWidth(),bitmap.getHeight());
            LuminanceSource source = new RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(),intArray);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
            Reader reader = new MultiFormatReader();
            Result result = null;
            try {
                result = reader.decode(binaryBitmap);
            } catch (NotFoundException | ChecksumException | FormatException e) {
                e.printStackTrace();
                Toast.makeText(this,"Not a QR code",Toast.LENGTH_LONG).show();
            }

            if (result != null) {
                decodedText = result.getText();
            }
            else {
                Log.e("TAG", "onCreate: not a qr code");
            }
            // show the decoded text of qr code in android
            if (decodedText!=null) {
                imagePath.setVisibility(View.VISIBLE);
                imagePath.setText(decodedText);
//                Toast.makeText(GalleryImageQRScanner.this, decodedText, Toast.LENGTH_LONG).show();
                Log.e("TAG", "onCreate: " + decodedText);
            }
            else {
                Log.e("TAG", "onCreate: not a qr code");
            }
        });

        imagePath.setOnClickListener(view -> {
            Intent intent1 = new Intent(Intent.ACTION_VIEW);
            intent1.setData(Uri.parse(imagePath.getText().toString().trim()));
            startActivity(intent1);
        });
    }

    // Convert Image Uri into Bitmap
    private Bitmap convertBitmap(Context context, Uri uri){
        Bitmap bitmap = null;
        try {
            InputStream inputStream;
            try {
                inputStream = context.getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}