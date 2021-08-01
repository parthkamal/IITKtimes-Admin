package com.parth.iitktimes;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class addBooks extends AppCompatActivity {
    private MaterialCardView selectImage;
    private TextView docName;
    private Spinner semSpinner, branchSpinner;
    private Button btnUpload;
    private ActivityResultLauncher<String> someActivityResultLauncher; // why string
    private Uri pdfUri;
    private Note document;
    private ProgressDialog progressDialog;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_books);
        selectImage = findViewById(R.id.select_image);
        docName = findViewById(R.id.tvDocName);
        semSpinner = findViewById(R.id.spinner_sem);
        branchSpinner = findViewById(R.id.spinner_branch);
        btnUpload = findViewById(R.id.btnUploadNotes);

        progressDialog = new ProgressDialog(this);

        //that's why we have created empty constructor
        document = new Note();

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // here we will implement the logic for uploading pdf to the firebase
                StorageReference sRef = FirebaseStorage.getInstance().getReference().child("Notes").child(fileName);
                if(pdfUri!=null){
                    progressDialog.setMessage("Uploading....");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.show();
                    sRef.putFile(pdfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Uplaoded successfully",Toast.LENGTH_SHORT).show();
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
                }else{
                    Toast.makeText(getApplicationContext(),"Please select a document",Toast.LENGTH_SHORT).show();
                }
            }
        });
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                someActivityResultLauncher.launch("application/pdf");//mime type for pdf
            }
        });

        String[] semesterItems = {"First", "Second", "Third", "Fourth", "Fifth", "Sixth", "Seventh", "Eighth"};

        //attaching array adapter to the spinner
        //since we not using custom spinner therefore we are using the default array adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, semesterItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semSpinner.setAdapter(adapter);


        //on item selected listener to the spinner
        semSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    document.setSemester(semSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                document.setSemester(null);
            }
        });

        String[] branchItems = {"Mechanical", "Electrical", "CSE", "MTH"};

        //attaching array adapter to the spinner
        //since we not using custom spinner therefore we are using the default array adapter
        ArrayAdapter<String> branchAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, branchItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branchSpinner.setAdapter(branchAdapter);


        //on item selected listener to the spinner
        branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                document.setBranch(branchSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
               document.setBranch(null);
            }
        });

        //activity result launcher
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        Cursor returnCursor =
                                getContentResolver().query(result, null, null, null, null);
                        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                        returnCursor.moveToFirst();
                        fileName = returnCursor.getString(nameIndex);
                        docName.setText(returnCursor.getString(nameIndex));
                        pdfUri = result;
                    }
                });


    }
}