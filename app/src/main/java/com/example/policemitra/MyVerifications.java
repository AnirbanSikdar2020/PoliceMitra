package com.example.policemitra;

import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import android.annotation.SuppressLint;
        import android.content.Intent;
        import android.database.Cursor;
        import android.graphics.Color;
        import android.os.Build;
        import android.os.Bundle;
        import android.view.MotionEvent;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.Toast;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.Task;
        import com.google.android.material.floatingactionbutton.FloatingActionButton;
        import com.google.firebase.firestore.DocumentSnapshot;
        import com.google.firebase.firestore.FirebaseFirestore;
        import com.google.firebase.firestore.Query;
        import com.google.firebase.firestore.QuerySnapshot;

        import java.util.ArrayList;
        import java.util.List;

public class MyVerifications extends AppCompatActivity {

    ImageView back;
    List<ViewMyVerificationsModel> modelList = new ArrayList<>();
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    CustomAdapterMyVerificationsView adapter;
    FirebaseFirestore db;
    Query query;
    loader loader;
    DBHelper DB;
    String email;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_complaints);
        mRecyclerView = findViewById(R.id.complaintsView);
        db = FirebaseFirestore.getInstance();
        loader = new loader(MyVerifications.this);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        back = findViewById(R.id.back);
        DB = new DBHelper(this);

        loader.loaderShow();
        modelList.clear();

        Cursor res = DB.getData();
        if (res.getCount() > 0) {
            while (res.moveToNext()) {
                email = String.valueOf(res.getString(1));
            }
        }
        if(email!="")
        {
            showData();
        }
        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        back.setBackgroundColor(Color.parseColor("#FFDBA7"));
                        Intent intentLogin = new Intent(MyVerifications.this, MainActivity.class);
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
        query = db.collection("verifications")
                .whereEqualTo("User Email",email);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            ViewMyVerificationsModel model = new ViewMyVerificationsModel(
                                    doc.getString("Aadhar"),
                                    doc.getString("File Url"),
                                    doc.getString("Details"),
                                    doc.getString("Name"),
                                    doc.getString("User Email"),
                                    doc.getString("Status"));
                            modelList.add(model);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            adapter = new CustomAdapterMyVerificationsView(MyVerifications.this, modelList);
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