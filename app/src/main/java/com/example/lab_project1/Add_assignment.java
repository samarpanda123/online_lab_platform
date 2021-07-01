package com.example.lab_project1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Add_assignment extends AppCompatActivity {

    Button Selectfile, upload, remove;
     // URi s are URLS that are meant for local storage

    TextView notification;
    EditText  course_id, course_name,  Assignment_name, Assign_desp, start_date, end_date, start_time, end_time;
    ProgressDialog progressDialog;
    Button submit_btn, btn_start_date, btn_end_date, btn_start_time, btn_end_time;
    String new_url;
    int year, month, date;
    Uri fileURLs;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assignment);


        Selectfile = findViewById(R.id.select_file);
        upload = findViewById(R.id.Upload);
        remove = findViewById(R.id.Remove);
        course_id = findViewById(R.id.course_id);
        course_name = findViewById(R.id.course_name);
        Assign_desp = findViewById(R.id.Assign_desp);
        notification = findViewById(R.id.notification);
        Assignment_name = findViewById(R.id.Assign_name);
        start_date = findViewById(R.id.start_date);
        end_date = findViewById(R.id.end_date);
        start_time= findViewById(R.id.start_time);
        end_time = findViewById(R.id.end_time);
        submit_btn = findViewById(R.id.create_assign);
        btn_start_date = findViewById(R.id.btn_start_date);
        btn_end_date = findViewById(R.id.btn_end_date);
        btn_start_time = findViewById(R.id.btn_start_time);
        btn_end_time = findViewById(R.id.btn_end_time);






        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_url = "";
                notification.setText("Select file");
            }
        });

        btn_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh-mm a");
                        start_time.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };
                new TimePickerDialog(Add_assignment.this, onTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        });


        btn_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh-mm a");
                        end_time.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };
                new TimePickerDialog(Add_assignment.this, onTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        });




        btn_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY");
                        start_date.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };
                new DatePickerDialog( Add_assignment.this, onDateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btn_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY");
                        end_date.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };
                new DatePickerDialog( Add_assignment.this, onDateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        Selectfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(Add_assignment.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    Select_an_Item();
                }
                else
                    ActivityCompat.requestPermissions( Add_assignment.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
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
                String A_id = course_id.getText().toString();
                String A_name = course_name.getText().toString();
                String A_Assign_name = Assignment_name.getText().toString();
                String A_Assign_desp = Assign_desp.getText().toString();
                String A_startdate = start_date.getText().toString();
                String A_enddate = end_date.getText().toString();
                String A_starttime = start_time.getText().toString();
                String A_endtime = end_time.getText().toString();

                HashMap<String, String> userMap = new HashMap<>();
                if(new_url.length()>0){
                    String urls = new_url.toString();

                    userMap.put("Course_ID", A_id);
                    userMap.put("Course_Name", A_name);
                    userMap.put("Assignment_Name", A_Assign_name);
                    userMap.put("Assignment_Description", A_Assign_desp);
                    userMap.put("URL", urls);
                    userMap.put("Start_Date", A_startdate);
                    userMap.put("End_Date", A_enddate);
                    userMap.put("Start_Time", A_starttime);
                    userMap.put("End_Time", A_endtime);
                }
                else{
                    String str="N/A";


                    userMap.put("Course_ID", A_id);
                    userMap.put("Course_Name", A_name);
                    userMap.put("Assignment_Name", A_Assign_name);
                    userMap.put("Assignment_Description", A_Assign_desp);
                    userMap.put("URL", str);
                    userMap.put("Start_Date", A_startdate);
                    userMap.put("End_Date", A_enddate);
                    userMap.put("Start_Time", A_starttime);
                    userMap.put("End_Time", A_endtime);
                }

                DocumentReference ref = db.collection("Assignment_list").document();

                ref.set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isComplete())
                        {
                            Toast.makeText(Add_assignment.this, "Lab test is created successfully", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Add_assignment.this, "There is an error in creating Lab test"+e.getMessage(), Toast.LENGTH_SHORT).show();
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


        String Name_Assign = Assignment_name.getText().toString().trim();


        String filename = Name_Assign +"/"+System.currentTimeMillis();



        
        //StorageReference storageReference = storage.getReference(); // will return the root path

        storageReference.child("Assignment_list").child(filename)
                .putFile(fileURLs).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //String url = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();  // Return the URL of Uploaded file
                //new_url = url;
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        new_url = uri.toString();
                        progressDialog.cancel();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Add_assignment.this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Add_assignment.this, "Error: Failed to Upload!!...", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(Add_assignment.this, "Please Provide Permission To Select any File..", Toast.LENGTH_SHORT).show();
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