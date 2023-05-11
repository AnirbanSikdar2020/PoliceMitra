package com.example.policemitra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class forgot_password {

    Activity activity;
    private AlertDialog dialog;
    EditText email;
    Button rPwd;
    FirebaseAuth auth;

    forgot_password(Activity myactivity) {
        activity = myactivity;
    }

    void forgotPasswordShow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.forgotpassword, null));
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();
        email = dialog.findViewById(R.id.email);
        rPwd = dialog.findViewById(R.id.rpw);
        auth = FirebaseAuth.getInstance();
        rPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailid = String.valueOf(email.getText());
                auth.sendPasswordResetEmail(emailid).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(activity, "Password reset link is sent to your registered email-id", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(activity, "This email-id is not registered", Toast.LENGTH_SHORT).show();
                        }
                        email.setText("");
                    }
                });
            }
        });
    }
}
