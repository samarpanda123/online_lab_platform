package com.example.lab_project1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UpdateRequests extends AppCompatActivity implements UpdateRequestAdapter.Request {


    RecyclerView recyclerView;
    Toolbar toolbar;
    List<UpdateRequestModel> list = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_requests);

        toolbar = findViewById(R.id.toolbar_update_request);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.Update_Request_recycleview);
        UpdateRequestAdapter adapter = new UpdateRequestAdapter(list , UpdateRequests.this);
        recyclerView.setLayoutManager(new GridLayoutManager(UpdateRequests.this , 1));
        recyclerView.setAdapter(adapter);


        db.collection("Update_requests").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                list.clear();
                for(QueryDocumentSnapshot d : value)
                {
                    String StudentId = d.get("UserId").toString();
                    String Querry = d.get("UserDetails").toString();
                    String documentId = d.getId().toString();
                    UpdateRequestModel updateRequestModel = new UpdateRequestModel(StudentId , Querry , documentId);
                    list.add(updateRequestModel);
                }
                adapter.notifyDataSetChanged();

            }
        });

    }

    @Override
    public void submitButton(String documentId) {

        db.collection("Update_requests").document(documentId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(UpdateRequests.this, "Request is deleted Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateRequests.this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}