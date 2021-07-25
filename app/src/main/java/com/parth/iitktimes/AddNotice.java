package com.parth.iitktimes;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.card.MaterialCardView;

import java.io.IOException;

public class AddNotice extends AppCompatActivity {
    static final int REQUEST_IMAGE_GET = 1;
    private ActivityResultLauncher<String> someActivityResultLauncher;

    private MaterialCardView addImage;
    private ImageView previewImage;
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notice);
        addImage =findViewById(R.id.select_image);
        previewImage =findViewById(R.id.preview_image);

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        //activity result launcher
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),result);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        previewImage.setImageBitmap(bitmap);
                    }
                });



    }

    public void selectImage() {
        someActivityResultLauncher.launch("image/*");
    }
}