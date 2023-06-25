package com.example.policemitra;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class home extends Fragment {

    ViewFlipper slider;
    ImageView constitution,wbpnotify,kpnotify;
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
        int sliderArray[] = {R.drawable.pic_1,R.drawable.police_1,R.drawable.police_2,R.drawable.police_4, R.drawable.police_6,R.drawable.police_9,R.drawable.police_10, R.drawable.pic_2,R.drawable.pic_3};
        slider=view.findViewById(R.id.slider);
        constitution = view.findViewById(R.id.constitution);
        wbpnotify = view.findViewById(R.id.wbpnotify);
        kpnotify = view.findViewById(R.id.kpnotify);


        for(int i=0;i<sliderArray.length;i++)
        {
            showImage(sliderArray[i]);
        }

        constitution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.indiacode.nic.in/bitstream/123456789/15240/1/constitution_of_india.pdf";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        kpnotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.kolkatapolice.gov.in/contactus.aspx";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        wbpnotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://wbpolice.gov.in/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
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