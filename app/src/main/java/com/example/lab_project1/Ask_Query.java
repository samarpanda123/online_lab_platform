package com.example.lab_project1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.HashMap;

public class Ask_Query extends AppCompatActivity {


    EditText course_id, ask_query;
    Button  submit;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask__query);

        course_id = findViewById(R.id.course_id);
        ask_query = findViewById(R.id.ask_query);

        submit = findViewById(R.id.submit_query);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String A_courseid = course_id.getText().toString();
                String A_ask_query = ask_query.getText().toString();

                HashMap<String, String> userMap = new HashMap<>();

                userMap.put("Course_ID", A_courseid);
                userMap.put("Ask Query", A_ask_query);

                DocumentReference ref = db.collection("Queries").document();

                ref.set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(Ask_Query.this, "Your Query is submitted successfully!!!...", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), StudentActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Ask_Query.this, "Error!! please resubmit your query...", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}