package com.parth.iitktimes;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
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

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddImages extends AppCompatActivity {
    //THIS ACTIVITY IS USED TO ADD IMAGES RELATED TO THE EVENTS HAPPENED IN THE COLLEGE

    private Spinner eventSpinner;
    private Button btnUploadImage;
    private MaterialCardView select_event_image;
    private ImageView preview_events_image;
    private Bitmap bitmap;
    private String downloadUrl="";
    private ProgressDialog progressDialog;
    UploadDataTask asyncTask =null;



    private ActivityResultLauncher<String> someActivityResultLauncher;

    //declaring constant
    private String imageCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_images);
        eventSpinner = findViewById(R.id.spinner_image_category);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        select_event_image = findViewById(R.id.select_event_image);
        preview_events_image= findViewById(R.id.preview_events_image);

        progressDialog = new ProgressDialog(this);



        String[] spinnerItems ={"Others","Festivals","Antaragni","Techkriti","Udghosh","Special"};

        //attaching array adapter to the spinner
        //since we not using custom spinner therefore we are using the default array adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventSpinner.setAdapter(adapter);




        //on item selected listener to the spinner
        eventSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               imageCategory = eventSpinner.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //setting the default category as others
                imageCategory =spinnerItems[0];
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
                        preview_events_image.setImageBitmap(bitmap);
                    }
                });

        //card view click listener for adding the image
        select_event_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                someActivityResultLauncher.launch("image/*");
            }
        });


        //upload image click listener
        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bitmap==null){
                    Toast.makeText(getApplicationContext(),"Please Select An Image",Toast.LENGTH_SHORT).show();
                } else {
                    uploadImage();
                }
            }
        });
    }






    // built in functions
    private void uploadImage() {
        progressDialog.setMessage("Uploading Events");
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
        final StorageReference filePath = storageReference.child("Events").child("Events Images").child(data + "jpg");
        //reference will be created if it is not STORAGE->NOTICE->IMAGES->UNIQUE_CODE.JPG
        UploadTask uploadTask = filePath.putBytes(data);
        //executing a images upload by put bytes method
        //adding success and failure listeners to the uploadTask
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @org.jetbrains.annotations.NotNull Exception e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<UploadTask.TaskSnapshot> task) {
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
                                    //dismissing the progressDialog
//                                    progressDialog.dismiss();
                                    //now since the task is completed we are uploading the data now;
                                    uploadImageData();
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
            public void onPaused(@NonNull @org.jetbrains.annotations.NotNull UploadTask.TaskSnapshot snapshot) {
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

    private void uploadImageData() {
//        progressDialog.setMessage("Uploading Events Category and Data");
        //giving reference to the database
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        //referencing to the exact location in the database
        DatabaseReference noticeDataRef = mDatabase.child("Events");

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

        //declaring an events data object in which we store our category of events and date of events
        eventsData eventsData = new eventsData(imageCategory,currentDate,currentTime,downloadUrl,uniqueKey);
        //storing the data with the unique key inside the reference
        noticeDataRef.child(uniqueKey).setValue(eventsData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Added events successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getApplicationContext(), "data couldn't upload", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }
    //class for async task operations
    private class UploadDataTask extends AsyncTask<Bitmap, Integer, Void> {
        private Bitmap bitmap;

        //empty constructor
        public UploadDataTask(Bitmap bitmap) {
            super();

        }

        //pre execute method of async task
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //setting the context for the progressDialog
            progressDialog = new ProgressDialog(getApplicationContext());
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setProgress(0);//initially progress is 0
            progressDialog.setMax(100);//sets the maximum value 100
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            //make a button for cancellation
            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    progressDialog.dismiss();
                    asyncTask.cancel(true);
                }
            });
        }

        @Override
        protected Void doInBackground(Bitmap... bitmaps) {
            bitmap = bitmaps[0];
            uploadImage();
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values[0]);
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            Toast.makeText(getApplicationContext(),"uploaded image successfully",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }


    }

    //private class for eventsData
    private class eventsData{
        String eventsCategory,eventDate,eventTime,downloadUrl,uniqueKey;

        public eventsData(String eventsCategory, String eventDate, String eventTime, String downloadUrl, String uniqueKey) {
            this.eventsCategory = eventsCategory;
            this.eventDate = eventDate;
            this.eventTime = eventTime;
            this.downloadUrl = downloadUrl;
            this.uniqueKey = uniqueKey;
        }

        public String getEventsCategory() {
            return eventsCategory;
        }

        public void setEventsCategory(String eventsCategory) {
            this.eventsCategory = eventsCategory;
        }

        public String getEventDate() {
            return eventDate;
        }

        public void setEventDate(String eventDate) {
            this.eventDate = eventDate;
        }

        public String getEventTime() {
            return eventTime;
        }

        public void setEventTime(String eventTime) {
            this.eventTime = eventTime;
        }

        public String getDownloadUrl() {
            return downloadUrl;
        }

        public void setDownloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
        }

        public String getUniqueKey() {
            return uniqueKey;
        }

        public void setUniqueKey(String uniqueKey) {
            this.uniqueKey = uniqueKey;
        }
    }

}