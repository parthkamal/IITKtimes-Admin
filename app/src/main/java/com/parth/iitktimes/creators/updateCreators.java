package com.parth.iitktimes.creators;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.parth.iitktimes.Creator;
import com.parth.iitktimes.R;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class updateCreators extends AppCompatActivity {
    //declaring private variables
    private MaterialCardView SelectImage;
    private EditText edCreatorName, edCreatorDesignation, edCreatorEmail, edCreatorMobile;
    private Button btnAddCreator;
    private CircleImageView previewImage;
    private Bitmap bitmap;
    private ActivityResultLauncher<String> someActivityResultLauncher;

    //download url of the uploaded images
    private String downloadUrl = "";

    //progressDialog for updates
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_creators);
        // initialisation
        SelectImage = findViewById(R.id.select_image);
        edCreatorName = findViewById(R.id.edCreatorName);
        edCreatorDesignation = findViewById(R.id.edCreatorDesignation);
        edCreatorEmail = findViewById(R.id.edCreatorEmail);
        edCreatorMobile = findViewById(R.id.edCreatorMobile);
        btnAddCreator = findViewById(R.id.btnAddCreator);
        previewImage = findViewById(R.id.preview_image);

        progressDialog = new ProgressDialog(this);

        SelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

        btnAddCreator.setOnClickListener(new View.OnClickListener() {
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
                } else if (bitmap == null) {
                    Toast.makeText(getApplicationContext(), "Please add an image", Toast.LENGTH_SHORT).show();
                } else {
                    addMember();
                }
            }
        });


        //activity result launcher
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), result);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        previewImage.setImageBitmap(bitmap);
                    }
                });
    }

    private void addMember() {
        progressDialog.setMessage("Uploading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
        //creating firebase storage reference
        FirebaseStorage storage = FirebaseStorage.getInstance();
        //creating a storage reference for our app
        StorageReference storageReference = storage.getReference();
        //we are using input stream method to sent the data in packets which results in less memory
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //compressing the bitmap to bite code without changing the quality
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        //storing the bitmap in a byte array
        byte[] data = baos.toByteArray();

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
                                    uploadData();
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Added members Successfully", Toast.LENGTH_SHORT).show();
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

    private void uploadData() {
        String Name = edCreatorName.getText().toString();
        String Email = edCreatorEmail.getText().toString();
        String Phone = edCreatorMobile.getText().toString();
        String Post = edCreatorDesignation.getText().toString();
        //giving reference to the database
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        //referencing to the exact location in the database
        DatabaseReference creatorsDataRef = mDatabase.child("Creators");

        //getting the key of the uploaded data
        final String uniqueKey = creatorsDataRef.push().getKey();

        Creator creator = new Creator(Name, Post, Email, Phone, downloadUrl);


        //storing the data with the unique key inside the reference
        creatorsDataRef.child(uniqueKey).setValue(creator).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getApplicationContext(), "data couldn't upload", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    //function to pick the image from the gallery
    private void pickImage() {
        someActivityResultLauncher.launch("image/*");
    }


}