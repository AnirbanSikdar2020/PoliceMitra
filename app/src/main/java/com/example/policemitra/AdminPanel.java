package com.example.policemitra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

public class AdminPanel extends AppCompatActivity {
    Toolbar toolbar;
    LinearLayout crime,comp,ver,fir,permissions;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_appbar, menu);
        return true;
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.AdminLogout) {
//            Log.i("MENU_DRAWER_TAG", "Logout");
            FirebaseAuth.getInstance().signOut();
            Intent intentLogin = new Intent(AdminPanel.this,login.class);
            startActivity(intentLogin);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        toolbar = findViewById(R.id.navbar);
        setSupportActionBar(toolbar);
        crime=findViewById(R.id.crimeReg);
        comp = findViewById(R.id.complaints);
        ver = findViewById(R.id.verify);
        fir = findViewById(R.id.fir);
        permissions = findViewById(R.id.permissions);

        crime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        crime.setBackgroundColor(Color.parseColor("#FFDBA7"));
                        Intent intentLogin = new Intent(AdminPanel.this,crime_registration.class);
                        startActivity(intentLogin);
                        break;
                    case MotionEvent.ACTION_UP:
                        crime.setBackgroundColor(Color.WHITE);
                        break;
                }
                return true;
            }
        });
        comp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        comp.setBackgroundColor(Color.parseColor("#FFDBA7"));
                        break;
                    case MotionEvent.ACTION_UP:
                        comp.setBackgroundColor(Color.WHITE);
                        break;
                }
                return true;
            }
        });
        ver.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        ver.setBackgroundColor(Color.parseColor("#FFDBA7"));
                        break;
                    case MotionEvent.ACTION_UP:
                        ver.setBackgroundColor(Color.WHITE);
                        break;
                }
                return true;
            }
        });
        fir.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        fir.setBackgroundColor(Color.parseColor("#FFDBA7"));
                        break;
                    case MotionEvent.ACTION_UP:
                        fir.setBackgroundColor(Color.WHITE);
                        break;
                }
                return true;
            }
        });
        permissions.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        permissions.setBackgroundColor(Color.parseColor("#FFDBA7"));
                        break;
                    case MotionEvent.ACTION_UP:
                        permissions.setBackgroundColor(Color.WHITE);
                        break;
                }
                return true;
            }
        });
    }


}