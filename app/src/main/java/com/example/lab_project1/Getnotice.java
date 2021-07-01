package com.example.lab_project1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Getnotice extends AppCompatActivity {


    private RecyclerView recyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getnotice);


        recyclerView = findViewById(R.id.recycle_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        List<NoticeModel> noticeModels = new ArrayList<>();
        Adapter adapter = new Adapter(noticeModels);
        recyclerView.setAdapter(adapter);


        db.collection("Notice").orderBy("date" , Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null)
                {
                    Toast.makeText(Getnotice.this, "Error"+error.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                noticeModels.clear();
                for(QueryDocumentSnapshot d : value)
                {
                    String published_by = d.get("published_by").toString();
                    String published_for = d.get("published_for").toString();
                    String notice = d.get("notice").toString();
                    noticeModels.add(new NoticeModel(published_by , published_for , notice));
                }
                adapter.notifyDataSetChanged();
            }
        });



    }
}