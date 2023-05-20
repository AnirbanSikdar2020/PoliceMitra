package com.example.policemitra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class crime_add extends AppCompatActivity {

    ImageView back;
    EditText fNumber, name, details, aadhar, location, ps;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button reg;
    FirebaseAuth mAuth;
    loader loader;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_add);
        fNumber = findViewById(R.id.F_Number);
        name = findViewById(R.id.name);
        details = findViewById(R.id.details);
        aadhar = findViewById(R.id.aadhar);
        location = findViewById(R.id.location);
        ps = findViewById(R.id.police_station);
        reg = findViewById(R.id.Registration);
        loader = new loader(crime_add.this);
        back = findViewById(R.id.back);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loader.loaderShow();
                HashMap<String, Object> crimeDetails = new HashMap<>();
                crimeDetails.put("File Number", fNumber.getText().toString());
                crimeDetails.put("Name", name.getText().toString());
                crimeDetails.put("Details", details.getText().toString());
                crimeDetails.put("Aadhar", aadhar.getText().toString());
                crimeDetails.put("Location", location.getText().toString());
                crimeDetails.put("Police Station", ps.getText().toString());

//                crimeDetails.put("Dob", uDetails.get(5));
//                crimeDetails.put("Password", uDetails.get(6));
                // Add a new document with a generated ID
                db.collection("criminalRecords")
                        .document(fNumber.getText().toString())
                        .set(crimeDetails)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                loader.loaderHide();
                                Toast.makeText(crime_add.this, "Registered",
                                        Toast.LENGTH_SHORT).show();
                                Intent intentLogin = new Intent(crime_add.this, crime_registration.class);
                                startActivity(intentLogin);

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                loader.loaderHide();
                                Toast.makeText(crime_add.this, "Registration failed, try again.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        back.setBackgroundColor(Color.parseColor("#FFDBA7"));
                        Intent intentLogin = new Intent(crime_add.this, crime_registration.class);
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


}