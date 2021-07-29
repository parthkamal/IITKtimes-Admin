package com.parth.iitktimes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parth.iitktimes.Creator;
import com.parth.iitktimes.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ReclyclerViewAdapter extends RecyclerView.Adapter<ReclyclerViewAdapter.ViewHolder> {
    private ArrayList<Creator> creatorArrayList;
    private Context context;

    public ReclyclerViewAdapter(ArrayList<Creator> creatorArrayList, Context context) {
        this.creatorArrayList = creatorArrayList;
        this.context = context;
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
    public void onBindViewHolder(@NonNull @NotNull ReclyclerViewAdapter.ViewHolder holder, int position) {
            //binding the values from the array list to the view holder
        holder.getTvName().setText(creatorArrayList.get(position).getName());
        holder.getTvEmail().setText(creatorArrayList.get(position).getEmail());
        holder.getTvPost().setText(creatorArrayList.get(position).getDesign());
        holder.getTvPhone().setText(creatorArrayList.get(position).getPhone());

    }

    @Override
    public int getItemCount() {
        return creatorArrayList.size();
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName,tvPost,tvEmail,tvPhone;

        public ViewHolder(View view) {
            super(view);
            // attaching the child views of the view holder
            this.tvName = view.findViewById(R.id.tvCreatorsName);
            this.tvPost = view.findViewById(R.id.tvCreatorPost);
            this.tvEmail = view.findViewById(R.id.tvCreatorEmail);
            this.tvPhone = view.findViewById(R.id.tvCreatorConatcts);
            // Define click listener for the ViewHolder's View

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
