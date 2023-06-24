
package com.example.policemitra;

import static com.example.policemitra.SendMail.EMAIL;
import static com.example.policemitra.SendMail.PASSWORD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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

public class GD_Alert {

    Activity activity;
    private AlertDialog dialog;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage;
    FirebaseDatabase database;
    EditText email;
    Button confirm;
    FirebaseAuth auth;
    EditText GD_number;
    TextInputLayout textInputLayoutGD_number;
    int counter = 0;
    DocumentReference updateData, docRef;
    String user_email,subject,aadhar,name;

    GD_Alert(Activity myactivity) {
        activity = myactivity;
    }

    void GDAlertShow(String doc, Class sendActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.activity_gd_alert, null));
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();
        GD_number = dialog.findViewById(R.id.GD_number);
        confirm = dialog.findViewById(R.id.confirm);
        textInputLayoutGD_number = dialog.findViewById(R.id.textInputGD);
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GD validation
                if (TextUtils.isEmpty(GD_number.getText().toString().trim())) {
                    showError(textInputLayoutGD_number, "Please enter GD number");
                    counter=1;
                } else {
                    textInputLayoutGD_number.setError(null);
                    counter=0;
                }
                if (counter == 0) {
                    docRef = db.collection("GD")
                            .document(doc);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    user_email = document.getString("User Email");
                                    subject = document.getString("Subject");
                                    aadhar = document.getString("Aadhar");
                                    name = document.getString("Name");
                                }
                            }
                        }
                    });
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("Status", "Accepted");
                    updates.put("GD Number", GD_number.getText().toString());
                    updateData = db.collection("GD").document(doc);
                    updateData.update(updates)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Toast.makeText(activity, "Approved", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(activity, "Mail sent to user", Toast.LENGTH_SHORT).show();
                                    buttonSendEmail("accept", user_email, name, aadhar, subject);
                                    Intent intentLogin = new Intent(activity, admin_GD.class);
                                    activity.startActivity(intentLogin);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(activity, "Data not updated", Toast.LENGTH_SHORT).show();
                                }
                            });
                }

            }

        });

    }

    public void buttonSendEmail(String flag, String email, String name, String aadhar, String subject) {
        try {

            String stringSenderEmail = EMAIL;
            String stringReceiverEmail = email;
            String stringPasswordSenderEmail = PASSWORD;

            String stringHost = "smtp.gmail.com";

            Properties properties = System.getProperties();

            properties.put("mail.smtp.host", stringHost);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");

            javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));
            if (flag == "accept") {
                mimeMessage.setSubject("GD Status Update for " + subject);
                mimeMessage.setText("Hello " + name + ", \n\nAs per you have requested for a GD of \nSubject : " + subject + "\nWe have accepted your GD. Your GD Number is "+GD_number.getText().toString()+" rapid action would be taken for the same. For more details we will call you within 48hours on your registered mobile number. \n\nRegards,\nPoliceMitra Team");
            } else {
                mimeMessage.setSubject("GD Status Update for " + subject);
                mimeMessage.setText("Hello " + name + ", \n\nAs per you have requested for a GD of \nSubject : " + subject + "\nWe are sorry to let you know that we would not be able to take your GD online. We would request you to kindly visit your nearest police station to raise the GD.\n\nRegards,\nPoliceMitra Team");
            }
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(mimeMessage);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void showError(TextInputLayout tag, String message) {
        tag.setError(message);
    }
}
