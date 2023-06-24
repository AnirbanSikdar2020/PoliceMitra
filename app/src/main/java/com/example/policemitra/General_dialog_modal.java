

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


public class General_dialog_modal {
    loader loader;
    Activity activity;
    private AlertDialog dialog;
    Button permissions,gd,fir;

    General_dialog_modal(Activity myactivity) {
        activity = myactivity;
    }

    void generalDialogShow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.general_modal, null));
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();
        loader = new loader(activity);
        permissions = dialog.findViewById(R.id.permissions);
        gd = dialog.findViewById(R.id.gd);

        permissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loader.loaderShow();
                Intent intentLogin = new Intent(activity, Permission_submit.class);
                activity.startActivity(intentLogin);
            }
        });

        gd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loader.loaderShow();
                Intent intentLogin = new Intent(activity, GD_submit.class);
                activity.startActivity(intentLogin);
            }
        });
    }
}


