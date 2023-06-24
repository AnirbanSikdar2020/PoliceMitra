
package com.example.policemitra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DeleteAlert {

    Activity activity;
    private AlertDialog dialog;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage;
    FirebaseDatabase database;
    EditText email;
    Button confirm, cancel;
    FirebaseAuth auth;

    DeleteAlert(Activity myactivity) {
        activity = myactivity;
    }

    void deleteAlertShow(String doc, Class sendActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.delete_alert, null));
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();

        confirm = dialog.findViewById(R.id.confirm);
        cancel = dialog.findViewById(R.id.cancel);
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("criminalRecords").document(doc)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Create a storage reference from our app

                                storage.getReference().child(doc).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        database.getReference().child(doc).removeValue();
                                        Toast.makeText(activity, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(activity, sendActivity);
                                        activity.startActivity(intent);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        Toast.makeText(activity, "Cannot Delete, Try Again", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(activity, sendActivity);
                                        activity.startActivity(intent);
                                    }
                                });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(activity, "Cannot Delete, Try Again", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(activity, sendActivity);
                                activity.startActivity(intent);
                            }
                        });


            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, sendActivity);
                activity.startActivity(intent);
            }
        });
    }
}
