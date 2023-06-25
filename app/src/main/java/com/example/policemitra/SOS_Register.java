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

public class SOS_Register {

    Activity activity;
    private AlertDialog dialog;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage;
    FirebaseDatabase database;
    EditText email;
    Button confirm;
    FirebaseAuth auth;
    EditText number,authority;
    TextInputLayout textInputLayoutNumber,textInputLayoutAuthority;
    int counter = 0;
    DocumentReference updateData, docRef;
    String user_email,subject,aadhar,name;

    SOS_Register(Activity myactivity) {
        activity = myactivity;
    }

    void SOS_RegisterShow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.sos_register, null));
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();
        number = dialog.findViewById(R.id.Number);
        authority = dialog.findViewById(R.id.authority);
        confirm = dialog.findViewById(R.id.confirm);
        textInputLayoutNumber = dialog.findViewById(R.id.textInputNumber);
        textInputLayoutAuthority = dialog.findViewById(R.id.textInputAuthority);
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Phone
                if (TextUtils.isEmpty(number.getText().toString().trim())) {
                    showError(textInputLayoutNumber, "Please enter Phone Number");
                    counter = 1;
                } else {
                    textInputLayoutNumber.setError(null);
                    counter = 0;
                }
                //Authority
                if (TextUtils.isEmpty(authority.getText().toString().trim())) {
                    showError(textInputLayoutAuthority, "Please enter Authority Name");
                    counter = 1;
                } else {
                    textInputLayoutAuthority.setError(null);
                    counter = 0;
                }
                if (counter == 0) {
                    HashMap<String, Object> sosDetails = new HashMap<>();
                    sosDetails.put("Number", number.getText().toString());
                    sosDetails.put("Authority", authority.getText().toString());

                    //upload data
                    db.collection("SOS")
                            .document(number.getText().toString())
                            .set(sosDetails)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(activity, "Saved", Toast.LENGTH_SHORT).show();
                                    dialog.hide();
                                }
                            });
                }
            }
        });
    }
    public void showError(TextInputLayout tag, String message) {
        tag.setError(message);
    }
}
