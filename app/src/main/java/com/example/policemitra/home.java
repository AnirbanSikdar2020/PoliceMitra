package com.example.policemitra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

//import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
//import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toolbar;
import android.widget.ViewFlipper;

import com.google.android.material.navigation.NavigationView;

public class home extends AppCompatActivity {
    ViewFlipper slider;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.appbar_options, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }

        int id = item.getItemId();

        if (id == R.id.action_item_one) {

            Log.i("MENU_DRAWER_TAG", "Sos");

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navidationView);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.menu_open,R.string.menu_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.test:
                        Log.i("MENU_DRAWER_TAG", "HOME ");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
                return false;
            }
        });

        int sliderArray[] = {R.drawable.slide2,R.drawable.slide1};
        slider=(ViewFlipper)findViewById(R.id.slider);

        for(int i=0;i<sliderArray.length;i++)
        {
            showImage(sliderArray[i]);
        }
    }

    public void showImage(int img)
    {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(img);
        slider.addView(imageView);
        slider.setFlipInterval(3000);
        slider.setAutoStart(true);
        slider.setInAnimation(this, android.R.anim.slide_in_left);
        slider.setOutAnimation(this, android.R.anim.slide_out_right);

    }
}
