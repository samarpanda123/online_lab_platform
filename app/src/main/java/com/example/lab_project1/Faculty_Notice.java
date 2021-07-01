package com.example.lab_project1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Faculty_Notice extends AppCompatActivity {

    private EditText Published_by;
    private EditText Published_for;
    private EditText Notice;
    private Button publish;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty__notice);





        Published_by = (EditText)findViewById(R.id.pub_by);
        Published_for = (EditText)findViewById(R.id.pub_for);
        Notice = (EditText)findViewById(R.id.description);
        publish = (Button)findViewById(R.id.publish);


        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DocumentReference ref1 = db.collection("Notice").document();

                Map<String,Object> user = new HashMap<>();
                user.put("published_by",Published_by.getText().toString().trim());
                user.put("published_for",Published_for.getText().toString().trim());
                user.put("notice",Notice.getText().toString().trim());
                user.put("date", FieldValue.serverTimestamp());

                ref1.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(Faculty_Notice.this, "Published Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext() , FacultyActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Faculty_Notice.this, "There is an Error in Publishing Notice"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });




    }
}