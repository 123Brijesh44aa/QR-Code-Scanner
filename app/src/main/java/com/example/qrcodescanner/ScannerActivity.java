package com.example.qrcodescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ScannerActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        textView = findViewById(R.id.scannerResult);

        Intent intent = getIntent();
        String data = intent.getExtras().getString("RES");
        if (data!=null){
            textView.setVisibility(View.VISIBLE);
            textView.setText(data);
        }

        textView.setOnClickListener(view -> {
            Intent intent1 = new Intent(Intent.ACTION_VIEW);
            intent1.setData(Uri.parse(data));
            startActivity(intent1);
        });
    }
}