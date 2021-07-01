package com.example.lab_project1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class Get_Assignment extends AppCompatActivity implements GetAssignmentAdapter.Assignment {

    RecyclerView recyclerView;
    Toolbar toolbar;
    EditText CourseId , AssignmentName;
    Button Download;
    List<GetAssignmentModel> list = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get__assignment);

        toolbar = findViewById(R.id.toolbar_get_assignment);
        setSupportActionBar(toolbar);

        CourseId = findViewById(R.id.course_id_get_assignment);
        AssignmentName = findViewById(R.id.assign_name_get_assignment);
        Download = findViewById(R.id.check_assign_list);

        recyclerView = findViewById(R.id.recycle_view_get_assignment);

        GetAssignmentAdapter adapter = new GetAssignmentAdapter(list , Get_Assignment.this);
        recyclerView.setLayoutManager(new GridLayoutManager(Get_Assignment.this , 1));
        recyclerView.setAdapter(adapter);


        Download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.collection("Submitted_Assignment").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        list.clear();
                        for(QueryDocumentSnapshot d : value)
                        {
                            String Studentid = d.get("Student_Id").toString();
                            String Courseid = d.get("Course_ID").toString();
                            String AssignName = d.get("Assignment_Name").toString();
                            String url = d.get("URL").toString();

                            if(Courseid.equals(CourseId.getText().toString().trim()) && AssignName.equals(AssignmentName.getText().toString().trim()))
                            {
                                GetAssignmentModel model = new GetAssignmentModel(Studentid , Courseid , AssignName , url);
                                list.add(model);
                            }
                        }
                        adapter.notifyDataSetChanged();

                    }
                });

            }
        });



    }

    @Override
    public void downloadAssignment(String url) {

        downloadfile(Get_Assignment.this , "Assignment" , ".pdf" , DIRECTORY_DOWNLOADS , url);

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
}