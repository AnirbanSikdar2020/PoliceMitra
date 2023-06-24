package com.example.policemitra;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Profile extends Fragment {

    LinearLayout edit, myComplaints, changePwd;
    loader loader;
    FirebaseStorage storage;
    FirebaseDatabase database;
    private ActivityResultLauncher<String> imagePickerLauncher;
    forgot_password forgot;
    TextView email, name;
    DBHelper DB;
    MyComplaintsModal myComplaintsModal;
    ImageView profile_img;
    String emailId;

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
        profile_img = view.findViewById(R.id.profile_img);
        forgot = new forgot_password(this.getActivity());
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        loader = new loader(this.getActivity());
        loader.loaderShow();
        DB = new DBHelper(this.getActivity());
        Cursor res = DB.getData();
        myComplaintsModal = new MyComplaintsModal(this.getActivity());
        if (res.getCount() > 0) {
            while (res.moveToNext()) {
                name.setText(String.valueOf(res.getString(0)));
                email.setText(String.valueOf(res.getString(1)));
                emailId = String.valueOf(res.getString(1));
                emailId = emailId.replaceAll("[@.]*", "");
            }
        }
        if(emailId!=null || emailId!="")
        {
            database.getReference().child(emailId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String image = snapshot.getValue(String.class);
                    Picasso.get().load(image).into(profile_img);
                    loader.loaderHide();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else {
            profile_img.setImageResource(R.drawable.img);
        }

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                result -> {
                    if (result != null) {
                        loader.loaderShow();
                        Uri uri = result;
                        profile_img.setImageURI(uri);
                        final StorageReference reference = storage.getReference()
                                .child(emailId);
                        reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        database.getReference().child(emailId).setValue(uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                loader.loaderHide();
                                                Toast.makeText(getActivity(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set up the image picker launcher
                launchImagePicker();
            }
        });

        edit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
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
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        myComplaints.setBackgroundColor(Color.parseColor("#FFDBA7"));
                        myComplaintsModal.MyComplaintsDialogShow();
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
                switch (event.getAction()) {
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

    private void launchImagePicker() {
        imagePickerLauncher.launch("image/*");
    }
}