package com.parth.iitktimes.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.parth.iitktimes.AddNotice;
import com.parth.iitktimes.NoticeData;
import com.parth.iitktimes.R;
import com.parth.iitktimes.updataDelete;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;

public class NoticeAdapter extends FirebaseRecyclerAdapter<NoticeData, NoticeAdapter.ViewHolder> {
    private Context context;


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public NoticeAdapter(@NonNull @NotNull FirebaseRecyclerOptions<NoticeData> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull NoticeAdapter.ViewHolder holder, int position, @NonNull @NotNull NoticeData model) {
        //binding data to the view holder
        //using picasso library for fetching image from the url
        DatabaseReference getImageReference = FirebaseDatabase.getInstance().getReference().child("Notice").child(model.getUniqueKey()).child("downloadUrl");
        holder.getNoticeTitle().setText(model.getNoticeTitle());
        holder.getNoticeDesc().setText(model.getNoticeDescription());
        getImageReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                String link = dataSnapshot.getValue(String.class);
                Picasso.get().load(link).into(holder.getImageView());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.getMaterialCardView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(context);
                alertDialog2.setTitle("Confirm Delete...");
                alertDialog2.setMessage("Are you sure you want delete this file?");
                alertDialog2.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Notice").child(model.getUniqueKey());
                                databaseReference.removeValue();
                                Toast.makeText(context, "notice Deleted Successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                alertDialog2.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                Toast.makeText(context, "You clicked on NO", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        });
                alertDialog2.show();
                return true;

            }
        });

//        //setting click listener on the card view
//        holder.getMaterialCardView().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(holder.getMaterialCardView().getContext(),Integer.toString(position),Toast.LENGTH_SHORT).show();
//                Intent intent= new Intent(holder.getMaterialCardView().getContext(), updataDelete.class);
//                intent.putExtra("model object",model);
//                holder.getMaterialCardView().getContext().startActivity(intent);
//            }
//        });
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_item, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private MaterialCardView materialCardView;
        private ImageView imageView;
        private TextView noticeTitle, noticeDesc;

        public ViewHolder(View view) {
            super(view);
            // attaching the child views of the view holder
            this.materialCardView = view.findViewById(R.id.item_card);
            this.imageView = view.findViewById(R.id.notice_image);
            this.noticeTitle = view.findViewById(R.id.notice_title);
            this.noticeDesc = view.findViewById(R.id.notice_desc);

        }

        public MaterialCardView getMaterialCardView() {
            return materialCardView;
        }

        public void setMaterialCardView(MaterialCardView materialCardView) {
            this.materialCardView = materialCardView;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }

        public TextView getNoticeTitle() {
            return noticeTitle;
        }

        public void setNoticeTitle(TextView noticeTitle) {
            this.noticeTitle = noticeTitle;
        }

        public TextView getNoticeDesc() {
            return noticeDesc;
        }

        public void setNoticeDesc(TextView noticeDesc) {
            this.noticeDesc = noticeDesc;
        }
    }
}
