package com.servecreative.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class dashboardscreen extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<User> userdataArrayList;
    MyAdapter myAdapter;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboardscreen);
        db = FirebaseFirestore.getInstance();
        initViews();
        initRecyclerView();
        eventChangeListener();


    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycleerview);
    }

    private void initRecyclerView() {
        userdataArrayList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new MyAdapter(dashboardscreen.this, userdataArrayList);
        recyclerView.setAdapter(myAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void eventChangeListener() {
        db.collection("users").orderBy("name", Query.Direction.ASCENDING).addSnapshotListener((value, error) -> {

            if (error != null) {
                Log.e("Firestore error", error.getLocalizedMessage());
                return;
            }
            assert value != null;
            for (DocumentChange dc : value.getDocumentChanges()) {
                if (dc.getType() == DocumentChange.Type.ADDED) {
                    userdataArrayList.add(dc.getDocument().toObject(User.class));
                }
                myAdapter.notifyDataSetChanged();
            }
        });
    }


}
