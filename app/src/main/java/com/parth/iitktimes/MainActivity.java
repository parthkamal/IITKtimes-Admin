package com.parth.iitktimes;

import androidx.appcompat.app.AppCompatActivity;
import com.parth.iitktimes.creators.addCreators;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {
    private MaterialCardView AddNotice;
    private MaterialCardView AddImages,AddCreator;
    private MaterialCardView addBooks,deleteNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AddNotice = findViewById(R.id.add_notice);
        AddImages = findViewById(R.id.add_images);
        AddCreator = findViewById(R.id.update_members);
        addBooks = findViewById(R.id.add_notes);
        deleteNotice = findViewById(R.id.delete_notice);

        deleteNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Notice.class));
            }
        });

        //on click listener for adding members
        AddCreator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,addCreators.class));
            }
        });

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

        addBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(MainActivity.this,addBooks.class));
            }
        });
    }
}