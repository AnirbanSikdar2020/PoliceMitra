package com.example.policemitra;

        import static androidx.core.content.ContextCompat.getSystemService;
        import static com.example.policemitra.SendMail.EMAIL;
        import static com.example.policemitra.SendMail.PASSWORD;

        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.inputmethod.InputMethodManager;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AlertDialog;
        import androidx.fragment.app.FragmentManager;
        import androidx.fragment.app.FragmentTransaction;

        import com.chaos.view.PinView;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.firestore.DocumentReference;
        import com.google.firebase.firestore.FirebaseFirestore;

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


public class verify_dialog {
    PinView otp_box;
    loader loader;
    Activity activity;
    private AlertDialog dialog;
    Button quickVerify,submitVerify;
    int b;
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView resend;
    String name, email, phone;
    DBHelper DB;

    verify_dialog(Activity myactivity) {
        activity = myactivity;
    }

    void verifyDialogShow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.verification_dialog, null));
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();
        loader = new loader(activity);
        quickVerify = dialog.findViewById(R.id.quickVerifyBtn);
        submitVerify = dialog.findViewById(R.id.verifyAadharSubmit);

        submitVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loader.loaderShow();
                Intent intentLogin = new Intent(activity, Verification_submit.class);
                activity.startActivity(intentLogin);
            }
        });

        quickVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(activity, Quick_Search_verify.class);
                activity.startActivity(intentLogin);
            }
        });

//        mAuth = FirebaseAuth.getInstance();
//        DB = new DBHelper(activity);
//        buttonSendEmail(name, phone, email, b);
//        resend = dialog.findViewById(R.id.resend);
//        resend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                buttonSendEmail(name, phone, email, b);
//                Toast.makeText(activity, "OTP sent", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//
//        otp_box = dialog.findViewById(R.id.otp);
//        otp_box.requestFocus();
//    }
//
//    public void buttonSendEmail(String name, String phone, String email, int num) {
//        try {
//
//            String stringSenderEmail = EMAIL;
//            String stringReceiverEmail = email;
//            String stringPasswordSenderEmail = PASSWORD;
//
//            String stringHost = "smtp.gmail.com";
//
//            Properties properties = System.getProperties();
//
//            properties.put("mail.smtp.host", stringHost);
//            properties.put("mail.smtp.port", "465");
//            properties.put("mail.smtp.ssl.enable", "true");
//            properties.put("mail.smtp.auth", "true");
//
//            javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
//                @Override
//                protected PasswordAuthentication getPasswordAuthentication() {
//                    return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
//                }
//            });
//
//            MimeMessage mimeMessage = new MimeMessage(session);
//            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));
//
//            mimeMessage.setSubject("Welcome to PoliceMitra");
//            mimeMessage.setText("Hello " + name + ", \n\nUse this One time password " + num + " to register with Policemitra. \n\nRegards,\nPoliceMitra Team");
//
//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Transport.send(mimeMessage);
//                    } catch (MessagingException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            thread.start();
//
//        } catch (AddressException e) {
//            e.printStackTrace();
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void register(ArrayList<String> uDetails, View v) {
//        mAuth.createUserWithEmailAndPassword(uDetails.get(2), uDetails.get(6))
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            HashMap<String, Object> userDetails = new HashMap<>();
//                            userDetails.put("Name", uDetails.get(0));
//                            userDetails.put("Phone", uDetails.get(1));
//                            userDetails.put("Email", uDetails.get(2));
//                            userDetails.put("Gender", uDetails.get(3));
//                            userDetails.put("Aadhar", uDetails.get(4));
//                            userDetails.put("Dob", uDetails.get(5));
//                            userDetails.put("Password", uDetails.get(6));
//                            // Add a new document with a generated ID
//                            db.collection("users")
//                                    .document(uDetails.get(2).toString())
//                                    .set(userDetails)
//                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void unused) {
//                                            Toast.makeText(activity, "Welcome to PoliceMitra",
//                                                    Toast.LENGTH_SHORT).show();
//                                            Boolean checkInsertData = DB.insertUserData(uDetails.get(0), uDetails.get(2));
//                                            if (checkInsertData == true) {
//                                                dialog.dismiss();
//                                                Intent intentLogin = new Intent(activity, MainActivity.class);
//                                                activity.startActivity(intentLogin);
//                                            } else {
//                                                Boolean checkUpdateData = DB.updateUserData(uDetails.get(0), uDetails.get(2));
//                                                if(checkUpdateData==true)
//                                                {
//                                                    dialog.dismiss();
//                                                    Intent intentLogin = new Intent(activity, MainActivity.class);
//                                                    activity.startActivity(intentLogin);
//                                                }
//                                            }
//
//
//                                        }
//                                    })
//                                    .addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            Toast.makeText(activity, "Registration failed, try again.",
//                                                    Toast.LENGTH_SHORT).show();
//                                            loader.loaderHide();
//                                            dialog.dismiss();
//                                        }
//                                    });
//                        } else {
//                            Toast.makeText(activity, "Authentication failed, try again.",
//                                    Toast.LENGTH_SHORT).show();
//                            loader.loaderHide();
//                            dialog.dismiss();
//
//                        }
//                    }
//                });
//
    }
}

