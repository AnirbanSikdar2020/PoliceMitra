package com.example.policemitra;

import static androidx.core.content.ContextCompat.getSystemService;
import static com.example.policemitra.SendMail.EMAIL;
import static com.example.policemitra.SendMail.PASSWORD;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class Criminal_View_dialog {

    FirebaseStorage storage;
    FirebaseDatabase database;
    loader loader;
    Activity activity;
    private AlertDialog dialog;
    DocumentReference docRef;
    String genderFirebase;
    FirebaseAuth mAuth;
    ImageView profile_img;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView name, aadhar, status, gender, details, doc, location, ps;

    Criminal_View_dialog(Activity myactivity) {
        activity = myactivity;
    }

    void CriminalViewDialogShow(String fileSelector) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.view_criminal_dialog, null));
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();
        loader = new loader(activity);

        name = dialog.findViewById(R.id.name);
        aadhar = dialog.findViewById(R.id.aadhar);
        status = dialog.findViewById(R.id.status);
        gender = dialog.findViewById(R.id.gender);
        details = dialog.findViewById(R.id.details);
        doc = dialog.findViewById(R.id.date);
        location = dialog.findViewById(R.id.location);
        ps = dialog.findViewById(R.id.ps);
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        profile_img = dialog.findViewById(R.id.img);
        loader.loaderShow();

        docRef = db.collection("criminalRecords")
                .document(fileSelector);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        database.getReference().child(fileSelector).addValueEventListener(new ValueEventListener() {
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

                        name.setText(document.getString("Name"));
                        aadhar.setText(document.getString("Aadhar"));
                        status.setText(document.getString("Status"));
                        doc.setText(document.getString("Doc"));
                        gender.setText(document.getString("Gender"));
                        location.setText(document.getString("Location"));
                        details.setText(document.getString("Details"));
                        ps.setText(document.getString("Police Station"));

                        loader.loaderHide();
                    } else {
                        Toast.makeText(activity, "Check your internet connection and try again", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity, "Check your internet connection and try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}


