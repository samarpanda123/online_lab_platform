package com.example.lab_project1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Submit_assignment extends AppCompatActivity {

    EditText CourseId , AssignmentName , CourseName , Student_Id;
    Button Selectfile , upload , remove , submit_btn;
    TextView notification;
    String new_url;
    int year, month, date;
    Uri fileURLs;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_assignment);

        CourseId = findViewById(R.id.course_id);
        AssignmentName = findViewById(R.id.Assign_name);
        CourseName = findViewById(R.id.course_name);
        Student_Id = findViewById(R.id.student_id);
        notification = findViewById(R.id.notification);
        Selectfile = findViewById((R.id.select_file));
        upload = findViewById(R.id.Upload);
        remove = findViewById(R.id.Remove);
        submit_btn = findViewById(R.id.submit_assign);

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_url = "";
                notification.setText("Select file");
            }
        });


        Selectfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(Submit_assignment.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    Select_an_Item();
                }
                else
                    ActivityCompat.requestPermissions( Submit_assignment.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
            }


        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fileURLs != null){
                    uploadFile(fileURLs);
                }
            }
        });


        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String A_id = CourseId.getText().toString();
                String A_name = CourseName.getText().toString();
                String A_Assign_name = AssignmentName.getText().toString();
                String A_Student_Id = Student_Id.getText().toString();


                HashMap<String, Object> userMap = new HashMap<>();

                String urls = new_url.toString();

                userMap.put("Course_ID", A_id);
                userMap.put("Course_Name", A_name);
                userMap.put("Assignment_Name", A_Assign_name);
                userMap.put("Student_Id", A_Student_Id);
                userMap.put("date", System.currentTimeMillis());
                userMap.put("URL", urls);


                DocumentReference ref = db.collection("Submitted_Assignment").document();

                ref.set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isComplete())
                        {
                            Toast.makeText(Submit_assignment.this, "Lab test is created successfully", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Submit_assignment.this, "There is an error in creating Lab test"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


//                root.push().setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(Add_assignment.this, "Assignment is created Successfully!!...", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(Add_assignment.this, "Error: Assignment not created due to some problem!!...", Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        });
    }

    private void uploadFile(Uri fileURLs) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading File");
        progressDialog.setProgress(0);
        progressDialog.show();


        String Name_Assign = AssignmentName.getText().toString().trim();


        String filename = Name_Assign +"/"+System.currentTimeMillis();



        //StorageReference storageReference = storage.getReference(); // will return the root path

        storageReference.child("Submitted_Assignment").child(filename)
                .putFile(fileURLs).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                String url = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();  // Return the URL of Uploaded file
//                new_url = url;

                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        new_url = uri.toString();
                        Toast.makeText(Submit_assignment.this, "Assinment Submitted Successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Submit_assignment.this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                // Store the url in real time database
//                DatabaseReference reference = database.getReference(); // Return the Path to Root
//
//                reference.child(filename).setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful()){
//                            Toast.makeText(Add_assignment.this, "File is successfully Uploaded", Toast.LENGTH_LONG).show();
//                        }
//                        else
//                            Toast.makeText(Add_assignment.this, "Error: File Uploading Failed!!...", Toast.LENGTH_LONG).show();
//                    }
//                }) ;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Submit_assignment.this, "Error: Failed to Upload!!...", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                // this will track the progress of our upload of File

                int currentProgress = (int) (100 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if( requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            Select_an_Item();
        }
        else
            Toast.makeText(Submit_assignment.this, "Please Provide Permission To Select any File..", Toast.LENGTH_SHORT).show();
    }

    private void Select_an_Item(){
        //In Here our Main task is to select file that needs to be uploaded into the system

        // For this purpose we will be using Intent

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);    // TO feach teh Selected File
        startActivityForResult(intent, 86);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( requestCode == 86 && resultCode == RESULT_OK && data != null){
            fileURLs = data.getData();
            notification.setText("File Selected : "+ data.getData().getLastPathSegment());
        }
        else
            Toast.makeText(this, "Error: Please select a file!!...", Toast.LENGTH_SHORT).show();
    }

}