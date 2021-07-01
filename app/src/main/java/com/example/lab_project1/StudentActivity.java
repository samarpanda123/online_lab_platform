package com.example.lab_project1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class StudentActivity extends AppCompatActivity {

    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;

    TextView lab_marks, ask_queries , Join_lab_test , Submit_assignment , Check_Notice;
    NavigationView navigationView;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
//        getSupportActionBar().setTitle("Student DashBoard");

        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final AlertDialog.Builder builder = new AlertDialog.Builder(StudentActivity.this);
        nav = (NavigationView)findViewById(R.id.nav_bar1);
        drawerLayout = (DrawerLayout)findViewById(R.id.Drawer_layout);

        toggle = new ActionBarDrawerToggle(this , drawerLayout , toolbar , R.string.open , R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //imageView = (ImageView) findViewById(R.id.top_navigate);
        drawerLayout = findViewById(R.id.Drawer_layout);
        navigationView = findViewById(R.id.nav_bar1);
        ask_queries = findViewById(R.id.ask_qad);
        lab_marks = findViewById(R.id.lab_marks1);
        Join_lab_test = findViewById(R.id.lab_test1);
        Submit_assignment = findViewById(R.id.lab_assign1);
        Check_Notice = findViewById(R.id.notice1);



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.Update_account:
                        View mview = getLayoutInflater().inflate(R.layout.studentupdateaccountpopup , null);

                        final EditText UserId = (EditText)mview.findViewById(R.id.Student_Update_userid);
                        final EditText UserDetails = (EditText)mview.findViewById(R.id.Student_updated_details);
                        builder.setView(mview);
                        builder.setCancelable(false);

                        AlertDialog alertDialog = builder.create();
                        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.setPositiveButton("Request Upadte", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DocumentReference ref = db.collection("Update_requests").document();
                                HashMap<String , Object>request = new HashMap<>();
                                request.put("UserId" , UserId.getText().toString().trim());
                                request.put("UserDetails" , UserDetails.getText().toString().trim());

                                ref.set(request).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(StudentActivity.this, "Update Request is Posted Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(StudentActivity.this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        });

                        builder.show();
                        break;

                    case R.id.attendence:
                        startActivity(new Intent(getApplicationContext() , Lab_Attendence.class));
                        break;
                    case R.id.Lout:
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        String mail = firebaseUser.getEmail();
                        auth.signOut();
                        Toast.makeText(StudentActivity.this, "User :"+mail+" Successfully Logged Out!!..", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        break;

                }
                return true;
            }

        });


//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawerLayout.openDrawer(Gravity.LEFT, true);
//            }
//        });


        ask_queries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Ask_Query.class));
            }
        });
        Submit_assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext() , Submit_assignment.class));
            }
        });
        lab_marks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext() , show_lab_marks.class));
            }
        });
        Join_lab_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext() , Attend_lab_test.class));
            }
        });
        Check_Notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext() , studentCheckNotice.class));
            }
        });

    }
}