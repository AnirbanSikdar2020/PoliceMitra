package com.example.policemitra;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
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
    String emailId;
    DBHelper DB;
    LinearLayout crime,comp,ver,GD,permissions;
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
            Log.i("MENU_DRAWER_TAG", emailId);
            if(emailId!=null || emailId !=""){
                Boolean checkDeleteData = DB.deleteUserData(emailId);
                if (checkDeleteData == true)
                {
                    Intent intentLogin = new Intent(AdminPanel.this,login.class);
                    startActivity(intentLogin);
                }
            }
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
        GD = findViewById(R.id.GD);
        permissions = findViewById(R.id.permissions);
        DB = new DBHelper(this);
        Cursor res = DB.getData();
        if(res.getCount()>0){
            while (res.moveToNext())
            {
                emailId = String.valueOf(res.getString(1));
//                emailId = emailId.replaceAll("[@.]*", "");
            }
        }
        crime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crime.setBackgroundColor(Color.parseColor("#FFDBA7"));
                Intent intentLogin = new Intent(AdminPanel.this,crime_registration.class);
                startActivity(intentLogin);
//                crime.setBackgroundColor(Color.WHITE);
            }
        });

        comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comp.setBackgroundColor(Color.parseColor("#FFDBA7"));
                Intent intentLogin = new Intent(AdminPanel.this,admin_complaints.class);
                startActivity(intentLogin);
            }
        });

        ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ver.setBackgroundColor(Color.parseColor("#FFDBA7"));
                Intent intentLogin = new Intent(AdminPanel.this,admin_verification.class);
                startActivity(intentLogin);
            }
        });

        GD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GD.setBackgroundColor(Color.parseColor("#FFDBA7"));
                Intent intentLogin = new Intent(AdminPanel.this,admin_GD.class);
                startActivity(intentLogin);
            }
        });

        permissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissions.setBackgroundColor(Color.parseColor("#FFDBA7"));
                Intent intentLogin = new Intent(AdminPanel.this,admin_permissions.class);
                startActivity(intentLogin);
            }
        });
    }


}