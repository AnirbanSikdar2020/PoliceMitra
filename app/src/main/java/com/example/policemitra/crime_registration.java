package com.example.policemitra;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class crime_registration extends AppCompatActivity {
    ImageView back;
    FloatingActionButton add;
    List<Model> modelList = new ArrayList<>();
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    loader loader;
    CustomAdapter adapter;
    FirebaseFirestore db;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_registration);
        mRecyclerView = findViewById(R.id.crimeView);
//        mRecyclerView.hasFixedSize(true);
        db = FirebaseFirestore.getInstance();
        loader = new loader(crime_registration.this);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        back = findViewById(R.id.back);
        add = findViewById(R.id.add_crime);
        showData();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(crime_registration.this, crime_add.class);
                startActivity(intentLogin);
            }
        });

        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        back.setBackgroundColor(Color.parseColor("#FFDBA7"));
                        Intent intentLogin = new Intent(crime_registration.this, AdminPanel.class);
                        startActivity(intentLogin);
                        break;
                    case MotionEvent.ACTION_UP:
                        back.setBackgroundColor(Color.WHITE);
                        break;
                }
                return true;
            }
        });
    }

    private void showData() {
        loader.loaderShow();
        db.collection("criminalRecords").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            Model model = new Model(doc.getString("File Number"),
                                    doc.getString("File Number"),
                                    doc.getString("Name"),
                                    doc.getString("Details"));
                            modelList.add(model);
                        }
                        adapter = new CustomAdapter(crime_registration.this, modelList);
                        mRecyclerView.setAdapter(adapter);
                        loader.loaderHide();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

}