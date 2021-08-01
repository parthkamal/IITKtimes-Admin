package com.parth.iitktimes.creators;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.parth.iitktimes.Creator;
import  com.parth.iitktimes.adapters.RecyclerViewAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.parth.iitktimes.R;
import com.parth.iitktimes.adapters.RecyclerViewAdapter;

import java.util.ArrayList;

public class addCreators extends AppCompatActivity {
    //defining variables for recycler view and adapters
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    private FloatingActionButton addMembers;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_creators);
        //initialization
        recyclerView = findViewById(R.id.creatorRecyclerView);
        recyclerView.setHasFixedSize(true);
        addMembers =findViewById(R.id.addCreators);
        addMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(addCreators.this,updateCreators.class));
            }
        });
        //declaring the type of the layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //getting the reference of the realtime database where our data is stored
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Creators");

        // It is a class provide by the FirebaseUI to make a
        // query in the database to fetch appropriate data
        FirebaseRecyclerOptions<Creator> options
                = new FirebaseRecyclerOptions.Builder<Creator>()
                .setQuery(databaseReference, Creator.class)
                .build();

        recyclerViewAdapter = new RecyclerViewAdapter(options);
        recyclerView.setAdapter(recyclerViewAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerViewAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        recyclerViewAdapter.stopListening();
    }
}