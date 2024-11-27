package com.example.cameraimageapp_2;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

   private static final int PERMISSION_CODE = 100;
   private static final int IMAGE_CAPTURE_CODE = 110;

   Uri image_Uri;

//    Declaring .xml variable
    Button btCapture;
    ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        finding id's of .xml tags id
        btCapture = findViewById(R.id.btCapture);
        imgView = findViewById(R.id.imgView);

//        event listener for btCapture Button
        btCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if the os version is less than and equal to Marshmallow
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

//                    and permission is not granted, then ask for the permission
                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
//                        permission is not granted, request it
                        String [] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
//                       show popup to request permission
                        requestPermissions(permission,PERMISSION_CODE);
                    }else {
//                        permission already granted and calling openCamera(); method
                        openCamera();
                    }
                }else {
//                    system os > Marshmallow and calling openCamera(); method
                    openCamera();
                }
            }
        });
    }

    private void openCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE,"new picture");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION,"From the Camera");
        image_Uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);

//        Camera Intent
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,image_Uri);
        startActivityForResult(intent,IMAGE_CAPTURE_CODE);
    }

//    handling permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        this method is called, when user presses Allow or Deny from the Permission Required popup

        switch (requestCode){
            case PERMISSION_CODE:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    permission from permission popup granted

//                     calling openCamera(); method
                    openCamera();
                }else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        called when, image was captured form camera

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
//            set the image captured to our imageView
            imgView.setImageURI(image_Uri);
        }
    }
}