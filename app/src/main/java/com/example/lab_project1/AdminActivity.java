package com.example.lab_project1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;

    FirebaseAuth Auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    EditText Filename;
    Boolean valid = false;
    Boolean valid1 = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
        nav = (NavigationView)findViewById(R.id.nav);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);

        toggle = new ActionBarDrawerToggle(this , drawerLayout , toolbar , R.string.open , R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();



        recyclerView = findViewById(R.id.recycle_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        List<NoticeModel> noticeModels = new ArrayList<>();
        Adapter adapter = new Adapter(noticeModels);
        recyclerView.setAdapter(adapter);


        db.collection("Notice").orderBy("date" , Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null)
                {
                    Toast.makeText(AdminActivity.this, "Error"+error.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                noticeModels.clear();
                for(QueryDocumentSnapshot d : value)
                {
                    long time = (long) d.get("time");
                    long curr = (long)System.currentTimeMillis();
                    long diff = curr - time;
                    diff = diff /3600000;


                    if(diff < 336)
                    {
                        String published_by = d.get("published_by").toString();
                        String published_for = d.get("published_for").toString();
                        String notice = d.get("notice").toString();
                        noticeModels.add(new NoticeModel(published_by , published_for , notice));
                    }

                }
                adapter.notifyDataSetChanged();
            }
        });








        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.menu_create:

                        View mview = getLayoutInflater().inflate(R.layout.popup , null);

                        final EditText UserEmail = (EditText)mview.findViewById(R.id.useremail);
                        final EditText UserPassword = (EditText)mview.findViewById(R.id.userpassword);
                        final EditText UserName  = (EditText)mview.findViewById(R.id.username);
                        final EditText UserType = (EditText)mview.findViewById(R.id.usertype);
                        final EditText UserId = (EditText)mview.findViewById(R.id.userid);

                        builder.setView(mview);
                        builder.setCancelable(false);

                        AlertDialog alertDialog = builder.create();
                        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        alertDialog.setCancelable(false);
                        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                validateData1(UserEmail);
                                validateData(UserEmail);
                                validateData(UserPassword);
                                validateData(UserType);
                                validateData(UserName);
                                validateData(UserId);
                                if(valid && valid1)
                                {
                                    Auth.createUserWithEmailAndPassword(UserEmail.getText().toString() , UserPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful())
                                            {
                                                String uid = Auth.getCurrentUser().getUid().toString();
                                                DocumentReference ref = db.collection("User").document(uid);
                                                Map<String,Object> user = new HashMap<>();
                                                user.put("Name",UserName.getText().toString().trim());
                                                user.put("type",UserType.getText().toString().trim());
                                                user.put("id",UserId.getText().toString().trim());
                                                user.put("isdeleted",false);
                                                ref.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful())
                                                        {
                                                            Toast.makeText(AdminActivity.this, "New User is Created Successfully", Toast.LENGTH_SHORT).show();
                                                            dialog.cancel();
                                                        }
                                                        else
                                                        {
                                                            Toast.makeText(AdminActivity.this, "Error "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                            else
                                            {
                                                Toast.makeText(AdminActivity.this, "Error "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                                else
                                {

                                    if(!valid)
                                    {
                                        Toast.makeText(AdminActivity.this, "Please fill all required fields ", Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(AdminActivity.this, "Please Use Nitc Email to register ", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }

                        });
                        builder.show();
                        Toast.makeText(AdminActivity.this, "Menu create pannel", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_update_request:
                        startActivity(new Intent(getApplicationContext() , UpdateRequests.class));
                        break;

                    case R.id.menu_edit:
                        View mview_edit = getLayoutInflater().inflate(R.layout.popup_del_edit_rec , null);
                        final EditText UserId_edit = (EditText)mview_edit.findViewById(R.id.userid);
                        final EditText Username_edit = (EditText)mview_edit.findViewById(R.id.username);
                        builder.setView(mview_edit);
                        AlertDialog alertDialog_edit = builder.create();

                        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });


                        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                validateData(UserId_edit);
                                validateData(Username_edit);
                                if(valid)
                                {
                                    String userid = UserId_edit.getText().toString();
                                    //DocumentReference ref1 = db.collection("User").document().get()
                                    CollectionReference ref1 = db.collection("User");
                                    ref1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful())
                                            {

                                                for(QueryDocumentSnapshot document : task.getResult())
                                                {
                                                    if(document.getString("id").equals(userid)) {
                                                        final String uid = document.getId();
                                                        DocumentReference ref2 = db.collection("User").document(uid);
                                                        Map<String,Object> user = new HashMap<>();
                                                        user.put("Name",Username_edit.getText().toString().trim());
                                                        ref2.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful())
                                                                {
                                                                    Toast.makeText(AdminActivity.this, "User data is updated", Toast.LENGTH_SHORT).show();
                                                                    dialog.cancel();
                                                                }
                                                                else
                                                                {
                                                                    Toast.makeText(AdminActivity.this, "Error "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                        break;
                                                    }
                                                }
                                                //Log.d("tag1", "onComplete: required information is "+ data.get);
                                            }
                                        }
                                    });
                                }
                                else
                                {
                                    Toast.makeText(AdminActivity.this, "Please fill all Required fields", Toast.LENGTH_SHORT).show();

                                }

                            }
                        });
                        builder.show();
                        Toast.makeText(AdminActivity.this, "Menu edit pannel", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_delete:
                        View mview_delete = getLayoutInflater().inflate(R.layout.popup_delete , null);
                        final EditText UserId_delete = (EditText)mview_delete.findViewById(R.id.userid);
                        builder.setView(mview_delete);
                        AlertDialog alertDialog_delete = builder.create();

                        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });



                        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                validateData(UserId_delete);
                                if(valid)
                                {
                                    String userid = UserId_delete.getText().toString();
                                    //DocumentReference ref1 = db.collection("User").document().get()
                                    CollectionReference ref1 = db.collection("User");
                                    ref1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful())
                                            {

                                                for(QueryDocumentSnapshot document : task.getResult())
                                                {
                                                    if(document.getString("id").equals(userid)) {
                                                        final String uid = document.getId();
                                                        DocumentReference ref2 = db.collection("User").document(uid);
                                                        Map<String,Object> user = new HashMap<>();
                                                        ref2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                if(task.isSuccessful())
                                                                {
                                                                    String status = task.getResult().get("isdeleted").toString();
                                                                    String x;
                                                                    if(status.equals("true"))
                                                                    {
                                                                        Log.d("tag1", "onComplete:  User is Already Deleted ");
                                                                        user.put("isdeleted",true);
                                                                        x = "User is Already Deleted";

                                                                    }
                                                                    else
                                                                    {
                                                                        Log.d("tag2", "onComplete:  User is deleted ");
                                                                        user.put("isdeleted",true);
                                                                        x = "User is deleted";
                                                                    }
                                                                    ref2.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if(task.isSuccessful())
                                                                            {
                                                                                Toast.makeText(AdminActivity.this, x, Toast.LENGTH_SHORT).show();
                                                                                dialog.cancel();
                                                                            }
                                                                            else
                                                                            {
                                                                                Toast.makeText(AdminActivity.this, "Error "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                                else
                                                                {
                                                                    Toast.makeText(AdminActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                                        break;
                                                    }
                                                }
                                                //Log.d("tag1", "onComplete: required information is "+ data.get);
                                            }
                                        }
                                    });
                                }
                                else
                                {
                                    Toast.makeText(AdminActivity.this, "Please fill all the required fields", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                        builder.show();


                        Toast.makeText(AdminActivity.this, "Menu delete pannel", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_recover:
                        View mview_recover = getLayoutInflater().inflate(R.layout.popup_recover , null);
                        final EditText UserId_recover = (EditText)mview_recover.findViewById(R.id.userid);
                        builder.setView(mview_recover);
                        AlertDialog alertDialog_recover = builder.create();

                        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });



                        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                validateData(UserId_recover);
                                if(valid)
                                {
                                    String userid = UserId_recover.getText().toString();
                                    //DocumentReference ref1 = db.collection("User").document().get()
                                    CollectionReference ref1 = db.collection("User");
                                    ref1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful())
                                            {

                                                for(QueryDocumentSnapshot document : task.getResult())
                                                {
                                                    if(document.getString("id").equals(userid)) {
                                                        final String uid = document.getId();
                                                        DocumentReference ref2 = db.collection("User").document(uid);
                                                        Map<String,Object> user = new HashMap<>();
                                                        ref2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                if(task.isSuccessful())
                                                                {
                                                                    String status = task.getResult().get("isdeleted").toString();
                                                                    String x;
                                                                    if(status.equals("true"))
                                                                    {
                                                                        //Log.d("tag1", "onComplete:  User is Recovered ");
                                                                        user.put("isdeleted",false);
                                                                        x = "User is Recovered";

                                                                    }
                                                                    else
                                                                    {
                                                                        //Log.d("tag2", "onComplete:  User is deleted ");
                                                                        user.put("isdeleted",false);
                                                                        x = "User is not deleted";
                                                                    }
                                                                    ref2.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if(task.isSuccessful())
                                                                            {
                                                                                Toast.makeText(AdminActivity.this, x, Toast.LENGTH_SHORT).show();
                                                                                dialog.cancel();
                                                                            }
                                                                            else
                                                                            {
                                                                                Toast.makeText(AdminActivity.this, "Error "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                                else
                                                                {
                                                                    Toast.makeText(AdminActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                                        break;
                                                    }
                                                }
                                                //Log.d("tag1", "onComplete: required information is "+ data.get);
                                            }
                                        }
                                    });
                                }
                                else
                                {
                                    Toast.makeText(AdminActivity.this, "Please fill all the required fields", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                        builder.show();
                        Toast.makeText(AdminActivity.this, "Menu Recover pannel", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_curriculum:
                        View mview_carriculum = getLayoutInflater().inflate(R.layout.carriculum , null);
                        Filename = (EditText)mview_carriculum.findViewById(R.id.carriculum_id);
                        builder.setView(mview_carriculum);
                        AlertDialog alertDialog_carriculum = builder.create();

                        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });


                        builder.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                validateData(Filename);
                                if(valid)
                                {
                                    String filename = Filename.getText().toString();
                                    selectpdf_file();
                                }
                                else
                                {
                                    Toast.makeText(AdminActivity.this, "Please fill all the required fields", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                        builder.show();

                        Toast.makeText(AdminActivity.this, "Menu delete pannel", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_publish_notice:

                        View mview_notice = getLayoutInflater().inflate(R.layout.popup_notice , null);
                        EditText Published_by = (EditText)mview_notice.findViewById(R.id.published_by);
                        EditText Published_for = (EditText)mview_notice.findViewById(R.id.published_for);
                        EditText Notice = (EditText)mview_notice.findViewById(R.id.notice);
                        builder.setView(mview_notice);
                        AlertDialog alertDialog_notice = builder.create();

                        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.setPositiveButton("Publish Notice", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                validateData(Published_by);
                                validateData(Published_for);
                                validateData(Notice);

                               if(valid)
                               {
                                   Log.d("tag1", "onClick: in Publish");
                                   DocumentReference ref1 = db.collection("Notice").document();

                                   Map<String,Object> user = new HashMap<>();
                                   user.put("published_by",Published_by.getText().toString().trim());
                                   user.put("published_for",Published_for.getText().toString().trim());
                                   user.put("notice",Notice.getText().toString().trim());
                                   user.put("time" , System.currentTimeMillis());
                                   user.put("date",FieldValue.serverTimestamp());

                                   ref1.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {
                                           if(task.isComplete())
                                           {
                                               Toast.makeText(AdminActivity.this, "Notice is Published Successfully", Toast.LENGTH_SHORT).show();
                                               dialog.cancel();
                                           }
                                           else
                                           {
                                               Toast.makeText(AdminActivity.this, "There is an Error in Publishing Notice"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                           }
                                       }
                                   });
                               }
                               else
                               {
                                   Toast.makeText(AdminActivity.this, "Please fill all the required fields", Toast.LENGTH_SHORT).show();
                               }

                            }
                        });
                        builder.show();
                        Toast.makeText(AdminActivity.this, "Menu delete pannel", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.Enroll:
                        View mview_enroll = getLayoutInflater().inflate(R.layout.popup_enroll , null);
                        EditText Username_enroll = (EditText)mview_enroll.findViewById(R.id.teachername);
                        EditText Subject = (EditText)mview_enroll.findViewById(R.id.subject);
                        EditText UserId_enroll = (EditText)mview_enroll.findViewById(R.id.teacherid);
                        builder.setView(mview_enroll);
                        AlertDialog alertDialog_enroll = builder.create();

                        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.setPositiveButton("Enroll", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                    validateData(Username_enroll);
                                    validateData(Subject);
                                    validateData(UserId_enroll);

                                    if(valid)
                                    {
                                        DocumentReference ref1 = db.collection("Enroll").document();

                                        Map<String,Object> user = new HashMap<>();
                                        user.put("Teacher_name",Username_enroll.getText().toString().trim());
                                        user.put("Subect_name",Subject.getText().toString().trim());
                                        user.put("userid",UserId_enroll.getText().toString().trim());
                                        user.put("date",FieldValue.serverTimestamp());

                                        ref1.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isComplete())
                                                {
                                                    Toast.makeText(AdminActivity.this, "Teacher is enrolled  Successfully", Toast.LENGTH_SHORT).show();
                                                    dialog.cancel();
                                                }
                                                else
                                                {
                                                    Toast.makeText(AdminActivity.this, "There is an Error in Publishing Notice"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                    else
                                    {
                                        Toast.makeText(AdminActivity.this, "Please fill required fields", Toast.LENGTH_SHORT).show();
                                    }


                            }
                        });
                        builder.show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.lout_admin:
                        FirebaseUser firebaseUser = Auth.getCurrentUser();
                        String mail = firebaseUser.getEmail();
                        Auth.signOut();
                        Toast.makeText(AdminActivity.this, "User :"+mail+" Successfully Logout!!..", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        break;

                }
                return true;
            }

            private void validateData1(EditText userEmail) {

                String email = userEmail.getText().toString().trim();
                int len = email.length();
                String str = email.substring(len - 10 , len);
                Log.d("tag133", "validateData1: Checking String "+str);

                if(!str.equals("mnnit.ac.in"))
                {
                    valid1 = false;
                    userEmail.setError("Email must be MNNIT Email ");
                    return;
                }
                else
                {
                    valid1 = true;
                }


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
        });

    }



    private void selectpdf_file() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent , "Select Pdf file") , 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            uploadPdfFile(data.getData());
        }
    }

    private void uploadPdfFile(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading ....");
        progressDialog.show();
        //StorageReference ref = storageReference.child(Filename.getText().toString()+".pdf");
        String filename = Filename.getText().toString()+".pdf";

        //StorageReference storageReference = storage.getReference(); // will return the root path

        storageReference.child("Carriculum").child(filename).putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DocumentReference ref = db.collection("Carriculum").document();
                        HashMap<String , Object> user = new HashMap<>();
                        user.put("FileName" , Filename.getText().toString().trim());
                        user.put("URL" , uri.toString().trim());

                        ref.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AdminActivity.this, "Carriculum is uploaded Successfully", Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AdminActivity.this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminActivity.this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminActivity.this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0 * snapshot.getBytesTransferred())/(snapshot.getTotalByteCount());
                progressDialog.setMessage("Uploaded : "+(int)progress+" %");
            }
        });



    }
}