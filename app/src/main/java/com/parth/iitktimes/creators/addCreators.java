package com.parth.iitktimes.creators;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parth.iitktimes.Creator;
import  com.parth.iitktimes.adapters.ReclyclerViewAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.parth.iitktimes.R;

import java.util.ArrayList;

public class addCreators extends AppCompatActivity {
    //defining variables for recycler view and adapters
    private RecyclerView recyclerView;
    private ReclyclerViewAdapter reclyclerViewAdapter;
    private ArrayList<Creator> CreatorArrayList;
    private FloatingActionButton addMembers;

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
        //filling the array list
        CreatorArrayList = new ArrayList<>();
        CreatorArrayList.add(new Creator("Parth","founder","kamalparth40@gmail.com","8957090459"));
        reclyclerViewAdapter = new ReclyclerViewAdapter(CreatorArrayList,this);
        recyclerView.setAdapter(reclyclerViewAdapter);
    }
}