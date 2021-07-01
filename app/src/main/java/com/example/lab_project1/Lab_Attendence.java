package com.example.lab_project1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Lab_Attendence extends AppCompatActivity {

    EditText CourseId, Coursename, studentid, studentname;
    Button submit;
    RadioGroup radioGroup;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab__attendence);

        CourseId = (EditText) findViewById(R.id.course_id);
        Coursename = (EditText) findViewById(R.id.course_name);
        studentid = (EditText) findViewById(R.id.student_id);
        studentname = (EditText) findViewById(R.id.student_name);
        submit = (Button) findViewById(R.id.submit_button1);
        radioGroup = (RadioGroup) findViewById(R.id.radio_button);
        radioGroup.clearCheck();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
            }
        });

        //Toast.makeText(this, "Coming to this page", Toast.LENGTH_SHORT).show();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.mm.dd G 'at' hh:mm:ss z");
                String currentDateandTime = sdf.format(date);
                DocumentReference ref = db.collection("Attandence").document();


                int selectedId = radioGroup.getCheckedRadioButtonId();

                RadioButton radioButton = (RadioButton)radioGroup.findViewById(selectedId);

                HashMap<String, Object> user = new HashMap<>();
                //Log.d("tag1", "onClick: radio"+radioButton);


                user.put("courseid", CourseId.getText().toString());
                user.put("coursename", Coursename.getText().toString());
                user.put("studentid", studentid.getText().toString());
                user.put("studentname", studentname.getText().toString());
                user.put("attendence", radioButton.getText().toString());
                user.put("date", currentDateandTime);

                ref.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Toast.makeText(Lab_Attendence.this, "Attandence Stored Successfully", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(getApplicationContext(),StudentActivity.class));
                        Toast.makeText(Lab_Attendence.this, "Value :"+ radioButton.getText().toString(), Toast.LENGTH_LONG).show();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Lab_Attendence.this, "There is some error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

    }
}