package com.example.policemitra;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class drawer_menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_menu);
        getSupportActionBar().hide();
    }
}