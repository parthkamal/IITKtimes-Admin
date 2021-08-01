package com.parth.iitktimes;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

import de.hdodenhof.circleimageview.CircleImageView;

public class updataDelete extends AppCompatActivity {
    //declaring private variables
    private MaterialCardView SelectImage;
    private EditText edCreatorName, edCreatorDesignation, edCreatorEmail, edCreatorMobile;
    private Button btnUpdateCreator,btnDeleteCreator;
    private CircleImageView previewImage;
    private Bitmap mbitmap;
    private ActivityResultLauncher<String> someActivityResultLauncher;

    //download url of the uploaded images
    private String downloadUrl = "";

    //progressDialog for updates
    private ProgressDialog progressDialog;

    private Creator obj;
    private int FLAG_CHANGE_IMAGE =0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updata_delete);

        //giving context to the progress dialog
        progressDialog = new ProgressDialog(this);

        // initialisation
        SelectImage = findViewById(R.id.select_image);
        edCreatorName = findViewById(R.id.edCreatorName);
        edCreatorDesignation = findViewById(R.id.edCreatorDesignation);
        edCreatorEmail = findViewById(R.id.edCreatorEmail);
        edCreatorMobile = findViewById(R.id.edCreatorMobile);
        btnUpdateCreator = findViewById(R.id.updateCreator);
        btnDeleteCreator = findViewById(R.id.deleteCreator);
        previewImage = findViewById(R.id.preview_image);

        progressDialog = new ProgressDialog(this);

        //accessing the object from the intent as a serializable extra
        obj = (Creator) getIntent().getSerializableExtra("model object");

        edCreatorName.setText(obj.getName());
        edCreatorDesignation.setText(obj.getDesign());
        edCreatorEmail.setText(obj.getEmail());
        edCreatorMobile.setText(obj.getPhone());

        edCreatorName.requestFocus();



        //loading the image from the preview data url to the bitmap variable and preview image
         Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                //initializing the variable and preview image source
                mbitmap = bitmap;
                previewImage.setImageBitmap(mbitmap);

            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Toast.makeText(getApplicationContext(),"couldnt load bitmap",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.get().load(obj.getDownloadUrl()).into(target);

        //click listener for upload button
        btnUpdateCreator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = edCreatorName.getText().toString();
                String Email = edCreatorEmail.getText().toString();
                String Phone = edCreatorMobile.getText().toString();
                String Post = edCreatorDesignation.getText().toString();

                if (Name.isEmpty()) {
                    edCreatorName.setError("*required");
                } else if (Email.isEmpty()) {
                    edCreatorEmail.setError("*required");
                } else if (Phone.isEmpty()) {
                    edCreatorMobile.setError("*required");
                } else if (Post.isEmpty()) {
                    edCreatorDesignation.setError("*required");
                } else if (mbitmap== null) {
                    Toast.makeText(getApplicationContext(), "Please add an image", Toast.LENGTH_SHORT).show();
                } else {
                    updateMember();
                }
            }
        });

        //activity result launcher
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        try {
                            mbitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), result);
                            //updating the flag that the image is changed
                            FLAG_CHANGE_IMAGE =1;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        previewImage.setImageBitmap(mbitmap);
                    }
                });


        //select image click listener
        SelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                someActivityResultLauncher.launch("image/*");
            }
        });

        //delete btn click event
        btnDeleteCreator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCreator();
            }
        });
    }
    private void deleteCreator(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Creators").child(obj.getUniquekey());
        databaseReference.removeValue();
        Toast.makeText(getApplicationContext(),"deleted successfully",Toast.LENGTH_SHORT).show();
    }

    private void updateMemberData(){
        String Name = edCreatorName.getText().toString();
        String Email = edCreatorEmail.getText().toString();
        String Phone = edCreatorMobile.getText().toString();
        String Post = edCreatorDesignation.getText().toString();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Creators").child(obj.getUniquekey());
        databaseReference.child("name").setValue(Name);
        databaseReference.child("design").setValue(Post);
        databaseReference.child("email").setValue(Email);
        //we dont need to change the key
        databaseReference.child("phone").setValue(Phone);

    }

    private void updateMember(){
        if(FLAG_CHANGE_IMAGE==0){
            //dont change the url and just call the data
            progressDialog.setMessage("Updating.....");
            progressDialog.show();
            updateMemberData();
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Updated members Successfully", Toast.LENGTH_SHORT).show();
        }else{
            progressDialog.setMessage("Updating.....");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();
            //creating firebase storage reference
            FirebaseStorage storage = FirebaseStorage.getInstance();
            //creating a storage reference for our app
            StorageReference storageReference = storage.getReference();
            //we are using input stream method to sent the data in packets which results in less memory
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //compressing the bitmap to bite code without changing the quality
            mbitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            //storing the bitmap in a byte array
            byte[] data = baos.toByteArray();
            downloadUrl ="";

            // creating a reference for the image
            final StorageReference filePath = storageReference.child("Creators").child("Creator Images").child(data + "jpg");
            //reference will be created if it is not STORAGE->NOTICE->IMAGES->UNIQUE_CODE.JPG
            UploadTask uploadTask = filePath.putBytes(data);
            //executing a images upload by put bytes method
            //adding success and failure listeners to the uploadTask
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //adding the success listeners for getting the uri of the uploaded path in the database
                                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        //here we have stored the download url of the uploaded image which we can use for downloading the image
                                        downloadUrl = String.valueOf(uri);
                                        //now since the task is completed we are uploading the data now;
                                        updateMemberData();
                                        progressDialog.dismiss();
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Creators").child(obj.getUniquekey());
                                        databaseReference.child("downloadUrl").setValue(downloadUrl);
                                        Toast.makeText(getApplicationContext(), "Updated members Successfully", Toast.LENGTH_SHORT).show();
                                        FLAG_CHANGE_IMAGE=0;
                                    }
                                });
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onPaused(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                    Toast.makeText(getApplicationContext(), "pauses", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                    double progress = 100.0 * (snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    int currentprogress = (int) progress;
                    //updating the progress dialog
                    progressDialog.setProgress(currentprogress);
                }
            });
        }
    }
}