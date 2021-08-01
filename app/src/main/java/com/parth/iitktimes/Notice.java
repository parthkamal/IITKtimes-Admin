package com.parth.iitktimes;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.parth.iitktimes.adapters.NoticeAdapter;

public class Notice extends AppCompatActivity {
    private NoticeAdapter noticeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        RecyclerView noticeRecyclerView = findViewById(R.id.noticeRecView);
        noticeRecyclerView = findViewById(R.id.noticeRecView);
        noticeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        noticeRecyclerView.setHasFixedSize(false);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Notice");
        FirebaseRecyclerOptions<NoticeData> options = new FirebaseRecyclerOptions.Builder<NoticeData>()
                .setQuery(databaseReference,NoticeData.class).build();
        noticeAdapter = new NoticeAdapter(options);
        noticeRecyclerView.setAdapter(noticeAdapter);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        noticeAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        noticeAdapter.stopListening();
    }
}