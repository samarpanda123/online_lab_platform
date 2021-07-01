package com.example.lab_project1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Add_lab_marks extends AppCompatActivity {

    EditText CourseId , StudentName , CourseName , Student_Id , Type , Marks;
    Button Submit_button;
    Toolbar toolbar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lab_marks);

        toolbar = findViewById(R.id.toolbar_add_lab_marks);
        setSupportActionBar(toolbar);


        CourseId = findViewById(R.id.course_id_add_lab_marks);
        StudentName = findViewById(R.id.student_name_add_lab_marks);
        CourseName = findViewById(R.id.course_name_add_lab_marks);
        Student_Id = findViewById(R.id.student_id_add_lab_marks);
        Type = findViewById(R.id.content_add_lab_marks);
        Marks = findViewById(R.id.Marks_add_lab_marks);
        Submit_button = findViewById(R.id.button_add_lab_marks);

        Submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference ref = db.collection("Lab_marks").document();
                HashMap<String , Object>user = new HashMap<>();

                user.put("Course_Id" , CourseId.getText().toString().trim());
                user.put("Course_Name" , CourseName.getText().toString().trim());
                user.put("Student_Id" , Student_Id.getText().toString().trim());
                user.put("Student_Name" , StudentName.getText().toString().trim());
                user.put("Type" , Type.getText().toString().trim());
                user.put("Marks" , Marks.getText().toString().trim());

                ref.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Add_lab_marks.this, "Lab marks Are added Sucessfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Add_lab_marks.this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });




    }
}