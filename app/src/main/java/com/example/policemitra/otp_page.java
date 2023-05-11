package com.example.policemitra;

import static com.example.policemitra.SendMail.EMAIL;
import static com.example.policemitra.SendMail.PASSWORD;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
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


public class otp_page {
    PinView otp_box;
    loader loader;
    Activity activity;
    private AlertDialog dialog;
    Button verify;
    int b;
    FirebaseAuth mAuth;
    TextView resend;
    String name, email, phone;

    otp_page(Activity myactivity) {
        activity = myactivity;
    }

    void calculateOtp() {
        int min = 1111;
        int max = 9999;
        b = (int) (Math.random() * (max - min + 1) + min);
    }

    void otpDialogShow(ArrayList<String> uDetails, String selector) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.otp, null));
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();
        loader = new loader(activity);
        phone = uDetails.get(1);
        name = uDetails.get(0);
        email = uDetails.get(2);
        mAuth = FirebaseAuth.getInstance();
        calculateOtp();
        buttonSendEmail(name, phone, email, b);
        resend = dialog.findViewById(R.id.resend);
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateOtp();
                buttonSendEmail(name, phone, email, b);
                Toast.makeText(activity, "OTP sent", Toast.LENGTH_SHORT).show();
            }
        });
        verify = dialog.findViewById(R.id.otpVerifyBtn);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otp_box.getText().toString().equals(String.valueOf(b))) {
                    Toast.makeText(activity, "Verified", Toast.LENGTH_SHORT).show();
                    if (selector == "reg") {
                        loader.loaderShow();
                        register(uDetails);
                    }

                } else {
                    Toast.makeText(activity, "Incorrect OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });
        otp_box = dialog.findViewById(R.id.otp);
        otp_box.requestFocus();
    }

    public void buttonSendEmail(String name, String phone, String email, int num) {
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

            mimeMessage.setSubject("Welcome to PoliceMitra");
            mimeMessage.setText("Hello " + name + ", \n\nUse this One time password " + num + " to register with Policemitra. \n\nRegards,\nPoliceMitra Team");

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

    public void register(ArrayList<String> uDetails) {
        mAuth.createUserWithEmailAndPassword(uDetails.get(2), uDetails.get(5))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(activity, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            Intent intentLogin = new Intent(activity, MainActivity.class);
                            activity.startActivity(intentLogin);

                        } else {
                            Toast.makeText(activity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            loader.loaderHide();
                            dialog.dismiss();

                        }
                    }
                });
    }
}

