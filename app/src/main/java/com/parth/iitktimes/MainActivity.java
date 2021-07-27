package com.parth.iitktimes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {
    private MaterialCardView AddNotice;
    private MaterialCardView AddImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AddNotice = findViewById(R.id.add_notice);
        AddImages = findViewById(R.id.add_images);

        AddImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigating to the upload images activity through an explicit intent
                startActivity(new Intent(MainActivity.this, com.parth.iitktimes.AddImages.class));
            }
        });

        AddNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, com.parth.iitktimes.AddNotice.class));

            }
        });
    }
}