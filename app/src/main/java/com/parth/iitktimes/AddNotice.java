package com.parth.iitktimes;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddNotice extends AppCompatActivity {
    private ActivityResultLauncher<String> someActivityResultLauncher;

    private MaterialCardView addImage;
    private ImageView previewImage;
    private Bitmap bitmap;
    private EditText edNoticeTitle, edNoticeDescription;
    private Button btnUploadNotice;

    //download url of the uploaded images
    private String downloadUrl = "";

    //progressDialog for updates
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notice);
        addImage = findViewById(R.id.select_image);
        previewImage = findViewById(R.id.preview_image);
        edNoticeTitle = findViewById(R.id.edNoticeTitle);
        edNoticeDescription = findViewById(R.id.edNoticeDescription);
        btnUploadNotice = findViewById(R.id.btnUploadNotice);

        //setting the context for the progressDialog
        progressDialog = new ProgressDialog(this);


        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
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
        edNoticeTitle.requestFocus();
        btnUploadNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String NoticeTitle = edNoticeTitle.getText().toString();
                String NoticeDescription = edNoticeDescription.getText().toString();

                if (NoticeTitle.isEmpty()) {
                    edNoticeTitle.setError("empty title");
                    edNoticeTitle.requestFocus();
                } else if (NoticeDescription.length() < 8) {
                    edNoticeDescription.setError("please add description to the notice");
                    edNoticeDescription.requestFocus();
                } else if (bitmap == null) {
                    Toast.makeText(getApplicationContext(), "Please add an image", Toast.LENGTH_SHORT).show();
                } else {
                    //calling upload images function
                    uploadImage();
                }
            }
        });
    }

    private void uploadData() {
        String NoticeTitle = edNoticeTitle.getText().toString();
        String NoticeDescription = edNoticeDescription.getText().toString();
        //giving reference to the database
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        //referencing to the exact location in the database
        DatabaseReference noticeDataRef = mDatabase.child("Notice");

        //getting the key of the uploaded data
        final String uniqueKey = noticeDataRef.push().getKey();

        //now storing the date and time of uploading by using calender class and initialize it to the constructor
        //we are creating a  java object for the notice data by using a inner class

        Calendar calendarForUploading = Calendar.getInstance();

        //defining the date format
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy");
        //storing the date as an string
        String currentDate = simpleDateFormat.format(calendarForUploading.getTime());

        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        String currentTime = timeFormat.format(calendarForUploading.getTime());

        //making a notice data object
        NoticeData noticeData = new NoticeData(NoticeTitle,currentDate,currentTime,downloadUrl,NoticeDescription,uniqueKey);
        //storing the data with the unique key inside the reference
        noticeDataRef.child(uniqueKey).setValue(noticeData).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    private void uploadImage() {
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
        final StorageReference filePath = storageReference.child("Notice").child("Notice Images").child(data + "jpg");
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
                                    Toast.makeText(getApplicationContext(), "Added Notice Successfully", Toast.LENGTH_SHORT).show();
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
        })
        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                double progress = 100.0 * (snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                int currentprogress = (int) progress;
                //updating the progress dialog
                progressDialog.setProgress(currentprogress);
            }
        });
    }

    //launching the image launcher
    public void selectImage() {
        someActivityResultLauncher.launch("image/*");
    }

    //declaring a inner class for uploading writing Notice Data
}