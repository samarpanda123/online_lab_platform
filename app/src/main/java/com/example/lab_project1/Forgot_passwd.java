package com.example.lab_project1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_passwd extends AppCompatActivity {

    TextView forgot_password;
    Button submit;
    FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_passwd);


        fAuth = FirebaseAuth.getInstance();
        submit = findViewById(R.id.submit_button);
        forgot_password = findViewById(R.id.forgot_pass);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = forgot_password.getText().toString();
                fAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Forgot_passwd.this, "A Reset Password Link has send to your email address successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Forgot_passwd.this, "Error! Reset Link Not Send... \n Please try again", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}