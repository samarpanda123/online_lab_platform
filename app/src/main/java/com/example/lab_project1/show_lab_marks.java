package com.example.lab_project1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class show_lab_marks extends AppCompatActivity {

    TextView CourseId , StudentId;
    Button Submit_button;
    Toolbar toolbar;
    RecyclerView recyclerView;
    List<ShowLabMarksModel> showLabMarksModels = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_lab_marks);

        toolbar = findViewById(R.id.toolbar_add_lab_marks);
        setSupportActionBar(toolbar);

        CourseId = findViewById(R.id.course_id_show_lab_marks);
        StudentId = findViewById(R.id.student_id_show_lab_marks);
        Submit_button = findViewById(R.id.button_show_lab_marks);

        recyclerView = findViewById(R.id.recycle_view_show_lab_marks);
        ShowLabMarksAdapter adapter = new ShowLabMarksAdapter(showLabMarksModels);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext() , 1));
        recyclerView.setAdapter(adapter);




        Submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectionReference ref = db.collection("Lab_marks");

                ref.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        showLabMarksModels.clear();
                        for(QueryDocumentSnapshot d : value)
                        {
                            String courseid = d.get("Course_Id").toString().trim();
                            String studentid = d.get("Student_Id").toString().trim();


                            if(courseid.equals(CourseId.getText().toString().trim()) && studentid.equals(StudentId.getText().toString().trim()))
                            {
                                String coursename = d.get("Course_Name").toString().trim();
                                String studentname = d.get("Student_Name").toString().trim();
                                String type = d.get("Type").toString().trim();
                                String marks = d.get("Marks").toString().trim();

                                ShowLabMarksModel showLabMarksModel = new ShowLabMarksModel(courseid , coursename , studentid , studentname , type , marks);
                                showLabMarksModels.add(showLabMarksModel);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

            }
        });


    }
}