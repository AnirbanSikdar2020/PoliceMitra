package com.example.policemitra;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class imageSlider extends AppCompatActivity {

    ViewFlipper slider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_slider);

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