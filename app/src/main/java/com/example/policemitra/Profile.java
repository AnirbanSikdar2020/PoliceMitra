package com.example.policemitra;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile extends Fragment {

    LinearLayout edit,myComplaints,changePwd;
    forgot_password forgot;
    TextView email,name;
    DBHelper DB;

    public Profile() {
        // Required empty public constructor
    }

    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        edit = view.findViewById(R.id.editProfile);
        myComplaints = view.findViewById(R.id.myComplaints);
        changePwd = view.findViewById(R.id.changePwd);
        name = view.findViewById(R.id.Uname);
        email = view.findViewById(R.id.Uemail);
        forgot = new forgot_password(this.getActivity());
        DB = new DBHelper(this.getActivity());
        Cursor res = DB.getData();
        if(res.getCount()>0){
            while (res.moveToNext())
            {
                name.setText(String.valueOf(res.getString(0)));
                email.setText(String.valueOf(res.getString(1)));
            }
        }
        edit.setOnTouchListener( new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        edit.setBackgroundColor(Color.parseColor("#FFDBA7"));
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.setReorderingAllowed(true);

                        transaction.replace(R.id.container, editprofile.class, null);
                        transaction.commit();
                        break;
                    case MotionEvent.ACTION_UP:
                        edit.setBackgroundColor(Color.WHITE);
                        break;
                }
                return true;
            }
        });
        myComplaints.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        myComplaints.setBackgroundColor(Color.parseColor("#FFDBA7"));
//                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                        FragmentTransaction transaction = fragmentManager.beginTransaction();
//                        transaction.setReorderingAllowed(true);
//
//                        transaction.replace(R.id.container, editprofile.class, null);
//                        transaction.commit();
                        break;
                    case MotionEvent.ACTION_UP:
                        myComplaints.setBackgroundColor(Color.WHITE);
                        break;
                }
                return true;
            }
        });
        changePwd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        changePwd.setBackgroundColor(Color.parseColor("#FFDBA7"));
                        forgot.forgotPasswordShow();
                        break;
                    case MotionEvent.ACTION_UP:
                        changePwd.setBackgroundColor(Color.WHITE);
                        break;
                }
                return true;
            }
        });
//        edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i("MENU_DRAWER_TAG", "HOME ");
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction transaction = fragmentManager.beginTransaction();
//                transaction.setReorderingAllowed(true);
//
//                transaction.replace(R.id.container, editprofile.class, null);
//                transaction.commit();
//            }
//        });
        // Inflate the layout for this fragment
        return view;
    }
}