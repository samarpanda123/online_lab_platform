package com.example.lab_project1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    EditText Email , Password;
    Button LogIn;
    TextView ForgotPassword, textView;
    FirebaseAuth Auth;
    boolean valid=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);
        LogIn = findViewById(R.id.loginbutton);
        ForgotPassword = findViewById(R.id.forgotpassword);
        Auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("User");
        textView = findViewById(R.id.forgotpassword);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Forgot_passwd.class);
                startActivity(intent);

                Toast.makeText(MainActivity.this, "Moving to Reset Password Page", Toast.LENGTH_SHORT).show();
            }
        });

        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateData(Email);
                validateData(Password);

                if(valid) {
                    String email = Email.getText().toString().trim();
                    String password = Password.getText().toString().trim();
                    Auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String uid = Auth.getCurrentUser().getUid().toString();
                                ref.document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                                        String type = documentSnapshot.getString("type").toString();
                                        Log.d("type1", "On Success : User Profile is created " + type);
                                        if (type.equals("Admin")) {
                                            startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                                        } else if (type.equals("Faculty")) {
                                            startActivity(new Intent(getApplicationContext(), FacultyActivity.class));
                                        } else {
                                            startActivity(new Intent(getApplicationContext(), StudentActivity.class));
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MainActivity.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            } else {
                                Toast.makeText(MainActivity.this, "User Credentials are not valid", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(MainActivity.this, "ALL the fields are required...", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void validateData(EditText userEmail) {

        if(userEmail.getText().toString().isEmpty())
        {
            valid = false;
            userEmail.setError("required Field");
            return;
        }
        else
        {
            valid = true;

        }

    }
}