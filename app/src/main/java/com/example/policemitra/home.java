package com.example.policemitra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class home extends AppCompatActivity {
    ViewFlipper slider;
    ImageButton test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        test = findViewById(R.id.drawer);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                View includedLayout = inflater.inflate(R.layout.activity_drawer_menu, null, false);

                // Add the included layout to the current layout
                ViewGroup layout = findViewById(R.id.home_layout);
                layout.addView(includedLayout);
//                Intent intent = new Intent(home.this,drawer_menu.class);
//                startActivity(intent);
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
