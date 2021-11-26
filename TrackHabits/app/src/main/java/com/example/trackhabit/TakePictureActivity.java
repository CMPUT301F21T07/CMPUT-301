package com.example.trackhabit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;

public class TakePictureActivity extends AppCompatActivity {
    Button takePictureButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);
        takePictureButton = findViewById(R.id.take_picture_button);
        takePictureButton.setOnClickListener(view -> camera());
    }

    private void camera() {
        if (ContextCompat.checkSelfPermission(TakePictureActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(TakePictureActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }
        boolean b = ContextCompat.checkSelfPermission(TakePictureActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED;
        System.out.println(b);
        if (ContextCompat.checkSelfPermission(TakePictureActivity.this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            setResult(100, data);
        }
    }

}