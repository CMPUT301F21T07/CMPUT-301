package com.example.trackhabit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.Toast;

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
            System.out.println("what about this one");

            String[] permission = {Manifest.permission.CAMERA};
            ActivityCompat.requestPermissions(TakePictureActivity.this, permission, 101);
        }
        boolean b = ContextCompat.checkSelfPermission(TakePictureActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED;
        System.out.println(b);
        if (ContextCompat.checkSelfPermission(TakePictureActivity.this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            System.out.println("gets here");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 102);
        } else {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("data", "permission denied");
            setResult(101, resultIntent);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 101) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Showing the toast message
                Toast.makeText(TakePictureActivity.this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(TakePictureActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102) {
            setResult(102, data);
            finish();
        }
    }

}