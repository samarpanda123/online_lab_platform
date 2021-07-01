package com.example.lab_project1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class FacultyActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ImageView imageView;
    TextView Lab_marks;
    TextView Check_pseudocode;
    TextView Create_lab_test;
    TextView Post_an_assignment;
    TextView Get_notice;
    FirebaseAuth auth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);
        // setSupportActionBar(toolbar);
        final AlertDialog.Builder builder = new AlertDialog.Builder(FacultyActivity.this);
        drawerLayout = findViewById(R.id.Drawer_layout);
        navigationView = findViewById(R.id.nav_bar);

//        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.naviagtion_drawer_open, R.string.navigation_drawer_close);
//        drawerLayout.addDrawerListener(toogle);

        imageView = findViewById(R.id.navigation);


        Lab_marks = (TextView)findViewById(R.id.lab_marks);
        Check_pseudocode = (TextView)findViewById(R.id.Pseudocode);
        Create_lab_test = (TextView)findViewById(R.id.lab_test);
        Post_an_assignment = (TextView)findViewById(R.id.lab_assign);
        Get_notice = (TextView)findViewById(R.id.notice1);

        Lab_marks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext() , Add_lab_marks.class));
            }
        });

        Check_pseudocode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext() , Check_Pseudocode.class));
            }
        });

        Get_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext() , Getnotice.class));
            }
        });

        Create_lab_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext() , Create_lab_test.class));
            }
        });
        Post_an_assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext() , Add_assignment.class));
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT,true);
             }
        });







        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.menu_get_assignment:
                        startActivity(new Intent(getApplicationContext() , Get_Assignment.class));
                        break;
                    case R.id.Notice:
                        startActivity(new Intent(getApplicationContext() , Faculty_Notice.class));
                        break;
                    case R.id.Lout:
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        String mail = firebaseUser.getEmail();
                        auth.signOut();
                        Toast.makeText(FacultyActivity.this, "User :"+mail+" Successfully Logout!!..", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        break;

                }
                return true;
            }

        });









    }
}