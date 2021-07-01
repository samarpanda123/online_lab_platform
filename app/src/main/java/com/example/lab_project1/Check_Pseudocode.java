package com.example.lab_project1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class Check_Pseudocode extends AppCompatActivity implements PseudoCodeAdapter.Myclick {

    RecyclerView recyclerView;
    private EditText CourseId;
    private EditText TestName;
    private Button get_pseudocode;
    EditText review;
    AlertDialog.Builder builder;
    private Toolbar toolbar;
    List<PseudoCodeModel>list = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check__pseudocode);
        toolbar = findViewById(R.id.toolbar_check_pseudo);
        setSupportActionBar(toolbar);

        CourseId = findViewById(R.id.course_id_pseudo_code);
        TestName = findViewById(R.id.test_name_pseudo_code);
        get_pseudocode = findViewById(R.id.check_pseudo_list);

        recyclerView = findViewById(R.id.recycle_view_pseudo_code);
        PseudoCodeAdapter adapter = new PseudoCodeAdapter(list , Check_Pseudocode.this);
        recyclerView.setLayoutManager(new GridLayoutManager(Check_Pseudocode.this , 1));
        recyclerView.setAdapter(adapter);


        get_pseudocode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Submit_Pseudo").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        for(QueryDocumentSnapshot d : value)
                        {
                            String courseid = d.get("Course_ID").toString().trim();
                            String testname = d.get("Course_Name").toString().trim();
                            list.clear();
                            if(courseid.equals(CourseId.getText().toString().trim()) && testname.equals(TestName.getText().toString().trim()))
                            {
                                String studentid = d.get("Student_Id").toString().trim();
                                Date Time = d.getTimestamp("time").toDate();
                                String url = d.get("URL").toString().trim();
                                String documentid = d.getId();
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY  HH:mm:ss a");
                                String new_time = sdf.format(new Date(String.valueOf(Time)));

                                PseudoCodeModel pseudoCodeModel = new PseudoCodeModel(courseid , testname , studentid , new_time, url , documentid);
                                list.add(pseudoCodeModel);
                            }
                            adapter.notifyDataSetChanged();
                        }

                    }
                });
            }
        });



    }

    @Override
    public void firstButton(String str) {

        downloadfile(Check_Pseudocode.this , "labtest" , ".pdf" , DIRECTORY_DOWNLOADS , str);

    }

    private void downloadfile(Context context , String filename , String extension , String path , String url){

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

    @Override
    public void secondButton(String documentid) {


        builder = new AlertDialog.Builder(Check_Pseudocode.this);
        View view = getLayoutInflater().inflate(R.layout.pseudopopup , null);
        review = view.findViewById(R.id.review_pseudopopup);
        builder.setView(view);
        //AlertDialog alertDialog = builder.create();
        AlertDialog alertDialog = builder.create();
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setCancelable(false);
        builder.setPositiveButton("Update Status", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DocumentReference ref = db.collection("Submit_Pseudo").document(documentid);
                HashMap<String , Object>user = new HashMap<>();
                Log.d("something", "onClick: "+review.getText().toString());
                user.put("status" , review.getText().toString().trim());
                ref.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Check_Pseudocode.this, "Your Review is updated Sucessfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Check_Pseudocode.this, "There is some error in Updating your Review"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.show();

    }
}