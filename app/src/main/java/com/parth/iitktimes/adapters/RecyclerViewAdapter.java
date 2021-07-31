package com.parth.iitktimes.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.parth.iitktimes.updataDelete;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.parth.iitktimes.Creator;
import com.parth.iitktimes.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends FirebaseRecyclerAdapter<Creator, RecyclerViewAdapter.ViewHolder> {
    private Context context;

    public RecyclerViewAdapter(
            @NonNull FirebaseRecyclerOptions<Creator> options) {
        super(options);
    }

    @NonNull
    @NotNull


    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.creators_items, parent, false);

        return new ViewHolder(view);


    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull RecyclerViewAdapter.ViewHolder holder, int position, @NonNull @NotNull Creator model) {
        //binding data to the view holder
        //using picasso library for fetching image from the url
        DatabaseReference getImageReference = FirebaseDatabase.getInstance().getReference().child("Creators").child(model.getUniquekey()).child("downloadUrl");
        holder.getTvName().setText(model.getName());
        holder.getTvEmail().setText(model.getEmail());
        holder.getTvPost().setText(model.getDesign());
        holder.getTvPhone().setText(model.getPhone());
        getImageReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                String link = dataSnapshot.getValue(String.class);
                Picasso.get().load(link).into(holder.getImageView());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(context,"error loading image",Toast.LENGTH_SHORT).show();
            }
        });

        //setting click listener on the card view
        holder.getMaterialCardView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(holder.getMaterialCardView().getContext(),Integer.toString(position),Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(holder.getMaterialCardView().getContext(),updataDelete.class);
                intent.putExtra("model object",model);
                holder.getMaterialCardView().getContext().startActivity(intent);
            }
        });
    }



    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvPost, tvEmail, tvPhone;
        private MaterialCardView materialCardView;
        private CircleImageView imageView;

        public ViewHolder(View view) {
            super(view);
            // attaching the child views of the view holder
            this.imageView = view.findViewById(R.id.profile_image);
            this.tvName = view.findViewById(R.id.tvCreatorsName);
            this.tvPost = view.findViewById(R.id.tvCreatorPost);
            this.tvEmail = view.findViewById(R.id.tvCreatorEmail);
            this.tvPhone = view.findViewById(R.id.tvCreatorConatcts);
            this.materialCardView = view.findViewById(R.id.itemCard);

        }

        public MaterialCardView getMaterialCardView() {
            return materialCardView;
        }

        public void setMaterialCardView(MaterialCardView materialCardView) {
            this.materialCardView = materialCardView;
        }

        public CircleImageView getImageView() {
            return imageView;
        }

        public void setImageView(CircleImageView imageView) {
            this.imageView = imageView;
        }

        public TextView getTvName() {
            return tvName;
        }

        public TextView getTvPost() {
            return tvPost;
        }

        public TextView getTvEmail() {
            return tvEmail;
        }

        public TextView getTvPhone() {
            return tvPhone;
        }
    }
}
