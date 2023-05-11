package com.example.policemitra;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class home extends Fragment {

    ViewFlipper slider;
    public home() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        int sliderArray[] = {R.drawable.pic_1,R.drawable.pic_2,R.drawable.pic_3};
        slider=view.findViewById(R.id.slider);

        for(int i=0;i<sliderArray.length;i++)
        {
            showImage(sliderArray[i]);
        }
        // Inflate the layout for this fragment
        return view;
    }

    public void showImage(int img)
    {
        ImageView imageView = new ImageView(this.getContext());
        imageView.setBackgroundResource(img);
        slider.addView(imageView);
        slider.setFlipInterval(3000);
        slider.setAutoStart(true);
        slider.setInAnimation(this.getContext(), android.R.anim.slide_in_left);
        slider.setOutAnimation(this.getContext(), android.R.anim.slide_out_right);

    }
}