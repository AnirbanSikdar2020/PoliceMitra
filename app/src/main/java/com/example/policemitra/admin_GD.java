package com.example.policemitra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class admin_GD extends AppCompatActivity {

    ImageView back;
    List<ViewGDModel> modelList = new ArrayList<>();
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    CustomAdapterGDView adapter;
    FirebaseFirestore db;
    loader loader;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_complaints);
        mRecyclerView = findViewById(R.id.complaintsView);
//        mRecyclerView.hasFixedSize(true);
        db = FirebaseFirestore.getInstance();
        loader = new loader(admin_GD.this);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        back = findViewById(R.id.back);
//        add = findViewById(R.id.add_crime);
        loader.loaderShow();
        modelList.clear();
        showData();
        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        back.setBackgroundColor(Color.parseColor("#FFDBA7"));
                        Intent intentLogin = new Intent(admin_GD.this, AdminPanel.class);
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
        db.collection("GD").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            ViewGDModel model = new ViewGDModel(
                                    doc.getString("Aadhar"),
                                    doc.getString("File Url"),
                                    doc.getString("Subject"),
                                    doc.getString("Name"),
                                    doc.getString("Status"));
                            modelList.add(model);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            adapter = new CustomAdapterGDView(admin_GD.this, modelList);
                        }
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