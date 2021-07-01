package com.example.lab_project1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class Attend_lab_test extends AppCompatActivity {


    EditText CourseId , StudentName , CourseName , Student_Id;
    Button Selectfile , upload , Status_button , download;
    TextView notification , Status;
    String new_url;
    int year, month, date;
    Uri fileURLs;
    StorageReference storageReference  = FirebaseStorage.getInstance().getReference();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend_lab_test);


        CourseId = findViewById(R.id.course_id_attend_lab_test);
        StudentName = findViewById(R.id.student_name_attend_lab_test);
        CourseName = findViewById(R.id.course_name_attend_lab_teste);
        Student_Id = findViewById(R.id.student_id_attend_lab_test);
        Status_button = findViewById(R.id.status1);
        Selectfile = findViewById(R.id.select_file);
        download = findViewById(R.id.download_lab_qn);
        upload = findViewById(R.id.Upload);
        notification = findViewById(R.id.notification);
        Status = findViewById(R.id.status);



        Status_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectionReference ref = db.collection("Submit_Pseudo");

                ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot d : queryDocumentSnapshots)
                        {
                            String courseid = d.get("Course_ID").toString().trim();
                            String coursename = d.get("Course_Name").toString().trim();
                            String studentid = d.get("Student_Id").toString().trim();
                            Log.d("tag3", "onSuccess: values "+CourseId.getText().toString()+" "+CourseName.getText().toString()+" "+Student_Id.getText().toString());
                            if(courseid.equals(CourseId.getText().toString().trim()) && coursename.equals(CourseName.getText().toString().trim()) && studentid.equals(Student_Id.getText().toString().trim()))
                            {
                                Toast.makeText(Attend_lab_test.this, "inside if", Toast.LENGTH_SHORT).show();
                                Status.setText(d.get("status").toString());
//                                String documentid = d.getId();
//                                WriteBatch writeBatch = db.batch();
//                                DocumentReference ref1 = db.collection("Submit_Pseudo").document(documentid);
//                                writeBatch.update(ref1 , "status" , value);
//                                writeBatch.update(ref1 , "status" , value);
//                                writeBatch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//
//                                    }
//                                });
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Attend_lab_test.this, "Unable to fetch Status of Pseudo Code"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectionReference ref = db.collection("Lab_test");

                ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                        for(QueryDocumentSnapshot d : queryDocumentSnapshots)
                        {

                            String courseid = CourseId.getText().toString();
                            String lab_test_name = d.get("Labtest_Name").toString().trim();
                            Log.d("tag1", "onSuccess: In the for loop"+courseid+"  "+d.get("Course_ID").toString()+"  "+lab_test_name+"  "+CourseName.getText().toString().trim());
                            if(courseid.equals(d.get("Course_ID").toString()) && lab_test_name.equals(CourseName.getText().toString().trim()))
                            {

                                   final String url = d.get("URL").toString().trim();
                                   downloadfile(Attend_lab_test.this , "labtest" , ".pdf" , DIRECTORY_DOWNLOADS , url);
                                    break;
                            }
                            


                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Attend_lab_test.this, "Unable to fatch Downloadable url"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }





        });



//        remove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new_url = "";
//                notification.setText("Select file");
//            }
//        });



        Selectfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(Attend_lab_test.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    Select_an_Item();
                }
                else
                    ActivityCompat.requestPermissions( Attend_lab_test.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);


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


    }

    private void downloadfile(Context context , String filename , String extension , String path , String url)
    {
        DownloadManager downloadmanager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        final Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context , path , filename+extension);
        downloadmanager.enqueue(request);
    }


    private void uploadFile(Uri fileURLs) {

        String A_id = CourseId.getText().toString();
        String A_name = CourseName.getText().toString();
        String A_student_id = Student_Id.getText().toString();
        String A_student_name = StudentName.getText().toString();
        String A_status = Status.getText().toString();


        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading File");
        progressDialog.setProgress(0);
        progressDialog.show();




        String filename = A_id+"/"+A_student_id+"/"+System.currentTimeMillis();



        //StorageReference storageReference = storage.getReference(); // will return the root path

        storageReference.child("Pseudo_list").child(filename)
                .putFile(fileURLs).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //String url = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();  // Return the URL of Uploaded file
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        new_url = uri.toString();


                        DocumentReference ref = db.collection("Submit_Pseudo").document(A_id);
                        if(!A_status.equals("NotReviewed"))
                        {
                            HashMap<String, Object> userMap = new HashMap<>();
                            userMap.put("URL", new_url);
                            userMap.put("time" , FieldValue.serverTimestamp());
                            ref.update(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Attend_lab_test.this, "file is updated Succesfully", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Attend_lab_test.this, "There is some error in updating"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else
                        {

                            HashMap<String, Object> userMap = new HashMap<>();
                            userMap.put("Course_ID", A_id);
                            userMap.put("Course_Name", A_name);
                            userMap.put("Student_Name", A_student_name);
                            userMap.put("Student_Id", A_student_id);
                            userMap.put("status", A_status);
                            userMap.put("time" , FieldValue.serverTimestamp());
                            userMap.put("URL", new_url);
                            ref.set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Attend_lab_test.this, "Lab test uploded successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Attend_lab_test.this, "There is some error in Uploading "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Attend_lab_test.this, "Unable to Downoad url"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Attend_lab_test.this, "Error: Failed to Upload!!...", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(Attend_lab_test.this, "Please Provide Permission To Select any File..", Toast.LENGTH_SHORT).show();
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